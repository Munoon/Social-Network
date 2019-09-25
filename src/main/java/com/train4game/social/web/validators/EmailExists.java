package com.train4game.social.web.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD})
@Constraint(validatedBy = EmailExistsValidator.class)
public @interface EmailExists {
    String message() default "Email doesn't exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

