package com.train4game.social.config;

import com.train4game.social.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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
    @Description("Java Mail Sender")
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
        props.forEach(prop -> properties.setProperty("mail." + prop, env.getRequiredProperty(prefix + prop)));
        sender.setJavaMailProperties(properties);
        return sender;
    }

    @Bean
    @Description("Resource Bundle Message Source")
    public MessageSource messageSource() {
        final var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames("classpath:messages/app");
        return messageSource;
    }

    @Bean
    @Description("Message Util")
    public MessageUtil messageUtil(MessageSource messageSource) {
        return new MessageUtil(messageSource);
    }
}
