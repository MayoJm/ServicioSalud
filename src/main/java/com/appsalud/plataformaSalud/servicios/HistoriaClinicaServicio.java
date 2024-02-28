package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.HistoriaClinica;
import com.appsalud.plataformaSalud.entidades.UsuarioPaciente;
import com.appsalud.plataformaSalud.entidades.UsuarioProfesional;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.HistoriaClinicaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriaClinicaServicio {

    @Autowired
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;

    @Transactional
    public void crearHistoriaClinica(UsuarioPaciente usuariopaciente, UsuarioProfesional usuarioProfesional,
            String nombre, Integer edad,
            String sexo, Double peso, String notas, LocalDate fechaConsulta, Boolean alta)
            throws MiException {
        List<String> datosHistoricos = new ArrayList<String>();
        datosHistoricos.add(notas);
        List<LocalDate> fechas = new ArrayList<LocalDate>();
        fechas.add(fechaConsulta);
        validar(nombre, edad, sexo, peso, datosHistoricos, fechas);
        System.out.println("datos validados");

        HistoriaClinica historiaClinica = new HistoriaClinica();

        System.out.println("historiaClinica creada");
        historiaClinica.setUsuarioPaciente(usuariopaciente);
        historiaClinica.setUsuarioProfesional(usuarioProfesional);
        historiaClinica.setEdad(edad);
        historiaClinica.setSexo(sexo);
        historiaClinica.setPeso(peso);
        historiaClinica.setNombre(nombre);
        historiaClinica.setDatosHistoricos(datosHistoricos);
        historiaClinica.setFechaConsulta(fechas);
        historiaClinica.setAlta(alta);

        historiaClinicaRepositorio.save(historiaClinica);
    }

    @Transactional
    public void modificarHistoriaClinica(HistoriaClinica historiaClinica, String nombre, Integer edad, String sexo,
            Double peso,
            List<String> datosHistoricos, List<LocalDate> fechaConsulta) throws MiException {
        validar(nombre, edad, sexo, peso, datosHistoricos, fechaConsulta);

        historiaClinica.setNombre(nombre);
        historiaClinica.setEdad(edad);
        historiaClinica.setSexo(sexo);
        historiaClinica.setPeso(peso);
        historiaClinica.setDatosHistoricos(datosHistoricos);
        historiaClinica.setFechaConsulta(fechaConsulta);
        historiaClinicaRepositorio.save(historiaClinica);

    }

    @Transactional
    public void anularHistoriaClinica(String id) throws MiException {
        Optional<HistoriaClinica> respuesta = historiaClinicaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            HistoriaClinica historiaClinica = respuesta.get();
            historiaClinica.setAlta(false);
            historiaClinicaRepositorio.save(historiaClinica);
        }
    }

    public void validar(String nombre, Integer edad, String sexo, Double peso, List<String> datosHistoricos,
            List<LocalDate> fechas) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser vacio");
        }
        if (edad == null) {
            throw new MiException("La edad no puede ser vacia");
        }
        if (sexo == null || sexo.isEmpty()) {
            throw new MiException("El sexo no puede ser vacio");
        }
        if (peso == null) {
            throw new MiException("El peso no puede ser vacio");
        }
        if (datosHistoricos == null || datosHistoricos.isEmpty()) {
            throw new MiException("Los datos historicos no pueden ser vacios");
        }
        if (fechas == null || fechas.isEmpty()) {
            throw new MiException("La fecha de consulta no puede ser vacia");
        }
    }

    public boolean tieneHistoriaClinica(UsuarioPaciente paciente, UsuarioProfesional profesional) {
        // Buscar la historia clínica del paciente por su ID
        Optional<HistoriaClinica> historiaClinica = historiaClinicaRepositorio.findByPacienteAndProfesional(paciente,
                profesional);

        // Devolver true si se encuentra la historia clínica, false en caso contrario
        return historiaClinica.isPresent();
    }

    public Optional<HistoriaClinica> obtenerHistoriaClinica(UsuarioPaciente paciente, UsuarioProfesional profesional) {
        return historiaClinicaRepositorio.findByPacienteAndProfesional(paciente, profesional);
    }

}
