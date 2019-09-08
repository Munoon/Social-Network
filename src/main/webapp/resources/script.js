const registrationEl = document.getElementById("registrationEl");
const loginEl = document.getElementById("loginEl");

function showRegister() {
    registrationEl.hidden = false;
    loginEl.hidden = true;
}

function showLogin() {
    registrationEl.hidden = true;
    loginEl.hidden = false;
}

function login(email, pass) {
    const inputs = document.forms['loginForm'].elements;
    inputs['username'].value = email;
    inputs['password'].value = pass;
}




