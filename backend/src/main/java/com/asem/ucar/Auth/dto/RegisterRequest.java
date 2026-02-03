package com.asem.ucar.Auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Enter your username")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Enter your password")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{7,15}$", message = "Invalid phone number")
        String phoneNumber,

        @NotBlank(message = "Enter your email")
        @Email(message = "Email should be valid")
        String email
) {}