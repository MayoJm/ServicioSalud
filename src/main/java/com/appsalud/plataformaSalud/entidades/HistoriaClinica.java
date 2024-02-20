package com.appsalud.plataformaSalud.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class HistoriaClinica {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @ManyToOne
    private UsuarioPaciente usuarioPaciente; // se quiere mostrar en vista el nombre y dni
    @ManyToOne
    private UsuarioProfesional usuarioProfesional; // se quiere mostrar en vista el nombre y especialidad
    // signos vitales
    // private String edad; SE PODRIA INTEGRAR COMO ATRIBUTO A USUARIOPACIENTE, o
    // agregar fechaDeNac
    private String presionArterial;
    private String saturacion;
    private String temperatura;
    private Double peso;
    private String frecuenciaRespiratoria;
    private Double talla;
    private String frecuenciaCardiaca;
    private Double IMC;

    private String motivoConsulta;
    private String antecedentes;
    private String alergia;
    private String intervencionQuirurgica;
    private Boolean vacunasCompletas;

    private String examenFisico;

    private String diagnostico;

    private String tratamiento;

    private Date fechaConsulta;

}
