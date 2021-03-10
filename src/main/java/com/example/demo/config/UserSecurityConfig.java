package com.example.demo.config;

import com.example.demo.security.CustomAccessDeniedHandler;
import com.example.demo.security.CustomOAuth2LoginSuccessHandler;
import com.example.demo.security.CustomOAuth2UserService;
import com.example.demo.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@Order(3)
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/company").access("hasRole('A')");

        http.csrf().ignoringAntMatchers("/popup/jusoPopup");

        http.formLogin().loginPage("/login")
                .loginProcessingUrl("/login");

        http.logout().logoutUrl("/logout").invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSION_ID")
                .logoutSuccessUrl("/");

        http.rememberMe().key("hotpleGO")
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(604800);

        http.oauth2Login().loginPage("/login")
                .successHandler(customOAuth2LoginSuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
    }
}
