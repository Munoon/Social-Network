package com.train4game.social.service.scheduler;

import com.train4game.social.model.Token;
import com.train4game.social.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class EntityRemover {
    private TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 * * *")
    private void deleteUnconfirmedUsers() {
        tokenRepository.deleteUsersByExpiredTokens(LocalDateTime.now(), Token.Type.REGISTER);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void deleteExpiredResetPasswordTokens() {
        tokenRepository.deleteExpiredResetPasswordTokens(LocalDateTime.now(), Token.Type.RESET_PASSWORD);
    }
}
