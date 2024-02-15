package com.appsalud.plataformaSalud.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turno {

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

   private LocalDateTime fechaHora;

   private String fechaFormateada;
   private String horaFormateada;

   private String descripcion;

    @ManyToOne
    private UsuarioPaciente usuarioPaciente;

    @ManyToOne
    private UsuarioProfesional usuarioProfesional;

    private Boolean alta; 
}
