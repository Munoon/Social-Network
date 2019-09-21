package com.train4game.social.recaptcha;

import com.train4game.social.service.RecaptchaService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReCaptchaConstraintValidator implements ConstraintValidator<ValidReCaptcha, String> {
    @Autowired
    private RecaptchaService service;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty())
            return false;

        return service.isVerifyRecaptcha(value);
    }
}
