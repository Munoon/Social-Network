package com.train4game.social.web.csrf;

import com.train4game.social.model.User;
import com.train4game.social.service.RecaptchaService;
import com.train4game.social.to.UserTo;
import com.train4game.social.web.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.train4game.social.TestUtil.userAuth;
import static com.train4game.social.data.UserTestData.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class CsrfIntegrationTest extends AbstractWebTest {
    @Autowired
    private RecaptchaService recaptchaService;

    @Test
    public void loginWithoutCsrf() throws Exception {
        mockMvc.perform(post("/login")
                .param("email", USER.getEmail())
                .param("password", USER.getPassword()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void loginWithCsrf() throws Exception {
        mockMvc.perform(post("/login")
                .param("email", USER.getEmail())
                .param("password", USER.getPassword())
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void registerWithoutCsrf() throws Exception {
        UserTo userTo = createNewUserTo();
        String validRecaptcha = "valid-recaptcha";
        when(recaptchaService.isVerifyRecaptcha(validRecaptcha)).thenReturn(true);
        mockMvc.perform(post("/register")
                .param("name", userTo.getName())
                .param("surname", userTo.getSurname())
                .param("email", userTo.getEmail())
                .param("password", userTo.getPassword())
                .param("confirmPassword", userTo.getPassword())
                .param("g-recaptcha-response", validRecaptcha))
                .andExpect(status().isForbidden());
    }

    @Test
    public void registerWithCsrf() throws Exception {
        UserTo userTo = createNewUserTo();
        String validRecaptcha = "valid-recaptcha";
        when(recaptchaService.isVerifyRecaptcha(validRecaptcha)).thenReturn(true);
        mockMvc.perform(post("/register")
                .param("name", userTo.getName())
                .param("surname", userTo.getSurname())
                .param("email", userTo.getEmail())
                .param("password", userTo.getPassword())
                .param("confirmPassword", userTo.getPassword())
                .param("g-recaptcha-response", validRecaptcha)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?disabled"));
    }

    @Test
    public void updateUserToWithoutCsrf() throws Exception {
        User user = new User(ADMIN);
        user.setEmail("newemail@gmail.com");
        mockMvc.perform(post("/")
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .with(userAuth(ADMIN)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUserToWithCsrf() throws Exception {
        UserTo userTo = createNewUserTo();
        String validRecaptcha = "valid-recaptcha";
        when(recaptchaService.isVerifyRecaptcha(validRecaptcha)).thenReturn(true);
        mockMvc.perform(post("/register")
                .param("name", userTo.getName())
                .param("surname", userTo.getSurname())
                .param("email", userTo.getEmail())
                .param("password", userTo.getPassword())
                .param("confirmPassword", userTo.getPassword())
                .param("g-recaptcha-response", validRecaptcha)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?disabled"));
    }
}
