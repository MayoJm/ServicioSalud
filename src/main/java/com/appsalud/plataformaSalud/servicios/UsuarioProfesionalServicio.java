package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import com.appsalud.plataformaSalud.enumeraciones.Rol;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.TurnoRepositorio;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioProfesionalServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private TurnoRepositorio turnoRepositorio;

    @Transactional
    public Usuario buscarProfesionalPorId(String id) {
        return usuarioRepositorio.buscarPorId(id);
    }

    @Transactional
    public void crearUsuarioProfesional(String nombre, String apellido, String email, String password, String password2,
                                        Especialidad especialidad, String descripcionEspecialidad,
                                        Integer valorConsulta, String matricula, String dni, String direccion,
                                        String telefono, List<ObraSocial> obrasSociales) throws MiException {
        UsuarioProfesional usuarioProfesional = new UsuarioProfesional();

        validarProfesional(nombre, apellido, email, password, password2, especialidad, descripcionEspecialidad,
                valorConsulta, matricula, dni, direccion, telefono/* obrasSociales*/);

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
        usuarioProfesional.setEstado(true);
        usuarioProfesional.setObrasSociales((List<ObraSocial>) obrasSociales);
        usuarioProfesional.setReputacion(null);
        usuarioRepositorio.save(usuarioProfesional);
    }
    @Transactional
    public void modificarProfesional(String nombre, String apellido, String email, String password, String password2,
                                     Especialidad especialidad, String descripcionEspecialidad, Integer reputacion, Integer valorConsulta,
                                     String matricula, String dni, String direccion, String telefono, Boolean estado
                                     /*ArrayList<ObraSocial> obrasSociales*/) throws MiException {
        validarProfesional(nombre, apellido, email, password, password2, especialidad, descripcionEspecialidad,
                valorConsulta, matricula, dni, direccion, telefono/* obrasSociales*/);

        Optional<UsuarioProfesional> respuesta = usuarioRepositorio.buscarPorEmail(email);

        if (respuesta.isPresent()) {
            UsuarioProfesional usuarioProfesional = respuesta.get();

            usuarioProfesional.setNombre(nombre);
            usuarioProfesional.setApellido(apellido);
            usuarioProfesional.setEmail(email);
            usuarioProfesional.setPassword(new BCryptPasswordEncoder().encode(password));
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


    public void validarProfesional(String nombre, String apellido, String email, String password, String password2,
                                   Especialidad especialidad, String descripcionEspecialidad, Integer valorConsulta,
                                   String matricula, String dni, String direccion, String telefono/* List<ObraSocial> obrasSociales*/)
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
        if (direccion == null || direccion.isEmpty()) {
            throw new MiException("La dirección no puede ser nula ni vacía");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new MiException("El teléfono no puede ser nulo ni vacío");
        }
//        if (obrasSociales == null || obrasSociales.isEmpty()) {
//            throw new MiException("La lista de obras sociales no puede ser nula o vacía");
//        }
    }
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosProfesional() {

        List<Usuario> usuariosProfesional = new ArrayList<>();

        usuariosProfesional = usuarioRepositorio.findAll();

        return usuariosProfesional;

    }
    @Transactional
    public void crearTurno(Date hora, Date fecha, String descripcion, UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional) {
        Turno turno = new Turno();
        turno.setHora(hora);
        turno.setFecha(fecha);
        turno.setDescripcion(descripcion);
        turno.setUsuarioPaciente(usuarioPaciente);
        turno.setUsuarioProfesional(usuarioProfesional);
        
        turnoRepositorio.save(turno);        
    }
    @Transactional
    public void anularProfesional(String email){
        Optional<UsuarioProfesional> respuesta = usuarioRepositorio.buscarPorEmail(email);

        if (respuesta.isPresent()) {
            UsuarioProfesional usuarioProfesional = respuesta.get();
            usuarioProfesional.setEstado(false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioProfesional usuarioProfesional = usuarioRepositorio.buscarPorEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if(usuarioProfesional != null){
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuarioProfesional.getRol().toString());
            permisos.add(p);
        return new User(usuarioProfesional.getEmail(), usuarioProfesional.getPassword(), permisos);
        }else {
            return null;
        }
    }
}

