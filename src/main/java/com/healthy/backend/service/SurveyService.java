package com.healthy.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.QuestionResponse;
import com.healthy.backend.dto.survey.StatusStudent;
import com.healthy.backend.dto.survey.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyRequest;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.Categories;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.SurveyQuestionOptionsChoices;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResult;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.enums.SurveyStatus;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.CategoriesRepository;
import com.healthy.backend.repository.StudentRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsChoicesRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsRepository;
import com.healthy.backend.repository.SurveyQuestionRepository;
import com.healthy.backend.repository.SurveyRepository;
import com.healthy.backend.repository.SurveyResultRepository;

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
    private final SurveyResultRepository surveyResultRepository;
    private final StudentRepository studentRepository;



    public String totalScore( List<SurveyQuestions> question, String resultId) {
       
        Map<String, Integer> mapScore = new HashMap<>();
        
        question.forEach(question1 -> {
            List<SurveyQuestionOptions> surveyQuestionOptionList = surveyQuestionOptionsRepository.findByQuestionID(question1.getQuestionID());
            surveyQuestionOptionList.forEach(option -> {
                mapScore.put(option.getOptionID(), option.getScore());
            });
        });
        
        int countQuestion = question.size() * 3;
        int sum = 0;    
        SurveyResult surveyResult = surveyResultRepository.findByResultID(resultId);
        Surveys survey = surveyRepository.findById(surveyResult.getSurveyID())
                .orElseThrow(() -> new ResourceNotFoundException("Not found survey" + surveyResult.getSurveyID()) );

        Students studentResult = studentRepository.findById(surveyResult.getStudentID())
                .orElseThrow(() -> new ResourceNotFoundException("Not found student"));
        List<SurveyQuestionOptionsChoices> choicesList = surveyQuestionOptionsChoicesRepository.findByResultID(surveyResult.getResultID());

        List<String> optionIds = choicesList.stream()
            .map(SurveyQuestionOptionsChoices::getOptionID)
            .collect(Collectors.toList());

        System.out.println("OptionIDs from choicesList: " + optionIds);
        Map<String, SurveyQuestionOptions> optionMap = surveyQuestionOptionsRepository.findAllById(
                choicesList.stream().map(SurveyQuestionOptionsChoices :: getOptionID).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(SurveyQuestionOptions :: getOptionID  , Function.identity()));

        for (String optionId : optionIds) {
            if (!optionMap.containsKey(optionId)) {
                System.err.println("SurveyQuestionOptions not found for OptionID: " + optionId);
            }
        }

        for(SurveyQuestionOptionsChoices result : choicesList  ) {
            SurveyQuestionOptions surveyQuestion = optionMap.get(result.getOptionID());
            sum += mapScore.getOrDefault(surveyQuestion.getOptionID(), 0);
        }    
     
        switch (survey.getCategoryID()) {
            case "CAT001" -> studentResult.setStressScore(sum);
            case "CAT002" -> studentResult.setAnxietyScore(sum);
            case "CAT003" -> studentResult.setDepressionScore(sum);
         default -> {
            throw new IllegalArgumentException("Invalid category: " + survey.getCategoryID());
        }
        }

        studentRepository.save(studentResult);
        return sum + "/" + countQuestion;

    }

    public SurveyResultsResponse getSurveyResults(String surveyId) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("SurveyId not found" + surveyId));
        List<SurveyQuestions> surveyQuestionList = surveyQuestionRepository.findBySurveyID(surveyId);
        List<SurveyResult> surveyResultList = surveyResultRepository.findBySurveyID(survey.getSurveyID());

        List<StatusStudent> statusStudentsList1 = new ArrayList<>();

        List<SurveyQuestionResultResponse> surveyQuestionResultList = surveyQuestionList.stream()
            .map(questionList -> {
                List<StatusStudent> status =  surveyResultList.stream().
                    map(result -> {
                        List<SurveyQuestionOptionsChoices> choices = surveyQuestionOptionsChoicesRepository.findByResultID(
                            result.getResultID());
                        SurveyResult studentResult = surveyResultRepository.findByResultID(result.getResultID());    

                        Map<String, SurveyQuestionOptions> mapChoice = surveyQuestionOptionsRepository.findAllById(
                            choices.stream().map(SurveyQuestionOptionsChoices :: getOptionID).collect(Collectors.toList()))
                            .stream()
                            .collect(Collectors.toMap(SurveyQuestionOptions :: getOptionID, Function.identity()));

                            for(SurveyQuestionOptionsChoices sqoc : choices ) {
                                SurveyQuestionOptions optionStudent = mapChoice.get(sqoc.getOptionID());
                                if(Objects.equals(questionList.getQuestionID(), optionStudent.getQuestionID())) {          
                                    StatusStudent mapToResultStudent = surveyMapper.maptoResultStudent(optionStudent, studentResult);                                       
                                    return mapToResultStudent;                                                
                                }
                            }
                    return null;
                    })
                .filter(Objects :: nonNull)
                .collect(Collectors.toList());    
            return surveyMapper.mapToSurveyQuestionResponse(
                questionList,
                status);
            })
        .collect(Collectors.toList()); 

        surveyResultList.forEach(score -> {
            SurveyResult studentResult = surveyResultRepository.findByResultID(score.getResultID());
            StatusStudent mapToResultStudent1 = surveyMapper.maptoResultStudent1(
                getStatusStudent(survey.getSurveyID(), studentResult.getStudentID()),
                totalScore(surveyQuestionList, score.getResultID()),
                score);
                statusStudentsList1.add(mapToResultStudent1);
        });
           
        return surveyMapper.mapToSurveyResultsResponse1(survey, surveyQuestionResultList, statusStudentsList1);    
        
    }
    

    public String getStatusStudent(String surveyId, String studentId) {
    List<SurveyResult> resultList = surveyResultRepository.findBySurveyID(surveyId);
    
    for (SurveyResult surveyCheck : resultList) {
        if (surveyCheck.getStudentID() != null && Objects.equals(studentId, surveyCheck.getStudentID())) {
            return "Finished";
        }
    }
    
    return "Not Finished";
}

    public StatusStudent getStudentIDSurveyResults(String surveyId, String studentId) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found by"+ surveyId));

        List<SurveyResult> surveyResultList = surveyResultRepository.findBySurveyID(survey.getSurveyID());
        
        if(surveyResultList.isEmpty()) {
            SurveyResult emptyStudent = new SurveyResult();
            emptyStudent.setStudentID(studentId);
            return surveyMapper.buildStatusStudent(emptyStudent, "Not Finished", "0");
        }
        else {
            boolean found = surveyResultList.stream()
                .anyMatch(check -> studentId != null && studentId.equals(check.getStudentID()));

            if(!found) {
                SurveyResult emptyStudent = new SurveyResult();
                emptyStudent.setStudentID(studentId);
                return surveyMapper.buildStatusStudent(emptyStudent, "Not Finished", "0");
            }    
        }
        return null;
    }


    public List<SurveysResponse> getAllSurveys() {
    List<Surveys> surveys = surveyRepository.findAll();

    if (surveys.isEmpty()) {
        throw new ResourceNotFoundException("No surveys found");
    }

    return surveys.stream().map(survey -> {
        List<SurveyQuestionResultResponse> surveyQuestionResultList = new ArrayList<>();
        List<SurveyQuestions> surveyQuestionsList = surveyQuestionRepository.findBySurveyID(survey.getSurveyID());
        // System.out.println("surveyId" + survey.getSurveyID());
        

        surveyQuestionsList.forEach(question -> {
            List<SurveyQuestionOptions> questionOptions = surveyQuestionOptionsRepository.findByQuestionID(question.getQuestionID());
            List<QuestionOption> questionOptionList = questionOptions.stream()
                    .map(option -> surveyMapper.mapToQuestionOption(option))
                    .collect(Collectors.toList());

            SurveyQuestionResultResponse sqr = surveyMapper.mapToQuestion(question, questionOptionList);
            surveyQuestionResultList.add(sqr);
        });

        
        List<SurveyResult> surveyResultList = surveyResultRepository.findBySurveyID(survey.getSurveyID());
        // System.out.println("surveyId" + surveyResultList);
        List<StatusStudent> statusStudentList = new ArrayList<>();

        for (SurveyResult surveyResult : surveyResultList) {
            String studentId = surveyResult.getStudentID();
            String status = getStatusStudent(survey.getSurveyID(), studentId); 
            String score = totalScore(surveyQuestionsList, surveyResult.getResultID()); 
            statusStudentList.add(surveyMapper.buildStatusStudent(surveyResult, status, score));
        }

        
        List<String> allStudentIds = studentRepository.findAllStudentIds(); 
        for (String studentId : allStudentIds) {
            boolean found = statusStudentList.stream()
                .anyMatch(status -> studentId != null && studentId.equals(status.getStudentId()));
            if (!found) {
                SurveyResult emptySurveyResult = new SurveyResult();
                emptySurveyResult.setStudentID(studentId);
                statusStudentList.add(surveyMapper.buildStatusStudent(emptySurveyResult, "Not Finished", "0"));
            }
        }
        return surveyMapper.buildSurveysResponse2(survey, surveyQuestionsList.size(), statusStudentList);
    })
    .toList();
}



    public void updateSurveyQuestion(String surveyID, SurveyQuestionResponse surveyQuestionResponse) {
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);

        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("surveyID not found" + surveyID);
        }
        Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestions.stream()
                .collect(Collectors.toMap(SurveyQuestions::getQuestionID, Function.identity()));

        for (QuestionResponse sqr : surveyQuestionResponse.getQuestionList()) {
            SurveyQuestions surveyQuestion1 = surveyQuestionMap.get(sqr.getId());

            Categories categories = categoriesRepository.findById(surveyQuestion1.getCategoryID())
                    .orElseThrow(() -> new ResourceNotFoundException("Categories not found" + surveyQuestion1.getCategoryID()));


            surveyQuestion1.setQuestionText(sqr.getQuestionText());
            categories.setCategoryName(SurveyCategory.valueOf(sqr.getQuestionCategory()));
            List<QuestionOption> questionOption = sqr.getQuestionOptions();

            List<SurveyQuestionOptions> answers = surveyQuestionOptionsRepository.findByQuestionID(surveyQuestion1.getQuestionID());

            if (!answers.isEmpty()) {
                for (int i = 0; i < answers.size(); i++) {
                    SurveyQuestionOptions ans = answers.get(i);

                    if (i < questionOption.size()) {
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
                                                    answer.getScore(), answer.getOptionText(), answer.getOptionID()))
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

        if (numberPart.isEmpty()) {
            throw new IllegalArgumentException("No numeric found in index: " + index);
        }

        int currentID = Integer.parseInt(numberPart);
        try {
            currentID = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric part in index: " + numberPart);
        }
        int newID = currentID + 1;
        String formatID = prefix + String.format("%0" + numberPart.length() + "d", newID);

        return formatID;
    }


    public void addSurveyQuestion(String surveyId, SurveyQuestionResponse questionResponse) {
        List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(surveyId);
        if (surveyQuestion.isEmpty()) {
            throw new ResourceNotFoundException("Not found survey for " + surveyId);
        }

        for (QuestionResponse questionRes : questionResponse.getQuestionList()) {

            SurveyQuestions newQuestion = surveyQuestionRepository.findFirstByOrderByQuestionIDDesc();
            System.out.println("newQuestion.." + newQuestion.getQuestionID());
            String latestQuestion = (newQuestion != null) ? getLastIdInDB(newQuestion.getQuestionID()) : "Q0001";

            // System.out.println("newQuestion1.." + latestQuestion);

            SurveyQuestions surveyQuestion1 = new SurveyQuestions();
            surveyQuestion1.setQuestionText(questionRes.getQuestionText());
            surveyQuestion1.setQuestionID(latestQuestion);
            surveyQuestion1.setSurveyID(surveyId);

            SurveyCategory categories;
            try {
                categories = SurveyCategory.valueOf(questionRes.getQuestionCategory());
            } catch (IllegalArgumentException ex) {
                throw new ResourceNotFoundException("Category not found " + questionRes.getQuestionCategory());
            }

            Categories category = categoriesRepository.findByCategoryName(categories);

            surveyQuestion1.setCategoryID(category.getCategoryID());
            surveyQuestionRepository.save(surveyQuestion1);

            List<SurveyQuestionOptions> optionsList = new ArrayList<>();
            List<QuestionOption> questionOption = questionRes.getQuestionOptions();

            String nextAnsId = null;
            int index = 0;
            SurveyQuestionOptions lastAns = null;
            for (QuestionOption QO : questionOption) {
                SurveyQuestionOptions option = new SurveyQuestionOptions();
                option.setOptionText(QO.getLabel());
                option.setScore(QO.getValue());

                if (index == 0) {
                    lastAns = surveyQuestionOptionsRepository.findFirstByOrderByOptionIDDesc();
                    nextAnsId = lastAns.getOptionID();
                }

                String newAns = getLastIdInDB(nextAnsId);
                nextAnsId = newAns;
                option.setQuestionID(newAns);
                option.setQuestionID(latestQuestion);
                optionsList.add(option);
                index++;
            }
            surveyQuestionOptionsRepository.saveAll(optionsList);

        }

    }


    public void addAnswerToQuestion(String surveyId, String questionId, List<QuestionOption> answerOption) {
        List<SurveyQuestions> surveyQuestionList = surveyQuestionRepository.findBySurveyID(surveyId);
        Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestionList.stream()
                .collect(Collectors.toMap(SurveyQuestions::getQuestionID, Function.identity()));

        SurveyQuestions surveyQuestion = surveyQuestionMap.get(questionId);
        List<SurveyQuestionOptions> optionsList = new ArrayList<>();
        int index = 0;
        String aswersDesc1 = null;
        SurveyQuestionOptions answerDesc = null;
        for (QuestionOption questionOption : answerOption) {
            SurveyQuestionOptions options = new SurveyQuestionOptions();

            if (index == 0) {
                answerDesc = surveyQuestionOptionsRepository.findFirstByOrderByOptionIDDesc();
                aswersDesc1 = answerDesc.getOptionID();
            }

            String numberOfAns = getLastIdInDB(aswersDesc1);
            aswersDesc1 = numberOfAns;

            options.setQuestionID(numberOfAns);
            options.setOptionText(questionOption.getLabel());
            options.setScore(questionOption.getValue());
            options.setQuestionID(surveyQuestion.getQuestionID());
            optionsList.add(options);
            index++;

        }
        surveyQuestionOptionsRepository.saveAll(optionsList);
    }

    public void updateSurveyStatus( String surveyId, SurveyRequest status) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey Not found"));
        survey.setStatus(SurveyStatus.valueOf(status.getStatus()));
        surveyRepository.save(survey);
    }


    public String calculateTotalRightQuestion(List<SurveyQuestions> questions, List<String> options, Surveys survey, Students student) {
        Map<String, Integer> mapScore = new HashMap<>();
        int sum = 0;
        int count = questions.size() * 3;
        
        questions.forEach(question -> {

            List<SurveyQuestionOptions> surveyOption = surveyQuestionOptionsRepository.findByQuestionID(question.getQuestionID());
            surveyOption.forEach(s -> {
                mapScore.put(s.getOptionID(), s.getScore());        
            });
        });

        for(String surveyQuestionOption : options) {
            sum += mapScore.getOrDefault(surveyQuestionOption, 0);
        }
        
        // switch (survey.getCategoryID()) {
        //     case "CAT001" :
        //         student.setStressScore(sum);
        //         break;
        //     case "CAT002" :
        //         student.setAnxietyScore(sum);
        //         break;
        //     case "CAT003" :
        //         student.setDepressionScore(sum);
        //         break;    
        //     default:
        //         break;
        // }
        switch (survey.getCategoryID()) {
            case "CAT001" -> student.setStressScore(sum);
            case "CAT002" -> student.setAnxietyScore(sum);
            case "CAT003" -> student.setDepressionScore(sum);
         default -> {
            throw new IllegalArgumentException("Invalid category: " + survey.getCategoryID());
        }
        }

        return sum + "/" + count;
          
    }


    public StatusStudent getScoreFromStudentInSuv(String surveyId, List<String> optionId, String studentId) {
        StatusStudent status = new StatusStudent();

        Surveys survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found survey"));

        List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(survey.getSurveyID());
        String check = null;
        for(String option : optionId) {
            if(Objects.equals(check, option)) {
                return null;
            }
            check = option;
        }

        Students student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found student"));

        String total = calculateTotalRightQuestion(surveyQuestion, optionId, survey, student);

        if(total != null) {
            SurveyResult surveyResult = new SurveyResult();
            String resultId = surveyResultRepository.findLastResultId();
            
            surveyResult.setResultID(resultId);
            surveyResult.setStudentID(studentId);
            surveyResult.setSurveyID(surveyId);
            
            surveyResultRepository.save(surveyResult);

            if(surveyResult.getResultID() != null) {
                saveSurveyOptionsChoice(surveyResult.getResultID(), optionId);

                status = surveyMapper.maptoResultStudent1(
                getStatusStudent(surveyId, studentId),
                total,
                surveyResult);
                if(status == null) {
                    return surveyMapper.maptoResultStudent1("0", "Not Finished", surveyResult);
                }

            }
        } 
        return status;
    }

    public void saveSurveyOptionsChoice(String resultID, List<String> optionId) {
        List<SurveyQuestionOptionsChoices> choiceList = new ArrayList<>();
        for(String surveyQuestionOption : optionId) {
            SurveyQuestionOptions question = surveyQuestionOptionsRepository.findByOptionID(surveyQuestionOption);
            String questionId = question.getQuestionID();
            

            SurveyQuestionOptionsChoices sqc = new SurveyQuestionOptionsChoices(resultID, questionId, surveyQuestionOption);
            choiceList.add(sqc);
        }
        surveyQuestionOptionsChoicesRepository.saveAll(choiceList);
    }

}