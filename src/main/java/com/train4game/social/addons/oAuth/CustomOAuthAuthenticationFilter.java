package com.train4game.social.addons.oAuth;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.addons.jwt.TokenAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomOAuthAuthenticationFilter extends OAuth2ClientAuthenticationProcessingFilter {
    private TokenAuthenticationService tokenService;

    public CustomOAuthAuthenticationFilter(String defaultFilterProcessesUrl, TokenAuthenticationService tokenService) {
        super(defaultFilterProcessesUrl);
        this.tokenService = tokenService;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((AuthorizedUser) authResult.getPrincipal()).getUserTo().getEmail();
        tokenService.addAuthentication(response, email);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
