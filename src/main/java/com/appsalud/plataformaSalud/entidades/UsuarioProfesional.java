/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.appsalud.plataformaSalud.entidades;

import com.appsalud.plataformaSalud.enumeraciones.Especialidad;
import com.appsalud.plataformaSalud.enumeraciones.ObraSocial;
import jakarta.persistence.Entity;
import java.util.ArrayList;

/**
 *
 * @author Sol
 */
@Entity
public class UsuarioProfesional { //queda pendiente extenderlo de la entidad Usuario
    private Especialidad especialidad;
    private String descripcionEspecialidad;
    private Integer reputacion;
    private Integer valorConsulta;
    private String matricula; //en vez de integer le deje en String
    private ArrayList<ObraSocial> obrasSociales;

    public UsuarioProfesional() {
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getDescripcionEspecialidad() {
        return descripcionEspecialidad;
    }

    public void setDescripcionEspecialidad(String descripcionEspecialidad) {
        this.descripcionEspecialidad = descripcionEspecialidad;
    }

    public Integer getReputacion() {
        return reputacion;
    }

    public void setReputacion(Integer reputacion) {
        this.reputacion = reputacion;
    }

    public Integer getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(Integer valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public ArrayList<ObraSocial> getObrasSociales() {
        return obrasSociales;
    }

    public void setObrasSociales(ArrayList<ObraSocial> obrasSociales) {
        this.obrasSociales = obrasSociales;
    }
    
    
}
