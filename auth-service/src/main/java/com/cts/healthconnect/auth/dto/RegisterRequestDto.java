package com.cts.healthconnect.auth.dto;

import com.cts.healthconnect.auth.entity.Role;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // ✅ EMAIL VALIDATION
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // ✅ PHONE NUMBER VALIDATION
    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[6-9][0-9]{9}$",
        message = "Invalid phone number"
    )
    private String phoneNumber;
}