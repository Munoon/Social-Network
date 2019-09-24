package com.train4game.social.to;

import com.train4game.social.web.validators.StringFieldsMatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@StringFieldsMatch(message = "{error.passwordsDontMatch}", first = "password", second = "confirmPassword")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetTo {
    @NotBlank
    @Size(min = 4, max = 100)
    private String password;
    @NotBlank
    @Size(min = 4, max = 100)
    private String confirmPassword;
    @NotBlank
    @Size(min = 36, max = 36)
    private String token;
}
