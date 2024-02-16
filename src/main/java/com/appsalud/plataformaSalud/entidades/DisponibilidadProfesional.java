package com.appsalud.plataformaSalud.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
// Hola amigo tenes que borrar esta tabla porque es una verga, sugerencia

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadProfesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private UsuarioProfesional usuarioProfesional;
}
