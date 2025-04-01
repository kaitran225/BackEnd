package com.healthy.backend.init;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.healthy.backend.dto.auth.request.RegisterRequest;
import com.healthy.backend.dto.programs.ProgramWeeklyScheduleRequest;
import com.healthy.backend.dto.programs.ProgramsRequest;
import com.healthy.backend.entity.Article;
import com.healthy.backend.entity.DefaultTimeSlot;
import com.healthy.backend.entity.Department;
import com.healthy.backend.entity.Notifications;
import com.healthy.backend.entity.Parents;
import com.healthy.backend.entity.Periodic;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.entity.Tags;
import com.healthy.backend.entity.UserLogs;
import com.healthy.backend.enums.Gender;
import com.healthy.backend.enums.NotificationType;
import com.healthy.backend.enums.ProgramTags;
import com.healthy.backend.enums.PsychologistStatus;
import com.healthy.backend.enums.Role;
import com.healthy.backend.enums.SurveyStandardType;
import com.healthy.backend.enums.SurveyStatus;
import com.healthy.backend.repository.ArticleRepository;
import com.healthy.backend.repository.DefaultTimeSlotRepository;
import com.healthy.backend.repository.DepartmentRepository;
import com.healthy.backend.repository.NotificationRepository;
import com.healthy.backend.repository.ParentRepository;
import com.healthy.backend.repository.PeriodicRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.StudentRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsRepository;
import com.healthy.backend.repository.SurveyQuestionRepository;
import com.healthy.backend.repository.SurveyRepository;
import com.healthy.backend.repository.TagsRepository;
import com.healthy.backend.repository.UserLogRepository;
import com.healthy.backend.repository.UserRepository;
import com.healthy.backend.service.AuthenticationService;
import com.healthy.backend.service.GeneralService;
import com.healthy.backend.service.ProgramService;

import lombok.RequiredArgsConstructor;

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
        initializeArticles();
        System.out.println("Articles initialized");
    }

    private void registerUsers() {
        List<RegisterRequest> users = List.of(
                new RegisterRequest("@Manager_Pass123", "Admin Admin", "manager@cybriadev.com", "0901721803", "Street 123, Ho Chi Minh City", Role.MANAGER.toString(), Gender.MALE.toString()),
                new RegisterRequest("@Manager_Pass123", "Staff Member", "staff@cybriadev.com", "0914055035", "Street 202, Ho Chi Minh City", Role.MANAGER.toString(), Gender.FEMALE.toString()),

                new RegisterRequest("@Psychologist_Pass123", "Dr. Brown", "psychologist1@cybriadev.com", "0918807263", "Street 101, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
                new RegisterRequest("@Psychologist_Pass123", "Dr. Blue", "psychologist2@cybriadev.com", "0913457620", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),

                new RegisterRequest("@Parent_Pass123", "Jane Smith", "parent1@cybriadev.com", "0812614184", "Street 789, Ho Chi Minh City", Role.PARENT.toString(), Gender.FEMALE.toString()),
                new RegisterRequest("@Parent_Pass123", "Bob Johnson", "parent2@cybriadev.com", "0829810129", "Street 404, Ho Chi Minh City", Role.PARENT.toString(), Gender.MALE.toString()),

                new RegisterRequest("@Student_Pass123", "John Doe", "student1@cybriadev.com", "0996477276", "Street 456, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("@Student_Pass123", "John Green", "student2@cybriadev.com", "0931926082", "Street 606, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("@Student_Pass123", "Alice Jones", "student3@cybriadev.com", "0836844105", "Street 303, Ho Chi Minh City", Role.STUDENT.toString(), Gender.FEMALE.toString())
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
        studentRepository.save(new Students("STU003", userRepository.findByEmail("student3@cybriadev.com").getUserId(), null, 9, "A", "Example High School", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));

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
                        "DPT001",
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
                        "DPT001",
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
                        "DPT001",
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
                        "DPT001",
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
                        "DPT001",
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
                        "DPT001",
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

        SurveyQuestions question1 = surveyQuestionsRepository.findById("SQR001").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO001", question1, "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO002", question1, "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO003", question1, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO004", question1, "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO005", question1, "Very Often", 4));

        SurveyQuestions question2 = surveyQuestionsRepository.findById("SQR002").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO006", question2, "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO007", question2, "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO008", question2, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO009", question2, "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO010", question2, "Very Often", 4));

        SurveyQuestions question3 = surveyQuestionsRepository.findById("SQR003").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO011", question3, "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO012", question3, "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO013", question3, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO014", question3, "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO015", question3, "Very Often", 4));

        SurveyQuestions question4 = surveyQuestionsRepository.findById("SQR004").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO016", question4, "Never", 4));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO017", question4, "Almost Never", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO018", question4, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO019", question4, "Fairly Often", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO020", question4, "Very Often", 0));

        SurveyQuestions question5 = surveyQuestionsRepository.findById("SQR005").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO021", question5, "Never", 4));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO022", question5, "Almost Never", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO023", question5, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO024", question5, "Fairly Often", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO025", question5, "Very Often", 0));

        SurveyQuestions question6 = surveyQuestionsRepository.findById("SQR006").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO026", question6, "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO027", question6, "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO028", question6, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO029", question6, "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO030", question6, "Very Often", 4));

        SurveyQuestions question7 = surveyQuestionsRepository.findById("SQR007").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO031", question7, "Never", 4));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO032", question7, "Almost Never", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO033", question7, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO034", question7, "Fairly Often", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO035", question7, "Very Often", 0));

        SurveyQuestions question8 = surveyQuestionsRepository.findById("SQR008").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO036", question8, "Never", 4));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO037", question8, "Almost Never", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO038", question8, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO039", question8, "Fairly Often", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO040", question8, "Very Often", 0));

        SurveyQuestions question9 = surveyQuestionsRepository.findById("SQR009").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO041", question9, "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO042", question9, "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO043", question9, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO044", question9, "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO045", question9, "Very Often", 4));

        SurveyQuestions question10 = surveyQuestionsRepository.findById("SQR010").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO046", question10, "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO047", question10, "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO048", question10, "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO049", question10, "Fairly Often", 3));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO050", question10, "Very Often", 4));

        SurveyQuestions question11 = surveyQuestionsRepository.findById("SQR011").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO051", question11, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO052", question11, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO053", question11, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO054", question11, "Nearly every day", 3));

        SurveyQuestions question12 = surveyQuestionsRepository.findById("SQR012").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO055", question12, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO056", question12, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO057", question12, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO058", question12, "Nearly every day", 3));

        SurveyQuestions question13 = surveyQuestionsRepository.findById("SQR013").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO059", question13, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO060", question13, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO061", question13, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO062", question13, "Nearly every day", 3));

        SurveyQuestions question14 = surveyQuestionsRepository.findById("SQR014").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO063", question14, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO064", question14, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO065", question14, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO066", question14, "Nearly every day", 3));

        SurveyQuestions question15 = surveyQuestionsRepository.findById("SQR015").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO067", question15, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO068", question15, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO069", question15, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO070", question15, "Nearly every day", 3));

        SurveyQuestions question16 = surveyQuestionsRepository.findById("SQR016").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO071", question16, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO072", question16, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO073", question16, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO074", question16, "Nearly every day", 3));

        SurveyQuestions question17 = surveyQuestionsRepository.findById("SQR017").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO075", question17, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO076", question17, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO077", question17, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO078", question17, "Nearly every day", 3));

        SurveyQuestions question18 = surveyQuestionsRepository.findById("SQR018").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO079", question18, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO080", question18, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO081", question18, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO082", question18, "Nearly every day", 3));

        SurveyQuestions question19 = surveyQuestionsRepository.findById("SQR019").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO083", question19, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO084", question19, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO085", question19, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO086", question19, "Nearly every day", 3));

        SurveyQuestions question20 = surveyQuestionsRepository.findById("SQR020").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO087", question20, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO088", question20, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO089", question20, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO090", question20, "Nearly every day", 3));

        SurveyQuestions question21 = surveyQuestionsRepository.findById("SQR021").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO091", question21, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO092", question21, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO093", question21, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO094", question21, "Nearly every day", 3));

        SurveyQuestions question22 = surveyQuestionsRepository.findById("SQR022").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO095", question22, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO096", question22, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO097", question22, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO098", question22, "Nearly every day", 3));

        SurveyQuestions question23 = surveyQuestionsRepository.findById("SQR023").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO099", question23, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO100", question23, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO101", question23, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO102", question23, "Nearly every day", 3));

        SurveyQuestions question24 = surveyQuestionsRepository.findById("SQR024").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO103", question24, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO104", question24, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO105", question24, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO106", question24, "Nearly every day", 3));

        SurveyQuestions question25 = surveyQuestionsRepository.findById("SQR025").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO107", question25, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO108", question25, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO109", question25, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO110", question25, "Nearly every day", 3));

        SurveyQuestions question26 = surveyQuestionsRepository.findById("SQR026").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO111", question26, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO112", question26, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO113", question26, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO114", question26, "Nearly every day", 3));

        SurveyQuestions question27 = surveyQuestionsRepository.findById("SQR027").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO115", question27, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO116", question27, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO117", question27, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO118", question27, "Nearly every day", 3));

        SurveyQuestions question28 = surveyQuestionsRepository.findById("SQR028").orElse(null);
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO119", question28, "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO120", question28, "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO121", question28, "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO122", question28, "Nearly every day", 3));


    }

    private void initializeArticles() {
        List<Article> articles = mentalHealthArticlesData.getMentalHealthArticles();
        articles.forEach(articleRepository::save);
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            this.initialize();
        }
    }
}
