const registrationEl = document.getElementById("registrationEl");
const loginEl = document.getElementById("loginEl");
const loginForm = document.forms['loginForm'];

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
    inputs['username'].value = email;
    inputs['password'].value = pass;
    loginForm.submit();
}




