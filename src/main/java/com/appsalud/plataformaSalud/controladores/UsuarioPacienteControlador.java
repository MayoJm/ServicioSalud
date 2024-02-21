package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioServicio;

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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/paciente")
public class UsuarioPacienteControlador {
    @Autowired
    private UsuarioPacienteServicio usuarioPacienteServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasRole('ROLE_PACIENTE')")
    @GetMapping("/dashboard-paciente")
    public String mostrarVistaPaciente(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioPaciente> usuarioPacienteOptional = usuarioPacienteServicio
                .buscarPacientePorEmail(email);
        UsuarioPaciente usuarioPaciente = usuarioPacienteOptional.get();
        model.addAttribute("usuarioPaciente", usuarioPaciente);
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
            usuarioPacienteServicio.crearUsuarioPaciente(nombre, apellido, email, password, password2, obraSocial, dni,
                    direccion, telefono);
            model.addAttribute("exito", "Paciente registrado con exito");
            return "redirect:/";
        } catch (Exception e) {
            List<ObraSocial> listaObrasSociales = Arrays.stream(ObraSocial.values()).collect(Collectors.toList());
            model.addAttribute("listaObrasSociales", listaObrasSociales);
            model.addAttribute("error", e.getMessage());
        }
        return "registroPaciente.html";
    }

    @GetMapping("/dashboard-paciente/dar-baja-cuenta")
    public String darBajaCuentaProfesional(Model model) {
        return "darBajaCuentaPaciente.html";
    }

    @PostMapping("/darBaja")
    public String darBajaPaciente() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        try {
            usuarioPacienteServicio.anularPaciente(email);
            return "redirect:/";
        } catch (Exception e) {
            e.getMessage();
            return "redirect:/dashboard-paciente";
        }
    }

    @GetMapping("/dashboard-paciente/modificar-paciente")
    public String modificarPaciente(Model model) {

        List<Especialidad> listaEspecialidades = Arrays.asList(Especialidad.values());
        model.addAttribute("listaEspecialidades", listaEspecialidades);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioPaciente> usuarioPacienteOptional = usuarioPacienteServicio
                .buscarPacientePorEmail(email);
        UsuarioPaciente usuarioPaciente = usuarioPacienteOptional.get();
        model.addAttribute("usuarioPaciente", usuarioPaciente);

        return "modificarPaciente.html";
    }

    @PostMapping("/modificar-paciente")
    public String modificarPaciente(@RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String passwordActual,
            @RequestParam String nuevoPassword,
            @RequestParam String dni,
            @RequestParam String direccion,
            @RequestParam String telefono,
            @RequestParam ObraSocial obraSocial,
            Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            if (!usuarioPacienteServicio.verificarPassword(email, passwordActual)) {

                model.addAttribute("error", "La contraseña actual ingresada es incorrecta.");
                return "redirect:/paciente/dashboard-paciente/modificar-paciente";
            }

            // Si el password actual coincide, proceder con la modificación del usuario
            Optional<UsuarioPaciente> usuarioPacienteOptional = usuarioPacienteServicio
                    .buscarPacientePorEmail(email);
            if (usuarioPacienteOptional.isPresent()) {
                usuarioPacienteServicio.modificarPaciente(nombre, apellido, email, passwordActual, nuevoPassword,
                        obraSocial, dni, direccion, telefono, Boolean.TRUE);
                model.addAttribute("exito", "Paciente modificado con exito");
                return "redirect:/paciente/dashboard-paciente";
            } else {
                model.addAttribute("error", "Paciente no encontrado");
                return "redirect:/paciente/dashboard-paciente";
            }

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/dashboard-paciente/modificarPaciente";
        }
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

    @GetMapping("/dashboard-paciente/cambiarImagen")
    public String cambiarImagen(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UsuarioPaciente> usuarioPacienteOptional = usuarioPacienteServicio
                .buscarPacientePorEmail(email);
        String rolUsuario = authentication.getAuthorities().iterator().next().getAuthority();

        model.addAttribute("rolUsuario", rolUsuario);
        if (usuarioPacienteOptional.isPresent()) {
            UsuarioPaciente usuarioPaciente = usuarioPacienteOptional.get();
            model.addAttribute("usuarioPaciente", usuarioPaciente);
            return "cambiarImagen.html";
        } else {
            model.addAttribute("error", "No se encontró ningún usuario paciente con el email proporcionado.");
            return "redirect:/paciente/dashboard-paciente";
        }
    }

    @PostMapping("/dashboard-paciente/cambiarImagen")
    public String cambiarImagen(@RequestParam MultipartFile archivo, ModelMap model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if (!email.isEmpty()) {

            try {
                usuarioServicio.guardarImagen(email, archivo);
                model.put("exito", "La imagen se a guardado con exito");

                return "redirect:/paciente/dashboard-paciente";
            } catch (MiException ex) {
                model.put("error", ex.getMessage());
                return "redirect:/paciente/dashboard-paciente";
            }
        } else {
            model.addAttribute("error", "No se encontró ningún usuario paciente con el email proporcionado.");
            return "redirect:/paciente/dashboard-paciente";
        }
    }

}
