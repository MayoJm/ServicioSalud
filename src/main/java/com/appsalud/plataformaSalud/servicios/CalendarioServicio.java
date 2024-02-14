package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Calendario;
import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.repositorios.CalendarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarioServicio {
    @Autowired
    private CalendarioRepositorio calendarioRepositorio;

    public Calendario crearCalendario(Calendario calendario) {

        return calendarioRepositorio.save(calendario);
    }

    public Calendario obtenerCalendario(UsuarioProfesional profesional) {
        return profesional.getCalendario();
    }

    public List<Turno> obtenerTurnosDisponibles(UsuarioProfesional profesional) {

        // Lógica para obtener los turnos disponibles del profesional
        Calendario calendario = profesional.getCalendario();


        if (calendario != null && calendario.getTurnos() != null) {
            return calendario.getTurnos().stream()
                    .filter(turno -> turno.getUsuarioPaciente() == null)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
    public void agregarTurno(Calendario calendario, Turno turno) {
        if (calendario != null && turno != null) {
            if (calendario.getTurnos() == null) {
                calendario.setTurnos(new ArrayList<>());
            }
            calendario.getTurnos().add(turno);
            System.out.println("Turno agregado al calendario: ");
            try {
                // Guardar el calendario actualizado en la base de datos
                calendarioRepositorio.save(calendario);
            } catch (Exception e) {
                // Manejar la excepción (por ejemplo, imprimir el mensaje de error)
                System.out.println("Error al guardar el calendario: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
