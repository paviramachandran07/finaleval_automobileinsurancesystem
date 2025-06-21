package com.hexaware.automobileinsurancesystem.util;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());
    private static final int MIN_SECRET_LENGTH = 32;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @PostConstruct
    public void init() {
        if (secret == null || secret.length() < MIN_SECRET_LENGTH) {
            throw new IllegalStateException("JWT secret key is missing or too short. Must be at least " + MIN_SECRET_LENGTH + " characters.");
        }
        if (expiration <= 0) {
            throw new IllegalStateException("JWT expiration must be a positive value.");
        }
        logger.info("JwtUtil initialized successfully.");
    }

    public String generateToken(String email, String role) {
        logger.info("Generating token for email: [REDACTED], role: " + role);
        String token = Jwts.builder()
                .setSubject(email)
                .claim("roles", List.of(role))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode(secret))
                .compact();
        logger.info("Token generated successfully for email: [REDACTED]");
        return token;
    }

    public String extractUsername(String token) {
        try {
            String username = getClaims(token).getSubject();
            logger.info("Extracted username: [REDACTED]");
            return username;
        } catch (Exception e) {
            logger.severe("Failed to extract username from token: " + e.getMessage());
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    public List<String> extractRoles(String token) {
        try {
            Claims claims = getClaims(token);
            Object roles = claims.get("roles");
            if (roles instanceof List) {
                return (List<String>) roles;
            }
            logger.warning("Roles claim is not a list: " + roles);
            return List.of();
        } catch (Exception e) {
            logger.severe("Failed to extract roles from token: " + e.getMessage());
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(secret))
                .parseClaimsJws(token)
                .getBody();
    }
  
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            boolean isValid = !claims.getExpiration().before(new Date());
            logger.info("Token validation result: " + isValid);
            return isValid;
        } catch (ExpiredJwtException e) {
            logger.warning("Token expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            logger.severe("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}






