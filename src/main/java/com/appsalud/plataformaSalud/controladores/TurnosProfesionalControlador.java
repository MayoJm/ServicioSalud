package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.DisponibilidadHoraria;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.servicios.DisponibilidadHorariaServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioProfesionalServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/profesional/dashboard-profesional/turnos-profesional")
public class TurnosProfesionalControlador {

    @Autowired
    private UsuarioProfesionalServicio usuarioProfesionalServicio;

    @Autowired
    private DisponibilidadHorariaServicio disponibilidadHorariaServicio;

    @GetMapping("/establecer-disponibilidad")
    public String mostrarFormularioEstablecerDisponibilidad(Model model) {

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

}
