package com.esiitech.monbondocteur.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/utilisateurs/activation", // 👈 on autorise cette route
                                "/api/utilisateurs",            // 👈 autorisation pour l’inscription aussi si besoin
                                "/api/**"                       // 👈 tout le reste si tu veux
                        ).permitAll()
                        .anyRequest().authenticated()      // 👈 sécurise les autres
                )
                .formLogin()
                .and()
                .httpBasic(); // permet l’authentification via Postman, etc.

        return http.build();
    }

}
