package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UsuarioControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @GetMapping("/modificar/{email}")
    public String modificarUsuario(@PathVariable String email){
        return ""; //vista de modificar_usuario.html (pendiente)
    }
    
    @GetMapping("/registrar")
    public String registrar() {
        return "usuario_form.html"; //pendiente
    }
    
     @PostMapping("/registro")
    public String registro(@RequestParam String nombre, String email, String password, String password2, String dni, String direccion, String telefono, ModelMap modelo) {

        try {
            usuarioServicio.crearUsuario(nombre, email, password, password2, dni, direccion, telefono);

            modelo.put("exito", "El Usuario fue registrado correctamente!");
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            return "usuario_form.html";
        }

        return "inicio.html"; //html pendiente
    }
}
