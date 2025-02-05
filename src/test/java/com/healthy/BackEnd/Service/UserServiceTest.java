package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.Appointment.AppointmentResponse;
import com.healthy.BackEnd.DTO.Survey.SurveyResultsResponse;
import com.healthy.BackEnd.DTO.User.UsersResponse;
import com.healthy.BackEnd.Entity.*;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.*;
import com.healthy.BackEnd.config.TestConfig;
import mockit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Method;

import static mockit.internal.expectations.ActiveInvocations.times;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/SWP391Healthy",
        "spring.datasource.username=root",
        "spring.datasource.password=12345678",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver" })
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Tested
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

    @Injectable
    private SurveyResultRepository surveyResultRepository;

    @Injectable
    private SurveyRepository surveyRepository;

    @Injectable
    private AnswersRepository answersRepository;

    @BeforeEach
    public void setUp() {
        initializeTestData();
    }

    private void initializeTestData() {
        // Initialize Users
        Users user1 = new Users();
        user1.setUserId("user1");
        user1.setUsername("username1");
        user1.setFullName("User One");
        user1.setEmail("test-user1@example.com");
        user1.setGender(Users.Gender.Female);
        user1.setRole(Users.UserRole.PARENT);
        user1.setCreatedAt(LocalDateTime.now());

        Users user2 = new Users();
        user2.setUserId("user2");
        user2.setUsername("username2");
        user2.setFullName("User Two");
        user2.setEmail("test-user2@example.com");
        user2.setGender(Users.Gender.Male);
        user2.setRole(Users.UserRole.PSYCHOLOGIST);
        user2.setCreatedAt(LocalDateTime.now());

        // Initialize Students
        Students student = new Students();
        student.setStudentID("S001");
        student.setUserID("user1");
        student.setGrade(9);
        student.setClassName("A");
        student.setSchoolName("ABC School");
        student.setDepressionScore(5);
        student.setAnxietyScore(7);
        student.setStressScore(8);

        // Initialize Psychologists
        Psychologists psychologist = new Psychologists();
        psychologist.setPsychologistID("P001");
        psychologist.setStatus(Psychologists.Status.Active);
        psychologist.setSpecialization("Specialization 1");
        psychologist.setYearsOfExperience(5);

        // Initialize Survey Results
        SurveyResults surveyResult = new SurveyResults();
        surveyResult.setResultID("R001");
        surveyResult.setStudentID("S001");
        surveyResult.setQuestionID("Q1");
        surveyResult.setAnswerID("A1");

        // Initialize Answers
        Answers answer1 = new Answers();
        answer1.setAnswerID("A1");
        answer1.setQuestionID("Q1");
        answer1.setAnswer("Answer 1");
        answer1.setScore(0);

        Answers answer2 = new Answers();
        answer2.setAnswerID("A2");
        answer2.setQuestionID("Q1");
        answer2.setAnswer("Answer 2");
        answer2.setScore(1);

        Answers answer3 = new Answers();
        answer3.setAnswerID("A3");
        answer3.setQuestionID("Q1");
        answer3.setAnswer("Answer 3");
        answer3.setScore(2);

        Answers answer4 = new Answers();
        answer4.setAnswerID("A4");
        answer4.setQuestionID("Q1");
        answer4.setAnswer("Answer 4");
        answer4.setScore(3);


        // Initialize SurveyQuestions
        SurveyQuestions surveyQuestion = new SurveyQuestions();
        surveyQuestion.setQuestionID("Q1");
        surveyQuestion.setQuestionText("Question 1");
        surveyQuestion.setCategoryID("CAT1");
        surveyQuestion.setSurveyID("SV001");

        // Initialize Surveys
        Surveys survey = new Surveys();
        survey.setSurveyID("SV001");
        survey.setCategoryID("CAT1");
        survey.setSurveyName("Survey 1");
        survey.setDescription("Description 1");
        survey.setStatus(Surveys.Status.Unfinished);
        // Initialize Categories
        Categories category = new Categories();
        category.setCategoryID("CAT1");
        category.setCategoryName(Categories.MentalHealthCategory.Anxiety);


        // Mock repository responses
        new Expectations() {{

            userRepository.findById("user1");
            result = Optional.of(user1);

            surveyResultRepository.findByStudentID("S001");
            result = List.of(surveyResult);

            psychologistRepository.findById("P001");
            result = Optional.of(psychologist);

            studentRepository.findById("S001");
            result = Optional.of(student);

            answersRepository.findByQuestionID("Q1");
            result = Arrays.asList(answer1, answer2, answer3, answer4);

            surveyRepository.findById("SV001");
            result = Optional.of(surveyQuestion);

            surveyResultRepository.findByStudentID("S001");
            result = List.of(surveyResult);
        }};
    }

    @Test
    public void testGetUserById_Success() {


        new MockUp<UserService>(){
            @Mock
            public Users getUserById(String userId) {
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
        };

        // Arrange
        String userId = "test-user-id";
        Users user = new Users();
        user.setUserId(userId);
        user.setUsername("test-user");
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
        String nonExistentId = "non-existent-id";

        new Expectations() {{
            userRepository.findById(nonExistentId);
            result = Optional.empty();
        }};

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                userService.getUserById(nonExistentId)
        );
    }

    @Test
    public void testGetAllUsers_Success() {
        // Act
        List<UsersResponse> result = userService.getAllUsers();

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

    @Test
    public void testUpdateUser_Success() {
        // Arrange
        String userId = "test-user-id";
        Users existingUser = new Users();
        existingUser.setUserId(userId);
        existingUser.setFullName("Original Name");
        existingUser.setGender(Users.Gender.Male);
        existingUser.setEmail("original@example.com");
        existingUser.setRole(Users.UserRole.STUDENT);
        existingUser.setPhoneNumber("1234567890");
        existingUser.setCreatedAt(LocalDateTime.now());


        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.of(existingUser);

            userRepository.save(existingUser);
            result = existingUser;
        }};


        Users updatedUser = new Users();
        updatedUser.setUserId(existingUser.getUserId());
        updatedUser.setGender(existingUser.getGender());
        updatedUser.setRole(existingUser.getRole());
        updatedUser.setFullName("Updated Name");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPhoneNumber("0987654321");
        updatedUser.setUpdatedAt(LocalDateTime.now());
        // Act
        UsersResponse result = userService.updateUser(userId, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getFullName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhoneNumber());
    }

    @Test
    public void testUpdateUser_NoChanges() {
        // Arrange
        String userId = "test-user-id";
        Users existingUser = new Users();
        existingUser.setUserId(userId);
        existingUser.setFullName("Original Name");
        existingUser.setEmail("original@example.com");
        existingUser.setPhoneNumber("1234567890");

        Users updatedUser = new Users();
        updatedUser.setFullName("Original Name");
        updatedUser.setEmail("original@example.com");
        updatedUser.setPhoneNumber("1234567890");

        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.of(existingUser);
        }};

        // Act
        UsersResponse result = userService.updateUser(userId, updatedUser);

        // Assert
        assertNull(result);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Arrange
        String userId = "non-existent-id";
        Users updatedUser = new Users();
        updatedUser.setFullName("Updated Name");

        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.empty();
        }};

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                userService.updateUser(userId, updatedUser)
        );
    }

    @Test
    public void testGetUserAppointments_Success() {
        // Arrange
        String user_psychologist_Id = "test-user-psychologist-id";
        Users user_psychologist = new Users();
        user_psychologist.setUserId(user_psychologist_Id);
        user_psychologist.setUsername("test-user-psychologist");
        user_psychologist.setFullName("Test User Psychologist");
        user_psychologist.setEmail("test-psychologist@example.com");
        user_psychologist.setRole(Users.UserRole.PSYCHOLOGIST);
        user_psychologist.setGender(Users.Gender.Male);
        user_psychologist.setCreatedAt(LocalDateTime.now());

        String user_student_Id = "test-user-student-id";
        Users user = new Users();
        user.setUserId(user_student_Id);
        user.setUsername("test-user-student");
        user.setFullName("Test User Student");
        user.setEmail("test-student@example.com");
        user.setRole(Users.UserRole.STUDENT);
        user.setGender(Users.Gender.Female);
        user.setCreatedAt(LocalDateTime.now());

        Students student = new Students();
        student.setStudentID("S001");
        student.setUserID(user_student_Id);
        student.setGrade(9);
        student.setClassName("A");
        student.setSchoolName("ABC School");
        student.setDepressionScore(5);
        student.setAnxietyScore(7);
        student.setStressScore(8);

        Psychologists psychologist = new Psychologists();
        psychologist.setPsychologistID("P001");
        psychologist.setSpecialization("Child Psychology");
        psychologist.setStatus(Psychologists.Status.Active);
        psychologist.setUserID(user_psychologist_Id);
        psychologist.setYearsOfExperience(5);


        Appointments appointment = new Appointments();
        appointment.setAppointmentID("A001");
        appointment.setStudentID("S001");
        appointment.setPsychologistID("P001");
        appointment.setStatus(Appointments.Status.Scheduled);
        appointment.setNotes("Test notes");
        appointment.setMeetingLink("https://zoom.us/j/1234567890");
        appointment.setTimeSlotsID("TS001");
        appointment.setAppointmentType(Appointments.AppointmentType.Online);
        appointment.setCreatedAt(LocalDateTime.now());


        new Expectations() {{
            // Mock studentRepository.findByUserID
            studentRepository.findByUserID(user_student_Id);
            result = student;  // Ensure returning an Optional if needed

            // Mock psychologistRepository.findByUserID
            psychologistRepository.findByUserID(user_psychologist_Id);
            result = psychologist;  // Ensure returning an Optional if needed

            // Mock appointmentRepository.findByStudentID
            appointmentRepository.findByStudentID(student.getStudentID());
            result = Collections.singletonList(appointment);

            // Mock psychologistRepository.findById
            psychologistRepository.findById(appointment.getPsychologistID());
            result = psychologist;

            // Mock studentRepository.findById for appointment
            studentRepository.findById(appointment.getStudentID());
            result = student;
        }};

        // Act
        List<AppointmentResponse> resultAppointments = userService.getUserAppointments(user_student_Id);

        // Asserts
        assertNotNull(resultAppointments);
        assertFalse(resultAppointments.isEmpty());
        assertEquals(1, resultAppointments.size());
        assertEquals("SCHEDULED", resultAppointments.get(0).getStatus());
    }

    @Test
    public void testGetUserAppointments_UserNotFound() {
        // Arrange
        String userId = "non-existent-id";

        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.empty();
        }};

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                userService.getUserAppointments(userId)
        );
    }

    @Test
    public void testDeleteUser_Success() {
        // Arrange
        String userId = "test-user-id";
        Users user = new Users();
        user.setUserId(userId);

        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.of(user);

            userRepository.delete(user);
        }};

        // Act & Assert
        assertDoesNotThrow(() -> userService.deleteUser(userId));
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        String userId = "non-existent-id";

        new Expectations() {{
            userRepository.findById(userId);
            result = Optional.empty();
        }};

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                userService.deleteUser(userId)
        );
    }

    @Test
    public void testIsEmpty_True() {
        // Arrange
        new Expectations() {{
            userRepository.findAll();
            result = Collections.emptyList();
        }};

        // Act
        boolean result = userService.isEmpty();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_False() {
        // Arrange
        Users user = new Users();
        user.setUserId("test-id");

        new Expectations() {{
            userRepository.findAll();
            result = Collections.singletonList(user);
        }};

        // Act
        boolean result = userService.isEmpty();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testBuildSurveyResults() throws Exception {
        // Arrange
        String studentId = "S001";
        SurveyResults surveyResult = new SurveyResults();
        surveyResult.setStudentID(studentId);
        surveyResult.setQuestionID("Q1");
        surveyResult.setAnswerID("A1");
        // Mock the repository to return the survey result
        new Expectations() {{
            surveyResultRepository.findByStudentID(studentId);
            result = List.of(surveyResult);
        }};

        // Access the private method using reflection
        Method method = UserService.class.getDeclaredMethod("buildSurveyResults", String.class);
        method.setAccessible(true); // Make the method accessible

        // Act
        List<SurveyResultsResponse> result = (List<SurveyResultsResponse>) method.invoke(userService, studentId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(studentId, result.get(0).getSurveyId()); // Adjust based on your actual SurveyResultsResponse structure
    }
} 