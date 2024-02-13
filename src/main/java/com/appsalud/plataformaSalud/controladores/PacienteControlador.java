package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "registroPaciente.html";
    }

    @PostMapping("/darBaja")
    public String darBajaPaciente(@RequestParam("email") String email) {
        try {
            usuarioPacienteServicio.anularPaciente(email);
            return "redirect:/dashboard-paciente";
        } catch (Exception e) {
            e.getMessage();
            return "redirect:/dashboard-paciente";
        }
    }

    @PostMapping("/modificar")
    public String modificarPaciente(@RequestParam String nombre,
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
            usuarioPacienteServicio.modificarPaciente(nombre, apellido, email, password, password2, obraSocial, dni, direccion, telefono, Boolean.TRUE);
            model.addAttribute("exito", "Paciente modificado con exito");
            return "redirect:/dashboard-paciente";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return ""; //aca redirigiria a la vista de modificar paciente.
    }
}

