package com.train4game.social.to;

import com.train4game.social.addons.recaptcha.ValidReCaptcha;
import com.train4game.social.web.validators.EmailExists;
import com.train4game.social.web.validators.StringFieldsMatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@StringFieldsMatch(message = "{error.emailsDontMatch}", first = "email", second = "confirmEmail")
public class PasswordForgotTo {
    @NotBlank
    @Email
    @EmailExists(message = "{error.emailDoesntExist}")
    private String email;

    @ValidReCaptcha
    private String reCaptchaResponse;
}
