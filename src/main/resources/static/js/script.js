document.getElementById("ingresarBtn").addEventListener("click", function() {
    window.location.href = "iniciarSesion.html";
});

function showSection(sectionId) {
    // Ocultar todas las secciones
    document.querySelectorAll('section').forEach(function (section) {
        section.style.display = 'none';
    });

    // Mostrar la sección seleccionada
    document.getElementById(sectionId).style.display = 'block';
}

