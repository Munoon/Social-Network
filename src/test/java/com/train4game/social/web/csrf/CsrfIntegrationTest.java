package com.train4game.social.web.csrf;

import com.train4game.social.config.AppConfig;
import com.train4game.social.config.MvcConfig;
import com.train4game.social.to.UserTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

import static com.train4game.social.data.UserTestData.USER;
import static com.train4game.social.data.UserTestData.createNewUserTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {AppConfig.class, MvcConfig.class})
@Transactional
public class CsrfIntegrationTest {
    private static final CharacterEncodingFilter FILTER = new CharacterEncodingFilter("UTF-8", true);
    private static final String URL = "/profile/";
    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(ctx)
                .addFilter(FILTER)
                .apply(springSecurity())
                .build();
    }

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
}
