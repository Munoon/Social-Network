package com.train4game.social.addons;

import com.train4game.social.service.UserService;
import com.train4game.social.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

import static com.train4game.social.web.SecurityUtil.getAuthorizedUser;

@Component
public class DatabaseLocaleResolver extends SessionLocaleResolver {
    @Autowired
    private UserService service;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        try {
            UserTo user = getAuthorizedUser().getUserTo();
            return Locale.forLanguageTag(user.getLocale());
        } catch (NullPointerException e) {
            return super.resolveLocale(request);
        }
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        try {
            UserTo user = getAuthorizedUser().getUserTo();
            service.updateLocale(user.getId(), locale.toLanguageTag());
        } catch (NullPointerException e) {
            // do nothing
        }

        super.setLocale(request, response, locale);
    }
}
