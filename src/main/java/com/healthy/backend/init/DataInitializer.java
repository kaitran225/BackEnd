package com.healthy.backend.init;

import com.healthy.backend.dto.auth.request.RegisterRequest;
import com.healthy.backend.dto.programs.ProgramWeeklyScheduleRequest;
import com.healthy.backend.dto.programs.ProgramsRequest;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.*;
import com.healthy.backend.repository.*;
import com.healthy.backend.service.AuthenticationService;
import com.healthy.backend.service.GeneralService;
import com.healthy.backend.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final TagsRepository tagsRepository;
    private final UserLogRepository userLogRepository;
    private final ArticleRepository articleRepository;
    private final DefaultTimeSlotRepository defaultTimeSlotRepository;
    private final NotificationRepository notificationRepository;
    private final SurveyQuestionOptionsRepository surveyQuestionOptionsRepository;
    private final SurveyRepository surveyRepository;
    private final PeriodicRepository periodicRepository;
    private final SurveyQuestionRepository surveyQuestionsRepository;
    private final GeneralService __;
    private final AuthenticationService authenticationService;
    private final ProgramService programService;
    private final MentalHealthArticlesData mentalHealthArticlesData;

    private static LocalDate getNextDayOfWeek(String dayOfWeek) {
        LocalDate today = LocalDate.now();
        DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase());

        return today.with(java.time.temporal.TemporalAdjusters.next(targetDay));
    }

    private void initialize() {
        registerUsers();
        System.out.println("Users registered");
        initializeParentsAndStudents();
        System.out.println("Parents and Students initialized");
        initializeDepartments();
        System.out.println("Departments initialized");
        initializePsychologists();
        System.out.println("Psychologists initialized");
        initializeDefaultSlots();
        System.out.println("DefaultTimeSlots initialize");
        initializeTags();
        System.out.println("Tags initialized");
        initializePrograms();
        System.out.println("Programs initialized");
        initializeSurveys();
        System.out.println("Surveys initialized");
        initializeSurveyQuestions();
        System.out.println("Survey Questions initialized");
        initializeAnswers();
        System.out.println("Answers initialized");
        initializeLogs();
        System.out.println("Logs initialized");
        initializeArticles();
        System.out.println("Articles initialized");
        initializeNotifications();
        System.out.println("Notifications initialized");
    }

    private void registerUsers() {
        List<RegisterRequest> users = List.of(
                new RegisterRequest("@Manager_Pass123", "Admin Admin", "manager@cybriadev.com", "1111111111", "Street 123, Ho Chi Minh City", Role.MANAGER.toString(), Gender.MALE.toString()),
                new RegisterRequest("@Manager_Pass123", "Staff Member", "staff@cybriadev.com", "2222222222", "Street 202, Ho Chi Minh City", Role.MANAGER.toString(), Gender.FEMALE.toString()),

                new RegisterRequest("@Psychologist_Pass123", "Dr. Brown", "psychologist1@cybriadev.com", "0912345671", "Street 101, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
                new RegisterRequest("@Psychologist_Pass123", "Dr. Blue", "psychologist2@cybriadev.com", "0912345672", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),

                new RegisterRequest("@Parent_Pass123", "Jane Smith", "parent1@cybriadev.com", "0812345671", "Street 789, Ho Chi Minh City", Role.PARENT.toString(), Gender.FEMALE.toString()),
                new RegisterRequest("@Parent_Pass123", "Bob Johnson", "parent2@cybriadev.com", "0812345672", "Street 404, Ho Chi Minh City", Role.PARENT.toString(), Gender.MALE.toString()),

                new RegisterRequest("@Student_Pass123", "John Doe", "student1@cybriadev.com", "0512345671", "Street 456, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("@Student_Pass123", "John Green", "student2@cybriadev.com", "0512345672", "Street 606, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("@Student_Pass123", "Alice Jones", "student3@cybriadev.com", "0512345673", "Street 303, Ho Chi Minh City", Role.STUDENT.toString(), Gender.FEMALE.toString())
        );
        users.forEach(authenticationService::register);
    }

    private void initializeParentsAndStudents() {
        // Initialize Parents
        parentRepository.save(new Parents("PAR001", userRepository.findByEmail("parent1@cybriadev.com").getUserId()));
        parentRepository.save(new Parents("PAR002", userRepository.findByEmail("parent2@cybriadev.com").getUserId()));

        // Initialize Students
        studentRepository.save(new Students("STU001", userRepository.findByEmail("student1@cybriadev.com").getUserId(), "PAR001", 10, "A", "Example High School", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));
        studentRepository.save(new Students("STU002", userRepository.findByEmail("student2@cybriadev.com").getUserId(), "PAR002", 9, "B", "Example High School", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));
        studentRepository.save(new Students("STU003", userRepository.findByEmail("student3@cybriadev.com").getUserId(), "PAR001", 9, "A", "Example High School", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));

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

    private void initializeDefaultSlots() {
        if (defaultTimeSlotRepository.count() > 0) {
            return;
        }

        List<DefaultTimeSlot> slots = new ArrayList<>();

        slots.addAll(generateTimeSlots("MORNING", LocalTime.of(8, 0), LocalTime.of(11, 0), "Morning"));
        slots.addAll(generateTimeSlots("AFTERNOON", LocalTime.of(13, 0), LocalTime.of(17, 0), "Afternoon"));

        defaultTimeSlotRepository.saveAll(slots);
    }

    private List<DefaultTimeSlot> generateTimeSlots(String prefix, LocalTime startTime, LocalTime endTime, String period) {
        List<DefaultTimeSlot> slots = new ArrayList<>();
        LocalTime time = startTime;
        int index = 0;

        while (time.isBefore(endTime)) {
            slots.add(new DefaultTimeSlot(
                    String.format("%s-%02d", prefix, index++),
                    time,
                    time.plusMinutes(30),
                    period
            ));
            time = time.plusMinutes(30);
        }

        return slots;
    }

    private void initializeTags() {
        List<Tags> tags = Arrays.stream(ProgramTags.values())
                .map(tag -> new Tags(String.format("TAG%03d", tag.ordinal() + 1), tag))
                .collect(Collectors.toList());
        tagsRepository.saveAll(tags);
    }

    private void initializePrograms() {
        List<ProgramsRequest> programs = List.of(
                new ProgramsRequest("Stress Management",
                        "Program to help manage stress", 20, 4,
                        getNextDayOfWeek("Monday").plusWeeks(0).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Monday",
                                "10:00:00",
                                "11:30:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG001", "TAG002", "TAG003")),
                        "PSY001",
                        "DPT006",
                        "Online",
                        "https://example.com/meeting1"
                ),
                new ProgramsRequest(
                        "Anxiety Support Group",
                        "Support group for individuals with anxiety", 15, 6,
                        getNextDayOfWeek("Tuesday").plusWeeks(1).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Tuesday",
                                "14:00:00",
                                "15:30:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG004", "TAG005", "TAG006")),
                        "PSY002",
                        "DPT007",
                        "Online",
                        "https://example.com/meeting2"
                ),
                new ProgramsRequest(
                        "Mindfulness Workshop",
                        "Workshop on mindfulness techniques", 25, 3,
                        getNextDayOfWeek("Wednesday").plusWeeks(2).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Wednesday",
                                "09:00:00",
                                "11:00:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG007", "TAG008", "TAG009")),
                        "PSY001",
                        "DPT003",
                        "Online",
                        "https://example.com/meeting3"
                ),
                new ProgramsRequest(
                        "Depression Counseling",
                        "Counseling for individuals with depression", 30, 2,
                        getNextDayOfWeek("Thursday").plusWeeks(3).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Thursday",
                                "10:00:00",
                                "12:00:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG010", "TAG011", "TAG012")),
                        "PSY001",
                        "DPT007",
                        "Online",
                        "https://example.com/meeting4"
                ),
                new ProgramsRequest(
                        "Depression Counseling",
                        "Counseling for individuals with depression", 30, 2,
                        getNextDayOfWeek("Friday").plusWeeks(4).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Friday",
                                "13:00:00",
                                "15:00:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG010", "TAG011", "TAG012")),
                        "PSY002",
                        "DPT007",
                        "Online",
                        "https://example.com/meeting4"
                ),
                new ProgramsRequest(
                        "Depression Counseling",
                        "Counseling for individuals with depression", 30, 2,
                        getNextDayOfWeek("Monday").plusWeeks(5).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Monday",
                                "08:00:00",
                                "10:00:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG010", "TAG011", "TAG012")),
                        "PSY002",
                        "DPT007",
                        "Online",
                        "https://example.com/meeting4"
                ),
                new ProgramsRequest(
                        "Depression Counseling",
                        "Counseling for individuals with depression", 30, 2,
                        getNextDayOfWeek("Tuesday").plusWeeks(4).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Tuesday",
                                "11:00:00",
                                "13:00:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG010", "TAG011", "TAG012")),
                        "PSY001",
                        "DPT007",
                        "Online",
                        "https://example.com/meeting4"
                ),
                new ProgramsRequest(
                        "Depression Counseling",
                        "Counseling for individuals with depression", 30, 2,
                        getNextDayOfWeek("Wednesday").plusWeeks(3).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Wednesday",
                                "12:00:00",
                                "14:00:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG010", "TAG011", "TAG012")),
                        "PSY002",
                        "DPT007",
                        "Online",
                        "https://example.com/meeting4"
                ),
                new ProgramsRequest(
                        "Depression Counseling",
                        "Counseling for individuals with depression", 30, 2,
                        getNextDayOfWeek("Thursday").plusWeeks(2).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Thursday",
                                "14:00:00",
                                "16:00:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG010", "TAG011", "TAG012")),
                        "PSY001",
                        "DPT007",
                        "Online",
                        "https://example.com/meeting4"
                ),
                new ProgramsRequest(
                        "Depression Counseling",
                        "Counseling for individuals with depression", 30, 2,
                        getNextDayOfWeek("Friday").plusWeeks(1).toString(),
                        new ProgramWeeklyScheduleRequest(
                                "Friday",
                                "16:00:00",
                                "18:00:00"
                        ),
                        "Active",
                        new HashSet<>(Set.of("TAG010", "TAG011", "TAG012")),
                        "PSY001",
                        "DPT007",
                        "Online",
                        "https://example.com/meeting4"
                )
        );
        programs.forEach(programsRequest -> programService._createProgram(programsRequest,
                Objects.requireNonNull(userRepository.findByUserId("UID001").orElse(null))));
    }

    private void initializeSurveys() {
        Surveys survey1 = new Surveys("SUV001", "Stress Survey",
                "Survey to assess stress levels", userRepository.findByEmail("psychologist1@cybriadev.com").getUserId(), LocalDate.now(), 2, SurveyStandardType.PSS_10);
        surveyRepository.save(survey1);
        periodicRepository.save(new Periodic(__.createPeriodicID(survey1), survey1, survey1.getStartDate(), survey1.getEndDate()));

        Surveys survey2 = new Surveys("SUV002", "Anxiety Assessment",
                "Assessment of anxiety symptoms", userRepository.findByEmail("psychologist2@cybriadev.com").getUserId(), LocalDate.now(), 2, SurveyStandardType.GAD_7);
        surveyRepository.save(survey2);
        periodicRepository.save(new Periodic(__.createPeriodicID(survey2), survey2, survey2.getStartDate(), survey2.getEndDate()));

        Surveys survey3 = new Surveys("SUV003", "Depression Screening",
                "Screening for depression", userRepository.findByEmail("psychologist2@cybriadev.com").getUserId(), LocalDate.now(), 2, SurveyStandardType.PHQ_9);
        surveyRepository.save(survey3);
        periodicRepository.save(new Periodic(__.createPeriodicID(survey3), survey3, survey3.getStartDate(), survey3.getEndDate()));

        Surveys survey4 = new Surveys("SUV004", "Mood Assessment",
                "Assessment of mood", userRepository.findByEmail("psychologist1@cybriadev.com").getUserId(), LocalDate.now(), 2, SurveyStandardType.PSS_10, SurveyStatus.INACTIVE);
        surveyRepository.save(survey4);
        periodicRepository.save(new Periodic(__.createPeriodicID(survey4), survey4, survey4.getStartDate(), survey4.getEndDate()));
    }

    private void initializeSurveyQuestions() {
        surveyQuestionsRepository.save(new SurveyQuestions("SQR001", "SUV001", "In the last month, how often have you been upset because something that happened was unexpected?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR002", "SUV001", "In the last month, how often have you felt that you were unable to control the important things in your life?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR003", "SUV001", "In the last month, how often have you felt nervous and stressed?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR004", "SUV001", "In the last month, how often have you felt confident about your ability to handle your personal problems?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR005", "SUV001", "In the last month, how often have you felt that things were going your way?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR006", "SUV001", "In the last month, how often have you found that you could not cope with all the things you had to do?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR007", "SUV001", "In the last month, how often have you been able to control irritations in your life?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR008", "SUV001", "In the last month, how often have you felt that you were on top of things?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR009", "SUV001", "In the last month, how often have you been angered because of things that happened that were outside of your control?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR010", "SUV001", "In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?"));

        surveyQuestionsRepository.save(new SurveyQuestions("SQR011", "SUV002", "Feeling nervous, anxious, or on edge?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR012", "SUV002", "Not being able to stop or control worrying?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR013", "SUV002", "Trouble relaxing"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR014", "SUV002", "Do you feel sad or hopeless most of the time?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR015", "SUV002", "Being so restless that it's hard to sit still?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR016", "SUV002", "Becoming easily annoyed or irritable?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR017", "SUV002", "Feeling afraid as if something awful might happen?"));

        surveyQuestionsRepository.save(new SurveyQuestions("SQR018", "SUV003", "Little interest or pleasure in doing things?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR019", "SUV003", "Feeling down, depressed, or hopeless?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR020", "SUV003", "Trouble falling or staying asleep, or sleeping too much?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR021", "SUV003", "Feeling tired or having little energy?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR022", "SUV003", "Poor appetite or overeating?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR023", "SUV003", "Feeling bad about yourself — or that you are a failure or have let yourself or your family down?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR024", "SUV003", "Trouble concentrating on things, such as reading the newspaper or watching television?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR025", "SUV003", "Moving or speaking so slowly that other people could have noticed? Or so fidgety or restless that you have been moving a lot more than usual?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR026", "SUV003", "Thoughts that you would be better off dead, or thoughts of hurting yourself in some way?"));

        surveyQuestionsRepository.save(new SurveyQuestions("SQR027", "SUV004", "How are you feeling today?"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR028", "SUV004", "Are you having a good day?"));

    }

    private void initializeAnswers() {

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO001", "SQR001", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO002", "SQR001", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO003", "SQR001", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO004", "SQR001", "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO005", "SQR001", "Very Often", 4));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO006", "SQR002", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO007", "SQR002", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO008", "SQR002", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO009", "SQR002", "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO010", "SQR002", "Very Often", 4));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO011", "SQR003", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO012", "SQR003", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO013", "SQR003", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO014", "SQR003", "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO015", "SQR003", "Very Often", 4));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO016", "SQR004", "Never", 4));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO017", "SQR004", "Almost Never", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO018", "SQR004", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO019", "SQR004", "Fairly Often", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO020", "SQR004", "Very Often", 0));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO021", "SQR005", "Never", 4));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO022", "SQR005", "Almost Never", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO023", "SQR005", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO024", "SQR005", "Fairly Often", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO025", "SQR005", "Very Often", 0));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO026", "SQR006", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO027", "SQR006", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO028", "SQR006", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO029", "SQR006", "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO030", "SQR006", "Very Often", 4));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO031", "SQR007", "Never", 4));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO032", "SQR007", "Almost Never", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO033", "SQR007", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO034", "SQR007", "Fairly Often", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO035", "SQR007", "Very Often", 0));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO036", "SQR008", "Never", 4));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO037", "SQR008", "Almost Never", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO038", "SQR008", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO039", "SQR008", "Fairly Often", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO040", "SQR008", "Very Often", 0));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO041", "SQR009", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO042", "SQR009", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO043", "SQR009", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO044", "SQR009", "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO045", "SQR009", "Very Often", 4));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO046", "SQR010", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO047", "SQR010", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO048", "SQR010", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO049", "SQR010", "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO050", "SQR010", "Very Often", 4));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO051", "SQR011", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO052", "SQR011", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO053", "SQR011", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO054", "SQR011", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO055", "SQR012", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO056", "SQR012", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO057", "SQR012", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO058", "SQR012", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO059", "SQR013", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO060", "SQR013", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO061", "SQR013", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO062", "SQR013", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO063", "SQR014", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO064", "SQR014", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO065", "SQR014", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO066", "SQR014", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO067", "SQR015", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO068", "SQR015", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO069", "SQR015", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO070", "SQR015", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO071", "SQR016", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO072", "SQR016", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO073", "SQR016", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO074", "SQR016", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO075", "SQR017", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO076", "SQR017", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO077", "SQR017", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO078", "SQR017", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO079", "SQR018", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO080", "SQR018", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO081", "SQR018", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO082", "SQR018", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO083", "SQR019", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO084", "SQR019", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO085", "SQR019", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO086", "SQR019", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO087", "SQR020", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO088", "SQR020", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO089", "SQR020", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO090", "SQR020", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO091", "SQR021", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO092", "SQR021", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO093", "SQR021", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO094", "SQR021", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO095", "SQR022", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO096", "SQR022", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO097", "SQR022", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO098", "SQR022", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO099", "SQR023", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO100", "SQR023", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO101", "SQR023", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO102", "SQR023", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO103", "SQR024", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO104", "SQR024", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO105", "SQR024", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO106", "SQR024", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO107", "SQR025", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO108", "SQR025", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO109", "SQR025", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO110", "SQR025", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO111", "SQR026", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO112", "SQR026", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO113", "SQR026", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO114", "SQR026", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO115", "SQR027", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO116", "SQR027", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO117", "SQR027", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO118", "SQR027", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO119", "SQR028", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO120", "SQR028", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO121", "SQR028", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO122", "SQR028", "Nearly every day", 3));

    }

    private void initializeLogs() {
        userLogRepository.save(new UserLogs("LOG001", userRepository.findByEmail("psychologist1@cybriadev.com").getUserId(), "192.168.0.1"));
        userLogRepository.save(new UserLogs("LOG002", userRepository.findByEmail("student2@cybriadev.com").getUserId(), "244.178.44.111"));
        userLogRepository.save(new UserLogs("LOG003", userRepository.findByEmail("psychologist1@cybriadev.com").getUserId(), "38.0.101.76"));
        userLogRepository.save(new UserLogs("LOG004", userRepository.findByEmail("parent2@cybriadev.com").getUserId(), "89.0.142.86"));
    }

    private void initializeArticles() {
        List<Article> articles = mentalHealthArticlesData.getMentalHealthArticles();
        articles.forEach(articleRepository::save);
    }

    private void initializeNotifications() {
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByEmail("psychologist1@cybriadev.com").getUserId(), "Appointment Scheduled", "Your appointment is scheduled", NotificationType.APPOINTMENT));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByEmail("student1@cybriadev.com").getUserId(), "New Appointment", "You have a new appointment", NotificationType.APPOINTMENT));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByEmail("student1@cybriadev.com").getUserId(), "New Survey", "You have a new survey", NotificationType.SURVEY));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByEmail("student1@cybriadev.com").getUserId(), "New Program", "You have a new program", NotificationType.PROGRAM));
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            this.initialize();
        }
    }
}
