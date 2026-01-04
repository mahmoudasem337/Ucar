package com.asem.ucar.User.controller;

import java.util.List;

import com.asem.ucar.User.model.User;
import com.asem.ucar.User.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam @NotBlank @Email String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable @NotNull Long id) {
        userService.deleteUserAccount(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
