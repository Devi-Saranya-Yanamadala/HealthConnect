package com.cts.healthconnect.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

public class JwtUtil {

    // ✅ Minimum 32 characters required for HS256
    private static final String SECRET_KEY =
            "healthconnect-secret-key-healthconnect-32";

    private static final long EXPIRATION_TIME = 60 * 60 * 1000;

    // ✅ Create a Key ONCE
    private static final Key KEY =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(KEY)   // ✅ CORRECT
                .compact();
    }

    public static Claims validateToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}



