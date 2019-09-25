package com.train4game.social.config;

import com.train4game.social.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Properties;

@Configuration
@EnableScheduling
@EnableAsync
@ComponentScan("com.train4game.social.**.service")
@PropertySource("classpath:settings.properties")
@Import({DbConfig.class, WebSecurityConfig.class, ThymeleafConfig.class})
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        final var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

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
        messageSource.setCacheSeconds(10);
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames("classpath:messages/app",
                "classpath:messages/validation");
        return messageSource;
    }

    @Bean
    @Description("Message Util")
    public MessageUtil messageUtil(MessageSource messageSource) {
        return new MessageUtil(messageSource);
    }

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
