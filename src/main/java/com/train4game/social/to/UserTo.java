package com.train4game.social.to;

import com.train4game.social.View;
import com.train4game.social.recaptcha.ValidReCaptcha;
import com.train4game.social.web.validators.StringFieldsMatch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@StringFieldsMatch(first = "password", second = "confirmPassword",
        message = "{error.passwordsDontMatch}", groups = View.UserRegister.class)
public class UserTo {
    Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    @NotBlank
    @Size(min = 2, max = 100)
    String surname;

    @NotBlank
    @Size(max = 200)
    String email;

    @NotBlank(groups = View.UserRegister.class)
    @Size(min = 4, max = 100, groups = View.UserRegister.class)
    String password;

    @NotBlank(groups = View.UserRegister.class)
    @Size(min = 4, max = 100, groups = View.UserRegister.class)
    String confirmPassword;

    @ValidReCaptcha(groups = View.UserRegister.class)
    private String reCaptchaResponse;

    public UserTo() {
    }

    public UserTo(Integer id, @NotBlank @Size(min = 2, max = 100) String name, @NotBlank @Size(min = 2, max = 100) String surname, @NotBlank @Size(max = 200) String email, @NotBlank @Size(min = 5, max = 100) String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getReCaptchaResponse() {
        return reCaptchaResponse;
    }

    public void setReCaptchaResponse(String reCaptchaResponse) {
        this.reCaptchaResponse = reCaptchaResponse;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
