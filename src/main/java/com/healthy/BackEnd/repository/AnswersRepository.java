package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Answers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswersRepository extends JpaRepository<Answers, String> {
} 