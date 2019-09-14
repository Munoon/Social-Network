package com.train4game.social.service;

import com.train4game.social.model.Recaptcha;
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

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    @Value("${google.recaptcha.html}")
    private String recaptchaHtml;

    @Autowired
    private RestOperations restOperations;

    @Autowired
    private HttpServletRequest req;

    public Recaptcha verifyRecaptcha(String recaptchaResponse) {
        String url = RECAPTCHA_URL + String.format("?secret=%s&response=%s&remoteip=%s", recaptchaSecret, recaptchaResponse, req.getRemoteAddr());
        log.info("Make get request on link: {}", url);
        return restOperations.getForObject(url, Recaptcha.class);
    }

    public boolean isVerifyRecaptcha(String recaptchaResponse) {
        return verifyRecaptcha(recaptchaResponse).isSuccess();
    }

    public String getRecaptchaHtml() {
        return recaptchaHtml;
    }
}
