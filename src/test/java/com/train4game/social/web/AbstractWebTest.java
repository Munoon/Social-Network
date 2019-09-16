package com.train4game.social.web;

import com.train4game.social.config.AppConfig;
import com.train4game.social.config.MvcConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringJUnitWebConfig(classes = {AppConfig.class, MvcConfig.class})
@ExtendWith(MockitoExtension.class)
public abstract class AbstractWebTest {
    private static final CharacterEncodingFilter FILTER = new CharacterEncodingFilter("UTF-8", true);

    @Autowired
    private WebApplicationContext ctx;

    protected MockMvc mockMvc;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(ctx)
                .addFilter(FILTER)
                .apply(springSecurity())
                .build();
    }
}
