package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Usuario;
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

    @Transactional
    public void crearUsuario(String nombre, String email, String password, String password2, String dni, String direccion, String telefono) throws MiException {
        Usuario usuario = new Usuario();
        validar(nombre, email, password, password2, dni, direccion, telefono);
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setDni(dni);
        usuario.setDireccion(direccion);
        usuario.setTelefono(telefono);
        usuario.setRol(Rol.PACIENTE);

        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void modificar(String nombre, String email, String password, String password2, String dni, String direccion, String telefono) throws MiException {
        validar(nombre, email, password, password2, dni, direccion, telefono);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(email);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setDni(dni);
            usuario.setDireccion(direccion);
            usuario.setTelefono(telefono);
            
            usuarioRepositorio.save(usuario);
        }

    }

    @Transactional
    public void modificarEstado(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            
            Usuario usuario = respuesta.get();
            
            if (usuario.getEstado()) {
                usuario.setEstado(false);
            }else {
                usuario.setEstado(true);
            }
        }
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        
        List<Usuario> usuarios = new ArrayList();
        
        usuarios = usuarioRepositorio.findAll();
        
        return usuarios;

    }
    
    public Usuario getOne(String email) {
        return usuarioRepositorio.getReferenceById(email);
    }
    
    @Transactional
    public Usuario buscarPorId(String id) {
        return usuarioRepositorio.getReferenceById(id);
    }

    public void validar(String nombre, String email, String password, String password2, String dni, String direccion, String telefono) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni vacio");
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
        if (dni == null || dni.isEmpty()) {
            throw new MiException("El dni no puede ser nulo ni vacio");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new MiException("La direccion no puede ser nula ni vacia");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new MiException("El telefono no puede ser nulo ni vacio");
        }
    }

}
