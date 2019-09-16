package com.train4game.social.web.csrf;

import com.train4game.social.model.User;
import com.train4game.social.to.UserTo;
import com.train4game.social.web.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static com.train4game.social.TestUtil.userAuth;
import static com.train4game.social.data.UserTestData.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class CsrfIntegrationTest extends AbstractWebTest {
    private static final String URL = "/profile/";

    @Test
    public void loginWithoutCsrf() throws Exception {
        mockMvc.perform(post(URL + "login")
                .param("username", USER.getEmail())
                .param("password", USER.getPassword()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void loginWithCsrf() throws Exception {
        mockMvc.perform(post(URL + "login")
                .param("username", USER.getEmail())
                .param("password", USER.getPassword())
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void registerWithoutCsrf() throws Exception {
        UserTo userTo = createNewUserTo();
        mockMvc.perform(post(URL + "register")
                .param("username", userTo.getEmail())
                .param("password", userTo.getPassword())
                .param("email", userTo.getEmail()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void registerWithCsrf() throws Exception {
        UserTo userTo = createNewUserTo();
        mockMvc.perform(post(URL + "register")
                .param("username", userTo.getEmail())
                .param("password", userTo.getPassword())
                .param("email", userTo.getEmail())
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserToWithoutCsrf() throws Exception {
        User user = new User(ADMIN);
        user.setEmail("newemail@gmail.com");
        mockMvc.perform(post(URL)
                .param("username", user.getEmail())
                .param("password", user.getPassword())
                .param("email", user.getEmail())
                .with(userAuth(ADMIN)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUserToWithCsrf() throws Exception {
        User user = new User(ADMIN);
        user.setEmail("newemail@gmail.com");
        mockMvc.perform(post(URL)
                .param("username", user.getEmail())
                .param("password", user.getPassword())
                .param("email", user.getEmail())
                .with(userAuth(ADMIN))
                .with(csrf()))
                .andExpect(status().isOk());
    }
}
