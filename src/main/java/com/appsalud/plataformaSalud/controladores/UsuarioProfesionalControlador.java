package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.appsalud.plataformaSalud.servicios.UsuarioProfesionalServicio;
import org.apache.catalina.filters.RemoteIpFilter;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.servicios.UsuarioServicio;
import java.util.Optional;

@Controller
@RequestMapping("/profesional")
public class UsuarioProfesionalControlador {

    @Autowired
    private UsuarioProfesionalServicio usuarioProfesionalServicio;

    @PreAuthorize("hasRole('ROLE_PROFESIONAL')")
    @GetMapping("/dashboard-profesional")
    public String mostrarVistaProfesional() {
        return "profesionalVista.html";
    }
    
    @GetMapping("/registrarProfesional")
    public String registroProfesional(Model model) {
        List<Especialidad> listaEspecialidades = Arrays.stream(Especialidad.values()).collect(Collectors.toList());
        List<ObraSocial> listaObrasSociales = Arrays.stream(ObraSocial.values()).collect(Collectors.toList());
        model.addAttribute("listaEspecialidades", listaEspecialidades);
        model.addAttribute("listaObrasSociales", listaObrasSociales);
        return "registroProfesional.html";
    }

    @PostMapping("/registroProfesional")
    public String registroProfesional(@RequestParam String nombre,
                                      @RequestParam String apellido,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String password2,
                                      @RequestParam Especialidad especialidad,
                                      @RequestParam String descripcionEspecialidad,
                                      @RequestParam Integer valorConsulta,
                                      @RequestParam String matricula,
                                      @RequestParam String dni,
                                      @RequestParam String direccion,
                                      @RequestParam String telefono,
                                      @RequestParam List<ObraSocial> obrasSociales,
                                      RedirectAttributes redirectAttributes) {
        try {
            usuarioProfesionalServicio.crearUsuarioProfesional(nombre, apellido, email, password, password2, especialidad,
                    descripcionEspecialidad, valorConsulta, matricula, dni, direccion, telefono,
                    obrasSociales);
            for (ObraSocial obraSocial : obrasSociales) {
                System.out.println(obraSocial);
            }
            redirectAttributes.addFlashAttribute("exito", "El Usuario fue registrado correctamente!");

            return "redirect:/";
        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registroProfesional";
        }
    }
    
    @PostMapping("/darBaja")
    public String darBajaPaciente(@RequestParam("email") String email) {
        try {
        usuarioProfesionalServicio.anularProfesional(email);
        return "redirect:/dashboard-profesional";
        } catch (Exception e) {
            e.getMessage();
            return "redirect:/dashboard-profesional";
        }
    }
    
    @PostMapping("/modificar")
    public String modificarProfesional(@RequestParam String nombre,
                                      @RequestParam String apellido,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String password2,
                                      @RequestParam Especialidad especialidad,
                                      @RequestParam String descripcionEspecialidad,
                                      @RequestParam Integer valorConsulta,
                                      @RequestParam String matricula,
                                      @RequestParam String dni,
                                      @RequestParam String direccion,
                                      @RequestParam String telefono,
                                      @RequestParam List<ObraSocial> obrasSociales,
                                      Model model) {
        try {
            UsuarioProfesional usuarioProfesional = usuarioProfesionalServicio.buscarProfesionalPorEmail(email).get();
            Integer reputacion = usuarioProfesional.getReputacion();
            usuarioProfesionalServicio.modificarProfesional(nombre, apellido, email, password, password2, especialidad, descripcionEspecialidad, reputacion, valorConsulta, matricula, dni, direccion, telefono, Boolean.TRUE);
        model.addAttribute("exito", "Profesional modificado con exito");
            return "redirect:/dashboard-profesional";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return ""; //aca redirigiria a la vista de modificar profesional.
    }
}