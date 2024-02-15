package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.FichaMedica;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.FichaMedicaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FichaMedicaServicio {

    @Autowired
    private FichaMedicaRepositorio fichaMedicaRepositorio;

    @Transactional
    public void crearFichaMedica(UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional, String edad,
            String presionArterial, String saturacion, String temperatura, Double peso, String frecuenciaRespiratoria, Double talla,
            String frecuenciaCardiaca, Double IMC, String motivoConsulta, String antecedentes, String alergia, String intervencionQuirurgica, Boolean vacunasCompletas,
            String examenFisico, String diagnostico, String tratamiento, Date fechaConsulta) throws MiException {

        validar(usuarioPaciente, usuarioProfesional, edad, presionArterial, saturacion, temperatura, peso, frecuenciaRespiratoria, talla, frecuenciaCardiaca, IMC, motivoConsulta, antecedentes, alergia, intervencionQuirurgica, vacunasCompletas, examenFisico, diagnostico, tratamiento, fechaConsulta);
        FichaMedica fichaMedica = new FichaMedica();

        fichaMedica.setAlergia(alergia);
        fichaMedica.setAntecedentes(antecedentes);
        fichaMedica.setDiagnostico(diagnostico);
        fichaMedica.setExamenFisico(examenFisico);
        fichaMedica.setFechaConsulta(fechaConsulta);
        fichaMedica.setFrecuenciaCardiaca(frecuenciaCardiaca);
        fichaMedica.setFrecuenciaRespiratoria(frecuenciaRespiratoria);
        fichaMedica.setIMC(IMC);
        fichaMedica.setIntervencionQuirurgica(intervencionQuirurgica);
        fichaMedica.setMotivoConsulta(motivoConsulta);
        fichaMedica.setPeso(peso);
        fichaMedica.setPresionArterial(presionArterial);
        fichaMedica.setSaturacion(saturacion);
        fichaMedica.setTalla(talla);
        fichaMedica.setTemperatura(temperatura);
        fichaMedica.setTratamiento(tratamiento);
        fichaMedica.setUsuarioPaciente(usuarioPaciente);
        fichaMedica.setUsuarioProfesional(usuarioProfesional);
        fichaMedica.setVacunasCompletas(vacunasCompletas);

        fichaMedicaRepositorio.save(fichaMedica);
    }
    @Transactional
    public void modificarFichaMedica(String id, UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional, String edad,
            String presionArterial, String saturacion, String temperatura, Double peso, String frecuenciaRespiratoria, Double talla,
            String frecuenciaCardiaca, Double IMC, String motivoConsulta, String antecedentes, String alergia, String intervencionQuirurgica, Boolean vacunasCompletas,
            String examenFisico, String diagnostico, String tratamiento, Date fechaConsulta) throws MiException {

        validar(usuarioPaciente, usuarioProfesional, edad, presionArterial, saturacion, temperatura, peso, frecuenciaRespiratoria, talla, frecuenciaCardiaca, IMC, motivoConsulta, antecedentes, alergia, intervencionQuirurgica, vacunasCompletas, examenFisico, diagnostico, tratamiento, fechaConsulta);
        Optional<FichaMedica> respuesta = fichaMedicaRepositorio.findById(id);
        
        if(respuesta.isPresent()) {
            FichaMedica fichaMedica = respuesta.get();
        fichaMedica.setAlergia(alergia);
        fichaMedica.setAntecedentes(antecedentes);
        fichaMedica.setDiagnostico(diagnostico);
        fichaMedica.setExamenFisico(examenFisico);
        fichaMedica.setFechaConsulta(fechaConsulta);
        fichaMedica.setFrecuenciaCardiaca(frecuenciaCardiaca);
        fichaMedica.setFrecuenciaRespiratoria(frecuenciaRespiratoria);
        fichaMedica.setIMC(IMC);
        fichaMedica.setIntervencionQuirurgica(intervencionQuirurgica);
        fichaMedica.setMotivoConsulta(motivoConsulta);
        fichaMedica.setPeso(peso);
        fichaMedica.setPresionArterial(presionArterial);
        fichaMedica.setSaturacion(saturacion);
        fichaMedica.setTalla(talla);
        fichaMedica.setTemperatura(temperatura);
        fichaMedica.setTratamiento(tratamiento);
        fichaMedica.setUsuarioPaciente(usuarioPaciente);
        fichaMedica.setUsuarioProfesional(usuarioProfesional);
        fichaMedica.setVacunasCompletas(vacunasCompletas);

        fichaMedicaRepositorio.save(fichaMedica);
        }
    }

    @Transactional(readOnly = true)
    public List<FichaMedica> listarFichasMedicasPorPaciente(UsuarioPaciente paciente) { //sino por idPaciente o dni
        List<FichaMedica> lista = fichaMedicaRepositorio.buscarPorPaciente(paciente);
        return lista;
    }

    public void validar(UsuarioPaciente usuarioPaciente, UsuarioProfesional usuarioProfesional, String edad,
            String presionArterial, String saturacion, String temperatura, Double peso, String frecuenciaRespiratoria, Double talla,
            String frecuenciaCardiaca, Double IMC, String motivoConsulta, String antecedentes, String alergia, String intervencionQuirurgica, Boolean vacunasCompletas,
            String examenFisico, String diagnostico, String tratamiento, Date fechaConsulta) throws MiException {
        if (usuarioPaciente == null) {
            throw new MiException("El paciente no puede ser nulo");
        }

        if (usuarioProfesional == null) {
            throw new MiException("El profesional no puede ser nulo");
        }
        if (edad == null || edad.isEmpty() || edad.isBlank()) {
            throw new MiException("La edad no puede ser nula o estar vacia");
        }
        if (presionArterial == null || presionArterial.isEmpty()) {
            throw new MiException("La presion arterial no puede ser nula o estar vacia");
        }
        if (saturacion == null || saturacion.isEmpty()) {
            throw new MiException("La saturacion no puede ser nula o estar vacia");
        }
        if (temperatura == null || temperatura.isEmpty()) {
            throw new MiException("La temperatura no puede ser nula o estar vacia");
        }
        if (peso == null) {
            throw new MiException("El peso no puede ser nulo");
        }
        if (frecuenciaRespiratoria == null || frecuenciaRespiratoria.isEmpty()) {
            throw new MiException("La frecuencia respiratoria no puede ser nula o estar vacia");
        }
        if (talla == null) {
            throw new MiException("La talla no puede ser nula");
        }
        if (examenFisico == null || examenFisico.isEmpty()) {
            throw new MiException("El examen fisico no puede ser nulo o estar vacio");
        }
        if (diagnostico == null || diagnostico.isEmpty()) {
            throw new MiException("El diagnostico no puede ser nulo o estar vacio");
        }
        if (tratamiento == null || tratamiento.isEmpty()) {
            throw new MiException("El tratamiento no puede ser nulo o estar vacio");
        }
        if (fechaConsulta == null) {
            throw new MiException("La fechade consulta no puede ser nula");
        }
    }

}

