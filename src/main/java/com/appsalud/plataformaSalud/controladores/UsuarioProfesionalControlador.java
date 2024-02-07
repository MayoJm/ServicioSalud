package com.appsalud.plataformaSalud.controladores;

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
}