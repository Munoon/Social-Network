package com.train4game.social.to;

import com.train4game.social.web.validators.StringFieldsMatch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@StringFieldsMatch(first = "password", second = "confirmPassword",
        message = "{error.passwordsDontMatch}")
public class UserTo {
    Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    @NotBlank
    @Size(max = 200)
    String email;

    @NotBlank
    @Size(min = 4, max = 100)
    String password;

    @NotBlank
    @Size(min = 4, max = 100)
    String confirmPassword;

    public UserTo() {
    }

    public UserTo(Integer id, @NotBlank @Size(min = 2, max = 100) String name, @NotBlank @Size(max = 200) String email, @NotBlank @Size(min = 5, max = 100) String password) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
