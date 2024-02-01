package com.appsalud.plataformaSalud.entidades;

import com.appsalud.plataformaSalud.enumeraciones.Rol;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private String email;
    private String password;
    private String dni;
    private String direccion;
    private String telefono;
    private Boolean estado;

    //@OneToOne
    //private Imagen imagen;
    //@OneToOne
    //private Calendario calendario;
    @Enumerated(EnumType.STRING)
    private Rol rol;

}
