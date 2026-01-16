package com.asem.ucar.Auth.init;

import com.asem.ucar.User.enums.Role;
import com.asem.ucar.User.model.User;
import com.asem.ucar.User.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {
    @Bean
    public ApplicationRunner initializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the admin user already exists
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                // Create the admin user
                var adminUser = new User();
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@example.com");
                adminUser.setRole(Role.ROLE_ADMIN);
                adminUser.setPhoneNumber("123456789");

                userRepository.save(adminUser);
            }
        };
    }
}
