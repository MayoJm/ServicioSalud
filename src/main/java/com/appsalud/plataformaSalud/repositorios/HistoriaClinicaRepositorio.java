package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.HistoriaClinica;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriaClinicaRepositorio extends JpaRepository<HistoriaClinica, String> {
    @Query("SELECT f FROM HistoriaClinica f WHERE f.usuarioPaciente = :paciente")
    public List<HistoriaClinica> buscarPorPaciente(@Param("paciente") UsuarioPaciente paciente);

    @Query("SELECT hc FROM HistoriaClinica hc WHERE hc.usuarioPaciente = :paciente AND hc.usuarioProfesional = :profesional")
    public Optional<HistoriaClinica> findByPacienteAndProfesional(@Param("paciente") UsuarioPaciente paciente,
            @Param("profesional") UsuarioProfesional profesional);
}
