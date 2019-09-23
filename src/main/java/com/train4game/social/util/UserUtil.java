package com.train4game.social.util;

import com.train4game.social.model.Role;
import com.train4game.social.model.User;
import com.train4game.social.to.UserTo;

public class UserUtil {
    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getSurname(), userTo.getEmail(), userTo.getPassword(), Role.ROLE_USER);
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getPassword());
    }
}
