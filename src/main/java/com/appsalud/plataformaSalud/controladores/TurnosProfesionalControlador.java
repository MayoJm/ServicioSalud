package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.DisponibilidadHoraria;
import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.servicios.DisponibilidadHorariaServicio;
import com.appsalud.plataformaSalud.servicios.TurnoServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioProfesionalServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/profesional/dashboard-profesional/turnos-profesional")
public class TurnosProfesionalControlador {

    @Autowired
    private UsuarioProfesionalServicio usuarioProfesionalServicio;

    @Autowired
    private DisponibilidadHorariaServicio disponibilidadHorariaServicio;
    @Autowired
    private TurnoServicio turnoServicio;
    @Autowired
    private UsuarioPacienteServicio usuarioPacienteServicio;

    @GetMapping("/establecer-disponibilidad")
    public String mostrarFormularioEstablecerDisponibilidad(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        model.addAttribute("usuarioProfesional", usuarioProfesional);
        return "disponibilidad.html";
    }

    @PostMapping("/establecer-disponibilidad-form")
    public String establecerDisponibilidad(@RequestParam Map<String, String> formData,
            RedirectAttributes redirectAttributes) {
        try {
            // Obtener el profesional actual desde la sesión
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                    .buscarProfesionalPorEmail(email);
            UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();

            // Crear una lista para almacenar las disponibilidades
            List<DisponibilidadHoraria> disponibilidades = new ArrayList<>();

            // Iterar sobre el formData para obtener los datos de cada día de la semana
            for (DayOfWeek diaSemana : DayOfWeek.values()) {
                // Obtener los nombres de los campos para este día de la semana
                String horaInicioKey = diaSemana.toString().toLowerCase() + "Inicio";
                String horaFinKey = diaSemana.toString().toLowerCase() + "Fin";

                // Verificar si los campos para este día están presentes en el formData y si
                // contienen valores no vacíos
                if (formData.containsKey(horaInicioKey) && formData.containsKey(horaFinKey)
                        && !formData.get(horaInicioKey).isEmpty() && !formData.get(horaFinKey).isEmpty()) {
                    // Obtener los horarios de inicio y fin para este día de la semana
                    String horaInicioStr = formData.get(horaInicioKey);
                    String horaFinStr = formData.get(horaFinKey);

                    // Convertir las cadenas de hora en LocalTime
                    LocalTime horaInicio = LocalTime.parse(horaInicioStr);
                    LocalTime horaFin = LocalTime.parse(horaFinStr);

                    // Crear una instancia de DisponibilidadHoraria y agregarla a la lista
                    DisponibilidadHoraria disponibilidad = new DisponibilidadHoraria();
                    disponibilidad.setUsuarioProfesional(usuarioProfesional);
                    disponibilidad.setDiaSemana(diaSemana);
                    disponibilidad.setHoraInicio(horaInicio);
                    disponibilidad.setHoraFin(horaFin);
                    disponibilidades.add(disponibilidad);
                }
            }

            // Guardar las disponibilidades en la base de datos si hay al menos una
            // disponibilidad válida
            if (!disponibilidades.isEmpty()) {
                disponibilidadHorariaServicio.guardarDisponibilidadProfesional(disponibilidades, usuarioProfesional);
                redirectAttributes.addFlashAttribute("exito", "Disponibilidad establecida correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "Debes proporcionar la hora de inicio y fin para al menos un día.");
            }

            return "redirect:/profesional/dashboard-profesional";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un problema al establecer la disponibilidad.");
            return "redirect:/profesional/dashboard-profesional";
        }
    }

    @GetMapping("/disponibles")
    public String mostrarFormularioTurnosDisponibles(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional;
        usuarioProfesional = usuarioProfesionalOptional.get();
        model.addAttribute("usuarioProfesional", usuarioProfesional);

        List<Turno> turnos = turnoServicio.listarTurnosPorProfesional(email);
        model.addAttribute("turnos", turnos);
        return "turnosDisponiblesProfesional.html";
    }

    @GetMapping("/disponibles/modificar-turno/{id}")
    public String modificarTurno(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String turnoId = id;
        Turno turno = turnoServicio.getOne(turnoId);
        UsuarioPaciente usuarioPaciente = (UsuarioPaciente)turno.getUsuarioPaciente();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        String profesionalId = usuarioProfesional.getId();
        // model.addAttribute(usuarioProfesional);
        model.addAttribute("profesionalId", profesionalId);
        model.addAttribute("usuarioProfesional", usuarioProfesional);
        model.addAttribute("turnoId", turnoId);
        model.addAttribute("usuaraioPaciente", usuarioPaciente);
        return "modificarTurno.html";
    }

    @GetMapping("/disponibles/modificar-turno/horarios-disponibles")
    @ResponseBody
    public ResponseEntity<List<DisponibilidadHoraria>> obtenerHorariosDisponibles(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSeleccionada)
            throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional profesional = usuarioProfesionalOptional.get();

        List<DisponibilidadHoraria> horariosDisponibles = turnoServicio.obtenerHorariosDisponiblesParaDia(profesional,
                fechaSeleccionada);
        return ResponseEntity.ok().body(horariosDisponibles);
    }

    @PostMapping("/disponibles/modificar")
    public String modificarTurno(@RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora, @RequestParam("turnoId") String turnoId,
            @RequestParam("descripcion") String descripcion, Model model,
            RedirectAttributes redirectAttributes) throws MiException {
        try {
            String horaInicioStr = hora.split(" - ")[0];
            String fechaHoraStr = fecha + " " + horaInicioStr;
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            turnoServicio.modificarTurno(turnoId, fechaHora, descripcion);
            redirectAttributes.addFlashAttribute("exito", "Turno modificado con exito");
            return "redirect:/profesional/dashboard-profesional/turnos-profesional/disponibles";
        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un problema al modificar el turno");
            return "redirect:/profesional/dashboard-profesional/turnos-profesional/disponibles";
        }
    }

    @GetMapping("/disponibles/cancelar-turno/{id}")
    public String cancelarTurno(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        String turnoId = id;
        turnoServicio.eliminarTurno(turnoId);
        redirectAttributes.addFlashAttribute("exito", "Turno cancelado con exito");
        return "redirect:/profesional/dashboard-profesional/turnos-profesional/disponibles";
    }

    @GetMapping("/buscar-paciente")
    public String buscarPaciente(Model model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        List<UsuarioPaciente> pacientes = usuarioPacienteServicio.listarPacientes();
        if (pacientes.isEmpty()) {
            model.addAttribute("error",
                    "No hay pacientes disponibles en este momento. Por favor, intente más tarde.");
        } else {
            model.addAttribute("pacientes", pacientes);
        }
        model.addAttribute("usuarioProfesional", usuarioProfesional);
        return "buscar-paciente.html";
    }

    @GetMapping("/seleccionar-paciente/calendar/{pacienteId}")
    public String mostrarCalendario(Model model, @PathVariable("pacienteId") String pacienteId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        UsuarioPaciente paciente = usuarioPacienteServicio.buscarPorId(pacienteId);
        System.out.println(paciente.getApellido());
        model.addAttribute("usuarioProfesional", usuarioProfesional);
        model.addAttribute("paciente", paciente);
        return "calendar.html";
    }

    @PostMapping("/seleccionar-paciente/calendar/reservar-turno")
    public String crearTurno(@RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora, @RequestParam("pacienteId") String pacienteId,
            @RequestParam("descripcion") String descripcion, Model model,
            RedirectAttributes redirectAttributes) throws MiException, ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String horaInicioStr = hora.split(" - ")[0];
            String fechaHoraStr = fecha + " " + horaInicioStr;
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            UsuarioPaciente usuarioPaciente = usuarioPacienteServicio.buscarPorId(pacienteId);
            turnoServicio.solicitarTurnoPorProfesional(usuarioPaciente, usuarioProfesional, fechaHora, descripcion);
            redirectAttributes.addFlashAttribute("exito", "Turno creado con exito");
            return "redirect:/profesional/dashboard-profesional";
        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un problema al crear el turno");
            return "redirect:/profesional/dashboard-profesional";
        }
    }
}
