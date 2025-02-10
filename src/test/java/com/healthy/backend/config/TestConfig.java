package com.healthy.backend.config;

import com.healthy.backend.repository.*;
import com.healthy.backend.service.UserService;
import mockit.Mocked;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Mocked
    private UserRepository userRepository;

    @Mocked
    private StudentRepository studentRepository;

    @Mocked
    private PsychologistRepository psychologistRepository;

    @Mocked
    private ParentRepository parentRepository;

    @Mocked
    private AppointmentRepository appointmentRepository;

    @Mocked
    private SurveyResultRepository surveyResultRepository;

    @Mocked
    private SurveyRepository surveyRepository;

    @Mocked
    private AnswersRepository answersRepository;


    @Bean
    public UserService userService() {
        return new UserService(
            studentRepository,
            surveyResultRepository,
            surveyRepository,
            answersRepository,
            userRepository,
            psychologistRepository,
            parentRepository,
            appointmentRepository
        );
    }
} 