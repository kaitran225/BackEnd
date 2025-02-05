package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.Entity.*;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.*;
import mockit.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
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

    @Test
    public void testGetPsychologistById_Success() {
        // Arrange
        String psychologistId = "test-psych-id";
        Psychologists psychologist = new Psychologists();
        psychologist.setPsychologistID(psychologistId);
        psychologist.setSpecialization("Child Psychology");
        psychologist.setYearsOfExperience(5);
        psychologist.setStatus(Psychologists.Status.Active);

        Users user = new Users();
        user.setUserId("user-id");
        user.setFullName("Dr. Smith");

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
        String psychologistId = "non-existent-id";

        new Expectations() {{
            psychologistRepository.findById(psychologistId);
            result = Optional.empty();
        }};

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> psychologistService.getPsychologistById(psychologistId));
    }

    @Test
    public void testGetAllPsychologistDTO_Success() {
        // Arrange
        Psychologists psych1 = new Psychologists();
        psych1.setPsychologistID("psych1");
        psych1.setSpecialization("Child Psychology");
        
        Psychologists psych2 = new Psychologists();
        psych2.setPsychologistID("psych2");
        psych2.setSpecialization("Family Therapy");

        Users user = new Users();
        user.setUserId("user-id");
        user.setFullName("Dr. Smith");

        new Expectations() {{
            psychologistRepository.findAll();
            result = Arrays.asList(psych1, psych2);

            userRepository.findById(anyString);
            result = Optional.of(user);

            appointmentRepository.findByPsychologistID(anyString);
            result = Collections.emptyList();
        }};

        // Act
        var result = psychologistService.getAllPsychologistDTO();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
} 