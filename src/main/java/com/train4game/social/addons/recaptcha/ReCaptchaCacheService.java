package com.train4game.social.addons.recaptcha;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class ReCaptchaCacheService {
    private static final int MAX_TRIES = 5;
    private static final int MAX_SAVE_HOURS = 5;
    private LoadingCache<String, Integer> cache;

    public ReCaptchaCacheService() {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(MAX_SAVE_HOURS, TimeUnit.HOURS).build(new CacheLoader<>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }

    public void reCaptchaSucceeded(String key) {
        cache.invalidate(key);
    }

    public void reCaptchaFailed(String key) {
        int attempts = cache.getUnchecked(key);
        cache.put(key, ++attempts);
    }

    public boolean isBlocked(String key) {
        return cache.getUnchecked(key) >= MAX_TRIES;
    }
}
