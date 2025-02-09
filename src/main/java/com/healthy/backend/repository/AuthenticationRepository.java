package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Users, String> {
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByResetToken(String resetToken);
}

