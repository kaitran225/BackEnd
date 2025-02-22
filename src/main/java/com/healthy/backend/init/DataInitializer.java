package com.healthy.backend.init;

import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.enums.Gender;
import com.healthy.backend.enums.NotificationType;
import com.healthy.backend.enums.Role;
import com.healthy.backend.repository.*;
import com.healthy.backend.service.AuthenticationService;
import com.healthy.backend.service.GeneralService;
import com.healthy.backend.service.PsychologistService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
@RequiredArgsConstructor

public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TagsRepository tagsRepository;
    private final ProgramRepository programRepository;
    private final SurveyRepository surveyRepository;
    private final UserLogRepository userLogRepository;
    private final ArticleRepository articleRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;
    private final GeneralService __;
    private final AuthenticationService authenticationService;
    private final PsychologistService psychologistService;

    private void initialize() {
        registerUsers();
        initializeParentsAndStudents();
        initializeDepartments();
        initializePsychologists();
        initializeTimeSlots();
        initializeTags();
        initializePrograms();
        initializeSurveys();
        initializeLogs();
        initializeBlogs();
        initializeAppointments();
        initializeNotifications();
    }

    private void registerUsers() {
        List<RegisterRequest> users = List.of(
                new RegisterRequest("admin", "adminpass", "Admin Admin", "admin@example.com", "1111111111", "Street 123, Ho Chi Minh City", Role.MANAGER.toString(), Gender.MALE.toString()),
                new RegisterRequest("staff_member", "staff_pass", "Staff Member", "staff@example.com", "2222222222", "Street 202, Ho Chi Minh City", Role.MANAGER.toString(), Gender.FEMALE.toString()),

                new RegisterRequest("psychologist_user", "psychologist_pass", "Dr. Brown", "psychologist@example.com", "0912345671", "Street 101, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
                new RegisterRequest("psychologist_user2", "psychologist_pass", "Dr. Blue", "psychologist2@example.com", "0912345672", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
                new RegisterRequest("psychologist_user3", "psychologist_pass", "Dr. Grey", "psychologist3@example.com", "0912345673", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
                new RegisterRequest("psychologist_user4", "psychologist_pass", "Dr. Yellow", "psychologist4@example.com", "0912345674", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
                new RegisterRequest("psychologist_user5", "psychologist_pass", "Dr. Green", "psychologist5@example.com", "0912345675", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),


                new RegisterRequest("parent_user", "parent_pass", "Jane Smith", "parent@example.com", "0812345671", "Street 789, Ho Chi Minh City", Role.PARENT.toString(), Gender.FEMALE.toString()),
                new RegisterRequest("parent_user2", "parent_pass", "Bob Johnson", "parent2@example.com", "0812345672", "Street 404, Ho Chi Minh City", Role.PARENT.toString(), Gender.MALE.toString()),

                new RegisterRequest("student_user", "student_pass", "John Doe", "student@example.com", "0512345671", "Street 456, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("student_user2", "student_pass", "John Green", "student2@example.com", "0512345672", "Street 606, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("student_user3", "student_pass", "Alice Jones", "student3@example.com", "0512345673", "Street 303, Ho Chi Minh City", Role.STUDENT.toString(), Gender.FEMALE.toString()),
                );
        users.forEach(authenticationService::register);
    }

    private void initializeParentsAndStudents() {
        List<Parents> parents = List.of(
                new Parents("P001", userRepository.findByUsername("parent_user").getUserId()),
                new Parents("P002", userRepository.findByUsername("parent_user2").getUserId())
        );
        List<Students> students = List.of(
                new Students("S001", userRepository.findByUsername("student_user").getUserId(), parent.getParentID(), 10, "A", "Example High School", 5, 10, 2)
        );
        parents.forEach(parentRepository::save);
        students.forEach(studentRepository::save);
    }

    private void initializeDepartments() {
        departmentRepository.save(new Department("DP01", "Child & Adolescent Psychology"));
    }

    private void initializePsychologists() {
        psychologistRepository.save(new Psychologists("PSY001", userRepository.findByUsername("psychologist_user").getUserId(), 10, Psychologists.Status.Active, "DP01"));
    }

    private void initializeTimeSlots() {
        for (int i = 1; i <= 30; i++) {
            psychologistService.createDefaultTimeSlots(LocalDate.of(2025, 3, i), "PSY001");
        }
    }

    private void initializeTags() {
        List<Tags> tags = Arrays.stream(Tags.Tag.values())
                .map(tag -> new Tags(String.format("TAG%03d", tag.ordinal() + 1), tag))
                .collect(Collectors.toList());
        tagsRepository.saveAll(tags);
    }

    private void initializePrograms() {
        programRepository.save(new Programs("PRG001", "Stress Management", "Manage stress", 20, 4, Programs.Status.Active,
                departmentRepository.findById("DP01").orElseThrow(), psychologistRepository.findById("PSY001").orElseThrow(),
                null, LocalDate.parse("2025-02-23"), "https://example.com/meeting1", Programs.Type.Online));
    }

    private void initializeSurveys() {
        surveyRepository.save(new Surveys("SUR001", "Stress Survey", "Survey to assess stress", "CAT001", userRepository.findByUsername("student_user").getUserId(), Surveys.Status.Active));
    }

    private void initializeLogs() {
        userLogRepository.save(new UserLogs("L001", userRepository.findByUsername("student_user").getUserId(), "192.168.0.1"));
        userLogRepository.save(new UserLogs("L002", userRepository.findByUsername("student_user").getUserId(), "244.178.44.111"));
        userLogRepository.save(new UserLogs("L003", userRepository.findByUsername("student_user").getUserId(), "38.0.101.76"));
        userLogRepository.save(new UserLogs("L004", userRepository.findByUsername("student_user").getUserId(), "89.0.142.86"));
    }

    private void initializeBlogs() {
        articleRepository.save(new Article("ATC001", "Managing Stress", userRepository.findByUsername("psychologist_user").getUserId(), "Tips for managing stress..."));
        articleRepository.save(new Article("ATC002", "Anxiety Management", userRepository.findByUsername("psychologist_user2").getUserId(), "Tips for managing anxiety..."));
        articleRepository.save(new Article("ATC003", "Depression Management", userRepository.findByUsername("psychologist_user").getUserId(), "Tips for managing depression..."));
        articleRepository.save(new Article("ATC004", "Sleep Disorders", userRepository.findByUsername("psychologist_user2").getUserId(), "Tips for managing sleep disorders..."));
        articleRepository.save(new Article("ATC005", "Eating Disorders", userRepository.findByUsername("psychologist_user2").getUserId(), "Tips for managing eating disorders..."));
        articleRepository.save(new Article("ATC006", "Addiction Management", userRepository.findByUsername("psychologist_user").getUserId(), "Tips for managing addiction..."));
        articleRepository.save(new Article("ATC007", "Anxiety and Depression Management", userRepository.findByUsername("psychologist_user2").getUserId(), "Tips for managing anxiety..."));
        articleRepository.save(new Article("ATC008", "Stress Management", userRepository.findByUsername("psychologist_user").getUserId(), "Tips for managing stress and anxiety ..."));
    }

    private void initializeAppointments() {
        appointmentRepository.save(new Appointments(__.generateAppointmentId(), "TS00125022801", "S001", "PSY001", AppointmentStatus.SCHEDULED));
        appointmentRepository.save(new Appointments("APP002", "TS00125022802", "S002", "PSY001", AppointmentStatus.SCHEDULED));
        appointmentRepository.save(new Appointments("APP003", "TS00125022803", "S003", "PSY001", AppointmentStatus.SCHEDULED));
        appointmentRepository.save(new Appointments("APP004", "TS00125022804", "S004", "PSY001", AppointmentStatus.SCHEDULED));
    }

    private void initializeNotifications() {
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByUsername("psychologist_user").getUserId(), "Appointment Scheduled", "Your appointment is scheduled", NotificationType.APPOINTMENT));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByUsername("student_user").getUserId(), "New Appointment", "You have a new appointment",NotificationType.APPOINTMENT));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByUsername("student_user").getUserId(), "New Survey", "You have a new survey", NotificationType.SURVEY));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByUsername("student_user").getUserId(), "New Program", "You have a new program", NotificationType.PROGRAM));
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            this.initialize();
        }
    }
}
