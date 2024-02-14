package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioRepositorio extends JpaRepository<Calendario, String> {
}
