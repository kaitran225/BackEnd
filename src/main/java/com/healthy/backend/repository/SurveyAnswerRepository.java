package com.healthy.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.Answers;
import java.util.List;




public interface SurveyAnswerRepository extends JpaRepository<Answers, String> {
    Answers findByAnswerID(String answerId);

    List<Answers> findByQuestionID(String questionId);
}