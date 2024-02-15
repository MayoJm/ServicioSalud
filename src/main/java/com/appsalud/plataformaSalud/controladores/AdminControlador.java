package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioPacienteServicio usuarioPacienteServicio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard-admin")
    public String mostrarVistaAdmin() {
        return "adminVista.html"; //pendiente
    }

//    //desde dashboard a paciente
//    @GetMapping("/dashboard-admin/paciente")
//    public String adminPaciente() {
//        return "adminPaciente.html"; //pendiente
//    }

//    //desde dashboard a paciente
//    @GetMapping("/dashboard-admin/profesional")
//    public String adminProfesional() {
//        return "adminProfesional.html"; //pendiente
//    }
    
    @GetMapping("/dashboard-admin/listaPacientes")
    public String listaPacientes(Model model) {
        List<Usuario> pacientes = usuarioPacienteServicio.listarUsuariosPaciente();
        
            model.addAttribute("pacientes", pacientes);
            
            return "adminVista.html";
        
    }
    
//    @GetMapping("/dashboard-admin/profesional/buscarProfesional/{dni}") //revisar path
//public String buscarProfesionalPorDni(@PathVariable("dni") String dni, Model model) {
//
//        Optional<UsuarioPaciente> paciente = usuarioPacienteServicio.buscarPacientePorDni(dni);
//
//        if (paciente.isPresent()) {
//            model.addAttribute("paciente", paciente.get());
//            return "redirect:/dashboard-admin/paciente";
//        } else {
//            model.addAttribute("error", "El paciente con dni " + dni + " no ha sido encontrado");
//            return "redirect:/dashboard-admin/paciente";
//        }
//    }
}
