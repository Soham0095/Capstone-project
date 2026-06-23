package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HexFormat;

/**
 * Utility for creating and validating JWT tokens.
 * Token includes: sub (username), accountId claim, iat, exp.
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String hexSecret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        byte[] keyBytes = HexFormat.of().parseHex(hexSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    /** Generate a signed JWT for the given username and account id. */
    public String generateToken(String username, Integer accountId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .claim("accountId", accountId)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key)
                .compact();
    }

    /** Extract the username (subject) from a valid token. */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /** Extract the accountId claim from a valid token. */
    public Integer extractAccountId(String token) {
        return getClaims(token).get("accountId", Integer.class);
    }

    /** Validate the token signature and expiry. Returns false on any error. */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
