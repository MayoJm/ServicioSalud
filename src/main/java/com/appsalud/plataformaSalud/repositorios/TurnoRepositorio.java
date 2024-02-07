package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.Turno;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepositorio extends JpaRepository<Turno, String> {

    @Query("SELECT t FROM Turno t WHERE t.usuario_paciente_id = :idPaciente")
    public List<Turno> buscarPorPaciente(@Param("idPaciente") String idPaciente);
    
    @Query("SELECT t FROM Turno t WHERE t.usuario_profesional_id = :idProfesional")
    public List<Turno> buscarPorProfesional(@Param("idProfesional") String idProfesional);

}
