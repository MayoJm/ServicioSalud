package com.appsalud.plataformaSalud.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class HistoriaClinica {

    @Id
    @GeneratedValue(generator = "uuid")
    private String id;
    @ManyToOne
    private UsuarioPaciente usuarioPaciente; // se quiere mostrar en vista el nombre y dni
    @ManyToOne
    private UsuarioProfesional usuarioProfesional; // se quiere mostrar en vista el nombre y especialidad
    private String nombre;
    private Integer edad;
    private String sexo;
    private Double peso;
    private List<String> datosHistoricos;
    private List<LocalDate> fechaConsulta;
    private boolean alta;
}
