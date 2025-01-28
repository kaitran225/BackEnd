package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Users, String> {
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByResetToken(String resetToken);
}

