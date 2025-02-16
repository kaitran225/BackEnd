package com.healthy.backend.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.healthy.backend.dto.survey.SubmitSurveyRequest;
import com.healthy.backend.entity.*;
import com.healthy.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyQuestionMapper;
import com.healthy.backend.mapper.SurveyResultMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {
    @Autowired
    SurveyResultRepository surveyResultRepository;
    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;
    @Autowired
    SurveyResultMapper surveyMapper;
    @Autowired
    SurveyQuestionMapper surveyQuestionMapper;
    @Autowired
    SurveyRepository surveyRepository;
    @Autowired
    AnswersRepository answersRepository;
    @Autowired
    StudentRepository studentRepository;

    @Transactional
    public void submitSurveyResults(String surveyId, SubmitSurveyRequest request) {
        // Kiểm tra học sinh tồn tại
        Students student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Lấy tất cả câu hỏi của khảo sát
        List<SurveyQuestions> questions = surveyQuestionRepository.findBySurveyID(surveyId);

        // Kiểm tra số lượng câu trả lời phải bằng số lượng câu hỏi
        if (request.getAnswers().size() != questions.size()) {
            throw new IllegalArgumentException("You must answer all questions in the survey");
        }

        // Lưu từng câu trả lời
        request.getAnswers().forEach(answer -> {
            // Kiểm tra câu hỏi thuộc survey
            SurveyQuestions question = surveyQuestionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

            if (!question.getSurveyID().equals(surveyId)) {
                throw new IllegalArgumentException("Question does not belong to this survey");
            }

            // Kiểm tra answer hợp lệ
            Answers answerEntity = answersRepository.findById(answer.getAnswerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

            // Tạo và lưu kết quả
            SurveyResults result = new SurveyResults(
                    UUID.randomUUID().toString(), // Tạo ResultID ngẫu nhiên
                    student.getStudentID(),
                    question.getQuestionID(),
                    answerEntity.getAnswerID()
            );
            surveyResultRepository.save(result);
        });
    }

    public List<SurveyQuestions> getQuestionsBySurveyId(String surveyId) {
        return surveyQuestionRepository.findBySurveyID(surveyId);
    }

    public SurveyQuestionResultResponse mapToQuestionResponse(SurveyQuestions question) {
        SurveyQuestionResultResponse response = new SurveyQuestionResultResponse();
        response.setQuestionId(question.getQuestionID());
        response.setQuestionText(question.getQuestionText());
        response.setSurveyId(question.getSurveyID());
        response.setCategoryId(question.getCategoryID());
        return response;
    }
}


