package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.FichaMedica;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaMedicaRepositorio extends JpaRepository<FichaMedica, String>{
    @Query("SELECT f FROM FichaMedica f WHERE f.usuarioPaciente = :paciente")
    public List<FichaMedica> buscarPorPaciente(@Param("paciente") UsuarioPaciente paciente);
}

