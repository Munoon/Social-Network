package com.train4game.social.service.scheduler;

import com.train4game.social.model.Token;
import com.train4game.social.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class EntityRemover {
    private static final Logger log = LoggerFactory.getLogger(EntityRemover.class);
    private TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 * * *")
    private void deleteUnconfirmedUsers() {
        log.info("Deleting unconfirmed users");
        tokenRepository.deleteUsersByExpiredTokens(LocalDateTime.now(), Token.Type.REGISTER);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void deleteExpiredResetPasswordTokens() {
        log.info("Deleting expired reset password tokens");
        tokenRepository.deleteExpiredResetPasswordTokens(LocalDateTime.now(), Token.Type.RESET_PASSWORD);
    }
}
