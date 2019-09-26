package com.train4game.social.to;

import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserSettingsTo {
    @SafeHtml
    @Size(min = 2, max = 2)
    String locale;
}
