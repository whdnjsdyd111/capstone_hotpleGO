package com.example.demo.config;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.demo.broadcast.BoardSocket;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;

import javax.servlet.ServletContext;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServletContextAware endpointExporterInitializer(final ApplicationContext applicationContext) {
        return new ServletContextAware() {
            @Override
            public void setServletContext(ServletContext servletContext) {
                ServerEndpointExporter serverEndpointExporter = new ServerEndpointExporter();
                serverEndpointExporter.setApplicationContext(applicationContext);
                try {
                    serverEndpointExporter.afterPropertiesSet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
