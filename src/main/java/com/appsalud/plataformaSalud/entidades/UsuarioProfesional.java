package com.appsalud.plataformaSalud.entidades;

import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioProfesional extends Usuario {
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    private String descripcionEspecialidad;
    private Integer reputacion;
    private Integer valorConsulta;
    private String matricula;
    private String dni;
    private String direccion;
    private String telefono;

    @ElementCollection
    @Column(name = "obras_sociales")
    private List<ObraSocial> obrasSociales;

    @OneToMany(mappedBy = "usuarioProfesional", cascade = CascadeType.ALL)
    private List<DisponibilidadHoraria> disponibilidades;

    @OneToMany(mappedBy = "usuarioProfesional")
    private List<HistoriaClinica> historiasClinicas = new ArrayList<>();
}
