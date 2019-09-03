package com.train4game.social.service;

import com.train4game.social.TimingExtension;
import com.train4game.social.model.Role;
import com.train4game.social.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.train4game.social.data.UserTestData.*;

@SpringJUnitConfig(locations = "classpath:spring/spring-app.xml")
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtension.class)
class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    void create() {
        User user = new User(null, "New User", "email@gmail.com", "password", Role.ROLE_USER);
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