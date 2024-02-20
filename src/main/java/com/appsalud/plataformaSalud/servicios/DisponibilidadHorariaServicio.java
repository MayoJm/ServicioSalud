package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.DisponibilidadHoraria;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.repositorios.DisponibilidadHorariaRepositorio;
import com.appsalud.plataformaSalud.repositorios.TurnoRepositorio;
import com.appsalud.plataformaSalud.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilidadHorariaServicio {

    @Autowired
    private DisponibilidadHorariaRepositorio disponibilidadHorariaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TurnoRepositorio turnoRepositorio;

    public List<DisponibilidadHoraria> obtenerDisponibilidades(UsuarioProfesional profesional) {
        return disponibilidadHorariaRepositorio.findByUsuarioProfesional(profesional);
    }

    public void guardarDisponibilidadProfesional(List<DisponibilidadHoraria> disponibilidades,
                                                 UsuarioProfesional profesional) {
        // Aquí puedes realizar validaciones u otras operaciones necesarias antes de
        // guardar
        profesional.setDisponibilidades(disponibilidades);
        usuarioRepositorio.save(profesional);
        disponibilidadHorariaRepositorio.saveAll(disponibilidades);
    }

}
