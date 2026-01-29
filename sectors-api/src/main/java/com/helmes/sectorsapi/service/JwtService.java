package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.config.properties.JwtProperties;
import com.helmes.sectorsapi.exception.InvalidJwtException;
import com.helmes.sectorsapi.model.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtProperties.secretKey())
        );
    }

    public String generateToken(User user) {
        return Jwts.builder()
            .subject(user.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationMs()))
            .signWith(signingKey)
            .compact();
    }

    public String getUsername(String token) {
        try {
            return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidJwtException("Invalid JWT token", ex);
        }
    }
}
