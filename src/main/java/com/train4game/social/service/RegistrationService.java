package com.train4game.social.service;

import com.train4game.social.model.Token;
import com.train4game.social.model.User;
import com.train4game.social.repository.TokenRepository;
import com.train4game.social.service.email.AsyncEmailSender;
import com.train4game.social.to.RegisterUserTo;
import com.train4game.social.util.exception.NotFoundException;
import com.train4game.social.util.exception.TokenExpiredException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Locale;

import static com.train4game.social.util.TokenUtil.isTokenExpired;
import static com.train4game.social.util.UserUtil.createNewFromTo;
import static com.train4game.social.util.exception.Messages.NOT_FOUND;
import static com.train4game.social.util.exception.Messages.TOKEN_EXPIRED;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class RegistrationService {
    private static final String MESSAGE_PREFIX = "token.registration.mail.";
    private static final String TOKEN_URL = "/confirm-token";

    private UserService userService;
    private TokenRepository tokenRepository;
    private AsyncEmailSender asyncEmailSender;

    @Transactional
    public User registerUser(RegisterUserTo userTo) {
        User user = userService.create(createNewFromTo(userTo));
        Token token = new Token(Token.Type.REGISTER, user);
        tokenRepository.save(token);
        sendEmail(token);
        return user;
    }

    @Transactional(noRollbackFor = TokenExpiredException.class)
    public User enableUser(String tokenMsg) {
        Token token = getToken(tokenMsg);
        if (isTokenExpired(token)) {
            userService.delete(token.getUser());
            throw new TokenExpiredException(TOKEN_EXPIRED, token);
        }
        tokenRepository.delete(token);
        User user = token.getUser();
        userService.enable(user);
        return user;
    }

    @Transactional(noRollbackFor = TokenExpiredException.class)
    public void resendToken(String email) {
        Token token = tokenRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " is not found"));
        if (isTokenExpired(token)) {
            userService.delete(token.getUser());
            throw new TokenExpiredException(TOKEN_EXPIRED, token);
        }
        handleResend(token);
    }

    private void sendEmail(Token token) {
        String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build().toUriString();
        Locale locale = LocaleContextHolder.getLocale();
        asyncEmailSender.sendTokenEmail(token, MESSAGE_PREFIX, appUrl + TOKEN_URL, locale);
    }

    private void handleResend(Token token) {
        int sendNumber = token.getResendNumber();
        if (token.getResendNumber() < Token.RESEND_NUMBER) {
            sendEmail(token);
            token.setResendNumber(sendNumber + 1);
        } else {
            userService.delete(token.getUser());
            throw new TokenExpiredException(TOKEN_EXPIRED, token);
        }
    }

    private Token getToken(String tokenMsg) {
        return tokenRepository.findByToken(tokenMsg)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "Token")));
    }

    public Token getToken(User user) {
        return tokenRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "Token")));
    }

    public User getUser(RegisterUserTo userTo) {
        try {
            return userService.getByEmail(userTo.getEmail());
        } catch (NotFoundException e) {
            return null;
        }
    }
}
