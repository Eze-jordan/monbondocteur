package com.esiitech.monbondocteur.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/") // ou "/api/" si tu veux restreindre
            .allowedOrigins("https://moubengou-bodri.highticketdeveloper.com",
                    "http://127.0.0.1:5501",
                    "http://localhost:5501")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization");
}
}
