package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.TurnoRepositorio;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurnoServicio {

    @Autowired
    private TurnoRepositorio turnoRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearTurno(LocalDateTime fechaHora, String descripcion, UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional) throws MiException {
        
        validarTurno(fechaHora, descripcion, usuarioPaciente, usuarioProfesional);
        Turno turno = new Turno();
        turno.setFechaHora(fechaHora);
        turno.setDescripcion(descripcion);
        turno.setUsuarioPaciente(usuarioPaciente);
        turno.setUsuarioProfesional(usuarioProfesional);
        turno.setAlta(Boolean.TRUE);
        turnoRepositorio.save(turno);
    }

    @Transactional
    public void modificarTurno(String id, LocalDateTime fechaHora, String descripcion, UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional) throws MiException {
        
        validarTurno(fechaHora, descripcion, usuarioPaciente, usuarioProfesional);
        
        Optional<Turno> respuesta = turnoRepositorio.findById(id);
        
        if(respuesta.isPresent()) {
            Turno turno = respuesta.get();
            
            turno.setFechaHora(fechaHora);
            turno.setDescripcion(descripcion);
            turno.setUsuarioPaciente(usuarioPaciente);
            turno.setUsuarioProfesional(usuarioProfesional);
            
            turnoRepositorio.save(turno);
        }
    }
    
    @Transactional
    public void bajaTurno(String id) {
        Optional<Turno> respuesta = turnoRepositorio.findById(id);
        
        if(respuesta.isPresent()) {
            Turno turno = respuesta.get();
            
            if(turno.getAlta()) {
                turno.setAlta(Boolean.FALSE);
                turnoRepositorio.save(turno);
            }
        }
    }
    
    @Transactional(readOnly = true) 
    public List<Turno> listarTurnosPorPaciente(String email) {
        Optional<UsuarioPaciente> respuesta = usuarioRepositorio.buscarPorEmailPaciente(email);
        List<Turno> turnosPaciente = new ArrayList<>();
        UsuarioPaciente paciente = respuesta.get();
        turnosPaciente = turnoRepositorio.buscarPorPaciente(paciente);
        
        return turnosPaciente;
}
     @Transactional(readOnly = true) 
    public List<Turno> listarTurnosPorProfesional(String email) {
        Optional<UsuarioProfesional> respuesta = usuarioRepositorio.buscarPorEmail(email);
        List<Turno> turnosProfesional = new ArrayList<>();
        UsuarioProfesional profesional = respuesta.get();
        turnosProfesional = turnoRepositorio.buscarPorProfesional(profesional);
        
        return turnosProfesional;
    }
    
    public void validarTurno(LocalDateTime fechaHora, String descripcion, UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional) throws MiException{
        
        if (fechaHora == null) {
            throw new MiException("La fecha no puede ser nula");
        }
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new MiException("La fecha no puede ser anterior a la fecha actual");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiException("La descripcion no puede ser nula ni vacia");
        }
        if (usuarioPaciente == null) {
            throw new MiException("El paciente no puede ser nulo");
        }
        if (usuarioProfesional == null) {
            throw new MiException("El profesional no puede ser nulo");
        }
        
    }
}
