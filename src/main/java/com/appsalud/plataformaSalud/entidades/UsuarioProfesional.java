package com.appsalud.plataformaSalud.entidades;

import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UsuarioProfesional extends Usuario {
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    private String descripcionEspecialidad;
    private Integer reputacion;
    private Integer valorConsulta;
    private String matricula;
    private ArrayList<ObraSocial> obrasSociales;

    public UsuarioProfesional() {
    }

}
