package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Psychologists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PsychologistRepository extends JpaRepository<Psychologists, String> {
    Psychologists findByUserID(String userID);


} 