package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.HistoriaClinica;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriaClinicaRepositorio extends JpaRepository<HistoriaClinica, String>{
    @Query("SELECT f FROM HistoriaClinica f WHERE f.usuarioPaciente = :paciente")
    public List<HistoriaClinica> buscarPorPaciente(@Param("paciente") UsuarioPaciente paciente);
}

