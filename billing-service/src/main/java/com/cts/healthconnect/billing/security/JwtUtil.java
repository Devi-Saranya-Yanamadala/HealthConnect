package com.cts.healthconnect.billing.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JwtUtil {

    // ✅ EXACT SAME SECRET AS auth-service
    private static final String SECRET_KEY =
            "healthconnect-secret-key-healthconnect-32";

    private static final Key KEY =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static Claims parse(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(KEY)   // ✅ CORRECT
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}