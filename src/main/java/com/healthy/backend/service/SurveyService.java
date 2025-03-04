package com.healthy.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
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
import com.healthy.backend.enums.Role;
import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.enums.SurveyStatus;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.CategoriesRepository;
import com.healthy.backend.repository.ParentRepository;
import com.healthy.backend.repository.StudentRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsChoicesRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsRepository;
import com.healthy.backend.repository.SurveyQuestionRepository;
import com.healthy.backend.repository.SurveyRepository;
import com.healthy.backend.repository.SurveyResultRepository;
import com.healthy.backend.security.TokenService;

import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenService tokenService;
    private final ParentRepository parentRepository;


    public List<SurveysResponse> getAllSurveys(HttpServletRequest request) {
        Role role = tokenService.retrieveUser(request).getRole();
        List<Surveys> surveys = surveyRepository.findAll();
        
        switch (role) {
            case PARENT:
                List<Students> children = parentRepository.findByUserID(
                    tokenService.retrieveUser(request).getUserId()).getStudents();
                return surveys.stream()
                        .map(survey -> {
                            List<StatusStudent> statusList = children.stream()
                                    .map(chil -> {
                                        return surveyMapper.buildStatusStudent1(
                                            chil.getStudentID(),
                                            getStatusStudent(survey.getSurveyID(), chil.getStudentID()));
                                    })
                                    .collect(Collectors.toList());
                            return surveyMapper.buildSurveysResponse2(
                                survey,
                                surveyQuestionRepository.countQuestionInSuv(survey.getSurveyID()),
                                statusList);
                            })
                .collect(Collectors.toList());
                    
                
        case MANAGER:
        case PSYCHOLOGIST:
            return surveys.stream()
                .map(survey -> {
                    int countResultInSuv = surveyResultRepository.countResultInSuv(survey.getSurveyID());
                    int completeStudent = studentRepository.countStudent();
                    String statusComplete = (countResultInSuv > 0) ? "COMPLETED" : "NOT COMPLETED";
                    String presentComplete = countResultInSuv + "/" + completeStudent;
                    int questionList = surveyQuestionRepository.countQuestionInSuv(survey.getSurveyID());



                    return surveyMapper.buildSurveysResponse1(survey, questionList, statusComplete, presentComplete); 
                })
                .collect(Collectors.toList());

            case STUDENT:
                String student = tokenService.getRoleID(tokenService.retrieveUser(request));
                List<SurveyResult> surveyResultList = surveyResultRepository.findByStudentID(student);

                if(surveyResultList.isEmpty()) {
                    return List.of();
                }

                List<Surveys> surveyList = surveyRepository.findAll();
                HashMap<String, String> map = new HashMap<>();
                for(Surveys survey : surveyList) {
                    map.put(survey.getSurveyID(), getStatusStudent(survey.getSurveyID(), student));
                }
                return surveyList.stream()
                    .map(survey -> {
                        return surveyMapper.buildSurveysResponse3(
                            survey,
                            surveyQuestionRepository.countQuestionInSuv(survey.getSurveyID()),
                            map.get(survey.getSurveyID())
                        );
                    })
                    .collect(Collectors.toList());

            default:
                throw new RuntimeException("You don't have permission to access");
        }         
    }

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

    public SurveyResultsResponse getSurveyResults(HttpServletRequest request, String surveyId) {
        Role role = tokenService.retrieveUser(request).getRole();
        
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("SurveyId not found" + surveyId));
        List<SurveyQuestions> surveyQuestionList = surveyQuestionRepository.findBySurveyID(surveyId);
        List<SurveyResult> surveyResultList = surveyResultRepository.findBySurveyID(survey.getSurveyID());

        List<StatusStudent> statusStudentsList1 = new ArrayList<>();
        switch (role) {
            case MANAGER :
            case PSYCHOLOGIST:
                
                surveyResultList.forEach(score -> {
                    SurveyResult studentResult = surveyResultRepository.findByResultID(score.getResultID());
                    StatusStudent mapToResultStudent1 = surveyMapper.maptoResultStudent1(
                        getStatusStudent(survey.getSurveyID(), studentResult.getStudentID()),
                        totalScore(surveyQuestionList, score.getResultID()),
                        score);
                    statusStudentsList1.add(mapToResultStudent1);
                });
           
            return surveyMapper.mapToSurveyResultsResponse2(survey,statusStudentsList1);

            case PARENT :
                List<Students> chilren = parentRepository.findByUserID(
                    tokenService.retrieveUser(request).getUserId()).getStudents();
                HashMap<String, String> map = new HashMap<>();


                chilren.forEach(chil -> {
                    List<SurveyResult> surveyResultList1 = surveyResultRepository.findSurveyIDAndStudentID1(
                        survey.getSurveyID(), chil.getStudentID());   


                    surveyResultList1.forEach(score -> {
                    
                        StatusStudent mapToResultStudent1 = surveyMapper.maptoResultStudent1(
                            getStatusStudent(survey.getSurveyID(), chil.getStudentID()),
                            totalScore(surveyQuestionList, score.getResultID()),
                            score);
                        statusStudentsList1.add(mapToResultStudent1);
                    });                             
                });    
                return surveyMapper.mapToSurveyResultsResponse2(survey,statusStudentsList1);
                      
            default:
                throw new RuntimeException("You don't have permission to access");
        }
        
    }
    

    public String getStatusStudent(String surveyId, String studentId) { 
        if (!surveyResultRepository.existsBySurveyIDAndStudentID(surveyId, studentId)) {
            return "NOT COMPLETED";
        }
    
    return "COMPLETED";
    }

    public SurveyResultsResponse getStudentIDSurveyResults(HttpServletRequest request, String surveyId, String studentId) {
    Role role = tokenService.retrieveUser(request).getRole();
    
    Surveys survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new ResourceNotFoundException("Survey not found by " + surveyId));
    List<SurveyQuestions> questionList = surveyQuestionRepository.findBySurveyID(surveyId);    
    List<StatusStudent> statusEmty = new ArrayList<>();
    List<SurveyResultsResponse> emtyList = new ArrayList<>();

    switch (role) {
        case MANAGER:
        case PSYCHOLOGIST:
            List<SurveyResult> surveyResultList = surveyResultRepository.findSurveyIDAndStudentID1(surveyId, studentId);
            List<StatusStudent> resultScore = new ArrayList<>();

            
            surveyResultList.forEach(result -> {
                StatusStudent statusStudent = surveyMapper.buildStatusStudentId(
                    "COMPLETED",
                    studentId,
                    totalScore(questionList, result.getResultID())
                );
                resultScore.add(statusStudent);
            });

            if (surveyResultList.isEmpty()) {
                SurveyResult emptyStudent = new SurveyResult();
                emptyStudent.setStudentID(studentId);
                statusEmty.add(surveyMapper.buildStatusStudent(emptyStudent, "NOT COMPLETED", "0"));

                SurveyResultsResponse emtyResult = surveyMapper.mapToSurveyQuestionResponse3(statusEmty);
                emtyList.add(emtyResult);
                return emtyList.get(0);
            }    

            
            List<SurveyQuestionResultResponse> listQues = questionList.stream()
                .map(question -> {
                    List<StatusStudent> status = surveyResultList.stream()
                        .map(result -> {
                            List<SurveyQuestionOptionsChoices> choices = surveyQuestionOptionsChoicesRepository.findByResultID(result.getResultID());

                            Map<String, SurveyQuestionOptions> mapChoice = surveyQuestionOptionsRepository.findAllById(
                                choices.stream().map(SurveyQuestionOptionsChoices::getOptionID).collect(Collectors.toList())
                            ).stream().collect(Collectors.toMap(SurveyQuestionOptions::getOptionID, Function.identity()));

                            for (SurveyQuestionOptionsChoices sqoc : choices) {
                                SurveyQuestionOptions optionStudent = mapChoice.get(sqoc.getOptionID());
                                if (Objects.equals(question.getQuestionID(), optionStudent.getQuestionID())) {
                                    return surveyMapper.maptoResultStudent(optionStudent, result);
                                }
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                    
                    return surveyMapper.mapToSurveyQuestionResponse(
                        question,
                        status
                    );
                })
                .collect(Collectors.toList());

            
            return surveyMapper.mapToSurveyResultsResponse12(listQues, resultScore);

        default:
            throw new RuntimeException("You don't have permission to access");
    }
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


    public void addSurveyQuestion(HttpServletRequest request, String surveyId, SurveyQuestionResponse questionResponse) {
        
        Role role = tokenService.retrieveUser(request).getRole();
        switch (role) {
            case MANAGER :
            case PSYCHOLOGIST:
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
                
            default:
                throw new RuntimeException("You don't have permission to access");
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
            String newResultId = (resultId != null) ? getLastIdInDB(resultId) : "ANS001";
            
            surveyResult.setResultID(newResultId);
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