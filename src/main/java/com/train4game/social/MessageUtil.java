package com.train4game.social;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@AllArgsConstructor
public class MessageUtil {
    public static final Locale RU_LOCALE = new Locale("ru");

    private final MessageSource messageSource;

    public String getMessage(String code, Locale locale, String... args) {
        return messageSource.getMessage(code, args, locale);
    }

    public String getMessage(String code, String... args) {
        return getMessage(code, LocaleContextHolder.getLocale(), args);
    }
}
