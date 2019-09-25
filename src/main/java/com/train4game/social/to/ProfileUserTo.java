package com.train4game.social.to;

import com.train4game.social.web.validators.StringFieldsMatch;
import com.train4game.social.web.validators.UniqueEmail;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@StringFieldsMatch(first = "password", second = "confirmPassword",
        message = "{error.passwordsDontMatch}")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProfileUserTo {
    Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    @NotBlank
    @Size(max = 200)
    @UniqueEmail(message = "{error.uniqueEmail}")
    String email;
}
