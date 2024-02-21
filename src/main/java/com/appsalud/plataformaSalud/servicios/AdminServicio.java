package com.appsalud.plataformaSalud.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;

@Service
public class AdminServicio {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public List<UsuarioProfesional> listarUsuariosProfesionalesInactivos() {
        return usuarioRepositorio.findProfesionalesInactivosNoAprobados();
    }

    @Transactional
    public List<UsuarioProfesional> listarUsuariosProfesionalesPendientes() {
        return usuarioRepositorio.findProfesionalesByEstadoAndAprobacion(false, true);
    }

    @Transactional
    public List<UsuarioProfesional> listarUsuariosProfesionalesActivos() {
        return usuarioRepositorio.findProfesionalesByEstadoAndAprobacion(true, false);
    }

    @Transactional
    public List<UsuarioPaciente> listarUsuariosPacientesInactivos() {
        return usuarioRepositorio.findPacientesInactivosNoAprobados();
    }

    @Transactional
    public List<UsuarioPaciente> listarUsuariosPacientesPendientes() {
        return usuarioRepositorio.findPacientesByEstadoAndAprobacion(false, true);
    }

    @Transactional
    public List<UsuarioPaciente> listarUsuariosPacientesActivos() {
        return usuarioRepositorio.findPacientesByEstadoAndAprobacion(true, false);
    }
}
