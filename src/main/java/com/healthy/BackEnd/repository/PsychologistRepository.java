package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Psychologists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PsychologistRepository extends JpaRepository<Psychologists, String> {
    Psychologists findByUserID(String userID);
    

} 