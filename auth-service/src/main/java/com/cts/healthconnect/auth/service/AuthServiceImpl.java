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

    @Override
    public void register(RegisterRequestDto dto) {

        if (repository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());   // ✅ enum usage
        user.setActive(true);

        repository.save(user);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {

        User user = repository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }

        String token = JwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()   // ✅ convert enum → String for JWT
        );

        return new AuthResponseDto(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }
}