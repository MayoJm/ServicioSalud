package com.appsalud.plataformaSalud.entidades;

import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioPaciente extends Usuario {

    @Enumerated(EnumType.STRING)
    private ObraSocial obraSocial;
    private String dni;
    private String direccion;
    private String telefono;

    @OneToMany(mappedBy = "usuarioPaciente")
    private List<HistoriaClinica> fichasMedicas = new ArrayList<>();
    //private EstudiosClinicos estudiosClinicos; //A DEBATIR
    //@OneToOne
    //private Imagen imagen;

}
