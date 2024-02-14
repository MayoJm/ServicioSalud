package com.appsalud.plataformaSalud.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Calendario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToMany
    private List<Turno> turnos;

    public boolean contieneTurnoParaFechaHora(LocalDateTime fechaHora) {
        for (Turno turno : turnos) {
            if (turno.getFechaHora().equals(fechaHora)) {
                return true;
            }
        }
        return false;
    }
 }
