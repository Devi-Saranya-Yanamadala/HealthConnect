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

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequestDto dto) {
        service.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        return service.login(dto);
    }
}
