package com.week5.SecurityApp.SecurityApplication.services;

import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * The type Jwt service.
 */
@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate access token string.
     *
     * @param user the user
     * @return the string
     */
    public String generateAccessToken(UserEntity user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Generate refresh token string.
     *
     * @param user the user
     * @return the string
     */
    public String generateRefreshToken(UserEntity user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 6))
                .signWith(getSecretKey())
                .compact();
    }


    /**
     * Get user id from token long.
     *
     * @param token the token
     * @return the long
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }


    /**
     * Is valid token boolean.
     *
     * @param token the token
     * @return the boolean
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSecretKey())  // Set the signing key
                    .build()
                    .parseClaimsJws(token);  // Parse and validate the JWT
            return true;  // Token is valid
        } catch (Exception e) {
            // Handle token parsing exceptions such as expiration, malformed token, etc.
            return false;  // Token is invalid or expired
        }
    }


}
