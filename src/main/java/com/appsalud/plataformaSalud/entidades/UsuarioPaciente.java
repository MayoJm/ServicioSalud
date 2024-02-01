package com.appsalud.plataformaSalud.entidades;

import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioPaciente extends Usuario{
    @Enumerated(EnumType.STRING)
    private ObraSocial obraSocial;
    
//    private EstudiosClinicos estudiosClinicos; //A DEBATIR
}
