package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.DisponibilidadHoraria;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadHorariaRepositorio extends JpaRepository<DisponibilidadHoraria, Long>{

    @Query("SELECT dh FROM DisponibilidadHoraria dh WHERE dh.usuarioProfesional = :profesional")
    List<DisponibilidadHoraria> findByUsuarioProfesional(@Param("profesional") UsuarioProfesional profesional);

}
