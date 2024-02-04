package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.repositorios.TurnoRepositorio;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioProfesionalServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private TurnoRepositorio turnoRepositorio;

    @Transactional
    public Usuario buscarProfesionalPorId(String id) {
        return usuarioRepositorio.buscarPorId(id);
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
}
