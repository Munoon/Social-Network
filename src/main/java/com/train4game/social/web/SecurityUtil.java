package com.train4game.social.web;

import com.train4game.social.model.User;

import static com.train4game.social.model.Role.ROLE_ADMIN;

public class SecurityUtil {
    private static User AUTHORIZED_USER = new User(User.START_SEQ, "Admin", "admin@gmail.com", "admin", ROLE_ADMIN);

    private SecurityUtil() {
    }

    public static void setAuthUser(User user) {
        AUTHORIZED_USER = user;
    }

    public static User authUser() {
        return AUTHORIZED_USER;
    }

    public static int authUserId() {
        return AUTHORIZED_USER.getId();
    }
}
