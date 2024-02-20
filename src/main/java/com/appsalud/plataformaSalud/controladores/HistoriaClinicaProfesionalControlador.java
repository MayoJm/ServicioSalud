package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.servicios.HistoriaClinicaServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profesional/dashboard-profesional/historia-clinica-profesional")
public class HistoriaClinicaProfesionalControlador {

    @Autowired
    UsuarioProfesionalServicio usuarioProfesionalServicio;

    @Autowired
    HistoriaClinicaServicio historiaClinicaServicio;

    @Autowired
    UsuarioPacienteServicio usuarioPacienteServicio;

    @GetMapping("/historia-clinica-form")
    public String mostrarHistoriaClinica(Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);

        if (usuarioProfesionalOptional.isPresent()) {
            UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
            modelo.addAttribute("profesional", usuarioProfesional);
        }
        return "historiaClinicaForm.html";
    }

    @PostMapping("/historia-clinica")
    public String registrarHistoriaClinica(Model modelo,
                                                           @RequestParam("pacienteId") String dni,
                                                           @RequestParam("profesionalId") String profesionalId,
                                                           @RequestParam("nombre") String nombre,
                                                           @RequestParam("edad") Integer edad,
                                                           @RequestParam("sexo") String sexo,
                                                           @RequestParam("peso") Double peso,
                                                           @RequestParam("datosHistoricos") List<String> datosHistoricos,
                                                           @RequestParam("fechaConsulta") List<LocalDate> fechaConsulta)
            throws MiException {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            //Obtengo profesional
            Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                    .buscarProfesionalPorEmail(email);
            if (!usuarioProfesionalOptional.isPresent()) {
                modelo.addAttribute("error", "No se encontró un profesional con este email");
            }
            UsuarioProfesional profesional = usuarioProfesionalOptional.get();

            //Obtengo paciente
            Optional<UsuarioPaciente> paciente = usuarioPacienteServicio.buscarPacientePorDni(dni);

            if (paciente.isPresent()) {
                UsuarioPaciente usuarioPaciente = paciente.get();
                historiaClinicaServicio.crearHistoriaClinica(paciente.get(), profesional, nombre,
                        edad, sexo, peso, datosHistoricos, fechaConsulta, false);
                modelo.addAttribute("exito", "Historia clinica registrada correctamente");
                return "vistaProfesional";
            } else {
                modelo.addAttribute("error", "No se encontró un paciente con este dni");
                return "/profesional/dashboard-profesional/historia-clinica-profesional/historia-clinica-form";
            }

        } catch (MiException ex) {
            ex.getMessage();
            modelo.addAttribute("error", ex.getMessage());
            return "/profesional/dashboard-profesional/historia-clinica-profesional/historia-clinica-form";
        }
    }
}
