package com.train4game.social.config;

import com.train4game.social.model.User;
import com.train4game.social.repository.UserRepository;
import com.train4game.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/login", "/register",
                        "/resend-token", "/confirm-token",
                        "/forgot-password", "/reset-password")
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers("/rest/**", "/**")
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/rest/admin/**", "/admin/**")
                .hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/profile", true)
                .loginProcessingUrl("/login")
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .rememberMeCookieName("remember")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout");
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserRepository repository) {
        return map -> {
            String googleId = (String) map.get("sub");
            String email = (String) map.get("email");

            return repository.findByGoogleIdOrEmail(googleId, email).orElseGet(() -> {
                User user = new User();
                user.setName((String) map.get("given_name"));
                user.setSurname((String) map.get("family_name"));
                user.setLocale((String) map.get("locale"));
                user.setEmail(email);
                user.setGoogleId(googleId);
                user.setEnabled(true);
                user.setPassword("password"); // TODO: refactor all method
                return repository.save(user);
            });
        };
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/webjars/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Description("Password Encoder")
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
