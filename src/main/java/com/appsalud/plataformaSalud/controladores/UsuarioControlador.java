package com.appsalud.plataformaSalud.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UsuarioControlador {

   

    @GetMapping("/modificar/{email}")
    public String modificarUsuario(@PathVariable String email) {
        return ""; //vista de modificar_usuario.html (pendiente)
    }

   
}
