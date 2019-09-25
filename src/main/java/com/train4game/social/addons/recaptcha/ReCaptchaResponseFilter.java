package com.train4game.social.addons.recaptcha;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReCaptchaResponseFilter implements Filter {
    private static final String RECAPTCHA_ALIAS = "reCaptchaResponse";
    private static final String RECAPTCHA_RESPONSE = "g-recaptcha-response";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getParameter(RECAPTCHA_RESPONSE) != null) {
            ReCaptchaHttpServletRequest reCaptchaRequest = new ReCaptchaHttpServletRequest(request);
            chain.doFilter(reCaptchaRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private static class ReCaptchaHttpServletRequest extends HttpServletRequestWrapper {
        private Map<String, String[]> params;

        ReCaptchaHttpServletRequest(HttpServletRequest request) {
            super(request);
            params = new HashMap<>(request.getParameterMap());
            params.put(RECAPTCHA_ALIAS, request.getParameterValues(RECAPTCHA_RESPONSE));
        }

        @Override
        public String getParameter(String name) {
            return params.containsKey(name) ? params.get(name)[0] : null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return params;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(params.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return params.get(name);
        }
    }
}