package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.Answers;


public interface AnswersRepository extends JpaRepository<Answers, String> {

    // @Query("SELECT an FROM Answers an ORDER BY an.answerID DESC")
    Answers findFirstByOrderByAnswerIDDesc();
    List<Answers> findByQuestionID(String questionID);
} 