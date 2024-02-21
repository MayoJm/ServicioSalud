package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.Usuario;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<UsuarioProfesional> buscarPorEmail(@Param("email") String email);

    @Query("SELECT p FROM Usuario p WHERE p.id = :id")
    public Usuario buscarPorId(@Param("id") String id);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> buscarPorEmailUsuario(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<UsuarioPaciente> buscarPorEmailPaciente(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<UsuarioProfesional> buscarPorEmailProfesional(@Param("email") String email);

    @Query("SELECT u FROM UsuarioProfesional u WHERE u.estado = false AND u.aprobacion = false")
    List<UsuarioProfesional> findProfesionalesInactivosNoAprobados();

    @Query("SELECT DISTINCT u FROM UsuarioPaciente u WHERE u.estado = false AND u.aprobacion = false")
    List<UsuarioPaciente> findPacientesInactivosNoAprobados();

    @Query("SELECT u FROM UsuarioProfesional u WHERE u.estado = ?1 AND u.aprobacion = ?2")
    List<UsuarioProfesional> findProfesionalesByEstadoAndAprobacion(boolean estado, boolean aprobacion);

    @Query("SELECT DISTINCT u FROM UsuarioPaciente u WHERE u.estado = ?1 AND u.aprobacion = ?2")
    List<UsuarioPaciente> findPacientesByEstadoAndAprobacion(boolean estado, boolean aprobacion);

    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Optional<UsuarioPaciente> buscarPacientePorDni(@Param("dni") String dni);

    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Optional<UsuarioProfesional> buscarProfesionalPorDni(@Param("dni") String dni);

    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROFESIONAL'")
    List<UsuarioProfesional> buscarProfesionales();
}