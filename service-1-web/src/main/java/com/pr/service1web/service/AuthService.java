package com.pr.service1web.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class AuthService {

    @Value("${jwt.secret}")
    private String base64Secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    public String generateBearerToken(String userId) {
        log.info("Generating token for user: {}", userId);
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, base64Secret)
                .compact();
    }

    public boolean isValidSessionId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String sid = authHeader.substring(7);
        return checkSidInDatabase(sid);
    }

    private boolean checkSidInDatabase(String sid) {
        return sid != null && !sid.isEmpty();
    }
}
