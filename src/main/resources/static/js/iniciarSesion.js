document.querySelector('.register-link').addEventListener('click', function() {
    window.location.href = "tipoUsuario.html";
});

function continuar() {
    var tipoUsuario = document.querySelector('input[name="tipoUsuario"]:checked');

    if (tipoUsuario) {
        if (tipoUsuario.value === "paciente") {
            window.location.href = "registroPaciente.html";
        } else if (tipoUsuario.value === "profesional") {
            window.location.href = "registroProfesional.html";
        }
    } else {
        alert("Selecciona un tipo de usuario antes de continuar.");
    }
}
    





