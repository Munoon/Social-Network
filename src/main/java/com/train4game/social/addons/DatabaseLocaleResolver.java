package com.train4game.social.addons;

import com.train4game.social.service.UserService;
import com.train4game.social.to.UserTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

import static com.train4game.social.web.SecurityUtil.getAuthorizedUser;

@Component
public class DatabaseLocaleResolver extends SessionLocaleResolver {
    private static final Logger log = LoggerFactory.getLogger(DatabaseLocaleResolver.class);

    @Autowired
    private UserService service;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        try {
            UserTo user = getAuthorizedUser().getUserTo();
            String strLocale = user.getLocale();
            Locale locale = Locale.forLanguageTag(strLocale);
            log.info("Set locale {} for user {} from model", strLocale, user.getId());
            return locale;
        } catch (NullPointerException e) {
            log.info("Set default locale");
            return super.resolveLocale(request);
        }
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        String languageTag = locale.toLanguageTag();

        try {
            UserTo user = getAuthorizedUser().getUserTo();
            service.updateLocale(user.getId(), languageTag);
            log.info("Update locale for user {} to {}", user.getId(), languageTag);
        } catch (NullPointerException e) {
            log.info("Update locale to {} for anonymous user", languageTag);
        }

        super.setLocale(request, response, locale);
    }
}
