package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.exception.BadCredentialsException;
import com.helmes.sectorsapi.model.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.helmes.sectorsapi.exception.ErrorCode.INVALID_CREDENTIALS;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    public String generateToken(User user) {
        return Jwts.builder()
            .subject(user.getUsername())
            .claim("uid", user.getId())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(getSigningKey())
            .compact();
    }

    public String getUsername(String token) {
        try {
            var claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

            return claims.getPayload().getSubject();
        } catch (JwtException | IllegalArgumentException _) {
            throw new BadCredentialsException("Invalid JWT token", INVALID_CREDENTIALS.name());
        }
    }

    private SecretKey getSigningKey() {
        var keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
