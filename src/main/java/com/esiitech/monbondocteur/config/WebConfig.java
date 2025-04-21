package com.esiitech.monbondocteur.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Autorise toutes les routes de l'API
                .allowedOrigins(
                        "https://api-monbondocteur.esiitech-gabon.com", // Si tu accèdes à l'API depuis cette URL
                        "https://moubengou-bodri.highticketdeveloper.com" // Frontend de Bodri
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                .allowedHeaders("*") // Tous les headers autorisés
                .allowCredentials(true) // Permet l'envoi de cookies/token (comme Authorization)
                .exposedHeaders("Authorization"); // Expose le header Authorization dans la réponse (optionnel mais utile pour JWT)
    }
}
