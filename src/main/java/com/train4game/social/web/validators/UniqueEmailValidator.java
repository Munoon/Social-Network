package com.train4game.social.web.validators;

import com.train4game.social.model.User;
import com.train4game.social.repository.UserRepository;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private UserRepository repository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        User user = repository.getByEmail(email).orElse(null);
        return user == null || !user.isEnabled();
    }
}
