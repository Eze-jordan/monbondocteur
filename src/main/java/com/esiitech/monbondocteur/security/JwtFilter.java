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
    private  UtilisateurService utilisateurService;
    private  JwtService jwtService;

    public JwtFilter(UtilisateurService utilisateurService, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Ignorer les endpoints publics
        if (path.startsWith("/api/users/connexion") ||
                path.startsWith("/api/users/activation") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/api/appointment") ||
                path.startsWith("/api/agenda")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String authorization = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (authorization != null && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
                if (!jwtService.isTokenExpired(token)) {
                    username = jwtService.extractUsername(token);
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = utilisateurService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            System.out.println("Erreur dans JwtFilter : " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Erreur d'authentification : " + e.getMessage());
            return; // on ne continue pas
        }

        filterChain.doFilter(request, response);
    }

}
