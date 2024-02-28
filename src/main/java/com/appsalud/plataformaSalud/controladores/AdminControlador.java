package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.servicios.AdminServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioProfesionalServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioServicio;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioProfesionalServicio usuarioProfesionalServicio;
    @Autowired
    private AdminServicio adminServicio;
    @Autowired
    private UsuarioPacienteServicio usuarioPacienteServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard-admin")
    public String mostrarVistaAdmin(Model model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorEmail(email);
        Usuario usuarioAdmin = usuarioOptional.get();
        model.addAttribute("usuarioAdmin", usuarioAdmin);

        return "adminVista.html";
    }

    @GetMapping("/dashboard-admin/gestion-paciente")
    public String listaPacientes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorEmail(email);
        Usuario usuarioAdmin = usuarioOptional.get();
        model.addAttribute("usuarioAdmin", usuarioAdmin);
        List<UsuarioPaciente> pacientes = adminServicio.listarUsuariosPacientesPendientes();
        if (pacientes.isEmpty()) {
            model.addAttribute("exito", "No se han encontrado pacientes para aprobar.");
        } else {
            model.addAttribute("pacientes", pacientes);
        }
        return "adminGestion-pacientes.html";
    }

    @GetMapping("/dashboard-admin/modificar-estado-paciente/{email}")
    public String modificarPaciente(Model model, @PathVariable String email) throws MiException {
        Optional<UsuarioPaciente> usuarioPacienteOptional = usuarioPacienteServicio.buscarPacientePorEmail(email);
        UsuarioPaciente usuarioPaciente = usuarioPacienteOptional.get();
        if (usuarioPaciente != null) {
            usuarioPacienteServicio.altaPaciente(email);
            model.addAttribute("usuarioPaciente", usuarioPaciente);
            return "redirect:/admin/dashboard-admin/gestion-paciente";
        } else {
            model.addAttribute("error", "No se ha encontrado al paciente solicitado.");
            return "redirect:/admin/dashboard-admin/gestion-paciente";
        }
    }

    @GetMapping("dashboard-admin/pacientes-inactivos")
    public String listaPacientesBajados(Model model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorEmail(email);
        Usuario usuarioAdmin = usuarioOptional.get();
        model.addAttribute("usuarioAdmin", usuarioAdmin);
        List<UsuarioPaciente> pacientes = adminServicio.listarUsuariosPacientesInactivos();
        if (pacientes.isEmpty()) {
            model.addAttribute("error",
                    "No hay pacientes inactivos en este momento. Por favor, intente más tarde.");
        } else {
            model.addAttribute("pacientes", pacientes);
        }
        return "adminGestion-pacientesActivos.html";
    }

    @GetMapping("dashboard-admin/pacientes-activos")
    public String listaPacientesActivos(Model model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorEmail(email);
        Usuario usuarioAdmin = usuarioOptional.get();
        model.addAttribute("usuarioAdmin", usuarioAdmin);
        List<UsuarioPaciente> pacientes = adminServicio.listarUsuariosPacientesActivos();
        if (pacientes.isEmpty()) {
            model.addAttribute("error",
                    "No hay pacientes activos en este momento. Por favor, intente más tarde.");
        } else {
            model.addAttribute("pacientes", pacientes);
        }
        return "adminGestion-pacientesActivos.html";
    }

    @GetMapping("/dashboard-admin/gestion-profesional")
    public String listaProfesionales(Model model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorEmail(email);
        Usuario usuarioAdmin = usuarioOptional.get();
        model.addAttribute("usuarioAdmin", usuarioAdmin);
        List<UsuarioProfesional> profesionales = adminServicio.listarUsuariosProfesionalesPendientes();
        if (profesionales.isEmpty()) {
            model.addAttribute("exito", "No se han encontrado profesionales para aprobar.");
        }
        model.addAttribute("profesionales", profesionales);
        return "adminGestion-profesionales.html";
    }

    @GetMapping("/dashboard-admin/modificar-estado-profesional/{email}")
    public String modificarProfesional(Model model, @PathVariable String email) throws MiException {
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        if (usuarioProfesional != null) {
            usuarioProfesionalServicio.darAltaProfesional(email);
            model.addAttribute("usuarioProfesional", usuarioProfesional);
            return "redirect:/admin/dashboard-admin/gestion-profesional";
        } else {
            model.addAttribute("error", "No se ha encontrado al profesional solicitado.");
            return "redirect:/admin/dashboard-admin/gestion-profesional";
        }
    }

    @GetMapping("dashboard-admin/profesionales-inactivos")
    public String listaProfesionalesBajados(Model model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorEmail(email);
        Usuario usuarioAdmin = usuarioOptional.get();
        model.addAttribute("usuarioAdmin", usuarioAdmin);
        List<UsuarioProfesional> profesionales = adminServicio.listarUsuariosProfesionalesInactivos();
        if (profesionales.isEmpty()) {
            model.addAttribute("error",
                    "No hay profesionales inactivos en este momento. Por favor, intente más tarde.");
        } else {
            model.addAttribute("profesionales", profesionales);
        }
        return "adminGestion-profesionalesActivos.html";
    }

    @GetMapping("dashboard-admin/profesionales-activos")
    public String listaProfesionalesActivos(Model model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorEmail(email);
        Usuario usuarioAdmin = usuarioOptional.get();
        model.addAttribute("usuarioAdmin", usuarioAdmin);
        List<UsuarioProfesional> profesionales = adminServicio.listarUsuariosProfesionalesActivos();
        if (profesionales.isEmpty()) {
            model.addAttribute("error",
                    "No hay profesionales Activos en este momento. Por favor, intente más tarde.");
        } else {
            model.addAttribute("profesionales", profesionales);
        }
        return "adminGestion-profesionalesActivos.html";
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) throws IOException {
        Usuario usuario = usuarioServicio.getOne(id);
        if (usuario.getImagen() == null) {
            usuarioServicio.guardarDefault(usuario);

            byte[] imagen = usuario.getImagen().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);

        } else {
            byte[] imagen = usuario.getImagen().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
        }
    }

    @GetMapping("/dashboard-admin/cambiarImagen")
    public String cambiarImagen(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorEmail(email);
        String rolUsuario = authentication.getAuthorities().iterator().next().getAuthority();

        model.addAttribute("rolUsuario", rolUsuario);
        if (usuarioOptional.isPresent()) {
            Usuario usuarioAdmin = usuarioOptional.get();
            model.addAttribute("usuarioAdmin", usuarioAdmin);
            return "cambiarImagen.html";
        } else {
            model.addAttribute("error", "No se encontró ningún usuario admin con el email proporcionado.");
            return "redirect:/admin/dashboard-admin";
        }
    }

    @PostMapping("/dashboard-admin/cambiarImagen")
    public String cambiarImagen(@RequestParam MultipartFile archivo, ModelMap model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (!email.isEmpty()) {

            try {
                usuarioServicio.guardarImagen(email, archivo);
                model.put("exito", "La imagen se a guardado con exito");

                return "redirect:/admin/dashboard-admin";
            } catch (MiException ex) {
                model.put("error", ex.getMessage());
                return "redirect:/admin/dashboard-admin";
            }
        } else {
            model.addAttribute("error", "No se encontró ningún usuario admin con el email proporcionado.");
            return "redirect:/admin/dashboard-admin";
        }
    }

}
