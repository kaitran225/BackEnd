package com.healthy.backend.repository;

import com.healthy.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, String> {

    @Query("SELECT u FROM Users u")
    List<Users> findAllUsers();

    Users findByUsername(String username);

    @Query("SELECT u.userId FROM Users u ORDER BY u.userId DESC LIMIT 1")
    String findLastUserId();
} 