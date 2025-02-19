package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthy.backend.entity.Answers;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<Answers, String> {
    Answers findByAnswerID(String answerId);

    List<Answers> findByQuestionID(String questionId);
}