package com.train4game.social.web.controllers;

import com.train4game.social.service.RecaptchaService;
import com.train4game.social.service.UserService;
import com.train4game.social.to.UserTo;
import com.train4game.social.web.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import static com.train4game.social.data.UserTestData.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractWebTest {
    private static final String URL = "/profile/";

    @Autowired
    @InjectMocks
    private RecaptchaService recaptchaService;

    @Autowired
    private UserService userService;

    @Test
    void registerInvalidReCaptcha() throws Exception {
        UserTo userTo = createNewUserTo();
        mockMvc.perform(post(URL + "register")
                .param("username", userTo.getEmail())
                .param("password", userTo.getPassword())
                .param("email", userTo.getEmail())
                .param("g-recaptcha-response", "invalid-resposne")
                .with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userTo", "reCaptchaResponse"))
                .andExpect(status().isOk());

        assertMatch(userService.getAll(), ADMIN, USER);
    }

    @Test
    void registerWithoutRecaptcha() throws Exception {
        UserTo userTo = createNewUserTo();
        mockMvc.perform(post(URL + "register")
                .param("username", userTo.getEmail())
                .param("password", userTo.getPassword())
                .param("email", userTo.getEmail())
                .with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userTo", "reCaptchaResponse"))
                .andExpect(status().isOk());

        assertMatch(userService.getAll(), ADMIN, USER);
    }

    @Test
    void register() throws Exception {
        UserTo userTo = createNewUserTo();
        String validRecaptcha = "valid-recaptcha";
        when(recaptchaService.isVerifyRecaptcha(validRecaptcha)).thenReturn(true);
        mockMvc.perform(post(URL + "register")
                .param("username", userTo.getEmail())
                .param("password", userTo.getPassword())
                .param("email", userTo.getEmail())
                .param("g-recaptcha-response", validRecaptcha)
                .with(csrf()))
                .andExpect(status().isOk());

        assertMatch(userService.getAll(), ADMIN, USER, createNewUser());
    }
}