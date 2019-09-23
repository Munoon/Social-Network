package com.train4game.social.service;

import com.train4game.social.AbstractTest;
import com.train4game.social.model.Role;
import com.train4game.social.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.train4game.social.data.UserTestData.*;

class UserServiceTest extends AbstractTest {
    @Autowired
    private UserService service;

    @Test
    void create() {
        User user = createNewUser();
        User created = service.create(user);
        user.setId(created.getId());
        assertMatch(service.getAll(), ADMIN, USER, user);
    }

    @Test
    void delete() {
        service.delete(ADMIN_ID);
        assertMatch(service.getAll(), USER);
    }

    @Test
    void get() {
        assertMatch(service.get(ADMIN_ID), ADMIN);
    }

    @Test
    void getByEmail() {
        assertMatch(service.getByEmail(ADMIN_EMAIL), ADMIN);
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(), ADMIN, USER);
    }

    @Test
    void update() {
        User updated = new User(ADMIN);
        updated.setName("New Name");
        updated.setEmail("newEmail@email.com");

        service.update(updated);
        assertMatch(service.getAll(), USER, updated);
    }
}