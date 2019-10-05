package com.train4game.social.service;

import com.train4game.social.model.Token;
import com.train4game.social.model.User;
import com.train4game.social.repository.TokenRepository;
import com.train4game.social.service.email.AsyncEmailSender;
import com.train4game.social.to.PasswordForgotTo;
import com.train4game.social.to.PasswordResetTo;
import com.train4game.social.to.UserTo;
import com.train4game.social.util.exception.NotFoundException;
import com.train4game.social.util.exception.TokenExpiredException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.train4game.social.util.TokenUtil.isTokenExpired;
import static com.train4game.social.util.exception.Messages.NOT_FOUND;
import static com.train4game.social.util.exception.Messages.TOKEN_EXPIRED;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class ProfileService {
    private static final String MESSAGE_PREFIX = "token.forgotPassword.mail.";
    private static final String TOKEN_URL = "/reset-password";

    private UserService userService;
    private TokenRepository tokenRepository;
    private AsyncEmailSender asyncEmailSender;
    private PasswordEncoder encoder;

    @Transactional
    public void processForgotPassword(PasswordForgotTo passwordForgotTo) {
        User user = userService.getByEmail(passwordForgotTo.getEmail());
        Token token = new Token(Token.Type.RESET_PASSWORD, user);
        String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build().toUriString();
        asyncEmailSender.sendTokenEmail(token, MESSAGE_PREFIX, appUrl + TOKEN_URL, LocaleContextHolder.getLocale());
        tokenRepository.save(token);
    }

    @Transactional(rollbackFor = TokenExpiredException.class)
    public void resetPassword(PasswordResetTo passwordResetTo) {
        Token token = getToken(passwordResetTo.getToken());
        User user = token.getUser();
        if (isTokenExpired(token)) {
            tokenRepository.delete(token);
            throw new TokenExpiredException(TOKEN_EXPIRED, token);
        }
        user.setPassword(encoder.encode(passwordResetTo.getPassword()));
        tokenRepository.delete(token);
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = userService.get(userTo.getId());
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
    }

    public Token getToken(String tokenMsg) {
        return tokenRepository.findByToken(tokenMsg)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "token")));
    }
}
