package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Students, String> {
} 