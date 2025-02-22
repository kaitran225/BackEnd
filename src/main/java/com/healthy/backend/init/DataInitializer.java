package com.healthy.backend.init;

import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.*;
import com.healthy.backend.repository.*;
import com.healthy.backend.service.AuthenticationService;
import com.healthy.backend.service.GeneralService;
import com.healthy.backend.service.PsychologistService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
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
    private final ProgramScheduleRepository programScheduleRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TagsRepository tagsRepository;
    private final ProgramRepository programRepository;
    private final SurveyRepository surveyRepository;
    private final UserLogRepository userLogRepository;
    private final ArticleRepository articleRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;
    private final CategoriesRepository categoriesRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final AnswersRepository answersRepository;
    private final SurveyQuestionRepository surveyQuestionsRepository;
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
        initializeProgramSchedule();
        initializeCategories();
        initializeSurveys();
        initializeSurveyQuestions();
        initializeAnswers();
        initializeSurveyResults();
        initializeLogs();
        initializeArticles();
        initializeAppointments();
        initializeNotifications();
    }

    private void registerUsers() {
        List<RegisterRequest> users = List.of(
                new RegisterRequest("admin", "adminpass", "Admin Admin", "admin@example.com", "1111111111", "Street 123, Ho Chi Minh City", Role.MANAGER.toString(), Gender.MALE.toString()),
                new RegisterRequest("staff_member", "staff_pass", "Staff Member", "staff@example.com", "2222222222", "Street 202, Ho Chi Minh City", Role.MANAGER.toString(), Gender.FEMALE.toString()),

                new RegisterRequest("psychologist_user", "psychologist_pass", "Dr. Brown", "psychologist@example.com", "0912345671", "Street 101, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
                new RegisterRequest("psychologist_user2", "psychologist_pass", "Dr. Blue", "psychologist2@example.com", "0912345672", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),

                new RegisterRequest("parent_user", "parent_pass", "Jane Smith", "parent@example.com", "0812345671", "Street 789, Ho Chi Minh City", Role.PARENT.toString(), Gender.FEMALE.toString()),
                new RegisterRequest("parent_user2", "parent_pass", "Bob Johnson", "parent2@example.com", "0812345672", "Street 404, Ho Chi Minh City", Role.PARENT.toString(), Gender.MALE.toString()),

                new RegisterRequest("student_user", "student_pass", "John Doe", "student@example.com", "0512345671", "Street 456, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("student_user2", "student_pass", "John Green", "student2@example.com", "0512345672", "Street 606, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("student_user3", "student_pass", "Alice Jones", "student3@example.com", "0512345673", "Street 303, Ho Chi Minh City", Role.STUDENT.toString(), Gender.FEMALE.toString())
        );
        users.forEach(authenticationService::register);
    }

    private void initializeParentsAndStudents() {
        // Initialize Parents
        parentRepository.save(new Parents("PRT001", userRepository.findByUsername("parent_user").getUserId()));
        parentRepository.save(new Parents("PRT002", userRepository.findByUsername("parent_user2").getUserId()));

        // Initialize Students
        studentRepository.save(new Students("STU001", userRepository.findByUsername("student_user").getUserId(), parentRepository.findById("PRT001").get().getParentID(), 10, "A", "Example High School", 5, 10, 2));
        studentRepository.save(new Students("STU002", userRepository.findByUsername("student_user2").getUserId(), parentRepository.findById("PRT002").get().getParentID(), 9, "B", "Example High School", 3, 4, 7));
        studentRepository.save(new Students("STU003", userRepository.findByUsername("student_user3").getUserId(), parentRepository.findById("PRT001").get().getParentID(), 9, "A", "Example High School", 2, 2, 1));

    }

    private void initializeDepartments() {
        List<Department> departments = List.of(
                new Department("DPT001", "Child & Adolescent Psychology"),
                new Department("DPT002", "School Counseling"),
                new Department("DPT003", "Behavioral Therapy"),
                new Department("DPT004", "Trauma & Crisis Intervention"),
                new Department("DPT005", "Family & Parent Counseling"),
                new Department("DPT006", "Stress & Anxiety Management"),
                new Department("DPT007", "Depression & Mood Disorders"),
                new Department("DPT008", "Special Education Support"),
                new Department("DPT009", "Social Skills & Peer Relation"),
                new Department("DPT010", "Suicide Prevention & Intervention"),
                new Department("DPT011", "Digital Well-being Intervention")
        );
        departments.forEach(departmentRepository::save);
    }

    private void initializePsychologists() {
        psychologistRepository.save(new Psychologists(__.generatePsychologistID(), "UID003", 10, PsychologistStatus.ACTIVE, "DPT001"));
        psychologistRepository.save(new Psychologists(__.generatePsychologistID(), "UID004", 8, PsychologistStatus.ACTIVE, "DPT007"));

    }

    private void initializeTimeSlots() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate date = today.plusDays(i); // Get the date incrementally
            psychologistService.createDefaultTimeSlots(date, "PSY001");
            psychologistService.createDefaultTimeSlots(date, "PSY002");
        }
    }

    private void initializeTags() {
        List<Tags> tags = Arrays.stream(ProgramTags.values())
                .map(tag -> new Tags(String.format("TAG%03d", tag.ordinal() + 1), tag))
                .collect(Collectors.toList());
        tagsRepository.saveAll(tags);
    }

    private void initializePrograms() {
        programRepository.save(new Programs("PRG001", "Stress Management",
                "Program to help manage stress", 20, 4, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP06").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG001", "TAG002", "TAG003"))),
                LocalDate.parse("2025-02-23"), "https://example.com/meeting1", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG002", "Anxiety Support Group",
                "Support group for individuals with anxiety", 15, 6, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP06").orElseThrow(),
                psychologistRepository.findById("PSY002").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG004", "TAG005", "TAG006"))),
                LocalDate.parse("2025-02-25"), "https://example.com/meeting2", ProgramType.OFFLINE));
        programRepository.save(new Programs("PRG003", "Mindfulness Workshop",
                "Workshop on mindfulness techniques", 25, 3, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP03").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG007", "TAG008", "TAG009"))),
                LocalDate.parse("2025-02-28"), "https://example.com/meeting3", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG004", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP07").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-03-01"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG005", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP07").orElseThrow(),
                psychologistRepository.findById("PSY002").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-03-12"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG006", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP07").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-03-22"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG007", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP07").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-03-28"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG008", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP07").orElseThrow(),
                psychologistRepository.findById("PSY002").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-04-20"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG009", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP07").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-04-22"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG010", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DP07").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-04-28"), "https://example.com/meeting4", ProgramType.ONLINE));

    }

    private void initializeProgramSchedule() {
        // Initialize Program Schedule
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG001", "Monday", LocalTime.parse("10:00:00"), LocalTime.parse("11:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG002", "Tuesday", LocalTime.parse("14:00:00"), LocalTime.parse("15:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG003", "Wednesday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG004", "Friday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG005", "Saturday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG006", "Wednesday", LocalTime.parse("07:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG007", "Tuesday", LocalTime.parse("08:30:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG008", "Monday", LocalTime.parse("07:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG009", "Tuesday", LocalTime.parse("09:30:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG010", "Monday", LocalTime.parse("09:30:00"), LocalTime.parse("10:30:00")));
    }

    private void initializeCategories() {
        List<Categories> categories = Arrays.stream(SurveyCategory.values())
                .map(category -> new Categories(String.format("CAT%03d", category.ordinal() + 1), category))
                .collect(Collectors.toList());
        categoriesRepository.saveAll(categories);
    }

    private void initializeSurveys() {
        surveyRepository.save(new Surveys("SUV001", "Stress Survey", "Survey to assess stress levels", "CAT001", userRepository.findByUsername("student_user").getUserId(), SurveyStatus.ACTIVE));
        surveyRepository.save(new Surveys("SUV002", "Anxiety Assessment", "Assessment of anxiety symptoms", "CAT002", userRepository.findByUsername("student_user2").getUserId(), SurveyStatus.ACTIVE));
        surveyRepository.save(new Surveys("SUV003", "Depression Screening", "Screening for depression", "CAT003", userRepository.findByUsername("student_user3").getUserId(), SurveyStatus.ACTIVE));
        surveyRepository.save(new Surveys("SUV004", "Mood Assessment", "Assessment of mood", "CAT001", userRepository.findByUsername("student_user").getUserId(), SurveyStatus.INACTIVE));

    }

    private void initializeSurveyQuestions() {
        surveyQuestionsRepository.save(new SurveyQuestions("SQR001", "SUV001", "How often do you feel stressed?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR002", "SUV001", "How much does stress interfere with your daily life?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR003", "SUV002", "Do you experience excessive worry or fear?", "CAT002"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR004", "SUV002", "How often do you have panic attacks?", "CAT002"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR005", "SUV003", "Do you feel sad or hopeless most of the time?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR006", "SUV003", "Have you lost interest in activities you once enjoyed?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR007", "SUV004", "How are you feeling today?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR008", "SUV004", "Are you having a good day?", "CAT001"));
    }

    private void initializeAnswers() {
        answersRepository.save(new Answers("ANS001", "SQR001", "Never", 0));
        answersRepository.save(new Answers("ANS002", "SQR001", "Sometimes", 1));
        answersRepository.save(new Answers("ANS003", "SQR001", "Often", 2));
        answersRepository.save(new Answers("ANS004", "SQR001", "Always", 3));
        answersRepository.save(new Answers("ANS005", "SQR002", "Never", 0));
        answersRepository.save(new Answers("ANS006", "SQR002", "Sometimes", 1));
        answersRepository.save(new Answers("ANS007", "SQR002", "Moderately", 2));
        answersRepository.save(new Answers("ANS008", "SQR002", "Very much", 3));
        answersRepository.save(new Answers("ANS009", "SQR003", "Rarely", 0));
        answersRepository.save(new Answers("ANS010", "SQR003", "Sometimes", 1));
        answersRepository.save(new Answers("ANS011", "SQR003", "Often", 2));
        answersRepository.save(new Answers("ANS012", "SQR003", "Always", 3));
        answersRepository.save(new Answers("ANS013", "SQR004", "Never", 0));
        answersRepository.save(new Answers("ANS014", "SQR004", "Once a month", 1));
        answersRepository.save(new Answers("ANS015", "SQR004", "Once a week", 2));
        answersRepository.save(new Answers("ANS016", "SQR004", "Multiple times a week", 3));
        answersRepository.save(new Answers("ANS017", "SQR005", "Never", 0));
        answersRepository.save(new Answers("ANS018", "SQR005", "Sometimes", 1));
        answersRepository.save(new Answers("ANS019", "SQR005", "Often", 2));
        answersRepository.save(new Answers("ANS020", "SQR005", "Always", 3));
        answersRepository.save(new Answers("ANS021", "SQR006", "Never", 0));
        answersRepository.save(new Answers("ANS022", "SQR006", "Sometimes", 1));
        answersRepository.save(new Answers("ANS023", "SQR006", "Often", 2));
        answersRepository.save(new Answers("ANS024", "SQR006", "Always", 3));
        answersRepository.save(new Answers("ANS025", "SQR007", "Good", 0));
        answersRepository.save(new Answers("ANS026", "SQR007", "Fair", 1));
        answersRepository.save(new Answers("ANS027", "SQR007", "Bad", 2));
        answersRepository.save(new Answers("ANS028", "SQR007", "Very bad", 3));
        answersRepository.save(new Answers("ANS029", "SQR008", "Yes", 0));
        answersRepository.save(new Answers("ANS030", "SQR008", "No", 1));
        answersRepository.save(new Answers("ANS031", "SQR008", "Not sure", 2));
        answersRepository.save(new Answers("ANS032", "SQR008", "Not at all", 3));
    }

    private void initializeSurveyResults() {
        surveyResultRepository.save(new SurveyResults("SRS001", "STU001", "SQR001", "ANS002"));
        surveyResultRepository.save(new SurveyResults("SRS002", "STU001", "SQR002", "ANS006"));
        surveyResultRepository.save(new SurveyResults("SRS003", "STU001", "SQR003", "ANS010"));
        surveyResultRepository.save(new SurveyResults("SRS004", "STU001", "SQR004", "ANS014"));
        surveyResultRepository.save(new SurveyResults("SRS005", "STU001", "SQR005", "ANS018"));
        surveyResultRepository.save(new SurveyResults("SRS006", "STU001", "SQR006", "ANS022"));
        surveyResultRepository.save(new SurveyResults("SRS007", "STU002", "SQR001", "ANS001"));
        surveyResultRepository.save(new SurveyResults("SRS008", "STU002", "SQR002", "ANS005"));
        surveyResultRepository.save(new SurveyResults("SRS009", "STU002", "SQR003", "ANS009"));
        surveyResultRepository.save(new SurveyResults("SRS010", "STU002", "SQR004", "ANS013"));
        surveyResultRepository.save(new SurveyResults("SRS011", "STU002", "SQR005", "ANS017"));
        surveyResultRepository.save(new SurveyResults("SRS012", "STU002", "SQR006", "ANS021"));
        surveyResultRepository.save(new SurveyResults("SRS013", "STU003", "SQR001", "ANS003"));
        surveyResultRepository.save(new SurveyResults("SRS014", "STU003", "SQR002", "ANS007"));
        surveyResultRepository.save(new SurveyResults("SRS015", "STU003", "SQR003", "ANS011"));
        surveyResultRepository.save(new SurveyResults("SRS016", "STU003", "SQR004", "ANS015"));
        surveyResultRepository.save(new SurveyResults("SRS017", "STU003", "SQR005", "ANS019"));
        surveyResultRepository.save(new SurveyResults("SRS018", "STU003", "SQR006", "ANS023"));
    }

    private void initializeLogs() {
        userLogRepository.save(new UserLogs("LoG001", userRepository.findByUsername("student_user").getUserId(), "192.168.0.1"));
        userLogRepository.save(new UserLogs("LOG002", userRepository.findByUsername("student_user2").getUserId(), "244.178.44.111"));
        userLogRepository.save(new UserLogs("LOG003", userRepository.findByUsername("student_user").getUserId(), "38.0.101.76"));
        userLogRepository.save(new UserLogs("LOG004", userRepository.findByUsername("student_user2").getUserId(), "89.0.142.86"));
    }

    private void initializeArticles() {
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
        appointmentRepository.save(new Appointments(__.generateAppointmentId(), "TSL00125022801", "S001", "PSY001", AppointmentStatus.SCHEDULED));
        appointmentRepository.save(new Appointments(__.generateAppointmentId(), "TSL00125022802", "S002", "PSY001", AppointmentStatus.SCHEDULED));
        appointmentRepository.save(new Appointments(__.generateAppointmentId(), "TSL00125022803", "S003", "PSY001", AppointmentStatus.SCHEDULED));
        appointmentRepository.save(new Appointments(__.generateAppointmentId(), "TSL00125022804", "S004", "PSY001", AppointmentStatus.SCHEDULED));
    }

    private void initializeNotifications() {
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByUsername("psychologist_user").getUserId(), "Appointment Scheduled", "Your appointment is scheduled", NotificationType.APPOINTMENT));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByUsername("student_user").getUserId(), "New Appointment", "You have a new appointment", NotificationType.APPOINTMENT));
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
