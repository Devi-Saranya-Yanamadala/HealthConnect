package com.cts.healthconnect.auth.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponseDto {
    private String username;
    private String role;
    private String accessToken;
    private String refreshToken;

}
