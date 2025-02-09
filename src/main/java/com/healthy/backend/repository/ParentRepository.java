package com.healthy.backend.repository;

import com.healthy.backend.entity.Parents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parents, String> {
    Parents findByUserID(String userID);
} 