package com.esiitech.monbondocteur.security;

import com.esiitech.monbondocteur.model.Utilisateur;
import com.esiitech.monbondocteur.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class JwtService {
    private final String ENCRYPTION_KEY = "a1fb5d451ce3c8b501159f213084e8a48abfcf3a76124643e9b58141a84c67de";
    private UtilisateurService utilisateurService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public Map<String, String> generate(String email){
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        return this.generateJwt(userDetails.getUtilisateur());
    }
    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate =this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }


    private <T> T getClaim(String token, Function<Claims, T> function ) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // Définit l'heure actuelle et l'expiration du JWT
    final long currentTime = System.currentTimeMillis();
    final long expirationTime = currentTime + 1 + 60 * 60 * 1000;  // 5 Heures

    // Cette méthode génère un JWT à partir des informations de l'utilisateur
    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        // Crée les informations de l'utilisateur à inclure dans le JWT
        Map<String, ? extends Serializable> claims = Map.of(
                "nom", utilisateur.getNom(),
                "role", utilisateur.getRole(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT,utilisateur.getEmail()
        );



        // Génére le JWT avec les informations et la clé de signature
        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        // Retourne le JWT dans un format de map avec la clé "bearer"
        return Map.of("bearer", bearer);
    }

    // Cette méthode retourne la clé de signature pour le JWT
    private Key getKey() {
        // Décode la clé d'encryption en base64
        final byte[] decode = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decode);  // Retourne la clé pour signer le JWT
    }


}
