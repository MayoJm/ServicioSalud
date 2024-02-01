package com.appsalud.plataformaSalud.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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

   @Temporal(TemporalType.TIMESTAMP)
    private Date hora;

   @Temporal(TemporalType.DATE)
    private Date fecha;

   private String descripcion;

    @ManyToOne
    private UsuarioPaciente usuarioPaciente;

    @ManyToOne
    private UsuarioProfesional usuarioProfesional;

}
