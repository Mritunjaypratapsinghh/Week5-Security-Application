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

@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

     public String generateToken(UserEntity user){
         return Jwts.builder()
                 .subject(user.getId().toString())
                 .claim("email", user.getEmail())
                 .claim("roles", Set.of("ADMIN","USER"))
                 .issuedAt(new Date())
                 .expiration(new Date(System.currentTimeMillis()+1000*60))
                 .signWith(getSecretKey())
                 .compact();
     }


     public Long getUserIdFromToken(String token){
          Claims claims = Jwts.parser()
                 .verifyWith(getSecretKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload();

          return Long.valueOf(claims.getSubject());
     }


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
