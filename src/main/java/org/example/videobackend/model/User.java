package org.example.videobackend.model;

// ... imports

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role; // <-- Add this field

}