package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(3)
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
}
