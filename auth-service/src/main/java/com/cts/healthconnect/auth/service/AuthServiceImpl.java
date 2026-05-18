package com.cts.healthconnect.auth.service;

import com.cts.healthconnect.auth.dto.*;
import com.cts.healthconnect.auth.entity.PasswordResetToken;
import com.cts.healthconnect.auth.entity.User;
import com.cts.healthconnect.auth.repository.PasswordResetTokenRepository;
import com.cts.healthconnect.auth.repository.UserRepository;
import com.cts.healthconnect.auth.security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

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

        // Short-lived access token
        String accessToken = JwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        // Long-lived refresh token
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

        // Validate refresh token from DB
        var refreshToken =
                refreshTokenService.validateRefreshToken(dto.getRefreshToken());

        // Load user
        User user = repository.findByUsername(refreshToken.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Generate new access token
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
    
    
    /* ================================================
     * Forgot password
      ================================================== */
    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequestDto dto) {
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("No account found with this email."));

        // delete any existing OTP for this email
        tokenRepository.deleteByEmail(dto.getEmail());

        // generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .otp(otp)
                .email(dto.getEmail())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        // send OTP email
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(dto.getEmail());
        mail.setSubject("HealthConnect - Password Reset OTP");
        mail.setText("Hello " + user.getUsername() + ",\n\n"
                + "Your OTP for password reset is: " + otp + "\n\n"
                + "This OTP is valid for 10 minutes. Do not share it with anyone.\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Regards,\nHealthConnect Team");

        mailSender.send(mail);
    }

    @Override
    @Transactional
    public void verifyOtpAndReset(VerifyOtpRequestDto dto) {
        PasswordResetToken resetToken = tokenRepository.findByEmailAndOtp(dto.getEmail(), dto.getOtp())
                .orElseThrow(() -> new RuntimeException("Invalid OTP. Please try again."));

        if (resetToken.isUsed()) {
            throw new RuntimeException("This OTP has already been used.");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        repository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }

    // =========================
    // LOGOUT
    // =========================
    @Override
    public void logout(RefreshTokenRequestDto dto) {
        refreshTokenService.deleteRefreshToken(dto.getRefreshToken());
    }
}