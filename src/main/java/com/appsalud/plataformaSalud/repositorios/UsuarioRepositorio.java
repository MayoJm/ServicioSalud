package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
     @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
    @Query("SELECT p FROM Usuario p WHERE p.id = :id")
    public Usuario buscarPorId(@Param("id") String id);
}
