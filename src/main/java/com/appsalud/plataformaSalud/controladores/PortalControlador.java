package com.appsalud.plataformaSalud.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.appsalud.plataformaSalud.servicios.UsuarioServicio;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }
        return "index.html";
    }

    @GetMapping("/tipoUsuario")
    public String tipoUsuario() {
        return "tipoUsuario.html";
    }

    @PostMapping("/tipoUsuario")
    public String tipoUsuario(@RequestParam("tipoUsuario") String tipoUsuario) {

        switch (tipoUsuario) {
            case "paciente":
                return "redirect:/paciente/registrarPaciente";
            case "profesional":
                return "redirect:/profesional/registrarProfesional";
            default:
                return "redirect:/tipoUsuario";
        }
    }
}
