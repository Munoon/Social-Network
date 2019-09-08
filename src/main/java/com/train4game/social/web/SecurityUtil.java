package com.train4game.social.web;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {
    private SecurityUtil() {
    }

    private static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return null;
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser getAuthorizedUser() {
        AuthorizedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public static User authUser() {
        return getAuthorizedUser().getUser();
    }

    public static int authUserId() {
        return getAuthorizedUser().getId();
    }
}
