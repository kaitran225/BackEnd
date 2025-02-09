package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parents, String> {
    Parents findByUserID(String userID);
} 