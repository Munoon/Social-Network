package com.train4game.social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.List;
import java.util.Properties;

@Configuration
@ComponentScan("com.train4game.social.**.service")
@PropertySource("classpath:settings.properties")
@Import({DbConfig.class, WebSecurityConfig.class})
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender javaMailSender() {
        final String prefix = "registration.mail.";
        final var sender = new JavaMailSenderImpl();
        sender.setHost(env.getRequiredProperty(prefix + "host"));
        sender.setPort(Integer.parseInt(env.getRequiredProperty(prefix + "port")));
        sender.setUsername(env.getRequiredProperty(prefix + "username"));
        sender.setPassword(env.getRequiredProperty(prefix + "password"));
        sender.setDefaultEncoding("UTF-8");
        List<String> props = List.of("transport.protocol", "smtp.auth",
                "smtp.starttls.enable", "debug");
        Properties properties = new Properties();
        props.forEach(prop -> properties.setProperty(prefix + prop, env.getRequiredProperty(prefix + prop)));
        sender.setJavaMailProperties(properties);
        return sender;
    }
}
