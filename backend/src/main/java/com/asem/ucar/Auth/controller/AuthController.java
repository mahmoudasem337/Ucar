package com.asem.ucar.Auth.controller;

import com.asem.ucar.Auth.dto.AuthRequest;
import com.asem.ucar.Auth.dto.AuthResponse;
import com.asem.ucar.Auth.dto.RegisterRequest;
import com.asem.ucar.Auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService service) {
        this.authService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register
            (@Valid @RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @Valid @RequestBody AuthRequest request)
    {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}