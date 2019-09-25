package com.train4game.social.to;

import com.train4game.social.recaptcha.ValidReCaptcha;
import com.train4game.social.web.validators.StringFieldsMatch;
import com.train4game.social.web.validators.UniqueEmail;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@StringFieldsMatch(first = "password", second = "confirmPassword",
        message = "{error.passwordsDontMatch}")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"password", "confirmPassword"})
@Getter
@Setter
public class RegisterUserTo {
    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    @NotBlank
    @Size(min = 2, max = 100)
    String surname;

    @NotBlank
    @Size(max = 200)
    @UniqueEmail(message = "{error.uniqueEmail}")
    String email;

    @NotBlank
    @Size(min = 4, max = 100)
    String password;

    @NotBlank
    @Size(min = 4, max = 100)
    String confirmPassword;

    @ValidReCaptcha
    private String reCaptchaResponse;
}
