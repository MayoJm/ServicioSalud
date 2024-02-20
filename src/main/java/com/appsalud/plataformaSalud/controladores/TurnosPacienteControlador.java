package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.DisponibilidadHoraria;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.TurnoRepositorio;
import com.appsalud.plataformaSalud.servicios.TurnoServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioProfesionalServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/paciente/dashboard-paciente/turnos-paciente")
public class TurnosPacienteControlador {
    @Autowired
    private TurnoServicio turnoServicio;

    @Autowired
    private UsuarioProfesionalServicio usuarioProfesionalServicio;

    @Autowired
    private TurnoRepositorio turnoRepositorio;

    @GetMapping("/buscar-turnos")
    public String buscarTurnosPaciente(Model model) throws MiException {
        List<UsuarioProfesional> profesionales = usuarioProfesionalServicio.listarUsuariosProfesionales();
        if (profesionales.isEmpty()) {
            model.addAttribute("error",
                    "No hay profesionales disponibles en este momento. Por favor, intente más tarde.");
        } else {
            model.addAttribute("profesionales", profesionales);
        }
        return "buscarProfesional.html";
    }

    @GetMapping("/horarios-disponibles")
    @ResponseBody
    public ResponseEntity<List<DisponibilidadHoraria>> obtenerHorariosDisponibles(
            @RequestParam("profesionalId") String profesionalId,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSeleccionada)
            throws MiException {

        System.out.println("profesionalId: " + profesionalId);
        UsuarioProfesional profesional = usuarioProfesionalServicio.buscarPorId(profesionalId);
        System.out.println("profesional: " + profesional.getNombre());

        List<DisponibilidadHoraria> horariosDisponibles = turnoServicio.obtenerHorariosDisponiblesParaDia(profesional,
                fechaSeleccionada);
        return ResponseEntity.ok().body(horariosDisponibles);

    }

    @GetMapping("/calendario")
    public String mostrarCalendario(Model model, @RequestParam("profesionalId") String profesionalId) {
        UsuarioProfesional profesional = usuarioProfesionalServicio.buscarPorId(profesionalId);
        System.out.println(profesional.getApellido());
        model.addAttribute("profesional", profesional);
        return "calendario.html";
    }

    @PostMapping("/solicitar-turno")
    @ResponseBody
    public ResponseEntity<String> solicitarTurno(
            @RequestParam("profesionalId") String profesionalId,
            @RequestParam("fechaSeleccionada") String fechaSeleccionada,
            @RequestParam("horarioSeleccionado") String horarioSeleccionado,
            @RequestParam("motivoConsulta") String motivoConsulta) {

        // Aquí manejas la solicitud de turno, por ejemplo, guardándola en la base de datos

        return ResponseEntity.ok().body("Turno solicitado exitosamente");
    }



}
