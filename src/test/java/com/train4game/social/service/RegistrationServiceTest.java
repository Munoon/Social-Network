package com.train4game.social.service;

import com.train4game.social.AbstractTest;
import com.train4game.social.model.User;
import com.train4game.social.to.RegisterUserTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.train4game.social.data.TokenTestData.*;
import static com.train4game.social.data.UserTestData.*;
import static com.train4game.social.model.User.Role.ROLE_USER;

class RegistrationServiceTest extends AbstractTest {
    @Autowired
    private RegistrationService service;

    @Autowired
    private UserService userService;

    @Test
    void registerUser() {
        RegisterUserTo register = getRegisterUserTo();
        User user = service.registerUser(register);
        User expected = new User(user.getId(), "New", "User", "newemail@gmail.com", "easyPass", false, ROLE_USER);
        assertMatch(userService.getAll(), ADMIN, USER, NEW_USER, expected);
    }

    @Test
    void getToken() {
        assertMatch(service.getToken(NEW_USER), TOKEN);
    }

    @Test
    void getUser() {
        User expected = new User(NEW_USER);
        User actual = service.getUser(new RegisterUserTo(expected.getName(), expected.getSurname(), expected.getEmail(), "newUser", null, null));
        assertMatch(actual, expected);
    }
}