package com.example.demo.config;

import com.example.demo.security.CustomAccessDeniedHandler;
import com.example.demo.security.CustomOAuth2LoginSuccessHandler;
import com.example.demo.security.CustomOAuth2UserService;
import com.example.demo.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
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
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

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

    @RequiredArgsConstructor
    @Configuration
    @Order(1)
    public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
        private final CustomAccessDeniedHandler customAccessDeniedHandler;
        private final PersistentTokenRepository repository;
        private final UserDetailsService userDetailsService;
        private final PasswordEncoder passwordEncoder;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**").authorizeRequests()
                    .antMatchers("/admin/login").permitAll()
                    .antMatchers("/admin/**").access("hasAuthority('A')");

            http.formLogin().loginPage("/admin/login").loginProcessingUrl("/admin/login");

            http.csrf().ignoringAntMatchers("/admin/rest/ck_upload");

            http.logout().logoutUrl("/admin/logout").clearAuthentication(true).invalidateHttpSession(true)
                    .logoutSuccessUrl("/admin/login");

            http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }
    }

    @RequiredArgsConstructor
    @Configuration
    @Order(2)
    public static class ManagerSecurityConfig extends WebSecurityConfigurerAdapter {
        private final CustomAccessDeniedHandler customAccessDeniedHandler;
        private final PersistentTokenRepository repository;
        private final UserDetailsService userDetailsService;
        private final PasswordEncoder passwordEncoder;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/manager/**").authorizeRequests()
                    .antMatchers("/manager/register").permitAll()
                    .antMatchers("/manager/login").permitAll()
                    .antMatchers("/manager/main").permitAll()
                    .antMatchers("/manager/**").permitAll();

            http.formLogin().loginPage("/manager/login").loginProcessingUrl("/manager/login");

            http.logout().logoutUrl("/manager/logout").clearAuthentication(true).invalidateHttpSession(true)
                    .deleteCookies("remember-me", "JSESSION_ID")
                    .logoutSuccessUrl("/manager/main");

            http.rememberMe().key("hotpleGO")
                    .tokenRepository(repository)
                    .tokenValiditySeconds(604800);

            http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        }
    }

    @RequiredArgsConstructor
    @Configuration
    @Order(3)
    public static class UserSecurityConfig extends WebSecurityConfigurerAdapter {
        private final CustomAccessDeniedHandler customAccessDeniedHandler;
        private final CustomOAuth2UserService customOAuth2UserService;
        private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
        private final UserDetailsService userDetailsService;
        private final PasswordEncoder passwordEncoder;
        private final PersistentTokenRepository repository;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests()
//                    .antMatchers("/alliance").access("hasAuthority('B')");

            http.csrf().ignoringAntMatchers("/popup/jusoPopup");

            http.formLogin().loginPage("/login")
                    .loginProcessingUrl("/login");

            http.logout().logoutUrl("/logout").clearAuthentication(true).invalidateHttpSession(true)
                    .deleteCookies("remember-me", "JSESSION_ID")
                    .logoutSuccessUrl("/");

            http.rememberMe().key("hotpleGO")
                    .tokenRepository(repository)
                    .tokenValiditySeconds(604800);

            http.oauth2Login().loginPage("/login")
                    .successHandler(customOAuth2LoginSuccessHandler)
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService);

            http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }
    }
}
