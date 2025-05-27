    package com.esiitech.monbondocteur.config;
    import com.esiitech.monbondocteur.security.CustomUserDetailsService;
    import com.esiitech.monbondocteur.security.JwtFilter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import java.util.List;

    @EnableMethodSecurity
    @Configuration
    public class SecurityConfig {
        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private final JwtFilter jwtFilter;
        private final CustomUserDetailsService customUserDetailsService;

        public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, JwtFilter jwtFilter, CustomUserDetailsService customUserDetailsService) {
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
            this.jwtFilter = jwtFilter;
            this.customUserDetailsService = customUserDetailsService;
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(customUserDetailsService);
            authProvider.setPasswordEncoder(bCryptPasswordEncoder);
            return authProvider;
        }



        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .cors(Customizer.withDefaults()) // ← Active CORS ici
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/api/users/connexion",
                                    "/api/users/activation",
                                    "/swagger-ui/**", "/v3/api-docs/**",
                                    "/api/agenda/**",
                                    "/api/users/**"
                            ).permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/appointment").hasRole("USER")
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }




        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                throws Exception {
            return  authenticationConfiguration.getAuthenticationManager();
        }



    }
