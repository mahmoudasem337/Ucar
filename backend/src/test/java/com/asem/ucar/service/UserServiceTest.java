package com.asem.ucar.service;

import com.asem.ucar.User.enums.Role;
import com.asem.ucar.User.exception.UserNotFoundException;
import com.asem.ucar.User.model.User;
import com.asem.ucar.User.repository.UserRepository;
import com.asem.ucar.User.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service unit tests")
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    User user = new User(1L, "ahmed", "ahmed123",
            Role.ROLE_USER, "01155128905", "ahmed@gmail.com");

    @Nested
    class GetByEmailTests {
        @Test
        void shouldReturnUserWhenEmailExists() {
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));
            User result = userService.getByEmail(user.getEmail());
            assertEquals(user, result);
            verify(userRepository).findByEmail(user.getEmail());
        }

        @Test
        void shouldThrowsUserNotFoundExceptionWhenEmailNotExists() {
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.empty());
            UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                    () -> userService.getByEmail(user.getEmail()));
            assertTrue(ex.getMessage().contains(user.getEmail()));
            verify(userRepository).findByEmail(user.getEmail());
        }

        @Test
        void shouldThrowIllegalArgumentExceptionWhenEmailIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> userService.getByEmail(null));
        }

        @Test
        void shouldThrowIllegalArgumentExceptionWhenEmailIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> userService.getByEmail("   "));
        }
    }

    @Nested
    class GetByIdTests {
        @Test
        void shouldReturnUserWhenIdExists() {
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));
            User result = userService.getById(user.getId());
            assertEquals(user, result);
            verify(userRepository).findById(user.getId());
        }

        @Test
        void shouldThrowsUserNotFoundExceptionWhenIdNotExists() {
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class,
                    () -> userService.getById(user.getId()));
            verify(userRepository).findById(user.getId());
        }

        @ParameterizedTest
        @ValueSource(longs = {0, -1})
        void shouldThrowIllegalArgumentExceptionWhenIdIsInvalid(long id) {
            assertThrows(IllegalArgumentException.class,
                    () -> userService.getById(id));
        }

    }

    @Nested
    class GetAllUsersTests {

        @Test
        void shouldReturnAllUsersWhenUsersExist() {
            List<User> users = List.of(
                    new User(1L, "ahmed", "pass", Role.ROLE_USER, "01114143565", "a@gmail.com"),
                    new User(2L, "ali", "pass", Role.ROLE_USER, "01224211405", "b@gmail.com")
            );
            when(userRepository.findAll()).thenReturn(users);
            List<User> result = userService.getAllUsers();
            assertEquals(2, result.size());
            assertEquals(users, result);
            verify(userRepository).findAll();
        }

        @Test
        void shouldReturnEmptyListWhenNoUsersExist() {
            when(userRepository.findAll()).thenReturn(List.of());
            List<User> result = userService.getAllUsers();
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(userRepository).findAll();
        }
    }

    @Nested
    class DeleteUserAccountTests {

        @Test
        void shouldDeleteUserWhenIdExists() {
            when(userRepository.existsById(user.getId())).thenReturn(true);
            userService.deleteUserAccount(user.getId());
            verify(userRepository).existsById(user.getId());
            verify(userRepository).deleteById(user.getId());
        }

        @Test
        void shouldThrowUserNotFoundExceptionWhenIdDoesNotExist() {
            when(userRepository.existsById(user.getId())).thenReturn(false);
            UserNotFoundException ex = assertThrows(
                    UserNotFoundException.class,
                    () -> userService.deleteUserAccount(user.getId())
            );
            assertTrue(ex.getMessage().contains(user.getId().toString()));
            verify(userRepository).existsById(user.getId());
            verify(userRepository, never()).deleteById(any());
        }
    }


}