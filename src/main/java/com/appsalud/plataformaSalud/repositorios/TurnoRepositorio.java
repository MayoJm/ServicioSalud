package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepositorio extends JpaRepository<Turno, String> {

    @Query("SELECT t FROM Turno t WHERE t.usuarioPaciente = :paciente")
    public List<Turno> buscarPorPaciente(@Param("paciente") UsuarioPaciente paciente);

    @Query("SELECT t FROM Turno t WHERE t.usuarioProfesional = :profesional")
    public List<Turno> buscarPorProfesional(@Param("profesional") UsuarioProfesional profesional);

    @Query("SELECT t FROM Turno t WHERE t.usuarioProfesional = :profesional AND t.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    List<Turno> findByUsuarioProfesionalAndFechaBetween(@Param("profesional") UsuarioProfesional profesional,
            @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT t FROM Turno t WHERE t.id = :id")
    public Turno buscarPorId(@Param("id") String id);
}
