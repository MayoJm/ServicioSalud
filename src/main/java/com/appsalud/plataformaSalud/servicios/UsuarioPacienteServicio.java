package com.appsalud.plataformaSalud.servicios;

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

    public void crearUsuarioPaciente(String nombre, String apellido, String email, String password, String password2,
                                     ObraSocial obraSocial, String dni, String direccion, String telefono) throws MiException {

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
        usuarioRepositorio.save(usuarioPaciente);
    }

    @Transactional
    public Optional<UsuarioPaciente> buscarPacientePorEmail(String mail) {
        return usuarioRepositorio.buscarPorEmailPaciente(mail);
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
        if (!password.equals(password2)) {
            throw new MiException("Los passwords deben ser iguales");
        }
        if (obraSocial == null) {
            throw new MiException("La obra social no puede ser nula");
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

    }

    public void validarModificacionDePaciente(String nombre, String apellido, String passwordActual, String nuevoPassword,
                                ObraSocial obraSocial, String dni, String direccion, String telefono) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede ser nulo ni vacio");
        }
        if (passwordActual.isEmpty() || nuevoPassword.isEmpty() || passwordActual == null ||  nuevoPassword.length() <= 5) {
            throw new MiException("El password no puede ser nulo ni vacio, y debe contener mas de 5 caracteres");
        }
        if (obraSocial == null) {
            throw new MiException("La obra social no puede ser nula");
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

    }
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosPaciente() {

        List<Usuario> usuariosPaciente = new ArrayList<>();
        usuariosPaciente = usuarioRepositorio.findAll();

        return usuariosPaciente;

    }

    @Transactional
    public void modificarPaciente(String nombre, String apellido, String email, String password, String password2,
                                  ObraSocial obraSocial, String dni, String direccion, String telefono, Boolean estado) throws MiException {
        validarPaciente(nombre, apellido, email, password, password2, obraSocial, dni, direccion, telefono);

        Optional<UsuarioPaciente> respuesta = usuarioRepositorio.buscarPorEmailPaciente(email);

        if (respuesta.isPresent()) {
            UsuarioPaciente usuarioPaciente = respuesta.get();

            usuarioPaciente.setNombre(nombre);
            usuarioPaciente.setApellido(apellido);
            usuarioPaciente.setEmail(email);
            usuarioPaciente.setPassword(new BCryptPasswordEncoder().encode(password));
            usuarioPaciente.setObraSocial(obraSocial);
            usuarioPaciente.setDni(dni);
            usuarioPaciente.setDireccion(direccion);
            usuarioPaciente.setTelefono(telefono);
            usuarioPaciente.setEstado(estado);



            usuarioRepositorio.save(usuarioPaciente);
        }
    }
    @Transactional
    public void anularPaciente(String email){
        Optional<UsuarioPaciente> respuesta = usuarioRepositorio.buscarPorEmailPaciente(email);
        if (respuesta.isPresent()) {
            UsuarioPaciente usuarioPaciente = respuesta.get();
            usuarioPaciente.setEstado(false);
            
            usuarioRepositorio.save(usuarioPaciente);
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
