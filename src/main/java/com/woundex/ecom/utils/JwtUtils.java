package com.woundex.ecom.utils;

import java.util.Date;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${woundex.app.jwtSecret}")
    private String jwtSecret;
    @Value("${woundex.app.jwtExpirationMs}")
    private int jwtExpirationMs;

     private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
   
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSignKey())
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            System.err.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}
