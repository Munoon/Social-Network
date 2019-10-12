package com.train4game.social.addons.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class TokenAuthenticationService {
    private final long expirationTime;
    private final String secret;
    private final String header;
    private final String prefix;
    private UserDetailsService userService;

    public TokenAuthenticationService(Environment env, UserDetailsService userService) {
        this.userService = userService;
        this.expirationTime = Long.parseLong(env.getProperty("jwt.expirationTime"));
        this.secret = env.getProperty("jwt.secret");
        this.header = env.getProperty("jwt.header");
        this.prefix = env.getProperty("jwt.prefix");
    }

    public void addAuthentication(HttpServletResponse response, String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        response.addHeader(header, prefix + " " + token);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isEmpty(token) || !token.startsWith(prefix)) {
            return null;
        }

        String email = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace(prefix, ""))
                .getBody()
                .getSubject();

        if (!StringUtils.isEmpty(email)) {
            UserDetails userDetails = userService.loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }

        return null;
    }
}
