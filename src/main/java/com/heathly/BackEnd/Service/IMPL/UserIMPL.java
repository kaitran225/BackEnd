package com.heathly.BackEnd.Service.IMPL;

import com.heathly.BackEnd.Service.UserService;
import com.heathly.BackEnd.dto.UserDTO;
import com.heathly.BackEnd.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.heathly.BackEnd.repository.UserRepo;

import java.time.LocalDateTime;

public class UserIMPL implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public String addUser(UserDTO userDTO) {
        // Check if email exists
        if (userRepo.findByEmail(userDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Check if username exists
        if (userRepo.findById(userDTO.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User ID is already registered");
        }

        // Convert string role to Role enum
        User.Role role = User.Role.valueOf(userDTO.getRole().toUpperCase());
        User.Status status = User.Status.valueOf(userDTO.getStatus().toUpperCase());

        // Create User entity
        User user = new User(
                userDTO.getUserId(),
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()), // Encode password
                userDTO.getFullName(),
                userDTO.getEmail(),
                userDTO.getPhoneNumber(),
                role,
                LocalDateTime.now(),
                LocalDateTime.now(),
                status
        );

        userRepo.save(user);
        return user.getUsername();
    }
}