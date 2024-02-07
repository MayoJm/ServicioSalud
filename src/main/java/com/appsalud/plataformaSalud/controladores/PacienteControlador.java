package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioProfesionalServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/paciente")
public class PacienteControlador {
    @Autowired
    private UsuarioPacienteServicio usuarioPacienteServicio;
    @PreAuthorize("hasRole('ROLE_PACIENTE')")
    @GetMapping("/dashboard-paciente")
    public String mostrarVistaPaciente() {
        return "pacienteVista.html";
    }

    @GetMapping("/registrarPaciente")
    public String registroProfesional(Model model) {
        List<ObraSocial> listaObrasSociales = Arrays.stream(ObraSocial.values()).collect(Collectors.toList());
        model.addAttribute("listaObrasSociales", listaObrasSociales);
        return "registroProfesional.html";
    }
}
