package com.train4game.social.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.train4game.social.HasId;
import com.train4game.social.View;
import com.train4game.social.web.validators.StringFieldsMatch;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@StringFieldsMatch(first = "password", second = "confirmPassword",
        message = "{error.passwordsDontMatch}", groups = View.UserRegister.class)
public class UserTo implements HasId {
    Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml
    String name;

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml
    String surname;

    @NotBlank
    @Size(max = 200)
    @SafeHtml
    String email;

    @NotBlank(groups = View.UserRegister.class)
    @Size(min = 4, max = 100, groups = View.UserRegister.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @SafeHtml
    String locale;

    Integer vkId;

    public UserTo() {
    }

    public UserTo(Integer id, @NotBlank @Size(min = 2, max = 100) String name, @NotBlank @Size(min = 2, max = 100) String surname, @NotBlank @Size(max = 200) String email, @NotBlank @Size(min = 5, max = 100) String password, String locale, Integer vkId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.locale = locale;
        this.vkId = vkId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getVkId() {
        return vkId;
    }

    public void setVkId(Integer vkId) {
        this.vkId = vkId;
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
