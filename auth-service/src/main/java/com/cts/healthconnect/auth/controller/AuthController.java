package com.cts.healthconnect.auth.controller;

import com.cts.healthconnect.auth.dto.*;
import com.cts.healthconnect.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    //  REGISTER
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequestDto dto) {
        service.register(dto);
    }

    //  LOGIN → returns accessToken + refreshToken
    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        return service.login(dto);
    }

    //  REFRESH TOKEN → new access token
    @PostMapping("/refresh")
    public AuthResponseDto refreshToken(
            @RequestBody RefreshTokenRequestDto dto) {

        return service.refreshAccessToken(dto);
    }

    //  LOGOUT → invalidate refresh token
    @PostMapping("/logout")
    public void logout(@RequestBody RefreshTokenRequestDto dto) {
        service.logout(dto);
    }
}