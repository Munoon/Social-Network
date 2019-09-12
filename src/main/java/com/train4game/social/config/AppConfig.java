package com.train4game.social.config;

import org.springframework.context.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.train4game.social.**.service")
@Import({DbConfig.class, WebSecurityConfig.class})
@PropertySource({"classpath:settings.properties"})
public class AppConfig {
    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
