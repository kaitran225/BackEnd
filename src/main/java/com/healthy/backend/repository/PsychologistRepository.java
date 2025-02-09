package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PsychologistRepository extends JpaRepository<Psychologists, String> {
    Psychologists findByUserID(String userID);

} 