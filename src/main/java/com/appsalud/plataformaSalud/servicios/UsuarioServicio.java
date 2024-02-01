package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.enumeraciones.Rol;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearUsuario(String nombre, String apellido, String email, String password, String password2) throws MiException {
        Usuario usuario = new Usuario();
        validar(nombre, apellido, email, password, password2);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USUARIO);

        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void modificar(String nombre, String apellido, String email, String password, String password2) throws MiException {
        validar(nombre, apellido, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(email);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            
            usuarioRepositorio.save(usuario);
        }

    }

    public void modificarEstado() {
        
    }

    public void listarUsuarios() {

    }

    public void validar(String nombre, String apellido, String email, String password, String password2) throws MiException {
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

    }

}
