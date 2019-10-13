package com.train4game.social;

import com.train4game.social.model.User;
import com.train4game.social.util.JsonUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.UnsupportedEncodingException;

public class TestUtil {
    public static RequestPostProcessor userAuth(User user) {
        return SecurityMockMvcRequestPostProcessors
                .authentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
                );
    }

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }
}
