package com.train4game.social.data;

import com.train4game.social.model.Token;
import com.train4game.social.to.RegisterUserTo;

import java.time.LocalDateTime;

import static com.train4game.social.data.UserTestData.NEW_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class TokenTestData {
    public static final int TOKEN_ID = 103;
    public static final String TOKEN_STR = "56426919-4642-40fd-b7ad-b530c795a197";
    public static final Token TOKEN = new Token(TOKEN_ID, TOKEN_STR, Token.Type.REGISTER, NEW_USER, LocalDateTime.of(2019, 9, 25, 12, 0, 0), LocalDateTime.of(2019, 9, 26, 12, 0, 0), 0);

    public static RegisterUserTo getRegisterUserTo() {
        return new RegisterUserTo("New", "User", "newemail@gmail.com", "easyPass", null, null);
    }

    public static void assertMatch(Token actual, Token expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user");
    }
}
