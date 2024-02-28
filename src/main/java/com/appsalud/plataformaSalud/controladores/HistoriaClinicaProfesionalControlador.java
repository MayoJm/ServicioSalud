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

}
