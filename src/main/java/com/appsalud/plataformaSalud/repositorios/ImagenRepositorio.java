package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String> {
}
