package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Answers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersRepository extends JpaRepository<Answers, String> {
    List<Answers> findByQuestionID(String questionID);
} 