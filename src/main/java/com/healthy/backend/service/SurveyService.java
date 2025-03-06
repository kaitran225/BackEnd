package com.healthy.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.QuestionResponse;
import com.healthy.backend.dto.survey.StatusStudent;
import com.healthy.backend.dto.survey.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.SurveyRequest;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.Categories;
import com.healthy.backend.entity.Parents;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.SurveyQuestionOptionsChoices;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResult;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.entity.Users;
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
    private final TokenService tokenService;
    private final GeneralService generalService;

    private final SurveyQuestionOptionsChoicesRepository surveyQuestionOptionsChoicesRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionOptionsRepository surveyQuestionOptionsRepository;
    private final CategoriesRepository categoriesRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final StudentRepository studentRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final ParentRepository parentRepository;

    private final SurveyMapper surveyMapper;


    public List<SurveysResponse> getAllSurveys(Users user) {
    return switch (user.getRole()) {
        case STUDENT -> {
            String studentId = tokenService.getRoleID(user);
            Students student = studentRepository.findByStudentID(studentId);
            yield getSurveyResult(List.of(student), ""); 
        }
        case MANAGER, PSYCHOLOGIST -> {
            List<Surveys> surveys = surveyRepository.findAll();
            yield surveys.stream()
                .map(survey -> surveyMapper.buildManagerSurveysResponse(
                    survey,
                    getTotalQuestion(survey),
                    getSurveyStatus(survey),
                    getTotalStudentDone(survey) + "/" + getTotalStudent()
                ))
                .toList();
        }
        case PARENT -> {
            Parents parent = parentRepository.findByUserID(user.getUserId());
            List<Students> children = parent.getStudents();
            yield getSurveyResult(children, parent.getParentID());

            // yield children.stream()
            //     .map(student -> getSurveyResult(student.getStudentID())) 
            //     .flatMap(List::stream) 
            //     .toList(); 
        }
        default -> throw new RuntimeException("You don't have permission to access this resource.");
    };
}




    public SurveyResultsResponse getSurveyResultsBySurveyID(HttpServletRequest request, String surveyId) {
        Role role = tokenService.retrieveUser(request).getRole();
        Surveys survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("SurveyId not found" + surveyId));
        return switch (role) {
            case PARENT -> {
                List<Students> children = parentRepository.findByUserID(tokenService.getRoleID(tokenService.retrieveUser(request))).getStudents();
                List<SurveyResult> surveyResultList = children.stream().flatMap(child -> getSurveyResultByStudentIDAndSurveyID(surveyId, child.getStudentID()).stream()).toList();
                yield surveyMapper.mapToListResultsResponse(survey,
                        surveyResultList.stream()
                                .map(surveyResult -> getStatusStudent(survey, surveyResult)).toList());
            }
            case STUDENT -> {
                SurveyResult surveyResult = getSurveyResultByStudentIDAndSurveyID(surveyId, tokenService.getRoleID(tokenService.retrieveUser(request))).getFirst();
                yield surveyMapper.mapToListResultsResponse(survey,
                        List.of(getStatusStudent(survey, surveyResult)));
            }
            case MANAGER, PSYCHOLOGIST -> {
                List<SurveyResult> surveyResultList = surveyResultRepository.findBySurveyID(survey.getSurveyID());
                yield surveyMapper.mapToListResultsResponse(survey,
                        surveyResultList.stream()
                                .map(surveyResult -> getStatusStudent(survey, surveyResult)).toList());
            }
            default -> throw new RuntimeException("You don't have permission to access");
        };
    }

    private StatusStudent getStatusStudent(Surveys survey, SurveyResult surveyResult) {
        return surveyMapper.maptoResultStudent1(
                getStatusStudent(survey.getSurveyID(), surveyResult.getStudentID()),
                surveyResult.getResult() + "/" + surveyResult.getMaxScore(),
                surveyResult.getStudentID());
    }

    private List<SurveyResult> getSurveyResultByStudentIDAndSurveyID(String surveyID, String studentId) {
        return surveyResultRepository.findSurveyIDAndStudentID(surveyID, studentId);
    }

    public SurveyQuestionResponse getSurveyResultByStudentID(HttpServletRequest request, String surveyID, String studentId) {

        studentId = tokenService.validateRequestStudentID(request, studentId);

        if (!surveyResultRepository.existsBySurveyIDAndStudentID(surveyID, studentId)) {
            throw new ResourceNotFoundException("No survey found for surveyID " + surveyID + " with studentID " + studentId);
        }

        Surveys survey = surveyRepository.findById(surveyID).orElseThrow(
                () -> new ResourceNotFoundException("No survey found for surveyID " + surveyID));
        List<SurveyResult> surveyResult = surveyResultRepository.findBySurveyIDAndStudentID(surveyID, studentId);
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
        }
        List<QuestionResponse> questionList = surveyQuestions
                .stream()
                .map(questions -> {
                            List<QuestionOption> questionOption = getQuestionResponse(questions);
                            questionOption.forEach(option -> {
                                if (option.getAnswerID().equals(surveyResult.getLast().getChoices().get(surveyQuestions.indexOf(questions)).getOptionID())) {
                                    option.setChecked(true);
                                }
                            });
                            return surveyMapper.buildQuestionResponse(
                                    questionOption,
                                    questions,
                                    surveyQuestions.indexOf(questions));
                        }
                )
                .toList();
        return surveyMapper.buildSurveyResultResponse(questionList,
                survey,
                getSurveyStatus(survey),
                surveyResult.getLast().getResult() + "/" + surveyResult.getLast().getMaxScore());
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
                            return surveyMapper.buildQuestionResponse(
                                    getQuestionResponse(questions),
                                    questions,
                                    surveyQuestions.indexOf(questions));
                        }
                )
                .toList();
        return surveyMapper.buildSurveyQuestionResponse(questionList, surveys);
    }

    private List<QuestionOption> getQuestionResponse(SurveyQuestions questions) {
        return surveyQuestionOptionsRepository
                .findByQuestionID(questions.getQuestionID())
                .stream()
                .map(surveyMapper::buildNewQuestionOption)
                .toList();
    }


    public void addSurveyQuestion(HttpServletRequest request, String surveyId, SurveyQuestionResponse questionResponse) {

        Role role = tokenService.retrieveUser(request).getRole();
        switch (role) {
            case MANAGER:
            case PSYCHOLOGIST:
                List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(surveyId);
                if (surveyQuestion.isEmpty()) {
                    throw new ResourceNotFoundException("Not found survey for " + surveyId);
                }

                for (QuestionResponse questionRes : questionResponse.getQuestionList()) {

                    SurveyQuestions newQuestion = surveyQuestionRepository.findFirstByOrderByQuestionIDDesc();
                    System.out.println("newQuestion.." + newQuestion.getQuestionID());
                    String latestQuestion = generalService.generateSurveyQuestionId();


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

                        String newAns = generalService.generateQuestionOptionId();
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

            String numberOfAns = generalService.generateQuestionOptionId();
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

    public void updateSurveyStatus(String surveyId, SurveyRequest status) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey Not found"));
        survey.setStatus(SurveyStatus.valueOf(status.getStatus()));
        surveyRepository.save(survey);
    }


    private String calculateTotalRightQuestion(List<SurveyQuestions> questions, List<String> options, Surveys survey, Students student) {
        Map<String, Integer> mapScore = new HashMap<>();
        int sum = 0;
        int count = questions.size() * 3;

        questions.forEach(question -> {

            List<SurveyQuestionOptions> surveyOption = surveyQuestionOptionsRepository.findByQuestionID(question.getQuestionID());
            surveyOption.forEach(s -> {
                mapScore.put(s.getOptionID(), s.getScore());
            });
        });

        for (String surveyQuestionOption : options) {
            sum += mapScore.getOrDefault(surveyQuestionOption, 0);
        }
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
        int sizeQues = surveyQuestionRepository.countQuestionInSuv(surveyId);
        String check = null;
        for (String option : optionId) {
            if (Objects.equals(check, option)) {
                return null;
            }
            check = option;
        }

        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found student"));

        String total = calculateTotalRightQuestion(surveyQuestion, optionId, survey, student);
        String[] parts = total.split("/");
        String firstParts = parts[0];
        int num = Integer.parseInt(firstParts);

        if (total != null) {
            SurveyResult surveyResult = new SurveyResult();
            String newResultId = generalService.generateSurveyResultId();

            surveyResult.setResultID(newResultId);
            surveyResult.setStudentID(studentId);
            surveyResult.setSurveyID(surveyId);
            surveyResult.setMaxScore(sizeQues * 3);
            surveyResult.setResult(num);

            surveyResultRepository.save(surveyResult);

            if (surveyResult.getResultID() != null) {
                saveSurveyOptionsChoice(surveyResult.getResultID(), optionId);

                status = surveyMapper.maptoResultStudent1(
                        getStatusStudent(surveyId, studentId),
                        total,
                        studentId);
                if (status == null) {
                    return surveyMapper.maptoResultStudent1("0", "Not Finished", studentId);
                }

            }
        }
        return status;
    }

    private void saveSurveyOptionsChoice(String resultID, List<String> optionId) {
        List<SurveyQuestionOptionsChoices> choiceList = new ArrayList<>();
        for (String surveyQuestionOption : optionId) {
            SurveyQuestionOptions question = surveyQuestionOptionsRepository.findByOptionID(surveyQuestionOption);
            String questionId = question.getQuestionID();

            SurveyQuestionOptionsChoices sqc = new SurveyQuestionOptionsChoices(resultID, questionId, surveyQuestionOption);
            choiceList.add(sqc);
        }
        surveyQuestionOptionsChoicesRepository.saveAll(choiceList);
    }


    private List<SurveysResponse> getSurveyResult(List<Students> students,String ID) {
        List<Surveys> surveyList = surveyRepository.findAll();
        HashMap<String, String> map = new HashMap<>();

        for (Surveys survey : surveyList) {
            students.forEach(
                student -> {
                    map.put(student.getStudentID(), getStatusStudent(survey.getSurveyID(),student.getStudentID()));
                }
            );
        }
        
        return surveyList.stream()
                .map(survey -> {
                    List<StatusStudent> statusStuList = students.stream()
                        .map(student -> {
                            
                            List<SurveyResult> surveyResultSTD = surveyResultRepository.findSurveyIDAndStudentID(
                                survey.getSurveyID(),
                                student.getStudentID());

                            String studentStatus1 = getStatusStudent(survey.getSurveyID(), student.getStudentID());    

                            if (surveyResultSTD.isEmpty()) {
                                return List.of(surveyMapper.maptoResultStudent1(
                                    studentStatus1,
                                    "0/0", 
                                    student.getStudentID()
                                )
                                );
                            }

                            return surveyResultSTD.stream()
                                .map(result -> {
                                    return surveyMapper.maptoResultStudent1(
                                        map.get(student.getStudentID()),
                                        result.getResult() + "/" + result.getMaxScore(),
                                        student.getStudentID()
                                        );
                                })
                            .collect(Collectors.toList());    
                        })
                        .flatMap(List :: stream)
                        .collect(Collectors.toList());
                             
                    return surveyMapper.buildSurveysResponse1(survey,
                    getTotalQuestion(survey),
                    ID.contains("PRT") ? null : getSurveyStatus(survey),
                    statusStuList);


                })
                .collect(Collectors.toList());                      
       } 

    @Transactional
    public int calculateTotalScore(List<SurveyQuestionOptionsChoices> questionOptionsChoices) {
        return questionOptionsChoices.stream().mapToInt(choices -> {
            return choices.getOptions().getScore();
        }).sum();
    }

    @Transactional
    public int calculateTotalScore(String resultID) {
        SurveyResult surveyResult = surveyResultRepository.findByIdWithChoices(resultID);
        return surveyResult.getChoices().stream()
                .map(SurveyQuestionOptionsChoices::getOptions) // Avoid null options
                .map(SurveyQuestionOptions::getScore)
                .mapToInt(Integer::intValue)
                .sum(); // Sum all
    }


    public int calculateMaxScore(Surveys surveys) {
        List<SurveyQuestions> surveyQuestionsList = surveyQuestionRepository.findBySurveyID(surveys.getSurveyID());

        return surveyQuestionsList.stream()
                .mapToInt(question ->
                        surveyQuestionOptionsRepository.findByQuestionID(question.getQuestionID())
                                .stream()
                                .mapToInt(SurveyQuestionOptions::getScore)
                                .max() // Get max score for the question
                                .orElse(0)
                )
                .sum();
    }

    private int getTotalQuestion(Surveys surveys) {
        return (int) surveyQuestionRepository.countBySurveyID(surveys.getSurveyID());
    }

    private int getTotalStudentDone(Surveys surveys) {
        return (int) surveyResultRepository.countDistinctStudentsBySurveyID(surveys.getSurveyID());
    }

    private int getTotalStudent() {
        return (int) studentRepository.count();
    }

    private String getSurveyStatus(Surveys surveys) {
        return getTotalStudent() == getTotalStudentDone(surveys) ? "COMPLETED" : "NOT COMPLETED";
    }


    private String getStatusStudent(String surveyId, String studentId) {
        return !surveyResultRepository.existsBySurveyIDAndStudentID(surveyId, studentId)
                ? "NOT COMPLETED"
                : "COMPLETED";
    }

//     public Feedback submitSurveyFeedback(HttpServletRequest request, String surveyId, String feedbackComment, Integer rating) {
    
//     Role role = tokenService.retrieveUser(request).getRole();
    
   
//     switch (role) {
//         case MANAGER:
//         case PSYCHOLOGIST:
            
//             Surveys survey = surveyRepository.findById(surveyId)
//                     .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
   
            
//             List<SurveyResult> surveyResults = surveyResultRepository.findBySurveyID(surveyId);
            
            
//             for (SurveyResult surveyResult : surveyResults) {
                
//                 if (surveyResult.getResult() >= 15) {
                    
//                     Appointments appointment = new Appointments();
//                     appointment.setAppointmentID(UUID.randomUUID().toString());  
//                     appointment.setStudentID(surveyResult.getStudentID());  
//                     appointment.setPsychologistID(assignedPsychologistId);  
//                     appointment.setStatus(AppointmentStatus.SCHEDULED);  

                    
//                     appointmentRepository.save(appointment);

//                     Notifications notification = new Notifications();
//                     notification.setAppointment(appointment);
//                     notification.setMessage("You have a new appointment scheduled.");
//                     notificationsRepository.save(notification);
//                 }
//             }

           
//             Feedback feedback = new Feedback();
//             feedback.setComment(feedbackComment); 
//             feedback.setRating(rating);  
//             feedback.setAppointment(appointment);  
            
//             feedbackRepository.save(feedback);

//             return feedback;

//         default:
           
//             throw new AssertionError("Unauthorized role. Only Managers and Psychologists can submit feedback.");
//     }
// }


}