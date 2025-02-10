package com.healthy.backend.service;


import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.UserRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PsychologistServiceTest {

    @Tested
    private PsychologistService psychologistService;

    @Injectable
    private PsychologistRepository psychologistRepository;

    @Injectable
    private AppointmentRepository appointmentRepository;

    @Injectable
    private UserRepository userRepository;

    @Injectable
    private AuthenticationService authenticationService;

    @Test
    public void testGetPsychologistById_Success() {

        String psychologistId = "PSY0003";

        authenticationService.register(
                new RegisterRequest(
                        "Testing",
                        "testing123",
                        "Test User",
                        "testing@example",
                        "1234567890",
                        Users.UserRole.PSYCHOLOGIST.toString(),
                        Users.Gender.Male.toString()));

        psychologistRepository.save(
                new Psychologists(psychologistId,
                        userRepository.findByUsername("Testing").getUserId(),
                        "Adolescent Psychology",
                        8,
                        Psychologists.Status.Active));

        Users user = new Users();

        Psychologists psychologist = new Psychologists();

        new Expectations() {{
            psychologistRepository.findById(psychologistId);
            result = Optional.of(psychologist);

            userRepository.findById(anyString);
            result = Optional.of(user);

            appointmentRepository.findByPsychologistID(psychologistId);
            result = Collections.emptyList();
        }};

        // Act
        var result = psychologistService.getPsychologistById(psychologistId);

        // Assert
        assertNotNull(result);
        assertEquals(psychologistId, result.getPsychologistId());
        assertEquals("Child Psychology", result.getSpecialization());
        assertEquals(5, result.getYearsOfExperience());
        assertEquals("Active", result.getStatus());
    }

    @Test
    public void testGetPsychologistById_NotFound() {
        // Arrange
        String psychologistId = "not-valid-id";

        new Expectations() {{
            psychologistRepository.findById(psychologistId);
            result = Optional.empty();
        }};

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> psychologistService.getPsychologistById(psychologistId));
    }

//    @Test
//    public void testGetAllPsychologistDTO_Success() {
//        // Arrange
//        Psychologists psych1 = new Psychologists();
//        psych1.setPsychologistID("PSY001");
//        psych1.setSpecialization("Child Psychology");
//
//        Psychologists psych2 = new Psychologists();
//        psych2.setPsychologistID("bc1b88b0");
//        psych2.setSpecialization("Family Therapy");
//
//        Users user = new Users();
//        user.setUserId("user-id");
//        user.setFullName("Dr. Smith");
//
//        new Expectations() {{
//            psychologistRepository.findAll();
//            result = Arrays.asList(psych1, psych2);
//
//            userRepository.findById(anyString);
//            result = Optional.of(user);
//
//            appointmentRepository.findByPsychologistID(anyString);
//            result = Collections.emptyList();
//        }};
//
//        // Act
//        var result = psychologistService.getAllPsychologistDTO();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }
} 