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


    //Query para buscar profesionales que necesitan aprobación de cuenta.
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.aprobacion = :aprobacion AND u.estado = :estado")
    public Optional<UsuarioProfesional> buscarPorEmailProfesionalAprobacion
    (@Param("rol") String rol,
     @Param("estado") Boolean estado,
     @Param("aprobacion") Boolean aprobacion);

    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Optional<UsuarioPaciente> buscarPacientePorDni(@Param("dni") String dni);

    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Optional<UsuarioProfesional> buscarProfesionalPorDni(@Param("dni") String dni);

    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROFESIONAL'")
    List<UsuarioProfesional> buscarProfesionales();
}