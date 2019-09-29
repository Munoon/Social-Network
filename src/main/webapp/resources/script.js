const registrationEl = document.getElementById("registrationEl");
const loginEl = document.getElementById("loginEl");
const loginForm = document.forms['loginForm'];
const alerts = document.querySelectorAll('.alert');

function showRegister() {
    registrationEl.hidden = false;
    loginEl.hidden = true;
    alerts.forEach(alert => alert.hidden = true);
}

function showLogin() {
    registrationEl.hidden = true;
    loginEl.hidden = false;
    alerts.forEach(alert => alert.hidden = true);
}

function login(email, pass) {
    const inputs = loginForm.elements;
    inputs['email'].value = email;
    inputs['password'].value = pass;
    loginForm.submit();
}