const wrapper = document.querySelector('.wrapper');
const loginLink = document.querySelector('.login-link');
const registerLink = document.querySelector('.register-link');
const btnPopup = document.querySelector('.btnLogin-popup');
const iconClose = document.querySelector('.icon-close');


registerLink.addEventListener('click', () => {
    wrapper.classList.add('active');
    adjustWrapperHeight();
});

loginLink.addEventListener('click', () => {
    wrapper.classList.remove('active');
    resetWrapperHeight();
});

btnPopup.addEventListener('click', () => {
    wrapper.classList.add('active-popup');
});

iconClose.addEventListener('click', () => {
    wrapper.classList.remove('active-popup');
});

function adjustWrapperHeight() {
    const formBox = document.querySelector('.form-box.register');
    const formHeight = formBox.offsetHeight;
    wrapper.style.height = formHeight + 'px';
}

function resetWrapperHeight() {
    wrapper.style.height = 'auto';
}

