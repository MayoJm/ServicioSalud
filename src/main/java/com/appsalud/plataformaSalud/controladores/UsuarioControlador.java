package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.servicios.UsuarioServicio;
import java.util.List;
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
    public String modificarUsuario(@PathVariable String email, ModelMap modelo){
        
        modelo.put("usuario", usuarioServicio.getOne(email));
        
        return "modificar_usuario.html"; //vista de modificar_usuario.html (pendiente)
    }
    
    @PostMapping("/modificar/{email}")
    public String modificarUsuario(@PathVariable String email, String nombre, String apellido, String password, String password2, ModelMap modelo) {
        try{
         
            usuarioServicio.modificar(nombre, apellido, email, password, password2);
         
            return "inicio.html"; //luego de modificacion exitosa redirige a la vista de inicio
        } catch(MiException ex) {
            modelo.put("error", ex.getMessage());
            
            return "modificar_usuario.html";
        }
    }
    
    @GetMapping("/registrar")
    public String registrar() {
        return "usuario_form.html"; //pendiente
    }
    
     @PostMapping("/registro")
    public String registro(@RequestParam String nombre, String apellido, String email, String password, String password2, ModelMap modelo) {

        try {
            usuarioServicio.crearUsuario(nombre, apellido,  email, password, password2);

            modelo.put("exito", "El Usuario fue registrado correctamente!");
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            return "usuario_form.html";
        }

        return "inicio.html"; //html pendiente
    }
    
    @GetMapping("/lista")
    public String listarUsuarios(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios",usuarios);
        
        return "usuario_list.html"; //pendiente
    }
    
    
}
