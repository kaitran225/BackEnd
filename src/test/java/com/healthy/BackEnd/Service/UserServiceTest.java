package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.Entity.*;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.*;
import com.healthy.BackEnd.config.TestConfig;
import mockit.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver"
})
public class UserServiceTest {

    @Tested(fullyInitialized = true)
    private UserService userService;

    @Injectable
    private UserRepository userRepository;

    @Injectable
    private PsychologistRepository psychologistRepository;

    @Injectable
    private StudentRepository studentRepository;

    @Injectable
    private ParentRepository parentRepository;

    @Injectable
    private AppointmentRepository appointmentRepository;

    @Test
    public void testGetUserById_Success() {
        // Arrange
        String userId = "test-user-id";
        Users user = new Users();
        user.setUserId(userId);
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setRole(Users.UserRole.STUDENT);
        user.setGender(Users.Gender.Male);
        user.setCreatedAt(LocalDateTime.now());

        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.of(user);
        }};

        // Act
        var result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("Test User", result.getFullName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testGetUserById_NotFound() {
        // Arrange
        String userId = "non-existent-id";

        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.empty();
        }};

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    public void testGetAllUsers_Success() {
        // Arrange
        Users user1 = new Users();
        user1.setUserId("user1");
        user1.setFullName("User One");
        Users user2 = new Users();
        user2.setUserId("user2");
        user2.setFullName("User Two");

        new Expectations() {{
            userRepository.findAll();
            result = Arrays.asList(user1, user2);
            userRepository.findAllUsers();
            result = Arrays.asList(user1, user2);
        }};

        // Act
        var result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllUsers_Empty() {
        // Arrange
        new Expectations() {{
            userRepository.findAll();
            result = Collections.emptyList();
        }};

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
    }
} 