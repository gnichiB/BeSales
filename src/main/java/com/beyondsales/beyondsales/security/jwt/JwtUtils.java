package com.beyondsales.beyondsales.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret:beyondsalesSecretKeyForJwtMustBeLongEnough123!}")
    private String jwtSecret; // ✅ Correction: utiliser jwtSecret partout

    @Value("${jwt.expiration:86400000}")
    private int jwtExpirationMs; // ✅ Externaliser l'expiration aussi

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes()); // ✅ Correction: jwtSecret au lieu de jwtSecret
    }

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // ✅ Utiliser la variable injectée
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // invalid token
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Méthode utilitaire pour extraire le token d'une requête HTTP
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}