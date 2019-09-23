const registrationEl = document.getElementById("registrationEl");
const loginEl = document.getElementById("loginEl");
const loginForm = document.forms['loginForm'];
const dropdownMenu = document.getElementById('dropdown-menu');

if (dropdownMenu !== null)
    dropdownMenu.querySelector('button').addEventListener('click', e => {
        e.preventDefault();
        dropdownMenu.querySelector('.dropdown-menu').classList.toggle('show');
    });

function showRegister() {
    registrationEl.hidden = false;
    loginEl.hidden = true;
}

function showLogin() {
    registrationEl.hidden = true;
    loginEl.hidden = false;
}

function login(email, pass) {
    const inputs = loginForm.elements;
    inputs['email'].value = email;
    inputs['password'].value = pass;
    loginForm.submit();
}