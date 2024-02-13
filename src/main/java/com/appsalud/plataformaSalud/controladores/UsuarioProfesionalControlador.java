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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String mostrarVistaProfesional(Model model) {
        List<Especialidad> listaEspecialidades = Arrays.asList(Especialidad.values());
        model.addAttribute("listaEspecialidades", listaEspecialidades);
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

    @GetMapping("/dashboard-profesional/darBajaCuenta")
    public String darBajaCuentaProfesional(Model model) {
        return "darBajaCuentaProfesional.html";
    }
    @PostMapping("/darBaja")
    public String darBajaPaciente(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
        usuarioProfesionalServicio.anularProfesional(email);
        return "redirect:/";
            usuarioProfesionalServicio.anularProfesional(email);
            return "redirect:/";
        } catch (Exception e) {
            e.getMessage();
            return "redirect:/dashboard-profesional";
        }
    }
@GetMapping("/dashboard-profesional/modificarProfesional")
public String modificarProfesional(Model model) {
    List<Especialidad> listaEspecialidades = Arrays.asList(Especialidad.values());
    model.addAttribute("listaEspecialidades", listaEspecialidades);
        return "modificarProfesional.html";
}
@PostMapping("/profesionalForm")
public String modificarProfesional(@RequestParam String nombre,
    @GetMapping("/dashboard-profesional/modificarProfesional")
    public String modificarProfesional(Model model) {
        List<Especialidad> listaEspecialidades = Arrays.asList(Especialidad.values());
        model.addAttribute("listaEspecialidades", listaEspecialidades);
        return "modificarProfesional.html";
    }
    @PostMapping("/profesionalForm")
    public String modificarProfesional(@RequestParam String nombre,
                                       @RequestParam String apellido,
                                       @RequestParam String passwordActual,
                                       @RequestParam String nuevoPassword,
                                       @RequestParam Especialidad especialidad,
                                       @RequestParam String descripcionEspecialidad,
                                       @RequestParam Integer valorConsulta,
                                       @RequestParam String dni,
                                       @RequestParam String direccion,
                                       @RequestParam String telefono,
                                                            Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            if (!usuarioProfesionalServicio.verificarPassword(email, passwordActual)) {

                model.addAttribute("error", "La contraseña actual ingresada es incorrecta.");
                return "";
            }

            // Si el password actual coincide, proceder con la modificación del usuario
            Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                    .buscarProfesionalPorEmail(email);
            if (usuarioProfesionalOptional.isPresent()) {
                UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
                Integer reputacion = usuarioProfesional.getReputacion();
                String matricula = usuarioProfesional.getMatricula();
                usuarioProfesionalServicio.modificarProfesional(nombre, apellido, email, passwordActual, nuevoPassword,
                        especialidad, descripcionEspecialidad, reputacion, valorConsulta, matricula, dni, direccion, telefono,
                        Boolean.TRUE);
                model.addAttribute("exito", "Profesional modificado con exito");
                return "redirect:/profesional/dashboard-profesional";
            } else {
                model.addAttribute("error", "No se encontró ningún usuario profesional con el email proporcionado.");
                return "redirect:/profesional/dashboard-profesional"; // Puedes redirigir a la vista de modificar profesional o hacer lo que
                // consideres necesario
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard-profesional"; // aca redirigiria a la vista de modificar profesional.
    }

}