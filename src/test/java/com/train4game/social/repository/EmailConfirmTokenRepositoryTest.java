package com.train4game.social.repository;

import com.train4game.social.model.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
class EmailConfirmTokenRepositoryTest {
    @Autowired
    private TokenRepository repository;

    @Test
    void findByToken() {
        Token token = repository.findByToken("56426919-4642-40fd-b7ad-b530c795a197").get();
        assertThat(token).isNotNull();
    }
}