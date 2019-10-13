package com.train4game.social.util;

import java.io.IOException;

import static com.train4game.social.web.json.JacksonObjectMapper.getMapper;

public class JsonUtil {
    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }
}
