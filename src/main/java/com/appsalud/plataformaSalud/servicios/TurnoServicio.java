package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.TurnoRepositorio;
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

    @Transactional
    public void crearTurno(Date hora, Date fecha, String descripcion, UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional) throws MiException {
        
        validarTurno(hora, fecha, descripcion, usuarioPaciente, usuarioProfesional);
        Turno turno = new Turno();
        turno.setHora(hora);
        turno.setFecha(fecha);
        turno.setDescripcion(descripcion);
        turno.setUsuarioPaciente(usuarioPaciente);
        turno.setUsuarioProfesional(usuarioProfesional);
        turno.setAlta(Boolean.TRUE);
        turnoRepositorio.save(turno);
    }

    @Transactional
    public void modificarTurno(String id, Date hora, Date fecha, String descripcion, UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional) throws MiException {
        
        validarTurno(hora, fecha, descripcion, usuarioPaciente, usuarioProfesional);
        
        Optional<Turno> respuesta = turnoRepositorio.findById(id);
        
        if(respuesta.isPresent()) {
            Turno turno = respuesta.get();
            
            turno.setHora(hora);
            turno.setFecha(fecha);
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
    public List<Turno> listarTurnosPorPaciente(String idPaciente) {
        List<Turno> turnosPaciente = new ArrayList<>();
        
        turnosPaciente = turnoRepositorio.buscarPorPaciente(idPaciente);
        
        return turnosPaciente;
}
     @Transactional(readOnly = true) 
    public List<Turno> listarTurnosPorProfesional(String idProfesional) {
        List<Turno> turnosProfesional = new ArrayList<>();
        
        turnosProfesional = turnoRepositorio.buscarPorPaciente(idProfesional);
        
        return turnosProfesional;
    }
    
    public void validarTurno(Date hora, Date fecha, String descripcion, UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional) throws MiException{
        
        if (hora == null) {
            throw new MiException("La fecha no puede ser nula");
        }
        if (fecha == null) {
            throw new MiException("La hora no puede ser nula");
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
