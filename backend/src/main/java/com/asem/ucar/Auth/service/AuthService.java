package com.asem.ucar.Auth.service;

import com.asem.ucar.Auth.dto.AuthRequest;
import com.asem.ucar.Auth.dto.AuthResponse;
import com.asem.ucar.Auth.dto.RegisterRequest;
import com.asem.ucar.Auth.mapper.UserMapper;
import com.asem.ucar.Auth.utils.JwtUtils;
import com.asem.ucar.User.exception.AuthenticationFailedException;
import com.asem.ucar.User.exception.UserNotFoundException;
import com.asem.ucar.User.model.User;
import com.asem.ucar.User.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    public AuthResponse register(RegisterRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        repository.save(user);
        String token = jwtUtils.generateToken(request.email(),user.getRole());
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException ex) {
            throw AuthenticationFailedException.wrongEmailOrPassword();
        }

        var user = repository.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);
        var jwtToken = jwtUtils.generateToken(request.email(),user.getRole());
        return new AuthResponse(jwtToken);
    }

    public Optional<String> getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return Optional.of(auth.getName());
        }
        return Optional.empty();
    }

}
