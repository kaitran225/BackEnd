package com.healthy.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.healthy.backend.enums.SurveyCategory;
import org.springframework.stereotype.Service;

import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.QuestionOption1;
import com.healthy.backend.dto.survey.QuestionResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.Categories;
import com.healthy.backend.entity.Categories.MentalHealthCategory;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.AnswersRepository;
import com.healthy.backend.repository.CategoriesRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsRepository;
import com.healthy.backend.repository.SurveyQuestionRepository;
import com.healthy.backend.repository.SurveyRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsChoicesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyQuestionOptionsChoicesRepository surveyQuestionOptionsChoicesRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyMapper surveyMapper;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionOptionsRepository surveyQuestionOptionsRepository;
    private final CategoriesRepository categoriesRepository;
    private final AnswersRepository answersRepository;

    public List<SurveysResponse> getAllSurveys() {

        List<Surveys> surveys = surveyRepository.findAll();

        if (surveys.isEmpty()) {
            throw new ResourceNotFoundException("No surveys found");
        }
        return surveys
                .stream()
                .map(survey
                        -> {
                    List<SurveyQuestions> surveyQuestionsList
                            = surveyQuestionRepository.findBySurveyID(survey.getSurveyID());
                    return surveyMapper.buildSurveysResponse(
                            survey, surveyQuestionsList.size());
                })
                .toList();
    }

    public void updateSurveyQuestion(String surveyID,SurveyQuestionResponse surveyQuestionResponse) {
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        
        if(surveyQuestions.isEmpty()) {
                throw new ResourceNotFoundException("surveyID not found"  + surveyID);
        }
        Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestions.stream()
                .collect(Collectors.toMap(SurveyQuestions::getQuestionID , Function.identity()));

        for(QuestionResponse sqr : surveyQuestionResponse.getQuestionList()) {
                SurveyQuestions surveyQuestion1 = surveyQuestionMap.get(sqr.getId());
                Categories categories = categoriesRepository.findById(surveyQuestion1.getCategoryID())
                                .orElseThrow(() -> new ResourceNotFoundException("Categories not found" + surveyQuestion1.getCategoryID()));
                
                
                
                surveyQuestion1.setQuestionText(sqr.getQuestionText());
                categories.setCategoryName(SurveyCategory.valueOf(sqr.getQuestionCategory()));
                List<QuestionOption> questionOption = sqr.getQuestionOptions();

                List<SurveyQuestionOptions> answers = surveyQuestionOptionsRepository.findByQuestionID(surveyQuestion1.getQuestionID());

                if(!answers.isEmpty()) {
                        for(int i = 0; i < answers.size(); i++) {
                                SurveyQuestionOptions ans = answers.get(i);
                                        
                                if(i < questionOption.size()) {
                                        ans.setOptionText(questionOption.get(i).getLabel());
                                        ans.setScore(questionOption.get(i).getValue()); 

                                }                                                                 
                        }
                }
                surveyQuestionOptionsRepository.saveAll(answers);
                surveyQuestionRepository.save(surveyQuestion1); 
                categoriesRepository.save(categories);              
        }
}

    public SurveyQuestionResponse getSurveyQuestion(String surveyID) {

        Surveys surveys = surveyRepository.findById(surveyID).orElseThrow(
                () -> new ResourceNotFoundException("No survey found for surveyID " + surveyID)
        );
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
        }

        List<QuestionResponse> questionList = surveyQuestions
                .stream()
                .map(questions -> {
                            Integer id = surveyQuestions.indexOf(questions);
                            List<QuestionOption> options =
                                    surveyQuestionOptionsRepository
                                            .findByQuestionID(questions.getQuestionID())
                                            .stream()
                                            .map(answer -> new QuestionOption(
                                                    answer.getScore(), answer.getOptionText()))
                                            .toList();
                            return surveyMapper.buildQuestionResponse
                                    (options, questions, id);
                        }
                )
                .toList();
        return surveyMapper.buildSurveyQuestionResponse(questionList, surveys);
    }

    

    public String getLastIdInDB(String index) {
        String prefix = index.replaceAll("\\d", "");
        String numberPart = index.replaceAll("\\D", "");

        if(numberPart.isEmpty() ) {
                throw new IllegalArgumentException("No numeric found in index: " + index);
        }

        int currentID = Integer.parseInt(numberPart);
        try {
                currentID = Integer.parseInt(numberPart);
        } 
        catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid numeric part in index: " + numberPart);
        }
        int newID = currentID + 1;
        String formatID = prefix + String.format("%0" + numberPart.length() +"d", newID);

        return formatID;
    }    

     
        public void addSurveyQuestion(String surveyId, SurveyQuestionResponse questionResponse) {
                List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(surveyId);
                if(surveyQuestion.isEmpty()) {
                        throw new ResourceNotFoundException("Not found survey for " + surveyId );
                }
                // Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestion.stream()
                //         .collect(Collectors.toMap(SurveyQuestions :: getQuestionID, Function.identity()));
                

                for(QuestionResponse questionRes : questionResponse.getQuestionList()) {

                        SurveyQuestions newQuestion = surveyQuestionRepository.findFirstByOrderByQuestionIDDesc();
                        System.out.println("newQuestion.." + newQuestion.getQuestionID());
                        String latestQuestion = (newQuestion != null) ? getLastIdInDB(newQuestion.getQuestionID()) : "Q0001";
                        
                        System.out.println("newQuestion1.." + latestQuestion);

                        SurveyQuestions surveyQuestion1 = new SurveyQuestions();
                        surveyQuestion1.setQuestionText(questionRes.getQuestionText());
                        surveyQuestion1.setQuestionID(latestQuestion);
                        surveyQuestion1.setSurveyID(surveyId);
                        
                        Categories.MentalHealthCategory categories;
                        try {
                                categories = MentalHealthCategory.valueOf(questionRes.getQuestionCategory());
                        }       
                        catch(IllegalArgumentException ex) {    
                                throw new ResourceNotFoundException("Category not found " + questionRes.getQuestionCategory() );
                        }
                        
                        Categories category = categoriesRepository.findByCategoryName(categories);
                        surveyQuestion1.setCategoryID(category.getCategoryID());
                        surveyQuestionRepository.save(surveyQuestion1);
                        
                        List<Answers> answers = new ArrayList<>();
                        List<QuestionOption> questionOption = questionRes.getQuestionOptions();
                        
                        String nextAnsId = null;
                        int index = 0;
                        Answers lastAns = null;
                        for(int i = 0; i < questionOption.size(); i++) {
                                Answers answer = new Answers();
                                answer.setAnswer(questionOption.get(i).getLabel());
                                answer.setScore(questionOption.get(i).getValue());

                                if(index == 0) {
                                        lastAns = answersRepository.findFirstByOrderByAnswerIDDesc();
                                        String newAns1 = lastAns.getAnswerID();
                                        nextAnsId = newAns1;
                                }
            
                                String newAns = (lastAns != null) ? getLastIdInDB(nextAnsId) : "A0001";
                                nextAnsId = newAns;
                                answer.setAnswerID(newAns);
                                answer.setQuestionID(latestQuestion);
                                answers.add(answer);
                                index++;
                        }
                        answersRepository.saveAll(answers);
                        
                }
                
        }

        public SurveyResultsResponse getSurveyResults(String surveyId) {
                Surveys survey = surveyRepository.findById(surveyId)
                                .orElseThrow(() -> new ResourceNotFoundException("SurveyId not found" + surveyId));
                List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(surveyId);
                        
                List<SurveyQuestionResultResponse> questionResultList = surveyQuestion.stream()
                        .map(surveyQuestion1 -> {
                                List<Answers> answer = answersRepository.findByQuestionID(surveyQuestion1.getQuestionID());
                                List<QuestionOption1> questionOption1 = new ArrayList<>();

                                answer
                                .forEach(ans -> {
                                        QuestionOption1 questionOption = surveyMapper.mapToQuestionOption(ans);
                                        questionOption1.add(questionOption);  
                                });
                                SurveyResults surveyResults = surveyResultRepository.findByQuestionID(surveyQuestion1.getQuestionID())
                                                                                        .stream()
                                                                                        .findFirst()
                                                                                        .orElse(null);
                                List<SurveyQuestionResultResponse> questionResults = new ArrayList<>();
                                if(surveyResults != null) {
                                        SurveyQuestionResultResponse sqr = surveyMapper.mapToSurveyQuestionResponse(surveyResults, surveyQuestion1, questionOption1);
                                        questionResults.add(sqr);
                                }  
                                return questionResults;            
                
                                })
                        .flatMap(List :: stream)
                        .collect(Collectors.toList());     
                return surveyMapper.mapToSurveyResultsResponse1(survey, questionResultList);
        }

        public void addAnswerToQuestion(String surveyId, String questionId, List<QuestionOption1> answerOption) {
                List<SurveyQuestions> surveyQuestionList = surveyQuestionRepository.findBySurveyID(surveyId);
                Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestionList.stream()
                                .collect(Collectors.toMap(SurveyQuestions :: getQuestionID, Function.identity()));

                SurveyQuestions surveyQuestion = surveyQuestionMap.get(questionId);
                List<Answers> answersList = new ArrayList<>();
                int index = 0;
                String aswersDesc1 = null;
                Answers answerDesc = null;
                for (QuestionOption1 questionOption : answerOption) {
                        Answers answer = new Answers();
                        
                        
                        if(index == 0) {
                                answerDesc = answersRepository.findFirstByOrderByAnswerIDDesc();  
                                String answersDesc = answerDesc.getAnswerID();
                                aswersDesc1 = answersDesc;    
                        }
                        

                        String numberOfAns =(answerDesc != null) ? getLastIdInDB(aswersDesc1) : "";
                        aswersDesc1 = numberOfAns;
                         
                        System.out.println("numberOrAns:" + numberOfAns);               
                        answer.setAnswerID(numberOfAns);
                        answer.setAnswer(questionOption.getLabel());
                        answer.setScore(questionOption.getValue());
                        answer.setQuestionID(surveyQuestion.getQuestionID());
                        answersList.add(answer);
                        index ++;
                        
                }
                answersRepository.saveAll(answersList);
                
        }        

}


