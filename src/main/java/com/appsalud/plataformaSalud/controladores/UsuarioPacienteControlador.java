package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/paciente")
public class UsuarioPacienteControlador {
    @Autowired
    private UsuarioPacienteServicio usuarioPacienteServicio;

    @PreAuthorize("hasRole('ROLE_PACIENTE')")
    @GetMapping("/dashboard-paciente")
    public String mostrarVistaPaciente() {
        return "pacienteVista.html";
    }

    @GetMapping("/registrarPaciente")
    public String registrarPaciente(Model model) {
        List<ObraSocial> listaObrasSociales = Arrays.stream(ObraSocial.values()).collect(Collectors.toList());
        model.addAttribute("listaObrasSociales", listaObrasSociales);
        return "registroPaciente.html";
    }

    @PostMapping("/registroPaciente")
    public String registroPaciente(@RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String password2,
                                   @RequestParam String dni,
                                   @RequestParam String direccion,
                                   @RequestParam String telefono,
                                   @RequestParam ObraSocial obraSocial,
                                   Model model) {
        try {
            usuarioPacienteServicio.crearUsuarioPaciente(nombre, apellido, email, password, password2, obraSocial, dni, direccion, telefono);
            model.addAttribute("exito", "Paciente registrado con exito");
            return "index.html";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "registroPaciente.html";
    }
    @GetMapping("/dashboard-paciente/dar-baja-cuenta")
    public String darBajaCuentaProfesional(Model model) {
        return "darBajaCuentaPaciente.html";
    }
    @PostMapping("/darBaja")
    public String darBajaPaciente(@RequestParam("email") String email) throws Exception {
        try {
            usuarioPacienteServicio.anularPaciente(email);
            return "redirect:/dashboard-paciente";
        } catch (Exception e) {
            e.getMessage();
            return "redirect:/dashboard-paciente";
        }
    }
    @GetMapping("/dashboard-paciente/modificar-paciente")
    public String modificarPaciente(Model model) {

        return "modificarPaciente.html";
    }
    @PostMapping("/modifcar-paciente")
    public String modificarPaciente(@RequestParam String nombre,
                                    @RequestParam String apellido,
                                    @RequestParam String passwordActual,
                                    @RequestParam String nuevoPassword,
                                    @RequestParam String dni,
                                    @RequestParam String direccion,
                                    @RequestParam String telefono,
                                    @RequestParam ObraSocial obraSocial,
                                    Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            if (!usuarioPacienteServicio.verificarPassword(email, passwordActual)) {

                model.addAttribute("error", "La contraseña actual ingresada es incorrecta.");
                return "redirect:/paciente/dashboard-paciente/modificar-paciente";
            }

            // Si el password actual coincide, proceder con la modificación del usuario
            Optional<UsuarioPaciente> usuarioPacienteOptional = usuarioPacienteServicio
                    .buscarPacientePorEmail(email);
            if (usuarioPacienteOptional.isPresent()) {
                UsuarioPaciente usuarioPaciente = usuarioPacienteOptional.get();

                usuarioPacienteServicio.modificarPaciente(nombre, apellido, email, passwordActual, nuevoPassword, obraSocial, dni, direccion, telefono, Boolean.TRUE);
                model.addAttribute("exito", "Paciente modificado con exito");
                return "redirect:/paciente/dashboard-paciente";
            } else {
                model.addAttribute("error", "Paciente no encontrado");
                return "redirect:/paciente/dashboard-paciente";
            }

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/paciente/dashboard-paciente";
        }
    }
    @GetMapping("/dashboard-paciente/buscar-turnos")
    public String buscarTurnosPaciente(Model model) {
        return "buscarTurnosPaciente.html";
    }
}


