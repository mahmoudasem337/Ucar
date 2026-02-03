package com.asem.ucar.Auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "Enter your email")
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Enter your password")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) {}