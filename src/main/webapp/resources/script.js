const registrationForm = document.getElementById("registrationForm");
const loginForm = document.getElementById("loginForm");

function showRegister() {
    registrationForm.hidden = false;
    loginForm.hidden = true;
}

function showLogin() {
    registrationForm.hidden = true;
    loginForm.hidden = false;
}




