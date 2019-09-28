package com.train4game.social.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.train4game.social.addons.DatabaseLocaleResolver;
import com.train4game.social.web.json.JacksonObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;
import java.util.Locale;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.train4game.social.**.web")
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private MessageSource messageSource;

    @Bean
    @Description("Jackson Object Mapper")
    public ObjectMapper objectMapper() {
        return JacksonObjectMapper.getMapper();
    }

    @Bean
    @Description("Cookie Locale Resolver")
    public LocaleResolver localeResolver() {
        final var localeResolver = new DatabaseLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        localeResolver.setLocaleAttributeName("lang");
        return localeResolver;
    }

    @Bean
    @Description("Locale Interceptor")
    public LocaleChangeInterceptor localeInterceptor() {
        final var localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/")
                .resourceChain(false);
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:static/");
    }

    @Override
    public Validator getValidator() {
        final var validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }
}
