package com.train4game.social.config;

import org.springframework.context.annotation.Import;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Import(WebSecurityConfig.class)
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

}
