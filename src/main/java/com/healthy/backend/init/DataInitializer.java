package com.healthy.backend.init;

import com.healthy.backend.entity.*;
import com.healthy.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Configuration
public class DataInitializer {



    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            PsychologistRepository psychologistRepository,
            ParentRepository parentRepository,
            StudentRepository studentRepository,
            CategoriesRepository categoryRepository,
            SurveyRepository surveyRepository,
            SurveyQuestionRepository surveyQuestionRepository,
            AnswersRepository answerRepository,
            TagsRepository tagRepository,
            ProgramRepository programRepository,
            TimeSlotRepository timeSlotRepository,
            AppointmentRepository appointmentRepository,
            ProgramParticipationRepository participationRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Kiểm tra nếu đã có dữ liệu trong bảng Users
            if (userRepository.count() > 0) {
                System.out.println("Dữ liệu đã được khởi tạo trước đó. Bỏ qua DataInitializer.");
                return; // Thoát khỏi phương thức nếu đã có dữ liệu
            }

            // Tạo Users
            Users managerUser = createUser(
                    "manager-1",
                    "manager_user",
                    passwordEncoder.encode("password"),
                    "Manager Full Name",
                    "manager@example.com",
                    "0987654321",
                    Users.UserRole.MANAGER,
                    Users.Gender.Male
            );

            Users psychologistUser = createUser(
                    "psychologist-1",
                    "psychologist_user",
                    passwordEncoder.encode("password"),
                    "Dr. Jane Doe",
                    "psychologist@example.com",
                    "0912345678",
                    Users.UserRole.PSYCHOLOGIST,
                    Users.Gender.Female
            );

            Users parentUser = createUser(
                    "parent-1",
                    "parent_user",
                    passwordEncoder.encode("password"),
                    "Parent Full Name",
                    "parent@example.com",
                    "0905123456",
                    Users.UserRole.PARENT,
                    Users.Gender.Other
            );

            Users studentUser = createUser(
                    "student-1",
                    "student_user",
                    passwordEncoder.encode("password"),
                    "Student Name",
                    "student@example.com",
                    "0905123457",
                    Users.UserRole.STUDENT,
                    Users.Gender.Male
            );

            saveUsers(userRepository, managerUser, psychologistUser, parentUser, studentUser);

            // Tạo Psychologist
            Psychologists psychologist = new Psychologists(
                    "psychologist-1",
                    psychologistUser.getUserId(),
                    "Child Psychology",
                    5,
                    Psychologists.Status.Active
            );
            psychologistRepository.save(psychologist);

            // Tạo Parent
            Parents parent = new Parents("parent-1", parentUser.getUserId());
            parentRepository.save(parent);

            // Tạo Student
            Students student = new Students(
                    "student-1",
                    studentUser.getUserId(),
                    parent.getParentID(),
                    10,
                    "Class A",
                    "Greenfield School",
                    15,  // anxietyScore
                    20,  // stressScore
                    18   // depressionScore
            );
            studentRepository.save(student);

            // Tạo Categories
            Categories stressCategory = new Categories("category-1", Categories.MentalHealthCategory.Stress);
            Categories anxietyCategory = new Categories("category-2", Categories.MentalHealthCategory.Anxiety);
            Categories depressionCategory = new Categories("category-3", Categories.MentalHealthCategory.Depression);
            categoryRepository.saveAll(List.of(stressCategory, anxietyCategory, depressionCategory));

            // Tạo Survey và Questions
            Surveys survey = new Surveys(
                    "survey-1",
                    "Stress Assessment",
                    "Stress level evaluation survey",
                    stressCategory.getCategoryID(),
                    managerUser.getUserId(),
                    Surveys.Status.Finished
            );
            surveyRepository.save(survey);

            SurveyQuestions question1 = new SurveyQuestions(
                    "question-1",
                    survey.getSurveyID(),
                    "How often do you feel stressed?",
                    stressCategory.getCategoryID()
            );
            surveyQuestionRepository.save(question1);

            // Tạo Answers
            List<Answers> answers = List.of(
                    new Answers("answer-1", question1.getQuestionID(), "Never", 0),
                    new Answers("answer-2", question1.getQuestionID(), "Sometimes", 1),
                    new Answers("answer-3", question1.getQuestionID(), "Often", 2)
            );
            answerRepository.saveAll(answers);

            // Tạo Tags
            Tags stressTag = new Tags("tag-1", Tags.Tag.Stress_Management);
            Tags wellnessTag = new Tags("tag-2", Tags.Tag.Wellness);
            tagRepository.saveAll(List.of(stressTag, wellnessTag));

            // Tạo Program
            Programs program = new Programs(
                    "program-1",
                    "Stress Management Program",
                    Programs.Category.Cognitive,
                    "Program to help manage stress",
                    50,
                    8,
                    Programs.Status.Active,
                    managerUser.getUserId(),
                    new HashSet<>(List.of(stressTag, wellnessTag)),
                    LocalDate.now().plusDays(7),
                    "https://meet.example.com/123",
                    Programs.Type.Online
            );
            programRepository.save(program);

            // Tạo TimeSlots
            TimeSlots timeSlot = new TimeSlots(
                    LocalDate.now().plusDays(1),
                    LocalTime.of(9, 0),
                    LocalTime.of(10, 0),
                    psychologist,  // Truyền trực tiếp đối tượng psychologist thay vì ID
                    1
            );
            timeSlotRepository.save(timeSlot);

//            // Tạo Appointment
//            Appointments appointment = new Appointments(
//                    "appointment-1",
//                    timeSlot.getTimeSlotsID(),
//                    student.getStudentID(),
//                    psychologist.getPsychologistID(),
//                    Appointments.Status.Scheduled
//            );
//            appointmentRepository.save(appointment);

            // Tạo Program Participation
            ProgramParticipation participation = new ProgramParticipation(
                    "participation-1",
                    student.getStudentID(),
                    program.getProgramID(),
                    ProgramParticipation.Status.Joined,
                    LocalDate.now()
            );
            participationRepository.save(participation);
        };
    }

    private Users createUser(String userId, String username, String password,
                             String fullName, String email, String phone,
                             Users.UserRole role, Users.Gender gender) {
        return Users.builder()
                .userId(userId)
                .username(username)
                .passwordHash(password)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phone)
                .role(role)
                .gender(gender)
                .build();
    }

    private void saveUsers(UserRepository repository, Users... users) {
        Arrays.stream(users).forEach(user -> {
            if (!repository.existsById(user.getUserId())) {
                repository.save(user);
            }
        });
    }
}
