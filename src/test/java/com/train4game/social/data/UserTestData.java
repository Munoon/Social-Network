package com.train4game.social.data;

import com.train4game.social.model.User;
import com.train4game.social.to.UserTo;

import java.util.Arrays;

import static com.train4game.social.model.Role.ROLE_ADMIN;
import static com.train4game.social.model.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTestData {
    public static final int ADMIN_ID = User.START_SEQ;
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final User ADMIN = new User(ADMIN_ID, "Admin", ADMIN_EMAIL, "admin", ROLE_ADMIN);
    public static final User USER = new User(ADMIN_ID + 1, "User", "user@gmail.com", "user", ROLE_USER);

    public static UserTo createNewUserTo() {
        return new UserTo(null, "UserTo", "userto@gmail.com", "userTo");
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered").isEqualTo(expected);
    }
}
