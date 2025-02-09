package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Students, String> {
    Students findByUserID(String userID);

    Students findByStudentID(String studentID);
} 