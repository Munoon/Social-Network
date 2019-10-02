package com.train4game.social.config;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.model.User;
import com.train4game.social.repository.UserRepository;
import com.train4game.social.service.UserService;
import com.train4game.social.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private AuthorizationCodeResourceDetails google;

    @Autowired
    private ResourceServerProperties googleResource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
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
            User user = userService.getByGoogleIdOrEmail(googleId, email);
            if (user == null) {
                user = UserUtil.createUserFromGoogleMap(map);
                userService.create(user);
            }
            return new AuthorizedUser(user);
        };
    }

    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google, oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource.getUserInfoUri(), google.getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        tokenServices.setPrincipalExtractor(principalExtractor(userRepository));
        googleFilter.setTokenServices(tokenServices);
        return googleFilter;
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
