package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepositorio extends JpaRepository<Turno, String> {

    @Query("SELECT t FROM Turno t WHERE t.id = :id")
    public Turno buscarPorId(@Param("id") String id);

//    @Query("SELECT t FROM Turno t WHERE t.usuario.rol = 'PACIENTE'")
//    public Turno buscarPorPaciente(@Param("id") String id);
}
