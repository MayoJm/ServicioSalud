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
        System.out.println("Generando turnos disponibles...");
        LocalDate fechaActual = LocalDate.now();

        for (DisponibilidadProfesional disponibilidad : disponibilidades) {
            System.out.println("entro al buclue");
            System.out.println("Generando turnos para disponibilidad: " + disponibilidad.toString());
            DayOfWeek diaSemana = disponibilidad.getDiaSemana();
            System.out.println("Dia de la semana: " + diaSemana.toString());
            LocalTime horaInicio = disponibilidad.getHoraInicio();
            System.out.println("Hora de inicio: " + horaInicio.toString());
            LocalTime horaFin = disponibilidad.getHoraFin();
            System.out.println("Hora de fin: " + horaFin.toString());
            // Iterar sobre el rango de horas y generar turnos disponibles de una hora
            LocalTime horaActual = horaInicio;
            while (horaActual.isBefore(horaFin)) {
                System.out.println("entro al buclue");
                System.out.println("Generando turno para hora: " + horaActual.toString());
                LocalDateTime fechaHoraTurno = fechaActual.with(TemporalAdjusters.nextOrSame(diaSemana)).atTime(horaActual);
                System.out.println("Fecha y hora del turno: " + fechaHoraTurno.toString());
                Turno turno = new Turno();
                System.out.println("Instancio turno");
                turno.setFechaHora(fechaHoraTurno);
                System.out.println("Seteo fecha y hora");
                turno.setUsuarioProfesional(profesional);
                System.out.println("Seteo profesional");
                turnosDisponibles.add(turno);
                System.out.println("Agrego turno a la lista");
                //turnoRepositorio.save(turno);

                horaActual = horaActual.plusHours(1); // Avanzar una hora
                System.out.println("Avanzo una hora");
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
}
