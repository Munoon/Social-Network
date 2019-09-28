package com.train4game.social.web;

import com.train4game.social.AbstractTest;
import com.train4game.social.addons.recaptcha.ReCaptchaResponseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class AbstractWebTest extends AbstractTest {
    private static final CharacterEncodingFilter FILTER = new CharacterEncodingFilter("UTF-8", true);

    @Autowired
    private WebApplicationContext ctx;

    protected MockMvc mockMvc;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(ctx)
                .addFilter(FILTER)
                .addFilter(new ReCaptchaResponseFilter())
                .apply(springSecurity())
                .build();
    }
}
