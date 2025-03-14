package com.healthy.backend.service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthy.backend.dto.survey.ConfirmationRequest;
import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.QuestionOption1;
import com.healthy.backend.dto.survey.QuestionOptionRequest;
import com.healthy.backend.dto.survey.QuestionRequest;
import com.healthy.backend.dto.survey.QuestionResponse;
import com.healthy.backend.dto.survey.QuestionResponse1;
import com.healthy.backend.dto.survey.StatusStudent;
import com.healthy.backend.dto.survey.SurveyQuestionRequest;
import com.healthy.backend.dto.survey.SurveyQuestionRequest1;
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
                yield getSurveyResult(List.of(student), tokenService.getRoleID(user));
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
        return surveyMapper.mapToResultStudent(
                getStatusStudent(survey.getSurveyID(), surveyResult.getStudentID()),
                surveyResult.getResult() + "/" + surveyResult.getMaxScore(),
                surveyResult.getStudentID());
    }

    private List<SurveyResult> getSurveyResultByStudentIDAndSurveyID(String surveyID, String studentId) {
        return surveyResultRepository.findBySurveyIDAndStudentID(surveyID, studentId);
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
                getStatusStudent(survey.getSurveyID(), studentId),
                surveyResult.getLast().getResult() + "/" + surveyResult.getLast().getMaxScore());
    }

    public void updateSurveyQuestion(HttpServletRequest request, String surveyID, SurveyQuestionRequest1 surveyQuestionRequest) {
        Role role = tokenService.retrieveUser(request).getRole();
        switch (role) {
            case MANAGER:
            case PSYCHOLOGIST:
                List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
                if(surveyQuestions.isEmpty()) {
                     throw new ResourceNotFoundException("SurveyID not found "+ surveyID);
                }

                SurveyCategory surveyCategory = SurveyCategory.valueOf(surveyQuestionRequest.getCategory().toUpperCase());
                Categories category = categoriesRepository.findByCategoryName(surveyCategory); 
                    
                Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestions.stream()
                    .collect(Collectors.toMap(SurveyQuestions :: getQuestionID, Function.identity()));
        
                for (QuestionResponse1 sqr : surveyQuestionRequest.getQuestionList()) {
                    SurveyQuestions surveyQuestion = surveyQuestionMap.get(sqr.getId());
                    surveyQuestion.setQuestionText(sqr.getQuestionText());
                    surveyQuestion.setCategoryID(category.getCategoryID());

                    List<QuestionOption1> questionOption = sqr.getQuestionOptions();
                    
                    Map<String, SurveyQuestionOptions> optionMap = questionOption.stream()
                        .map(option -> {
                            return surveyQuestionOptionsRepository.findByOptionID(option.getAnswerID());
                        })
                        .collect(Collectors.toMap(SurveyQuestionOptions :: getOptionID, Function.identity()));

                    List<SurveyQuestionOptions> optionList = questionOption.stream()
                        .map(option -> {
                            SurveyQuestionOptions sqo = optionMap.get(option.getAnswerID());
                            sqo.setOptionText(option.getLabel());
                            sqo.setScore(option.getValue());
                            
                            return sqo;
                        })
                        .collect(Collectors.toList());
                    surveyQuestionOptionsRepository.saveAll(optionList);             
                }    
                    
            return;
            default:
                throw new RuntimeException("You don't have permission to access");
        }
        
    }

        
    // public void updateSurveyQuestion(String surveyID, SurveyQuestionResponse surveyQuestionResponse) {
    //     List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);

    //     if (surveyQuestions.isEmpty()) {
    //         throw new ResourceNotFoundException("surveyID not found" + surveyID);
    //     }
    //     Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestions.stream()
    //             .collect(Collectors.toMap(SurveyQuestions::getQuestionID, Function.identity()));

    //     for (QuestionResponse sqr : surveyQuestionResponse.getQuestionList()) {
    //         SurveyQuestions surveyQuestion1 = surveyQuestionMap.get(sqr.getId());

    //         Categories categories = categoriesRepository.findById(surveyQuestion1.getCategoryID())
    //                 .orElseThrow(() -> new ResourceNotFoundException("Categories not found" + surveyQuestion1.getCategoryID()));


    //         surveyQuestion1.setQuestionText(sqr.getQuestionText());
    //         categories.setCategoryName(SurveyCategory.valueOf(sqr.getQuestionCategory()));
    //         List<QuestionOption> questionOption = sqr.getQuestionOptions();

    //         List<SurveyQuestionOptions> answers = surveyQuestionOptionsRepository.findByQuestionID(surveyQuestion1.getQuestionID());

    //         if (!answers.isEmpty()) {
    //             for (int i = 0; i < answers.size(); i++) {
    //                 SurveyQuestionOptions ans = answers.get(i);

    //                 if (i < questionOption.size()) {
    //                     ans.setOptionText(questionOption.get(i).getLabel());
    //                     ans.setScore(questionOption.get(i).getValue());

    //                 }
    //             }
    //         }
    //         surveyQuestionOptionsRepository.saveAll(answers);
    //         surveyQuestionRepository.save(surveyQuestion1);
    //         categoriesRepository.save(categories);
    //     }
    // }

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

    public String getLastIdInDB(String index) {
        String prefix = index.replaceAll("\\d", "");
        String numberPart = index.replaceAll("\\D", "");

        int currentID = Integer.parseInt(numberPart);
        int newID = currentID + 1;
        String formatID = prefix + String.format("%03d", newID);
        return formatID; 
    }

    public void addSurveyQuestion(HttpServletRequest request, String surveyId, SurveyQuestionRequest surveyQuestionRequest) {
        Role role = tokenService.retrieveUser(request).getRole();
        switch (role) {
            case MANAGER:
            case PSYCHOLOGIST:
                List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(surveyId);
                SurveyCategory surveyCategory = SurveyCategory.valueOf(surveyQuestionRequest.getCategory().toUpperCase());
                Categories category = categoriesRepository.findByCategoryName(surveyCategory);
                
                if(surveyQuestion.isEmpty()) {
                    throw new ResourceNotFoundException("Not found survey for" + surveyId);
                }

                for(QuestionRequest questionRequest : surveyQuestionRequest.getQuestionList()) {
                    String latestQuestion = generalService.generateSurveyQuestionId();
                    SurveyQuestions surveyQuestion1 = SurveyQuestions.builder()
                        .questionText(questionRequest.getQuestionText())
                        .questionID(latestQuestion)
                        .surveyID(surveyId)
                        .categoryID(category.getCategoryID())
                        .build();

                    surveyQuestionRepository.save(surveyQuestion1);

                    List<QuestionOptionRequest> questionOption = questionRequest.getQuestionOptions();
                    AtomicInteger index = new AtomicInteger(0);
                    String[] latestOptionID = {null};

                    List<SurveyQuestionOptions> surveyQuestionOptionses = questionOption.stream()
                        .map(questionOptionRequest -> {
                            if(index.get() == 0) {
                                latestOptionID[0] = generalService.generateQuestionOptionId();
                                System.out.println(latestOptionID[0]);
                            }
                            else {
                                latestOptionID[0] = (latestOptionID[0] != null) ? getLastIdInDB(latestOptionID[0]) : "SQO001";
                            }
                            index.getAndIncrement();

                            return SurveyQuestionOptions.builder()
                                .optionText(questionOptionRequest.getLabel())
                                .score(questionOptionRequest.getValue())
                                .optionID(latestOptionID[0])
                                .questionID(latestQuestion)
                                .build();

                        })
                        .collect(Collectors.toList());
                    
                    surveyQuestionOptionsRepository.saveAll(surveyQuestionOptionses);
                return;
                }
        default:
            throw new RuntimeException("You don't have permission to access");
        }
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
        int count = questions.size();
        int total;


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
            case "CAT001" -> {
                newAvgScore(survey, sum, student);
                total = count * 4;
            }
            case "CAT002" -> {
                newAvgScore(survey, sum, student);
                total = count * 3;
            }

            case "CAT003" -> {
                newAvgScore(survey, sum, student);
                total = count * 3;
            }

            default -> {
                throw new IllegalArgumentException("Invalid category: " + survey.getCategoryID());
            }
        }

        return sum + "/" + total;

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

        int result = switch (survey.getCategoryID()) {
            case "CAT001" -> sizeQues * 4;
            case "CAT002" -> sizeQues * 3;
            case "CAT003" -> sizeQues * 3;
            default -> throw new IllegalArgumentException("Invalid category: " + survey.getCategoryID());
        };

        if (total != null) {
            SurveyResult surveyResult = new SurveyResult();
            String newResultId = generalService.generateSurveyResultId();

            surveyResult.setResultID(newResultId);
            surveyResult.setStudentID(studentId);
            surveyResult.setSurveyID(surveyId);
            surveyResult.setMaxScore(result);
            surveyResult.setResult(num);

            surveyResultRepository.save(surveyResult);

            if (surveyResult.getResultID() != null) {
                saveSurveyOptionsChoice(surveyResult.getResultID(), optionId);

                status = surveyMapper.mapToResultStudent(
                        total,
                        studentId);
                if (status == null) {
                    return surveyMapper.mapToResultStudent("0", "NOT FINISHED", studentId);
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

    private List<SurveysResponse> getSurveyResult(List<Students> students, String ID) {
        List<Surveys> surveyList = surveyRepository.findAll();
        HashMap<String, String> map = new HashMap<>();

        for (Surveys survey : surveyList) {
            students.forEach(
                    student -> {
                        map.put(student.getStudentID(), getStatusStudent(survey.getSurveyID(), student.getStudentID()));
                    }
            );
        }

        return surveyList.stream()
                .map(survey -> {
                    List<StatusStudent> statusStuList = students.stream()
                            .map(student -> {

                                List<SurveyResult> surveyResultSTD = surveyResultRepository.findBySurveyIDAndStudentID(
                                        survey.getSurveyID(),
                                        student.getStudentID());

                                String studentStatus1 = getStatusStudent(survey.getSurveyID(), student.getStudentID());

                                if (surveyResultSTD.isEmpty()) {
                                    return List.of(surveyMapper.mapToResultStudent(
                                                    "0/0",
                                                    student.getStudentID()
                                            )
                                    );
                                }

                                return surveyResultSTD.stream()
                                        .map(result -> {
                                            return surveyMapper.mapToResultStudent(
                                                    result.getResult() + "/" + result.getMaxScore(),
                                                    student.getStudentID()
                                            );
                                        })
                                        .collect(Collectors.toList());
                            })
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

                    return surveyMapper.buildSurveysResponse(survey,
                            getTotalQuestion(survey),
                            ID.contains("PRT") ? null : getStatusStudent(survey.getSurveyID(), ID),
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
        return surveyResultRepository.countDistinctStudentsBySurveyID(surveys.getSurveyID());
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

    public List<ConfirmationRequest> getLowScoringStudentsForAppointment(HttpServletRequest request, String surveyId) {
        Role role = tokenService.retrieveUser(request).getRole();
        switch (role) {
            case MANAGER:
            case PSYCHOLOGIST:
                List<String> studentIdList = surveyResultRepository.findStudentsBySurveyID(surveyId);
                List<SurveyResult> resultList = studentIdList.stream()
                        .map(student -> {
                            return surveyResultRepository.findByStudentID(student).getLast();
                        })
                        .toList();

                return resultList.stream()
                        .filter(result -> result.getResult() == 0 || result.getResult() > (0.95 * result.getMaxScore()))
                        .map(result -> new ConfirmationRequest(result.getStudentID(), false))
                        .collect(Collectors.toList());

            default:
                throw new RuntimeException("You don't have permission to access");
        }

    }

    public boolean handleAppointmentRequest(List<ConfirmationRequest> requests) {

        return !requests.stream().anyMatch(request -> !request.isConfirmation());
    }

    // public double canculateScoreAvgStudent(String surveyId, String studentId) {
    //     List<SurveyResult> resultList = surveyResultRepository.findSurveyIDAndStudentID(surveyId, studentId);
    //     if(resultList.isEmpty()) {
    //         return 0.0;
    //     }

    //     int totalWeightScore = IntStream.range(0, resultList.size())
    //         .map(num -> resultList.get(num).getResult() * (num + 1))
    //         .sum();

    //     int totalWeight = IntStream.range(1, resultList.size() + 1).sum();

    //     return (double) totalWeightScore / totalWeight;

    // }

    public void newAvgScore(Surveys survey, int result, Students student) {
        String categoryId = survey.getCategoryID();
        int size = surveyResultRepository.countResultStudent(survey.getSurveyID(), student.getStudentID());
        BigDecimal newAverage = BigDecimal.ZERO;

        switch (categoryId) {
            case "CAT001":
                BigDecimal currentAvgStressScore = student.getStressScore();
                newAverage = calculateNewWeightedAvg(
                        currentAvgStressScore,
                        result,
                        size);

                student.setStressScore(newAverage);
                studentRepository.save(student);
                break;

            case "CAT002":
                BigDecimal currentAvgAnxietyScore = student.getAnxietyScore();
                newAverage = calculateNewWeightedAvg(
                        currentAvgAnxietyScore,
                        result,
                        size);

                student.setAnxietyScore(newAverage);
                studentRepository.save(student);
                break;

            case "CAT003":
                BigDecimal currentAvgDepressionScore = student.getDepressionScore();
                newAverage = calculateNewWeightedAvg(
                        currentAvgDepressionScore,
                        result,
                        size);

                student.setDepressionScore(newAverage);
                studentRepository.save(student);
                break;

            default:
                throw new AssertionError("Invalid category ID");
        }

    }

    public BigDecimal calculateNewWeightedAvg(BigDecimal presentResult, int result, int size) {
        int totalWeightBefore = (size * (size + 1)) / 2;
        int newWeight = size + 1;

        BigDecimal weightCurrentTotal = presentResult.multiply(BigDecimal.valueOf(totalWeightBefore));
        BigDecimal weightNewTotal = BigDecimal.valueOf(result).multiply(BigDecimal.valueOf(newWeight));
        BigDecimal totalWeights = BigDecimal.valueOf(totalWeightBefore).add(BigDecimal.valueOf(newWeight));

        return (weightCurrentTotal.add(weightNewTotal)).divide(totalWeights, 2, RoundingMode.HALF_UP);
    }
}