// Supongamos que obrasSociales es un array de obras sociales obtenido del backend
    let obrasSociales= ["PARTICULAR", "OSDE", "MEDIFE","MEDICUS","SWISS_MEDICAL","GALENO","OMINT","IOMA","LITERAL","LUIS_PASTEUR","SANCOR_SALUD","ACCORD_SALUD","FEDERADA_SALUD","UNION_PERSONAL","OSDEPYM","OSPACP","OBSBA","OSUTHGRA","HOSPITAL_ITALIANO","OSECAC","PREVENCION_SALUD","OSITAC","OSCAR"];

obrasSocialesBackend.forEach(obraSocial => {
    // Encuentra el checkbox correspondiente por el valor
    let checkbox = document.querySelector(`input[value="${obraSocial}"]`);

    // Marca el checkbox si se encuentra
    if (checkbox) {
        checkbox.checked = true;
    }
});
