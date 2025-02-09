package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Users, String> {
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByResetToken(String resetToken);
}

