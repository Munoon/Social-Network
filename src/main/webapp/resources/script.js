const registrationEl = document.getElementById("registrationEl");
const loginEl = document.getElementById("loginEl");
const loginForm = document.forms['loginForm'];
const dropdownMenu = document.getElementById('dropdown-menu');
let isNavMenuOpen = false;

if (dropdownMenu !== null) {
    dropdownMenu.querySelector('button').addEventListener('click', e => {
        e.preventDefault();
        let elementClassList = dropdownMenu.querySelector('.dropdown-menu').classList;
        elementClassList.toggle('show');
        isNavMenuOpen = elementClassList.contains('show');
    });

    document.addEventListener('click', e => {
        if (isNavMenuOpen)
            dropdownMenu.querySelector('.dropdown-menu').classList.remove('show');
    });
}

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