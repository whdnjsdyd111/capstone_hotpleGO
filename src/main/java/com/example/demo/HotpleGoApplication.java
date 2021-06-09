package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan
@EnableAutoConfiguration
public class HotpleGoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotpleGoApplication.class, args);
    }
}
