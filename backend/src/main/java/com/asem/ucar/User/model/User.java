package com.asem.ucar.User.model;

import com.asem.ucar.User.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated
    private Role role;

    private String phoneNumber;

    private String email;
}

