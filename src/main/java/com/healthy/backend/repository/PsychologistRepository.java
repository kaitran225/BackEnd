package com.healthy.backend.repository;

import com.healthy.backend.entity.Psychologists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PsychologistRepository extends JpaRepository<Psychologists, String> {
    Psychologists findByUserID(String userID);

} 