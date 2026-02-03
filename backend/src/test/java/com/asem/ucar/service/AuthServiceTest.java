package com.asem.ucar.service;

import com.asem.ucar.Auth.dto.AuthRequest;
import com.asem.ucar.Auth.dto.AuthResponse;
import com.asem.ucar.Auth.dto.RegisterRequest;
import com.asem.ucar.Auth.mapper.UserMapper;
import com.asem.ucar.Auth.service.AuthService;
import com.asem.ucar.Auth.utils.JwtUtils;
import com.asem.ucar.User.enums.Role;
import com.asem.ucar.User.exception.AuthenticationFailedException;
import com.asem.ucar.User.exception.UserNotFoundException;
import com.asem.ucar.User.model.User;
import com.asem.ucar.User.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Service unit tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("Register method tests")
    class RegisterTests {

        @Test
        void shouldSaveUserAndReturnToken() {
            RegisterRequest request = new RegisterRequest(
                    "testUser",
                    "password123",
                    "+201234567890",
                    "test@example.com"
            );
            User user = new User();
            user.setId(1L);
            user.setRole(Role.ROLE_USER);

            when(userMapper.toUser(request)).thenReturn(user);
            when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
            when(jwtUtils.generateToken(request.email(), user.getRole())).thenReturn("jwtToken");

            AuthResponse response = authService.register(request);

            assertEquals("jwtToken", response.token());
            assertEquals("encodedPassword", user.getPassword());
            verify(userRepository).save(user);
        }

        @Test
        void registerFailedMappingShouldReturnNull() {
            RegisterRequest request = new RegisterRequest(
                    "testUser",
                    "password123",
                    "+201234567890",
                    "test@example.com"
            );

            when(userMapper.toUser(request)).thenReturn(null);
            assertThrows(NullPointerException.class, () -> authService.register(request));

            verify(userRepository, never()).save(any());
            verify(jwtUtils, never()).generateToken(any(), any());
        }

        @Test
        void registerFailedPasswordEncoderShouldThrowsException() {
            RegisterRequest request = new RegisterRequest(
                    "testUser",
                    "password123",
                    "+201234567890",
                    "test@example.com"
            );

            User user = new User();

            when(userMapper.toUser(request)).thenReturn(user);
            when(passwordEncoder.encode(request.password())).thenThrow(new RuntimeException("Encoding failed"));

            RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));
            assertEquals("Encoding failed", ex.getMessage());

            verify(userRepository, never()).save(any());
            verify(jwtUtils, never()).generateToken(any(), any());
        }

        @Test
        void registerFailedRepositoryShouldThrowsException() {
            RegisterRequest request = new RegisterRequest(
                    "testUser",
                    "password123",
                    "+201234567890",
                    "test@example.com"
            );

            User user = new User();
            user.setRole(Role.ROLE_USER);

            when(userMapper.toUser(request)).thenReturn(user);
            when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
            doThrow(new RuntimeException("DB save failed")).when(userRepository).save(user);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));
            assertEquals("DB save failed", ex.getMessage());

            verify(jwtUtils, never()).generateToken(any(), any());
        }

        @Test
        void registerFailedJwtUtilsShouldThrowsException() {
            RegisterRequest request = new RegisterRequest(
                    "testUser",
                    "password123",
                    "+201234567890",
                    "test@example.com"
            );

            User user = new User();
            user.setRole(Role.ROLE_USER);

            when(userMapper.toUser(request)).thenReturn(user);
            when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
            when(userRepository.save(user)).thenReturn(user);
            when(jwtUtils.generateToken(request.email(), user.getRole()))
                    .thenThrow(new RuntimeException("Token generation failed"));

            RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));
            assertEquals("Token generation failed", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Authenticate method tests")
    class AuthenticateTests {

        private Authentication auth;
        @BeforeEach
        void setUpAuth() {
            auth = mock(Authentication.class);
        }

        @Test
        void shouldAuthenticateAndReturnToken() {
            AuthRequest request = new AuthRequest(
                    "test@example.com",
                    "password123"
            );

            User user = new User();
            user.setRole(Role.ROLE_USER);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(auth);

            when(userRepository.findByEmail(request.email()))
                    .thenReturn(Optional.of(user));

            when(jwtUtils.generateToken(request.email(), user.getRole()))
                    .thenReturn("jwtToken");

            AuthResponse response = authService.authenticate(request);

            assertEquals("jwtToken", response.token());

            verify(authenticationManager)
                    .authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(userRepository).findByEmail(request.email());
            verify(jwtUtils).generateToken(request.email(), user.getRole());
        }

        @Test
        void authenticateFailedWrongCredentialsShouldThrowAuthenticationFailedException() {
            AuthRequest request = new AuthRequest(
                    "test@example.com",
                    "wrongPassword"
            );

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Wrong email or password"));

            assertThrows(AuthenticationFailedException.class,
                    () -> authService.authenticate(request));

            verify(userRepository, never()).findByEmail(any());
            verify(jwtUtils, never()).generateToken(any(), any());
        }

        @Test
        void authenticateUserNotFoundShouldThrowUserNotFoundException() {
            AuthRequest request = new AuthRequest(
                    "test@example.com",
                    "password123"
            );

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(auth);

            when(userRepository.findByEmail(request.email()))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> authService.authenticate(request));

            verify(jwtUtils, never()).generateToken(any(), any());
        }

        @Test
        void authenticateFailedJwtUtilsShouldThrowException() {
            AuthRequest request = new AuthRequest(
                    "test@example.com",
                    "password123"
            );

            User user = new User();
            user.setRole(Role.ROLE_USER);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(auth);

            when(userRepository.findByEmail(request.email()))
                    .thenReturn(Optional.of(user));

            when(jwtUtils.generateToken(request.email(), user.getRole()))
                    .thenThrow(new RuntimeException("Token generation failed"));

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> authService.authenticate(request));

            assertEquals("Token generation failed", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Get current user email tests")
    class GetCurrentUserEmailTests {

        private Authentication authentication;
        private SecurityContext securityContext;

        @BeforeEach
        void setUp() {
            authentication = mock(Authentication.class);
            securityContext = mock(SecurityContext.class);
        }
        @AfterEach
        void clearContext() {
            SecurityContextHolder.clearContext();
        }

        @Test
        void shouldReturnEmailWhenAuthenticated() {
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("test@example.com");
            when(securityContext.getAuthentication()).thenReturn(authentication);

            SecurityContextHolder.setContext(securityContext);

            Optional<String> result = authService.getCurrentUserEmail();

            assertTrue(result.isPresent());
            assertEquals("test@example.com", result.get());
        }

        @Test
        void shouldReturnEmptyWhenNoAuthentication() {
            when(securityContext.getAuthentication()).thenReturn(null);
            SecurityContextHolder.setContext(securityContext);

            Optional<String> result = authService.getCurrentUserEmail();

            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyWhenNotAuthenticated() {
            when(authentication.isAuthenticated()).thenReturn(false);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            SecurityContextHolder.setContext(securityContext);

            Optional<String> result = authService.getCurrentUserEmail();

            assertTrue(result.isEmpty());
        }
    }

}