package com.appsalud.plataformaSalud.controladores;

import com.appsalud.plataformaSalud.entidades.*;
import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.servicios.HistoriaClinicaServicio;
import com.appsalud.plataformaSalud.servicios.TurnoServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioProfesionalServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profesional")
public class UsuarioProfesionalControlador {

    @Autowired
    private UsuarioProfesionalServicio usuarioProfesionalServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private TurnoServicio turnoServicio;
    @Autowired
    private HistoriaClinicaServicio historiaClinicaServicio;
    @Autowired
    private UsuarioPacienteServicio usuarioPacienteServicio;

    @PreAuthorize("hasRole('ROLE_PROFESIONAL')")
    @GetMapping("/dashboard-profesional")
    public String mostrarVistaProfesional(Model model) {
        List<Especialidad> listaEspecialidades = Arrays.asList(Especialidad.values());
        model.addAttribute("listaEspecialidades", listaEspecialidades);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);

        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        model.addAttribute("usuarioProfesional", usuarioProfesional);
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
            Model model) {
        try {
            usuarioProfesionalServicio.crearUsuarioProfesional(nombre, apellido, email, password, password2,
                    especialidad,
                    descripcionEspecialidad, valorConsulta, matricula, dni, direccion, telefono,
                    obrasSociales);

            model.addAttribute("exito", "El Usuario fue registrado correctamente!");

            return "redirect:/";
        } catch (MiException e) {
            List<Especialidad> listaEspecialidades = Arrays.stream(Especialidad.values()).collect(Collectors.toList());
            List<ObraSocial> listaObrasSociales = Arrays.stream(ObraSocial.values()).collect(Collectors.toList());
            model.addAttribute("listaEspecialidades", listaEspecialidades);
            model.addAttribute("listaObrasSociales", listaObrasSociales);
            model.addAttribute("error", e.getMessage());
            return "registroProfesional.html";
        }
    }

    @GetMapping("/dashboard-profesional/dar-baja-cuenta")
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
        } catch (Exception e) {
            e.getMessage();
            return "redirect:/dashboard-profesional";
        }
    }

    @GetMapping("/dashboard-profesional/modificar-profesional")
    public String modificarProfesional(Model model) {
        List<Especialidad> listaEspecialidades = Arrays.asList(Especialidad.values());
        model.addAttribute("listaEspecialidades", listaEspecialidades);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);

        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        model.addAttribute("usuarioProfesional", usuarioProfesional);

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
                return "redirect:/profesional/dashboard-profesional/modificarProfesional";
            }

            // Si el password actual coincide, proceder con la modificación del usuario
            Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                    .buscarProfesionalPorEmail(email);
            if (usuarioProfesionalOptional.isPresent()) {
                UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
                Integer reputacion = usuarioProfesional.getReputacion();
                String matricula = usuarioProfesional.getMatricula();
                usuarioProfesionalServicio.modificarProfesional(nombre, apellido, email, passwordActual, nuevoPassword,
                        especialidad, descripcionEspecialidad, reputacion, valorConsulta, matricula, dni, direccion,
                        telefono,
                        Boolean.TRUE);
                model.addAttribute("exito", "Profesional modificado con exito");
                return "redirect:/profesional/dashboard-profesional";
            } else {
                model.addAttribute("error", "No se encontró ningún usuario profesional con el email proporcionado.");
                return "redirect:/profesional/dashboard-profesional"; // Puedes redirigir a la vista de modificar
                // profesional o hacer lo que
                // consideres necesario
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            List<Especialidad> listaEspecialidades = Arrays.asList(Especialidad.values());
            model.addAttribute("listaEspecialidades", listaEspecialidades);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                    .buscarProfesionalPorEmail(email);
            UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
            model.addAttribute("usuarioProfesional", usuarioProfesional);
        }
        return "modificarProfesional.html"; // aca redirigiria a la vista de modificar profesional.
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

    @GetMapping("/dashboard-profesional/cambiarImagen")
    public String cambiarImagen(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        String rolUsuario = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rolUsuario", rolUsuario);

        if (usuarioProfesionalOptional.isPresent()) {
            UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
            model.addAttribute("usuarioProfesional", usuarioProfesional);
            return "cambiarImagen.html";
        } else {
            model.addAttribute("error", "No se encontró ningún usuario profesional con el email proporcionado.");
            return "redirect:/profesional/dashboard-profesional";
        }

    }

    @PostMapping("/dashboard-profesional/cambiarImagen")
    public String cambiarImagen(@RequestParam MultipartFile archivo, ModelMap model) throws MiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if (!email.isEmpty()) {

            try {
                usuarioServicio.guardarImagen(email, archivo);
                model.put("exito", "La imagen se a guardado con exito");

                return "redirect:/profesional/dashboard-profesional";
            } catch (MiException ex) {
                model.put("error", ex.getMessage());
                return "redirect:/profesional/dashboard-profesional";
            }
        } else {
            model.addAttribute("error", "No se encontró ningún usuario profesional con el email proporcionado.");
            return "redirect:/profesional/dashboard-profesional";
        }
    }

    @GetMapping("/dashboard-profesional/mis-pacientes")
    public String mostrarMisPaciente(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<Turno> turnos = turnoServicio.listarTurnosPorProfesional(email);
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        List<UsuarioPaciente> pacientes = usuarioProfesionalServicio.obtenerPacientes(email, turnos);
        model.addAttribute("pacientes", pacientes);
        model.addAttribute("usuarioProfesional", usuarioProfesional);

        return "misPacientes.html";
    }

    @PostMapping("/dashboard-profesional/mis-pacientes/seleccionar-paciente")
    public String mostrarInfoPaciente(@RequestParam("pacienteId") String pacienteId,
            RedirectAttributes redirectAttributes, Model model) {
        System.out.println("Paciente id: " + pacienteId);
        System.out.println(
                "                                                                                                                                ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional profesional = usuarioProfesionalOptional.get();
        UsuarioPaciente paciente = usuarioPacienteServicio.buscarPorId(pacienteId);
        boolean tieneHistoriaClinica = true;
        tieneHistoriaClinica = historiaClinicaServicio.tieneHistoriaClinica(paciente, profesional);

        if (tieneHistoriaClinica) {
            redirectAttributes.addAttribute("pacienteId", pacienteId);
            return "redirect:/profesional/dashboard-profesional/mis-pacientes/seleccionar-paciente/ver-historia-clinica";
        } else {
            redirectAttributes.addAttribute("pacienteId", pacienteId);
            return "redirect:/profesional/dashboard-profesional/mis-pacientes/seleccionar-paciente/crear-historia-clinica";
        }
    }

    @GetMapping("/dashboard-profesional/mis-pacientes/seleccionar-paciente/ver-historia-clinica")
    public String mostrarVistaVisualizacion(@RequestParam("pacienteId") String pacienteId, Model model) {
        UsuarioPaciente paciente = usuarioPacienteServicio.buscarPorId(pacienteId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        Optional<HistoriaClinica> historiaClinicaOptional = historiaClinicaServicio.obtenerHistoriaClinica(paciente,
                usuarioProfesional);
        if (historiaClinicaOptional.isPresent()) {
            HistoriaClinica historiaClinica = historiaClinicaOptional.get();
            model.addAttribute("paciente", paciente);
            model.addAttribute("historiaClinica", historiaClinica);
            model.addAttribute("usuarioProfesional", usuarioProfesional);
            return "historia-clinica-paciente.html";

        } else {
            model.addAttribute("error", "No se encontró ninguna historia clínica para el paciente seleccionado.");
            return "redirect:/dashboard-profesional/mis-pacientes";
        }

    }

    @GetMapping("/dashboard-profesional/mis-pacientes/seleccionar-paciente/crear-historia-clinica")
    public String mostrarVistaCreacion(Model model, @RequestParam("pacienteId") String pacienteId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional usuarioProfesional = usuarioProfesionalOptional.get();
        model.addAttribute("pacienteId", pacienteId);
        model.addAttribute("usuarioProfesional", usuarioProfesional);
        return "crearHistoriaClinica.html";
    }

    @PostMapping("/dashboard-profesional/mis-pacientes/historia-clinica-crear")
    public String crearHistoriaClinica(
            @RequestParam("pacienteId") String pacienteId,
            @RequestParam("edad") Integer edad,
            @RequestParam("peso") double peso,
            @RequestParam("sexo") String sexo,
            @RequestParam("fechaConsulta") LocalDate fechaConsulta,
            @RequestParam("notas") String notas,
            Model model,
            RedirectAttributes redirectAttributes) {
        System.out.println("Paciente id: " + pacienteId);
        System.out.println("Edad: " + edad);
        System.out.println("Peso: " + peso);
        System.out.println("Sexo: " + sexo);
        System.out.println("Fecha consulta: " + fechaConsulta);
        System.out.println("Notas: " + notas);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                    .buscarProfesionalPorEmail(email);
            UsuarioProfesional profesional = usuarioProfesionalOptional.get();
            UsuarioPaciente paciente = usuarioPacienteServicio.buscarPorId(pacienteId);
            String nombre = paciente.getNombre();
            historiaClinicaServicio.crearHistoriaClinica(paciente, profesional, nombre, edad, sexo, peso, notas,
                    fechaConsulta, true);
            redirectAttributes.addFlashAttribute("exito", "Historia clinica creada con exito");
            return "redirect:/profesional/dashboard-profesional/mis-pacientes";
        } catch (MiException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/profesional/dashboard-profesional/mis-pacientes";
        }

    }

    @PostMapping("/dashboard-profesional/mis-pacientes/seleccionar-paciente/historia-clinica-editar")
    public String editarHistoriaClinica(
            @RequestParam("pacienteId") String pacienteId,
            @RequestParam("nombre") String nombre,
            @RequestParam("edad") Integer edad,
            @RequestParam("sexo") String sexo,
            @RequestParam("peso") double peso,
            @RequestParam("nuevoDatoHistorico") String nuevoDatoHistorico,
            @RequestParam("nuevaFechaConsulta") LocalDate nuevaFechaConsulta,
            RedirectAttributes redirectAttributes, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<UsuarioProfesional> usuarioProfesionalOptional = usuarioProfesionalServicio
                .buscarProfesionalPorEmail(email);
        UsuarioProfesional profesional = usuarioProfesionalOptional.get();

        try {
            UsuarioPaciente paciente = usuarioPacienteServicio.buscarPorId(pacienteId);
            Optional<HistoriaClinica> historiaClinicaOptional = historiaClinicaServicio.obtenerHistoriaClinica(paciente,
                    profesional);

            if (paciente != null && profesional != null && historiaClinicaOptional.isPresent()) {
                HistoriaClinica historiaClinica = historiaClinicaOptional.get();
                List<LocalDate> fechasConsulta = historiaClinica.getFechaConsulta();
                List<String> datosHistoricos = historiaClinica.getDatosHistoricos();
                fechasConsulta.add(nuevaFechaConsulta);
                datosHistoricos.add(nuevoDatoHistorico);
                historiaClinicaServicio.modificarHistoriaClinica(historiaClinica, nombre, edad, sexo, peso,
                        datosHistoricos,
                        fechasConsulta);
                redirectAttributes.addFlashAttribute("exito", "Historia clinica modificada con exito");
                return "redirect:/profesional/dashboard-profesional/mis-pacientes";
            } else {
                redirectAttributes.addAttribute("error",
                        "No se encontró ninguna historia clínica para el paciente seleccionado.");
                return "redirect:/profesional/dashboard-profesional/mis-pacientes";
            }

        } catch (MiException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/profesional/dashboard-profesional/mis-pacientes";
        }
    }

}