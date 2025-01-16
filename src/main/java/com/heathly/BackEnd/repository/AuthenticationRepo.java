package com.heathly.BackEnd.repository;

import com.heathly.BackEnd.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepo extends JpaRepository<Users, String> {
    Users findByUsername(String username);

    }

