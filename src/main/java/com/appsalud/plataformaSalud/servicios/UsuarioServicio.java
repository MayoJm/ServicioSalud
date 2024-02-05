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

    @Transactional
    public void crearUsuarioProfesional(String nombre,String apellido,String email,String password,String password2,Especialidad especialidad,String descripcionEspecialidad,Integer reputacion,Integer valorConsulta,String matricula
            ,String dni,String direccion,String telefono,ArrayList<ObraSocial> obrasSociales) throws MiException {
        UsuarioProfesional usuarioProfesional = new UsuarioProfesional();

        validarProfesional( nombre, apellido, email, password, password2, especialidad, descripcionEspecialidad, reputacion, valorConsulta, matricula, dni, direccion, telefono, obrasSociales);
        usuarioProfesional.setNombre(nombre);
        usuarioProfesional.setApellido(apellido);
        usuarioProfesional.setEmail(email);
        usuarioProfesional.setPassword(new BCryptPasswordEncoder().encode(password));
        usuarioProfesional.setRol(Rol.PROFESIONAL);
        usuarioProfesional.setEspecialidad(especialidad);
        usuarioProfesional.setDescripcionEspecialidad(descripcionEspecialidad);
        usuarioProfesional.setReputacion(reputacion);
        usuarioProfesional.setValorConsulta(valorConsulta);
        usuarioProfesional.setMatricula(matricula);
        usuarioProfesional.setDni(dni);
        usuarioProfesional.setDireccion(direccion);
        usuarioProfesional.setTelefono(telefono);
        usuarioProfesional.setEstado(true);
        usuarioProfesional.setObrasSociales(obrasSociales);

        usuarioRepositorio.save(usuarioProfesional);
    }    public void crearUsuarioPaciente(String nombre, String apellido, String email, String password, String password2,ObraSocial obraSocial, String dni, String direccion, String telefono) throws MiException {

        UsuarioPaciente usuarioPaciente =new UsuarioPaciente();
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
    public void modificarProfesional(String nombre, String apellido, String email, String password, String password2, Especialidad especialidad, String descripcionEspecialidad, Integer reputacion, Integer valorConsulta, String matricula, String dni, String direccion, String telefono, Boolean estado, ArrayList<ObraSocial> obrasSociales) throws MiException {
        validarProfesional(nombre, apellido, email, password, password2, especialidad, descripcionEspecialidad, reputacion, valorConsulta, matricula, dni, direccion, telefono, obrasSociales);

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
            usuarioProfesional.setObrasSociales(obrasSociales);

            usuarioRepositorio.save(usuarioProfesional);
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosPaciente() {

        List<Usuario> usuariosPaciente = new ArrayList();
        usuariosPaciente = usuarioRepositorio.findAll();

        return usuariosPaciente;

    }
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosProfesional() {

        List<Usuario> usuariosProfesional = new ArrayList();

        usuariosProfesional = usuarioRepositorio.findAll();

        return usuariosProfesional;

    }


    public Usuario getOne(String email) {
        return usuarioRepositorio.getReferenceById(email);
    }

    @Transactional
    public Usuario buscarPorId(String id) {
        return usuarioRepositorio.getReferenceById(id);
    }

    public void validarProfesional(String nombre, String apellido, String email, String password, String password2, Especialidad especialidad, String descripcionEspecialidad, Integer reputacion, Integer valorConsulta, String matricula, String dni, String direccion, String telefono, ArrayList<ObraSocial> obrasSociales) throws MiException {
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
        if (reputacion == null || reputacion < 0) {
            throw new MiException("La reputación debe ser un número entero positivo");
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
        if (obrasSociales == null || obrasSociales.isEmpty()) {
            throw new MiException("La lista de obras sociales no puede ser nula o vacía");
        }
    }
    public void validarPaciente(String nombre, String apellido, String email, String password, String password2, ObraSocial obraSocial, String dni, String direccion, String telefono) throws MiException {
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
