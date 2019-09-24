package com.train4game.social.repository;

import com.train4game.social.model.Token;
import com.train4game.social.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    @Query("FROM Token v WHERE v.token = ?1")
    Optional<Token> findByToken(String token);

    Optional<Token> findByUser(User user);

    Optional<Token> findByUserEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u IN (SELECT t.user FROM Token t WHERE" +
            " t.expirationDate < :date AND t.type = :type)")
    void deleteUsersByExpiredTokens(@Param("date") LocalDateTime date,
                                    @Param("type") Token.Type type);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE expirationDate < :date AND t.type = :type")
    void deleteExpiredResetPasswordTokens(@Param("date") LocalDateTime date,
                                          @Param("type") Token.Type type);
}
