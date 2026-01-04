package com.asem.ucar.User.service;

import com.asem.ucar.User.exception.UserNotFoundException;
import com.asem.ucar.User.model.User;
import com.asem.ucar.User.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.byEmail(email));
    }

    public User getById(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.byId(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserAccount(Long id) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException.byId(id);
        }
        userRepository.deleteById(id);
    }

}
