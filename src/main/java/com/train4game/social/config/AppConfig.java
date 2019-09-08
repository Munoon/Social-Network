package com.train4game.social.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.train4game.social.**.service")
@Import({DbConfig.class, SpringSecurityInitializer.class})
public class AppConfig {
}
