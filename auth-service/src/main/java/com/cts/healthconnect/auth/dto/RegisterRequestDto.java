package com.cts.healthconnect.auth.dto;



import com.cts.healthconnect.auth.entity.Role;
import lombok.Data;

@Data
public class RegisterRequestDto {

    private String username;
    private String password;
    private Role role;
}
