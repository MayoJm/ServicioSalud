package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;


    public Usuario getOne(String email) {
        return usuarioRepositorio.getReferenceById(email);
    }

    @Transactional
    public Usuario buscarPorId(String id) {
        return usuarioRepositorio.getReferenceById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmailUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (usuario.getEstado().equals(false)) {
            throw new DisabledException("La cuenta está deshabilitada");
        }

        List<GrantedAuthority> permisos = new ArrayList<>();
        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
        permisos.add(p);
        return new User(usuario.getEmail(), usuario.getPassword(), permisos);
    }
    @Transactional
    public void guardarImagen(String email, MultipartFile archivo) throws MiException {
        Optional<Usuario> respuesta = usuarioRepositorio.buscarPorEmailUsuario(email);
        if (!respuesta.isEmpty()) {
            Usuario usuario = respuesta.get();
            usuario.setImagen(imagenServicio.guardar(archivo));
            usuarioRepositorio.save(usuario);
        }
    }
}