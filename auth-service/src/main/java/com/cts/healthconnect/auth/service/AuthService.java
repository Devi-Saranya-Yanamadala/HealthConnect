package com.cts.healthconnect.auth.service;



import com.cts.healthconnect.auth.dto.*;

public interface AuthService {

    void register(RegisterRequestDto dto);

    AuthResponseDto login(LoginRequestDto dto);

	AuthResponseDto refreshAccessToken(RefreshTokenRequestDto dto);

	void logout(RefreshTokenRequestDto dto);

	
}

