package com.train4game.social;

import com.train4game.social.model.User;
import com.train4game.social.to.UserTo;

import static com.train4game.social.util.UserUtil.asTo;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.userTo = asTo(user);
    }

    public int getId() {
        return userTo.getId();
    }

    public UserTo getUserTo() {
        return userTo;
    }

    public void setUserTo(UserTo userTo) {
        this.userTo = userTo;
    }

    public void updateUserTo(UserTo newTo) {
        userTo = newTo;
    }
}
