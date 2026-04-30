package com.cts.healthconnect.auth.service;

import com.cts.healthconnect.auth.entity.RefreshToken;
import com.cts.healthconnect.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    private static final int REFRESH_TOKEN_DAYS = 7;

    public String createRefreshToken(String username) {

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .username(username)
                .expiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_DAYS))
                .build();

        repository.save(token);
        return token.getToken();
    }

    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    public void deleteRefreshToken(String token) {
        repository.deleteByToken(token);
    }
}