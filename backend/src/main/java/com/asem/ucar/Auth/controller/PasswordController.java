package com.asem.ucar.Auth.controller;

import com.asem.ucar.Auth.dto.ForgotPasswordRequest;
import com.asem.ucar.Auth.dto.ResetPasswordRequest;
import com.asem.ucar.Auth.service.EmailService;
import com.asem.ucar.Auth.service.PasswordService;
import com.asem.ucar.Auth.utils.OtpUtils;
import com.asem.ucar.User.exception.UserNotFoundException;
import com.asem.ucar.User.model.User;
import com.asem.ucar.User.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class PasswordController {

    private final PasswordService passwordService;
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        passwordService.sendResetOtp(request.email());
        return ResponseEntity.ok(
                "If the email exists, an OTP has been sent."
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully.");
    }
}

