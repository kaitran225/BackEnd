package com.healthy.backend.service;

import com.healthy.backend.config.TestConfig;
import com.healthy.backend.dto.user.UsersResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.*;
import mockit.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        System.out.printf(String.format("User ID    : %s", usersResponse.getUserId()));
        System.out.println();

        System.out.printf(String.format("Full Name  : %s", usersResponse.getFullName()));
        System.out.println();

        System.out.printf(String.format("Email      : %s", usersResponse.getEmail()));
        System.out.println();

        System.out.printf(String.format("Gender     : %s", usersResponse.getGender()));
        System.out.println();

        System.out.printf(String.format("Role       : %s", usersResponse.getRole()));
        System.out.println();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = usersResponse.getCreatedAt().format(formatter);
        System.out.printf(String.format("Created At : %s", formattedDate));
        System.out.println();

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
            result = Arrays.asList(user1, user2); System.out.println("Expectation set for userRepository.findAll() to return a list of 2 users.");

            userRepository.findAllUsers();
            result = Arrays.asList(user1, user2); System.out.println("Expectation set for userRepository.findAllUsers() to return a list of 2 users.");
        }};

        List<UsersResponse> result = userService.getAllUsers();

        assertNotNull(result, "The result should not be null."); System.out.println("Test Get All Users Success: Result is not null.");
        assertEquals(2, result.size(), "The size of the result list should be 2.");System.out.println("Test Get All Users Success: Result contains 2 users.");

        UsersResponse firstUserResponse = result.getFirst();
        assertEquals(user1.getUserId(), firstUserResponse.getUserId(), "User 1 ID mismatch."); System.out.println("Test Get All Users Success: User 1 details match.");
        UsersResponse secondUserResponse = result.getLast();
        assertEquals(user2.getUserId(), secondUserResponse.getUserId(), "User 2 ID mismatch."); System.out.println("Test Get All Users Success: User 2 details match.");

        new Verifications() {{
            userRepository.findAll();
            System.out.println("Verification: userRepository.findAll() was called once.");
            userRepository.findAllUsers();
            System.out.println("Verification: userRepository.findAllUsers() was called once.");

            times = 1;
        }};
        for (UsersResponse user : result
             ) {show(user);
        }
    }

    @Test
    public void testGetAllUsers_Failure() {
        // Arrange
        new Expectations() {{
            userRepository.findAll();
            result = new ArrayList<>(); // Simulating empty result
            System.out.println("Expectation set for userRepository.findAll() to return an empty list.");
        }};

        // Act & Assert: Expect ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getAllUsers();
        });

        assertEquals("No users found", exception.getMessage(), "Exception message should match.");
        System.out.println("Test Get All Users Failure: Correct ResourceNotFoundException thrown.");

        new Verifications() {{
            userRepository.findAll();
            times = 1;
            System.out.println("Verification: userRepository.findAll() was called once.");
        }};
    }



    @Test
    public void testGetUserByIdWithMock_Success() {

        // Insert the un-mocked user
        Users user = this.user();
        String userId = user.getUserId();

        new MockUp<UserService>() {
            @Mock
            public UsersResponse getUserById(String id) {
                Users user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
                UsersResponse userResponse = new UsersResponse();
                userResponse.setCreatedAt(user.getCreatedAt());
                userResponse.setUserId(user.getUserId() + "   @Mock"); // Add '@Mock' suffix
                userResponse.setFullName(user.getFullName() + "   @Mock");
                userResponse.setEmail(user.getEmail() + "   @Mock");
                userResponse.setGender(user.getGender().toString() + "   @Mock");
                userResponse.setRole(user.getRole().toString());
                return userResponse;
            }
        };

        // Set up expectations for the mocked method
        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.of(user);
            System.out.println("Expectation set for userRepository.findById() to return the mock user.");
        }};

        UsersResponse result = this.userService.getUserById(userId);

        // A sanity check to ensure the result is not null
        assertNotNull(result);
        System.out.println("Test Get User By Id Success: Result is not null.");

        assertEquals(userId + "   @Mock", result.getUserId());
        System.out.println("Test Get User By Id Success: User ID matches and has '@Mock' suffix.");

        assertEquals(user.getFullName() + "   @Mock", result.getFullName());
        System.out.println("Test Get User By Id Success: Full Name matches and has '@Mock' suffix.");

        assertEquals(user.getEmail() + "   @Mock", result.getEmail());
        System.out.println("Test Get User By Id Success: Email matches and has '@Mock' suffix.");

        assertEquals(user.getGender().toString() + "   @Mock", result.getGender());
        System.out.println("Test Get User By Id Success: Gender matches and has '@Mock' suffix.");

        assertEquals(user.getRole().toString(), result.getRole());
        System.out.println("Test Get User By Id Success: Role matches.");

        assertNotNull(result.getCreatedAt());
        System.out.println("Test Get User By Id Success: Created Date is not null.");

        // Verification for the mocked method
        new Verifications() {{
            userRepository.findById(userId); times = 1;
            System.out.println("Verification: userRepository.findById() was called once with userId: " + userId);
        }};
        show(result);
    }


    @Test
    public void testGetUserByIdWithoutMock_Success() {

        // Insert the un-mocked user
        Users user = this.user();
        String userId = user.getUserId();

        new MockUp<UserService>() {
            @Mock
            public UsersResponse getUserById(String id) {
                Users user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
                UsersResponse userResponse = new UsersResponse();
                userResponse.setCreatedAt(user.getCreatedAt());
                userResponse.setUserId(user.getUserId() + "   @Mock"); // Add '@Mock' suffix
                userResponse.setFullName(user.getFullName() + "   @Mock");
                userResponse.setEmail(user.getEmail() + "   @Mock");
                userResponse.setGender(user.getGender().toString() + "   @Mock");
                userResponse.setRole(user.getRole().toString());
                return userResponse;
            }
        };

        // Set up expectations for the mocked method
        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.of(user);
            System.out.println("Expectation set for userRepository.findById() to return the mock user.");
        }};

        UsersResponse result = this.userService.getUserById(userId);

        // A sanity check to ensure the result is not null
        assertNotNull(result);
        System.out.println("Test Get User By Id Success: Result is not null.");

        assertEquals(userId + "   @Mock", result.getUserId());
        System.out.println("Test Get User By Id Success: User ID matches and has '@Mock' suffix.");

        assertEquals(user.getFullName() + "   @Mock", result.getFullName());
        System.out.println("Test Get User By Id Success: Full Name matches and has '@Mock' suffix.");

        assertEquals(user.getEmail() + "   @Mock", result.getEmail());
        System.out.println("Test Get User By Id Success: Email matches and has '@Mock' suffix.");

        assertEquals(user.getGender().toString() + "   @Mock", result.getGender());
        System.out.println("Test Get User By Id Success: Gender matches and has '@Mock' suffix.");

        assertEquals(user.getRole().toString(), result.getRole());
        System.out.println("Test Get User By Id Success: Role matches.");

        assertNotNull(result.getCreatedAt());
        System.out.println("Test Get User By Id Success: Created Date is not null.");

        // Verification for the mocked method
        new Verifications() {{
            userRepository.findById(userId); times = 1;
            System.out.println("Verification: userRepository.findById() was called once with userId: " + userId);
        }};
        show(result);
    }
}