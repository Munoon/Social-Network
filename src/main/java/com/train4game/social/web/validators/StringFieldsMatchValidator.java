package com.train4game.social.web.validators;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringFieldsMatchValidator implements ConstraintValidator<StringFieldsMatch, Object> {
    private String first;
    private String second;
    private String message;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void initialize(StringFieldsMatch constraintAnnotation) {
        first = constraintAnnotation.first();
        second = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            Object firstValue = new BeanWrapperImpl(obj).getPropertyValue(first);
            Object secondValue = new BeanWrapperImpl(obj).getPropertyValue(second);
            valid = validate(firstValue, secondValue);
            if (!valid) {
                context.
                        buildConstraintViolationWithTemplate(messageSource.getMessage(
                                message, null, LocaleContextHolder.getLocale()))
                        .addPropertyNode(first)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
            return valid;
        } catch (final Exception ignore) {
            //ignore
        }
        return valid;
    }

    private boolean validate(Object firstValue, Object secondValue) {
        return firstValue == null && secondValue == null
                || firstValue != null && firstValue.equals(secondValue);
    }
}
