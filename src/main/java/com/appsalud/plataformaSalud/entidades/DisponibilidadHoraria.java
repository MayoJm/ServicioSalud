package com.appsalud.plataformaSalud.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DayOfWeek diaSemana; //tuesday
    private LocalTime horaInicio; //08:00
    private LocalTime horaFin; //15:00

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private UsuarioProfesional usuarioProfesional;

    public DisponibilidadHoraria(DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
}
