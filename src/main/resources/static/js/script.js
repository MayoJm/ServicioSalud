/*=============== SHOW MENU ===============*/
const navMenu = document.getElementById('nav-menu'),
  navToggle = document.getElementById('nav-toggle'),
  navClose = document.getElementById('nav-close');

/* Menu show */
navToggle.addEventListener('click', () => {
  navMenu.classList.add('show-menu');
});

/* Menu hidden */
navClose.addEventListener('click', () => {
  navMenu.classList.remove('show-menu');
});


/*=============== LOGIN ===============*/
const login = document.getElementById('login'),
  loginBtn = document.getElementById('login-btn'),
  loginClose = document.getElementById('login-close');

/* Login show */
loginBtn.addEventListener('click', () => {
  login.classList.add('show-login');
});

/* Login hidden */
loginClose.addEventListener('click', () => {
  login.classList.remove('show-login');
});

document.addEventListener('DOMContentLoaded', function () {
    const toggleButtons = document.querySelectorAll('.Toggle-Button');

    toggleButtons.forEach(button => {
        button.addEventListener('click', () => {
            const description = button.nextElementSibling;
            console.log(description); // Agregamos esta línea para imprimir la descripción
            description.classList.toggle('hidden');
        });
    });
});

/*============REGISTRAR(signup)============*/
/*==============Rol Usuario================*/
signupClose = document.getElementById('select-role-close');

// Registro show
document.getElementById('signup').addEventListener('click', () => {
  document.getElementById('select-role').classList.add('show-login');
});
document.getElementById('signup2').addEventListener('click', () => {   //MARCADOR POR SI NO FUNCIONA XD
  document.getElementById('select-role').classList.add('show-login');
});

// Registro Hidden
signupClose.addEventListener('click', () => {
  document.getElementById('select-role').classList.remove('show-login');
});
// Obtener referencia al botón de Continuar
const selectRoleBtn = document.getElementById('select-role-btn');

// Manejar el clic en el botón
selectRoleBtn.addEventListener('click', () => {
  // Obtener la opción seleccionada
  const selectedRole = document.querySelector('input[name="tipoUsuario"]:checked');

  // Verificar si se ha seleccionado una opción
  if (selectedRole) {
      // Obtener el valor del atributo "th:value"
      const selectedValue = selectedRole.getAttribute('th:value');

      // Redirigir a la vista correspondiente
      if (selectedValue === 'profesional') {
          window.location.href = '/registroProfesional.html'; // Ruta a la vista de registro para profesionales
      } else if (selectedValue === 'paciente') {
          window.location.href = '/registroPaciente.html'; // Ruta a la vista de registro para pacientes
      }
  } else {
      // Mostrar un mensaje de error si no se ha seleccionado ninguna opción
      alert('Por favor seleccione un rol.');
  }
});
