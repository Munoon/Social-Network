package com.train4game.social.service;

import com.train4game.social.AbstractTest;
import com.train4game.social.model.User;
import com.train4game.social.to.UserSettingsTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.train4game.social.data.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest extends AbstractTest {
    @Autowired
    private UserService service;

    @Test
    void create() {
        User user = createNewUser();
        User created = service.create(user);
        user.setId(created.getId());
        assertMatch(service.getAll(), ADMIN, USER, NEW_USER, user);
    }

    @Test
    void deleteById() {
        service.deleteById(ADMIN_ID);
        assertMatch(service.getAll(), USER, NEW_USER);
    }

    @Test
    void delete() {
        service.delete(ADMIN);
        assertMatch(service.getAll(), USER, NEW_USER);
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
        assertMatch(service.getAll(), ADMIN, USER, NEW_USER);
    }

    @Test
    void update() {
        User updated = new User(ADMIN);
        updated.setName("New Name");
        updated.setEmail("newemail@email.com");

        service.update(updated);
        assertMatch(service.getAll(), updated, USER, NEW_USER);
    }

    @Test
    void updateLocale() {
        User expected = new User(ADMIN);
        expected.setLocale("ru");
        service.updateLocale(ADMIN_ID, "ru");
        assertMatch(service.get(ADMIN_ID), expected);
    }

    @Test
    void enable() {
        User expected = new User(NEW_USER);
        expected.setEnabled(true);
        service.enable(NEW_USER);
        assertMatch(service.get(NEW_USER.getId()), expected);
    }

    @Test
    void getUserSettings() {
        UserSettingsTo settings = service.getUserSettings(ADMIN_ID);
        assertThat(settings).isEqualToComparingFieldByField(new UserSettingsTo("en"));
    }

    @Test
    void updateSettings() {
        UserSettingsTo expected = new UserSettingsTo("ru");
        service.updateSettings(ADMIN_ID, expected);
        assertThat(service.getUserSettings(ADMIN_ID)).isEqualToComparingFieldByField(expected);
    }
}