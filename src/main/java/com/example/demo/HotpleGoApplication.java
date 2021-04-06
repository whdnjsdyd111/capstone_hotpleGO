package com.example.demo;

import com.example.demo.config.WebSocketConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@ComponentScan
@EnableAutoConfiguration
public class HotpleGoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotpleGoApplication.class, args);
    }
}
