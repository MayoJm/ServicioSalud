package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.Turno;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
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
    @Query("SELECT t FROM Turno t WHERE t.usuarioProfesional = :profesional AND t.usuarioPaciente IS NULL")
    List<Turno> buscarTurnosDisponiblesPorProfesional(@Param("profesional") UsuarioProfesional profesional);


    @Query("SELECT t FROM Turno t WHERE t.usuarioProfesional.email = :emailProfesional AND t.usuarioPaciente IS NOT NULL")
    List<Turno> buscarTurnosTomadosPorProfesional(String emailProfesional);
}
