package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Parents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parents, String> {
    Parents findByUserID(String userID);
} 