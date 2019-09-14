package com.train4game.social.service;

import com.train4game.social.model.Recaptcha;
import com.train4game.social.recaptcha.ReCaptchaCacheService;
import com.train4game.social.recaptcha.ReCaptchaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;

@Service
public class RecaptchaService {
    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final Logger log = LoggerFactory.getLogger(RecaptchaService.class);
    private ReCaptchaCacheService cacheService = new ReCaptchaCacheService();

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    @Value("${google.recaptcha.html}")
    private String recaptchaHtml;

    @Autowired
    private RestOperations restOperations;

    @Autowired
    private HttpServletRequest req;

    public Recaptcha verifyRecaptcha(String recaptchaResponse) {
        String ip = req.getRemoteAddr();
        if (cacheService.isBlocked(ip))
            throw new ReCaptchaException("Client exceeded maximum number of failed attempts");

        String url = RECAPTCHA_URL + String.format("?secret=%s&response=%s&remoteip=%s", recaptchaSecret, recaptchaResponse, ip);
        log.info("Make get request on link: {}", url);
        Recaptcha recaptcha = restOperations.getForObject(url, Recaptcha.class);

        if (recaptcha != null && !recaptcha.isSuccess() && recaptcha.hasClientError()) {
            cacheService.reCaptchaFailed(ip);
        } else {
            cacheService.reCaptchaSucceeded(ip);
        }

        return recaptcha;
    }

    public boolean isVerifyRecaptcha(String recaptchaResponse) {
        return verifyRecaptcha(recaptchaResponse).isSuccess();
    }

    public String getRecaptchaHtml() {
        return recaptchaHtml;
    }
}
