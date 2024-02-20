package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.enumeraciones.Rol;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioProfesionalServicio extends UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private TurnoServicio turnoServicio;

    @Transactional
    public void crearUsuarioProfesional(String nombre, String apellido, String email, String password, String password2,
            Especialidad especialidad, String descripcionEspecialidad,
            Integer valorConsulta, String matricula, String dni, String direccion,
            String telefono, List<ObraSocial> obrasSociales) throws MiException {
        UsuarioProfesional usuarioProfesional = new UsuarioProfesional();

        validarProfesional(nombre, apellido, email, password, password2, especialidad, descripcionEspecialidad,
                valorConsulta, matricula, dni, direccion, telefono/* obrasSociales */);

        usuarioProfesional.setNombre(nombre);
        usuarioProfesional.setApellido(apellido);
        usuarioProfesional.setEmail(email);
        usuarioProfesional.setPassword(new BCryptPasswordEncoder().encode(password));
        usuarioProfesional.setRol(Rol.PROFESIONAL);
        usuarioProfesional.setEspecialidad(especialidad);
        usuarioProfesional.setDescripcionEspecialidad(descripcionEspecialidad);
        usuarioProfesional.setValorConsulta(valorConsulta);
        usuarioProfesional.setMatricula(matricula);
        usuarioProfesional.setDni(dni);
        usuarioProfesional.setDireccion(direccion);
        usuarioProfesional.setTelefono(telefono);
        usuarioProfesional.setEstado(false);
        usuarioProfesional.setAprobacion(true);
        usuarioProfesional.setObrasSociales((List<ObraSocial>) obrasSociales);
        usuarioProfesional.setReputacion(null);
        usuarioRepositorio.save(usuarioProfesional);
    }

    @Transactional
    public void modificarProfesional(String nombre, String apellido, String email, String passwordActual,
            String nuevoPassword,
            Especialidad especialidad, String descripcionEspecialidad, Integer reputacion, Integer valorConsulta,
            String matricula, String dni, String direccion, String telefono, Boolean estado) throws MiException {
        validarModificacionDeProfesional(nombre, apellido, email, passwordActual, nuevoPassword, especialidad,
                descripcionEspecialidad,
                valorConsulta, dni, direccion, telefono);

        Optional<UsuarioProfesional> respuesta = usuarioRepositorio.buscarPorEmail(email);

        if (respuesta.isPresent()) {
            UsuarioProfesional usuarioProfesional = respuesta.get();

            usuarioProfesional.setNombre(nombre);
            usuarioProfesional.setApellido(apellido);
            usuarioProfesional.setEmail(email);
            usuarioProfesional.setPassword(new BCryptPasswordEncoder().encode(nuevoPassword));
            usuarioProfesional.setEspecialidad(especialidad);
            usuarioProfesional.setDescripcionEspecialidad(descripcionEspecialidad);
            usuarioProfesional.setReputacion(reputacion);
            usuarioProfesional.setValorConsulta(valorConsulta);
            usuarioProfesional.setMatricula(matricula);
            usuarioProfesional.setDni(dni);
            usuarioProfesional.setDireccion(direccion);
            usuarioProfesional.setTelefono(telefono);
            usuarioProfesional.setEstado(estado);
            // usuarioProfesional.setObrasSociales(obrasSociales);

            usuarioRepositorio.save(usuarioProfesional);
        }
    }

    @Transactional
    public UsuarioProfesional buscarPorId(String id) {
        return (UsuarioProfesional) usuarioRepositorio.buscarPorId(id);
    }

    @Transactional
    public Optional<UsuarioProfesional> buscarProfesionalPorDni(String dni) {
        return usuarioRepositorio.buscarProfesionalPorDni(dni);
    }

    @Transactional
    public Optional<UsuarioProfesional> buscarProfesionalPorEmail(String mail) {
        return usuarioRepositorio.buscarPorEmail(mail);
    }

    @Transactional(readOnly = true)
    public List<UsuarioProfesional> listarUsuariosProfesionales() throws MiException {
        try {
            List<UsuarioProfesional> usuariosProfesionales = usuarioRepositorio.buscarProfesionales();
            return usuariosProfesionales;
        } catch (Exception e) {
            return null;
        }

    }

    @Transactional
    public void anularProfesional(String email) throws MiException {
        Optional<UsuarioProfesional> respuesta = usuarioRepositorio.buscarPorEmail(email);
        if (respuesta.isEmpty()) {
            throw new MiException("No se encontró el profesional con email " + email);
        }
        if (respuesta.get().getEstado() == false) {
            throw new MiException("El profesional ya se encuentra inactivo");
        }
        UsuarioProfesional usuarioProfesional = respuesta.get();
        usuarioProfesional.setEstado(false);
        usuarioProfesional.setAprobacion(false);
        usuarioRepositorio.save(usuarioProfesional);

    }

    public void darAltaProfesional(String email) throws MiException {
        Optional<UsuarioProfesional> respuesta = usuarioRepositorio.buscarPorEmail(email);
        if (respuesta.isEmpty()) {
            throw new MiException("No se encontró el profesional con email " + email);
        }
        if (respuesta.get().getEstado() == true) {
            throw new MiException("El profesional ya se encuentra activo");
        }
        UsuarioProfesional usuarioProfesional = respuesta.get();
        usuarioProfesional.setEstado(true);
        usuarioProfesional.setAprobacion(false);
        usuarioRepositorio.save(usuarioProfesional);
    }

    public void validarProfesional(String nombre, String apellido, String email, String password, String password2,
            Especialidad especialidad, String descripcionEspecialidad, Integer valorConsulta,
            String matricula, String dni, String direccion, String telefono/* List<ObraSocial> obrasSociales */)
            throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni vacío");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede ser nulo ni vacío");
        }
        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo ni vacío");
        }
        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiException("El password no puede ser nulo ni vacío, y debe contener más de 5 caracteres");
        }
        if (!password.equals(password2)) {
            throw new MiException("Los passwords deben ser iguales");
        }
        if (especialidad == null) {
            throw new MiException("La especialidad no puede ser nula");
        }
        if (descripcionEspecialidad == null || descripcionEspecialidad.isEmpty()) {
            throw new MiException("La descripción de la especialidad no puede ser nula ni vacía");
        }

        if (valorConsulta == null || valorConsulta < 0) {
            throw new MiException("El valor de consulta debe ser un número entero positivo");
        }
        if (matricula == null || matricula.isEmpty()) {
            throw new MiException("La matrícula no puede ser nula ni vacía");
        }
        if (dni == null || dni.isEmpty()) {
            throw new MiException("El DNI no puede ser nulo ni vacío");
        }
        if (dni.length() != 8) {
            throw new MiException("El DNI debe contener 8 digitos");
        }
        if (password.length() > 10) {
            throw new MiException("El password no puede tener mas de 10 caracteres!!");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new MiException("La dirección no puede ser nula ni vacía");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new MiException("El teléfono no puede ser nulo ni vacío");

        }
        if (telefono.length() != 10) {
            throw new MiException("El teléfono debe tener 10 numeros ej: [2995101101] y estar todo junto");
        }
        if (verificarNumeros(telefono)) {
            throw new MiException("El numero de teléfono no debe contener letras u otros caracteres");
        }
        if (verificarNumeros(dni)) {
            throw new MiException("El dni no debe contener letras u otros caracteres");
        }

        // if (obrasSociales == null || obrasSociales.isEmpty()) {
        // throw new MiException("La lista de obras sociales no puede ser nula o
        // vacía");
        // }
    }

    public boolean verificarNumeros(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void validarModificacionDeProfesional(String nombre, String apellido, String email, String passwordActual,
            String nuevoPassword,
            Especialidad especialidad, String descripcionEspecialidad, Integer valorConsulta, String dni,
            String direccion, String telefono/* List<ObraSocial> obrasSociales */)
            throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni vacío");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede ser nulo ni vacío");
        }
        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo ni vacío");
        }
        if (passwordActual.isEmpty() || nuevoPassword.isEmpty() || passwordActual == null
                || nuevoPassword.length() <= 5) {
            throw new MiException("El password no puede ser nulo ni vacío, y debe contener más de 5 caracteres");
        }
        if (dni.length() != 8) {
            throw new MiException("El DNI debe contener 8 digitos");
        }
        if (nuevoPassword.length() > 10) {
            throw new MiException("El password no puede tener mas de 10 caracteres!!");
        }

        if (especialidad == null) {
            throw new MiException("La especialidad no puede ser nula");
        }
        if (descripcionEspecialidad == null || descripcionEspecialidad.isEmpty()) {
            throw new MiException("La descripción de la especialidad no puede ser nula ni vacía");
        }

        if (valorConsulta == null || valorConsulta < 0) {
            throw new MiException("El valor de consulta debe ser un número entero positivo");
        }

        if (dni == null || dni.isEmpty()) {
            throw new MiException("El DNI no puede ser nulo ni vacío");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new MiException("La dirección no puede ser nula ni vacía");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new MiException("El teléfono no puede ser nulo ni vacío");
        }
        if (telefono.length() != 10) {
            throw new MiException("El teléfono debe tener 10 numeros ej: [2995101101] y estar todo junto");
        }
        if (verificarNumeros(telefono)) {
            throw new MiException("El numero de teléfono no debe contener letras u otros caracteres");
        }
        if (verificarNumeros(dni)) {
            throw new MiException("El dni no debe contener letras u otros caracteres");
        }
        // if (obrasSociales == null || obrasSociales.isEmpty()) {
        // throw new MiException("La lista de obras sociales no puede ser nula o
        // vacía");
        // }
    }

    public boolean verificarPassword(String email, String password) {
        Optional<UsuarioProfesional> usuarioOptional = usuarioRepositorio.buscarPorEmailProfesional(email);
        if (usuarioOptional.isPresent()) {
            UsuarioProfesional usuarioProfesional = usuarioOptional.get();
            // Comparar la contraseña proporcionada con la contraseña almacenada
            return new BCryptPasswordEncoder().matches(password, usuarioProfesional.getPassword());
        }
        return false;
    }

    public List<UsuarioPaciente> obtenerPacientes(String email) {
        List<Turno> turnos = turnoServicio.listarTurnosPorProfesional(email);
        List<UsuarioPaciente> pacientes = new ArrayList<>();
        for (Turno turno : turnos) {
            pacientes.add(turno.getUsuarioPaciente());
        }
        return pacientes;
    }
}
