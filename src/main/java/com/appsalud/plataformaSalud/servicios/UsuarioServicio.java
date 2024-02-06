package com.appsalud.plataformaSalud.servicios;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio {

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



    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosPaciente() {

        List<Usuario> usuariosPaciente = new ArrayList<>();
        usuariosPaciente = usuarioRepositorio.findAll();

        return usuariosPaciente;

    }



    public Usuario getOne(String email) {
        return usuarioRepositorio.getReferenceById(email);
    }

    @Transactional
    public Usuario buscarPorId(String id) {
        return usuarioRepositorio.getReferenceById(id);
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
}
