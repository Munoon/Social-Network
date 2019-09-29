package com.train4game.social.data;

import com.train4game.social.model.User;
import com.train4game.social.to.UserTo;

import java.util.Arrays;

import static com.train4game.social.model.User.Role.ROLE_ADMIN;
import static com.train4game.social.model.User.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTestData {
    public static final int ADMIN_ID = User.START_SEQ;
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "Admin", ADMIN_EMAIL, "{noop}admin", true, ROLE_ADMIN);
    public static final User USER = new User(ADMIN_ID + 1, "User", "User", "user@gmail.com", "{noop}user", true, ROLE_USER);
    public static final User NEW_USER = new User(ADMIN_ID + 2, "New", "User", "newuser@gmail.com", "{noop}newUser", false, ROLE_USER);

    public static UserTo createNewUserTo() {
        return new UserTo(null, "Another", "User", "anotheruser@gmail.com", "user", "en");
    }

    public static User createNewUser() {
        return new User(null, "Another", "User", "anotheruser@gmail.com", "user", ROLE_USER);
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "password");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "password").isEqualTo(expected);
    }

    public static void assertMatchIgnoreId(Iterable<User> actual, User... expected) {
        assertMatchIgnoreId(actual, Arrays.asList(expected));
    }

    public static void assertMatchIgnoreId(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "password", "id").isEqualTo(expected);
    }
}
