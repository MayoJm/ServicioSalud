package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.DisponibilidadProfesional;
import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.repositorios.CalendarioRepositorio;
import com.appsalud.plataformaSalud.repositorios.DisponibilidadProfesionalRepositorio;
import com.appsalud.plataformaSalud.repositorios.TurnoRepositorio;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class DisponibilidadProfesionalServicio {
    @Autowired
    private CalendarioServicio calendarioServicio;
    @Autowired
    private CalendarioRepositorio calendarioRepositorio;

    @Autowired
    private DisponibilidadProfesionalRepositorio disponibilidadProfesionalRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TurnoRepositorio turnoRepositorio;

    public void guardarDisponibilidadProfesional(List<DisponibilidadProfesional> disponibilidades) {
        // Aquí puedes realizar validaciones u otras operaciones necesarias antes de guardar
        disponibilidadProfesionalRepositorio.saveAll(disponibilidades);
    }

    public void establecerDisponibilidad(UsuarioProfesional profesional, List<DisponibilidadProfesional> disponibilidades) {
        System.out.println("Estableciendo disponibilidad para el profesional " + profesional.getNombre() + " " + profesional.getApellido());
        System.out.println("Disponibilidades: " + disponibilidades.toString());
        // Generar turnos disponibles basados en la disponibilidad y asociarlos con el calendario del profesional
        List<Turno> turnosDisponibles = generarTurnosDisponibles(disponibilidades, profesional);

        asociarTurnosConCalendario(profesional, turnosDisponibles);
        System.out.println("Turnos asociados con calendario exitosamente");
    }


    private List<Turno> generarTurnosDisponibles(List<DisponibilidadProfesional> disponibilidades, UsuarioProfesional profesional) {
        List<Turno> turnosDisponibles = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now().plusDays(1); // Obtener el día siguiente al actual
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Obtener el último día del mes actual
        LocalDate ultimoDiaMes = fechaActual.with(TemporalAdjusters.lastDayOfMonth());

        // Iterar sobre cada día del mes
        for (LocalDate fecha = fechaActual; !fecha.isAfter(ultimoDiaMes); fecha = fecha.plusDays(1)) {
            DayOfWeek diaSemana = fecha.getDayOfWeek();

            // Verificar si hay disponibilidad para este día de la semana
            if (hayDisponibilidad(diaSemana, disponibilidades)) {
                // Iterar sobre las disponibilidades para este día
                for (DisponibilidadProfesional disponibilidad : disponibilidades) {
                    if (disponibilidad.getDiaSemana() == diaSemana) {
                        LocalTime horaInicio = disponibilidad.getHoraInicio();
                        LocalTime horaFin = disponibilidad.getHoraFin();

                        // Iterar sobre el rango de horas y generar turnos disponibles de una hora
                        LocalTime horaActual = horaInicio;
                        while (horaActual.isBefore(horaFin)) {
                            LocalDateTime fechaHoraTurno = fecha.atTime(horaActual);
                            String formattedDate = fechaHoraTurno.format(dateFormatter);
                            String formattedTime = fechaHoraTurno.format(timeFormatter);
                            Turno turno = new Turno();
                            turno.setFechaHora(fechaHoraTurno);
                            turno.setFechaFormateada(formattedDate);
                            turno.setHoraFormateada(formattedTime);
                            turno.setUsuarioProfesional(profesional);
                            turnosDisponibles.add(turno);

                            horaActual = horaActual.plusHours(1);
                        }
                    }
                }
            }
        }

        return turnosDisponibles;
    }



    private void asociarTurnosConCalendario(UsuarioProfesional usuarioProfesional, List<Turno> turnos) {
        try {

            for (Turno turno : turnos) {
                turnoRepositorio.save(turno);
            }

            System.out.println("Asociando turnos con calendario...");
            System.out.println("Usuario profesional: " + usuarioProfesional.getEmail().toString());
            System.out.println("Turnos: " + turnos.toString());
            usuarioProfesional.getCalendario().setTurnos(turnos);

            System.out.println("Turnos asociados con calendario: " + usuarioProfesional.getCalendario().getTurnos().toString());

            calendarioRepositorio.save(usuarioProfesional.getCalendario());
            System.out.println("Calendario guardado exitosamente");
            System.out.println("Turnos asociados con calendario: " + usuarioProfesional.getCalendario().getId().toString());
            System.out.println("Turnos asociados con calendario: " + usuarioProfesional.getCalendario().getTurnos().toString());

        } catch (Exception e) {
            System.out.println("Error al asociar turnos con calendario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean hayDisponibilidad(DayOfWeek diaSemana, List<DisponibilidadProfesional> disponibilidades) {
        for (DisponibilidadProfesional disponibilidad : disponibilidades) {
            if (disponibilidad.getDiaSemana() == diaSemana) {
                // Si se encuentra al menos una disponibilidad para este día, se retorna verdadero
                return true;
            }
        }
        // Si no se encuentra ninguna disponibilidad para este día, se retorna falso
        return false;
    }

}
