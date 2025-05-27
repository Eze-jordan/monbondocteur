package com.esiitech.monbondocteur.security;

import com.esiitech.monbondocteur.service.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
public class JwtFilter extends OncePerRequestFilter {

    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;

    public JwtFilter(UtilisateurService utilisateurService, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔓 Laisse passer les routes publiques sans traitement JWT
        if (path.startsWith("/api/users/connexion") ||
                path.startsWith("/api/users/activation") ||
                path.startsWith("/api/appointment") ||
                path.startsWith("/api/agenda") ||
                path.startsWith("/api/appointment/**") ||
                path.startsWith("/api/agenda/**") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtService.extractUsername(token);

                if (username != null && !jwtService.isTokenExpired(token) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = utilisateurService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("Erreur dans JwtFilter : " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("Erreur d'authentification : " + e.getMessage());
        }
    }
}
