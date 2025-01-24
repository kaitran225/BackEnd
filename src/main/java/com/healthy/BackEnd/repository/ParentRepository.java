package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Parents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parents, String> {
    Parents findByUserID(String userID);
} 