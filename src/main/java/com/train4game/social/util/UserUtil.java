package com.train4game.social.util;

import com.train4game.social.model.User;
import com.train4game.social.to.RegisterUserTo;
import com.train4game.social.to.UserTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class UserUtil {
    public static User createNewFromTo(RegisterUserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail(), userTo.getPassword(), User.Role.ROLE_USER);
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
