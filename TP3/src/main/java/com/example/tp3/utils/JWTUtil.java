package com.example.tp3.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    // Génération d'une clé sécurisée pour HS256
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Clé générée de manière sécurisée

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token valide 1h
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()  // Utilisation de parserBuilder
                .setSigningKey(SECRET_KEY)
                .build()  // Construction de l'objet parser
                .parseClaimsJws(token)  // Appel de parseClaimsJws
                .getBody();
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    private Date extractExpirationDate(String token) {
        Claims claims = Jwts.parser()  // Utilisation de parserBuilder
                .setSigningKey(SECRET_KEY)
                .build()  // Construction de l'objet parser
                .parseClaimsJws(token)  // Appel de parseClaimsJws
                .getBody();
        return claims.getExpiration();
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }
}
