package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Calendario;
import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.enumeraciones.Rol;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioPacienteServicio extends UsuarioServicio implements UserDetailsService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private CalendarioServicio calendarioServicio;

    public void crearUsuarioPaciente(String nombre, String apellido, String email, String password, String password2,
                                     ObraSocial obraSocial, String dni, String direccion, String telefono) throws MiException {

        Calendario calendario = new Calendario();
        calendarioServicio.crearCalendario(calendario);
        UsuarioPaciente usuarioPaciente = new UsuarioPaciente();
        validarPaciente(nombre, apellido, email, password, password2, obraSocial, dni, direccion, telefono);
        usuarioPaciente.setNombre(nombre);
        usuarioPaciente.setApellido(apellido);
        usuarioPaciente.setEmail(email);
        usuarioPaciente.setPassword(new BCryptPasswordEncoder().encode(password));
        usuarioPaciente.setRol(Rol.PACIENTE);
        usuarioPaciente.setDni(dni);
        usuarioPaciente.setObraSocial(obraSocial);
        usuarioPaciente.setDireccion(direccion);
        usuarioPaciente.setTelefono(telefono);
        usuarioPaciente.setEstado(true);
        usuarioPaciente.setCalendario(calendario);
        usuarioRepositorio.save(usuarioPaciente);
    }

    @Transactional
    public void modificarPaciente(String nombre, String apellido, String email, String passwordActual, String nuevoPassword,
                                  ObraSocial obraSocial, String dni, String direccion, String telefono, Boolean estado) throws MiException {
        validarModificacionDePaciente(nombre, apellido, passwordActual, nuevoPassword, obraSocial, dni, direccion, telefono);

        Optional<UsuarioPaciente> respuesta = usuarioRepositorio.buscarPorEmailPaciente(email);

        if (respuesta.isPresent()) {
            UsuarioPaciente usuarioPaciente = respuesta.get();

            usuarioPaciente.setNombre(nombre);
            usuarioPaciente.setApellido(apellido);
            usuarioPaciente.setEmail(email);
            usuarioPaciente.setPassword(new BCryptPasswordEncoder().encode(nuevoPassword));
            usuarioPaciente.setObraSocial(obraSocial);
            usuarioPaciente.setDni(dni);
            usuarioPaciente.setDireccion(direccion);
            usuarioPaciente.setTelefono(telefono);
            usuarioPaciente.setEstado(estado);


            usuarioRepositorio.save(usuarioPaciente);
        }
    }

    @Transactional
    public Optional<UsuarioPaciente> buscarPacientePorEmail(String mail) {
        return usuarioRepositorio.buscarPorEmailPaciente(mail);
    }

    @Transactional
    public Optional<UsuarioPaciente> buscarPacientePorDni(String dni) {
        return usuarioRepositorio.buscarPacientePorDni(dni);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosPaciente() {

        List<Usuario> usuariosPaciente = new ArrayList<>();
        usuariosPaciente = usuarioRepositorio.findAll();

        return usuariosPaciente;

    }


    @Transactional
    public void anularPaciente(String email) throws MiException {
        Optional<UsuarioPaciente> respuesta = usuarioRepositorio.buscarPorEmailPaciente(email);
        if (!respuesta.isPresent()) {
            throw new MiException("El paciente no existe");
        }
        if (respuesta.get().getEstado() == false) {
            throw new MiException("El paciente ya se encuentra inactivo");
        }
        UsuarioPaciente usuarioPaciente = respuesta.get();
        usuarioPaciente.setEstado(false);
        usuarioRepositorio.save(usuarioPaciente);

    }

    @Transactional
    public void altaPaciente(String email) throws MiException {
        Optional<UsuarioPaciente> respuesta = usuarioRepositorio.buscarPorEmailPaciente(email);
        if (!respuesta.isPresent()) {
            throw new MiException("El paciente no existe");
        }
        if (respuesta.get().getEstado() == true) {
            throw new MiException("El paciente ya se encuentra activo");
        }
        UsuarioPaciente usuarioPaciente = respuesta.get();
        usuarioPaciente.setEstado(true);
        usuarioRepositorio.save(usuarioPaciente);
    }

    public void validarPaciente(String nombre, String apellido, String email, String password, String password2,
                                ObraSocial obraSocial, String dni, String direccion, String telefono) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede ser nulo ni vacio");
        }
        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo ni vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("El password no puede ser nulo ni vacio, y debe contener mas de 5 caracteres");
        }
        if (password.length() > 9) {
            throw new MiException("El password no puede tener mas de 9 caracteres!!");
        }
        if (!password.equals(password2)) {
            throw new MiException("Los passwords deben ser iguales");
        }
        if (obraSocial == null) {
            throw new MiException("La obra social no puede ser nula");
        }
        if (dni == null || dni.isEmpty()) {
            throw new MiException("El DNI no puede ser nulo ni vacío");
        }
        if (dni.length() != 8) {
            throw new MiException("El DNI debe contener 8 digitos");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new MiException("La dirección no puede ser nula ni vacía");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new MiException("El numero de teléfono no puede ser nulo ni vacío");
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
    }

    public boolean verificarNumeros(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void validarModificacionDePaciente(String nombre, String apellido, String passwordActual, String nuevoPassword,
                                              ObraSocial obraSocial, String dni, String direccion, String telefono) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede ser nulo ni vacio");
        }
        if (passwordActual.isEmpty() || nuevoPassword.isEmpty() || passwordActual == null || nuevoPassword.length() <= 5 || nuevoPassword.length() > 9) {
            throw new MiException("El password no coincide con el actual o no puede ser nulo ni vacio, y debe contener mas de 5 caracteres y menos de 9");
        }
        if (obraSocial == null) {
            throw new MiException("La obra social no puede ser nula");
        }
        if (dni == null || dni.isEmpty()) {
            throw new MiException("El DNI no puede ser nulo ni vacío");
        }
        if (dni.length() != 8) {
            throw new MiException("El DNI debe contener 8 digitos");
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

    }

    public boolean verificarPassword(String email, String password) {

        Optional<UsuarioPaciente> usuarioOptional = usuarioRepositorio.buscarPorEmailPaciente(email);
        if (usuarioOptional.isPresent()) {
            UsuarioPaciente usuarioPaciente = usuarioOptional.get();
            // Comparar la contraseña proporcionada con la contraseña almacenada
            return new BCryptPasswordEncoder().matches(password, usuarioPaciente.getPassword());
        }
        return false;
    }


}
