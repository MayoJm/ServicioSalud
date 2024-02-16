package com.appsalud.plataformaSalud.repositorios;

import com.appsalud.plataformaSalud.entidades.DisponibilidadProfesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadProfesionalRepositorio extends JpaRepository<DisponibilidadProfesional, Long>{

}
