package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.User.UsersResponse;
import java.time.format.DateTimeFormatter;

import com.healthy.BackEnd.Entity.Users;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Init.DataInitializer;
import com.healthy.BackEnd.Repository.*;
import com.healthy.BackEnd.config.TestConfig;
import mockit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mockit.internal.expectations.ActiveInvocations.times;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/SWP391Healthy",
        "spring.datasource.username=root",
        "spring.datasource.password=12345678"})
@ExtendWith(MockitoExtension.class)
public class JmockitDemoTest {


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

    @Injectable
    private SurveyResultRepository surveyResultRepository;

    @Injectable
    private SurveyRepository surveyRepository;

    @Injectable
    private AnswersRepository answersRepository;

    private Users user1(){
        Users user1 = new Users();
        user1.setUserId("user1");
        user1.setUsername("username1");
        user1.setFullName("User One");
        user1.setEmail("test-user1@example.com");
        user1.setGender(Users.Gender.Female);
        user1.setRole(Users.UserRole.PARENT);
        user1.setCreatedAt(LocalDateTime.now());
        return user1;
    }
    private Users user2(){
        Users user2 = new Users();
        user2.setUserId("user2");
        user2.setUsername("username2");
        user2.setFullName("User Two");
        user2.setEmail("test-user2@example.com");
        user2.setGender(Users.Gender.Male);
        user2.setRole(Users.UserRole.PSYCHOLOGIST);
        user2.setCreatedAt(LocalDateTime.now());
        return user2;
    }
    private Users user(){
        String userId = "test-user-id";
        Users user = new Users();
        user.setUserId(userId);
        user.setUsername("test-user");
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setRole(Users.UserRole.STUDENT);
        user.setGender(Users.Gender.Male);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
    private void show(UsersResponse usersResponse) {
        // Header
        System.out.println("=============================================");
        System.out.println("        User Information Overview           ");
        System.out.println("=============================================");

        // User ID
        System.out.printf(String.format("User ID    : %s", usersResponse.getUserId()));
        System.out.println();

        // Full Name
        System.out.printf(String.format("Full Name  : %s", usersResponse.getFullName()));
        System.out.println();

        // Email
        System.out.printf(String.format("Email      : %s", usersResponse.getEmail()));
        System.out.println();

        // Gender
        System.out.printf(String.format("Gender     : %s", usersResponse.getGender()));
        System.out.println();

        // Role
        System.out.printf(String.format("Role       : %s", usersResponse.getRole()));
        System.out.println();

        // Date Created (with nice formatting)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = usersResponse.getCreatedAt().format(formatter);
        System.out.printf(String.format("Created At : %s", formattedDate));
        System.out.println();
        // Footer with a little visual enhancement
        System.out.println("=============================================");
        System.out.println("           End of User Information          ");
        System.out.println("=============================================");
    }

    @Tested
    private UserService userService;

    @Test
    public void testGetAllUsers_Success() {
        // Arrange
        Users user1 = this.user1();
        Users user2 = this.user2();

        new Expectations() {{
            userRepository.findAll();
            result = Arrays.asList(user1, user2);
            System.out.println("Expectation set for userRepository.findAll() to return a list of 2 users.");

            userRepository.findAllUsers();
            result = Arrays.asList(user1, user2);
            System.out.println("Expectation set for userRepository.findAllUsers() to return a list of 2 users.");
        }};

        // Act
        List<UsersResponse> result = userService.getAllUsers();

        // Assert
        assertNotNull(result, "The result should not be null.");
        System.out.println("Test Get All Users Success: Result is not null.");

        assertEquals(2, result.size(), "The size of the result list should be 2.");
        System.out.println("Test Get All Users Success: Result contains 2 users.");

        // Assert that both users are present in the result
        assertTrue(result.stream().anyMatch(user -> user.getUserId().equals(user1.getUserId())), "User 1 should be present in the result.");
        assertTrue(result.stream().anyMatch(user -> user.getUserId().equals(user2.getUserId())), "User 2 should be present in the result.");
        System.out.println("Test Get All Users Success: Both users are present in the result.");

        // Assert that the values for the first user match
        UsersResponse firstUserResponse = result.get(0);
        assertEquals(user1.getUserId(), firstUserResponse.getUserId(), "User 1 ID mismatch.");
        assertEquals(user1.getFullName(), firstUserResponse.getFullName(), "User 1 Full Name mismatch.");
        assertEquals(user1.getEmail() , firstUserResponse.getEmail(), "User 1 Email mismatch.");
        assertEquals(user1.getGender().toString(), firstUserResponse.getGender(), "User 1 Gender mismatch.");
        assertEquals(user1.getRole().toString(), firstUserResponse.getRole(), "User 1 Role mismatch.");
        System.out.println("Test Get All Users Success: User 1 details match.");

        // Assert that the values for the second user match
        UsersResponse secondUserResponse = result.get(1);
        assertEquals(user2.getUserId(), secondUserResponse.getUserId(), "User 2 ID mismatch.");
        assertEquals(user2.getFullName() , secondUserResponse.getFullName(), "User 2 Full Name mismatch.");
        assertEquals(user2.getEmail(), secondUserResponse.getEmail(), "User 2 Email mismatch.");
        assertEquals(user2.getGender().toString(), secondUserResponse.getGender(), "User 2 Gender mismatch.");
        assertEquals(user2.getRole().toString(), secondUserResponse.getRole(), "User 2 Role mismatch.");
        System.out.println("Test Get All Users Success: User 2 details match.");

        // Assert that all fields for each user are non-null
        assertNotNull(firstUserResponse.getUserId(), "User 1 ID should not be null.");
        assertNotNull(firstUserResponse.getFullName(), "User 1 Full Name should not be null.");
        assertNotNull(firstUserResponse.getEmail(), "User 1 Email should not be null.");
        assertNotNull(firstUserResponse.getGender(), "User 1 Gender should not be null.");
        assertNotNull(firstUserResponse.getRole(), "User 1 Role should not be null.");
        assertNotNull(firstUserResponse.getCreatedAt(), "User 1 Created At should not be null.");

        assertNotNull(secondUserResponse.getUserId(), "User 2 ID should not be null.");
        assertNotNull(secondUserResponse.getFullName(), "User 2 Full Name should not be null.");
        assertNotNull(secondUserResponse.getEmail(), "User 2 Email should not be null.");
        assertNotNull(secondUserResponse.getGender(), "User 2 Gender should not be null.");
        assertNotNull(secondUserResponse.getRole(), "User 2 Role should not be null.");
        assertNotNull(secondUserResponse.getCreatedAt(), "User 2 Created At should not be null.");
        System.out.println("Test Get All Users Success: All user fields are non-null.");

        // Verify that the repository methods were called the expected number of times
        new Verifications() {{
            userRepository.findAll(); times(1);
            System.out.println("Verification: userRepository.findAll() was called once.");

            userRepository.findAllUsers(); times(1);
            System.out.println("Verification: userRepository.findAllUsers() was called once.");
        }};
        for (UsersResponse user : result
             ) {show(user);
        }
    }


    @Test
    public void testGetUserById_Success() {
        // Arrange
        Users user = this.user();
        String userId = user.getUserId();

        new MockUp<UserService>() {
            @Mock
            public UsersResponse getUserById(String id) {
                Users user = userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
                UsersResponse userResponse = new UsersResponse();
                userResponse.setCreatedAt(user.getCreatedAt());
                userResponse.setUserId(user.getUserId() + "   @Mock");
                userResponse.setFullName(user.getFullName() + "   @Mock");
                userResponse.setEmail(user.getEmail() + "   @Mock");
                userResponse.setGender(user.getGender().toString() + "   @Mock");
                userResponse.setRole(user.getRole().toString());
                return userResponse;
            }
        };

        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.of(user);
            System.out.println("Expectation set for userRepository.findById() to return the mock user.");
        }};

        // Act
        UsersResponse result = this.userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        System.out.println("Test Get User By Id Success: Result is not null.");

        // Assert that the userId matches and has the "@Mock" suffix
        assertEquals(userId + "   @Mock", result.getUserId());
        System.out.println("Test Get User By Id Success: User ID matches and has '@Mock' suffix.");

        // Assert that the full name matches and has the "@Mock" suffix
        assertEquals(user.getFullName() + "   @Mock", result.getFullName());
        System.out.println("Test Get User By Id Success: Full Name matches and has '@Mock' suffix.");

        // Assert that the email matches and has the "@Mock" suffix
        assertEquals(user.getEmail() + "   @Mock", result.getEmail());
        System.out.println("Test Get User By Id Success: Email matches and has '@Mock' suffix.");

        // Assert that the gender matches and has the "@Mock" suffix
        assertEquals(user.getGender().toString() + "   @Mock", result.getGender());
        System.out.println("Test Get User By Id Success: Gender matches and has '@Mock' suffix.");

        // Assert that the role matches
        assertEquals(user.getRole().toString(), result.getRole());
        System.out.println("Test Get User By Id Success: Role matches.");

        // Assert that the created date is not null
        assertNotNull(result.getCreatedAt());
        System.out.println("Test Get User By Id Success: Created Date is not null.");

        new Verifications() {{
            userRepository.findById(userId); times(1);
            System.out.println("Verification: userRepository.findById() was called once with userId: " + userId);
        }};
        show(result);
    }
}