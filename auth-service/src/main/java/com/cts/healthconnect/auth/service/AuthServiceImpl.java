package com.cts.healthconnect.auth.service;

import com.cts.healthconnect.auth.dto.*;
import com.cts.healthconnect.auth.entity.User;
import com.cts.healthconnect.auth.repository.UserRepository;
import com.cts.healthconnect.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    // =========================
    // REGISTER
    // =========================
    @Override
    public void register(RegisterRequestDto dto) {

        if (repository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (repository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setActive(true);
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        repository.save(user);
    }

    // =========================
    // LOGIN (Access + Refresh)
    // =========================
    @Override
    public AuthResponseDto login(LoginRequestDto dto) {

        User user = repository.findByUsername(dto.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Invalid username"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }

        // ✅ Short-lived access token
        String accessToken = JwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        // ✅ Long-lived refresh token
        String refreshToken =
                refreshTokenService.createRefreshToken(user.getUsername());

        return new AuthResponseDto(
                user.getUsername(),
                user.getRole().name(),
                accessToken,
                refreshToken
        );
    }

    // =========================
    // REFRESH ACCESS TOKEN
    // =========================
    @Override
    public AuthResponseDto refreshAccessToken(RefreshTokenRequestDto dto) {

        // ✅ Validate refresh token from DB
        var refreshToken =
                refreshTokenService.validateRefreshToken(dto.getRefreshToken());

        // ✅ Load user
        User user = repository.findByUsername(refreshToken.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // ✅ Generate new access token
        String newAccessToken = JwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new AuthResponseDto(
                user.getUsername(),
                user.getRole().name(),
                newAccessToken,
                dto.getRefreshToken()
        );
    }

    // =========================
    // LOGOUT
    // =========================
    @Override
    public void logout(RefreshTokenRequestDto dto) {
        refreshTokenService.deleteRefreshToken(dto.getRefreshToken());
    }
}