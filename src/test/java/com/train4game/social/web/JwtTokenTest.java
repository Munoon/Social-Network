package com.train4game.social.web;

import com.train4game.social.addons.jwt.TokenAuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;

import static com.train4game.social.TestUtil.getContent;
import static com.train4game.social.data.UserTestData.USER;
import static com.train4game.social.data.UserTestData.contentJson;
import static com.train4game.social.util.UserUtil.asTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JwtTokenTest extends AbstractWebTest {
    @Autowired
    private Environment env;

    @Autowired
    private TokenAuthenticationService service;

    @Test
    void jwtLoginTest() throws Exception {
        String header = env.getProperty("jwt.header");
        String prefix = env.getProperty("jwt.prefix") + " ";
        String token = service.getToken(USER.getEmail());

        mockMvc.perform(post("/rest/login")
                .param("email", USER.getEmail())
                .param("password", USER.getPassword().substring(6)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getHeader(header)).isEqualTo(prefix + token));
    }

    @Test
    void getProfileWithJwtToken() throws Exception {
        String header = env.getRequiredProperty("jwt.header");
        String prefix = env.getRequiredProperty("jwt.prefix") + " ";
        String token = prefix + service.getToken(USER.getEmail());

        mockMvc.perform(get("/rest/profile")
                .header(header, token))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(asTo(USER)))
                .andExpect(status().isOk());
    }

    @Test
    void getProfileWithoutJwtToken() throws Exception {
        mockMvc.perform(get("/rest/profile"))
                .andDo(print())
                .andExpect(result -> assertThat(getContent(result)).isEqualTo(""))
                .andExpect(status().isFound());
    }

    @Test
    void jwtLoginWithoutPasswordTest() throws Exception {
        mockMvc.perform(post("/rest/login")
                .param("email", USER.getEmail()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtLoginWithoutLoginTest() throws Exception {
        mockMvc.perform(post("/rest/login")
                .param("password", USER.getPassword().substring(6)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtLoginWithoutLoginAndPasswordTest() throws Exception {
        mockMvc.perform(post("/rest/login"))
                .andExpect(status().isUnauthorized());
    }
}
