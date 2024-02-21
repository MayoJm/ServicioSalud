package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.*;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.TurnoRepositorio;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurnoServicio {

    @Autowired
    private TurnoRepositorio turnoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioPacienteServicio usuarioPacienteServicio;

    @Autowired
    private UsuarioProfesionalServicio usuarioProfesionalServicio;

    @Transactional
    public void modificarTurno(String id, LocalDateTime fechaHora, String descripcion, UsuarioPaciente usuarioPaciente,
            UsuarioProfesional usuarioProfesional) throws MiException {

        validarTurno(fechaHora, descripcion, usuarioPaciente, usuarioProfesional);

        Optional<Turno> respuesta = turnoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Turno turno = respuesta.get();

            turno.setFechaHora(fechaHora);
            turno.setDescripcion(descripcion);
            turno.setUsuarioPaciente(usuarioPaciente);
            turno.setUsuarioProfesional(usuarioProfesional);

            turnoRepositorio.save(turno);
        }
    }

    @Transactional
    public void bajaTurno(String id) {
        Optional<Turno> respuesta = turnoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Turno turno = respuesta.get();

            if (turno.getAlta()) {
                turno.setAlta(Boolean.FALSE);
                turnoRepositorio.save(turno);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Turno> listarTurnosPorPaciente(String email) {
        Optional<UsuarioPaciente> respuesta = usuarioRepositorio.buscarPorEmailPaciente(email);
        List<Turno> turnosPaciente = new ArrayList<>();
        UsuarioPaciente paciente = respuesta.get();
        turnosPaciente = turnoRepositorio.buscarPorPaciente(paciente);

        return turnosPaciente;
    }

    @Transactional(readOnly = true)
    public List<Turno> listarTurnosPorProfesional(String email) {
        Optional<UsuarioProfesional> respuesta = usuarioRepositorio.buscarPorEmail(email);
        List<Turno> turnosProfesional = new ArrayList<>();
        UsuarioProfesional profesional = respuesta.get();
        turnosProfesional = turnoRepositorio.buscarPorProfesional(profesional);

        return turnosProfesional;
    }

    public void validarTurno(LocalDateTime fechaHora, String descripcion, UsuarioPaciente usuarioPaciente,
            UsuarioProfesional usuarioProfesional) throws MiException {

        if (fechaHora == null) {
            throw new MiException("La fecha no puede ser nula");
        }
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new MiException("La fecha no puede ser anterior a la fecha actual");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiException("La descripcion no puede ser nula ni vacia");
        }
        if (usuarioPaciente == null) {
            throw new MiException("El paciente no puede ser nulo");
        }
        if (usuarioProfesional == null) {
            throw new MiException("El profesional no puede ser nulo");
        }

    }

    public List<DisponibilidadHoraria> obtenerHorariosDisponiblesParaDia(UsuarioProfesional profesional,
            LocalDate fecha) throws MiException {
        // Obtener la disponibilidad horaria del profesional
        List<DisponibilidadHoraria> disponibilidadHoraria = profesional.getDisponibilidades();

        // Obtener todos los turnos reservados para el profesional en la fecha
        // especificada
        LocalDateTime fechaInicioDia = fecha.atStartOfDay();
        LocalDateTime fechaFinDia = fecha.atTime(LocalTime.MAX);
        List<Turno> turnosReservados = turnoRepositorio.findByUsuarioProfesionalAndFechaBetween(profesional,
                fechaInicioDia, fechaFinDia);

        // Crear un conjunto para almacenar los horarios ocupados en la fecha
        // especificada
        Set<LocalTime> horariosOcupados = new HashSet<>();

        // Llenar el conjunto con los horarios ocupados
        turnosReservados.forEach(turno -> horariosOcupados.add(turno.getFechaHora().toLocalTime()));

        // Crear una lista para almacenar los horarios disponibles en la fecha
        // especificada
        List<DisponibilidadHoraria> horariosDisponibles = new ArrayList<>();

        // Iterar sobre las disponibilidades horarias del profesional
        for (DisponibilidadHoraria disponibilidad : disponibilidadHoraria) {
            // Verificar si el día de la disponibilidad coincide con el día de la semana de
            // la fecha
            if (disponibilidad.getDiaSemana() == fecha.getDayOfWeek() && disponibilidad.getHoraInicio() != null) {
                // Iterar sobre todos los horarios dentro del rango de la disponibilidad horaria
                LocalTime horaInicio = disponibilidad.getHoraInicio();
                LocalTime horaFin = disponibilidad.getHoraFin();
                LocalTime horaActual = horaInicio;
                while (horaActual.isBefore(horaFin) || horaActual.equals(horaFin)) {
                    // Verificar si el horario actual no está ocupado
                    if (!horariosOcupados.contains(horaActual)) {
                        // Agregar el horario disponible a la lista
                        horariosDisponibles.add(new DisponibilidadHoraria(fecha.getDayOfWeek(), horaActual,
                                horaActual.plusMinutes(60)));
                    }
                    // Incrementar la hora actual en intervalos de 30 minutos
                    horaActual = horaActual.plusMinutes(60);
                }
            }
        }
        if (horariosDisponibles.isEmpty()) {
            throw new MiException("No hay horarios disponibles para la fecha seleccionada");

        }
        return horariosDisponibles;
    }

    public void solicitarTurno(String profesionalId, String fechaSeleccionada, String horarioSeleccionado,
            String descripcion) throws ParseException, MiException {
        // Obtener usuario paciente de la sesión activa
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioPaciente> usuarioPaciente = usuarioPacienteServicio.buscarPacientePorEmail(email);

        // Obtener usuario profesional por ID
        UsuarioProfesional usuarioProfesional = usuarioProfesionalServicio.buscarPorId(profesionalId);

        // Formatear fecha y hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        // Procesamos el horario seleccionado para obtener solo la hora de inicio
        String horaInicioStr = horarioSeleccionado.split(" - ")[0];
        // Combinamos la fecha y la hora de inicio para obtener la fechaHora del turno
        String fechaHoraStr = fechaSeleccionada + " " + horaInicioStr;
        LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Crear el turno
        if (usuarioPaciente.isPresent() && usuarioProfesional != null) {
            UsuarioPaciente paciente = usuarioPaciente.get();
            Turno turno = new Turno();
            turno.setUsuarioPaciente(paciente);
            turno.setUsuarioProfesional(usuarioProfesional);
            turno.setFechaHora(fechaHora);
            turno.setFechaFormateada(fechaHora.format(dateFormatter));
            turno.setHoraFormateada(fechaHora.format(timeFormatter));
            turno.setDescripcion(descripcion);
            turno.setAlta(true); // Setear a true al persistir el turno

            // Persistir el turno en la base de datos
            turnoRepositorio.save(turno);
        } else {
            throw new MiException("Paciente o profesional no encontrados");
        }
    }
}
