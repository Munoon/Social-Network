package com.train4game.social;

import com.train4game.social.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class TestUtil {
    public static RequestPostProcessor userAuth(User user) {
        return SecurityMockMvcRequestPostProcessors
                .authentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
                );
    }
}
