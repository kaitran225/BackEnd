package com.healthy.backend.init;

import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.entity.*;
import com.healthy.backend.repository.*;
import com.healthy.backend.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private SurveyResultRepository surveyResultRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private PsychologistRepository psychologistRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private ProgramParticipationRepository programParticipationRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    private AnswersRepository answersRepository;

    @Autowired
    private StudentNoteRepository studentNoteRepository;

    @Autowired
    private UserLogRepository userLogRepository;

    @Autowired
    private AppointmentHistoryRepository appointmentHistoryRepository;

    @Autowired
    private ProgramScheduleRepository programScheduleRepository;

    @Autowired
    private CategoriesRepository categoryRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private void initialize() {
        // Initialize Users
        authenticationService.register(new RegisterRequest("admin", "adminpass", "Admin User", "admin@example.com", "1111111111", Users.UserRole.MANAGER.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("john_doe", "password123", "John Doe", "john@example.com", "1234567890", Users.UserRole.STUDENT.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("jane_smith", "password456", "Jane Smith", "jane@example.com", "9876543210", Users.UserRole.PARENT.toString(), Users.Gender.Female.toString()));
        authenticationService.register(new RegisterRequest("dr_brown", "password789", "Dr. Brown", "brown@example.com", "5555555555", Users.UserRole.PSYCHOLOGIST.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("staff_user", "staffpass", "Staff Member", "staff@example.com", "9999999999", Users.UserRole.MANAGER.toString(), Users.Gender.Female.toString()));
        authenticationService.register(new RegisterRequest("alice_jones", "alicepass", "Alice Jones", "alice@example.com", "2222222222", Users.UserRole.STUDENT.toString(), Users.Gender.Female.toString()));
        authenticationService.register(new RegisterRequest("bob_johnson", "bobpass", "Bob Johnson", "bob@example.com", "3333333333", Users.UserRole.PARENT.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("dr_blue", "bluepass", "Dr. Blue", "blue@example.com", "4444444444", Users.UserRole.PSYCHOLOGIST.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("john_green", "greenpass", "John Green", "green@example.com", "6666666666", Users.UserRole.STUDENT.toString(), Users.Gender.Male.toString()));
        // Initialize Parents
        parentRepository.save(new Parents("P001", userRepository.findByUsername("jane_smith").getUserId()));
        parentRepository.save(new Parents("P002", userRepository.findByUsername("bob_johnson").getUserId()));

        // Initialize Students
        studentRepository.save(new Students("S001", userRepository.findByUsername("john_doe").getUserId(), parentRepository.findById("P001").get().getParentID(), 10, "A", "Example High School", 5, 10, 2));
        studentRepository.save(new Students("S002", userRepository.findByUsername("alice_jones").getUserId(), parentRepository.findById("P002").get().getParentID(), 9, "B", "Example High School", 3, 4, 7));
        studentRepository.save(new Students("S003", userRepository.findByUsername("john_green").getUserId(), parentRepository.findById("P001").get().getParentID(), 9, "A", "Example High School", 2, 2, 1));
        // Initialize Psychologists
        psychologistRepository.save(new Psychologists("PSY001", userRepository.findByUsername("dr_brown").getUserId(), "Child Psychology", 10, Psychologists.Status.Active));
        psychologistRepository.save(new Psychologists("PSY002", userRepository.findByUsername("dr_blue").getUserId(), "Adolescent Psychology", 8, Psychologists.Status.Active));

        // Initialize Time Slots
        timeSlotRepository.save(new TimeSlots("TS150601", "PSY001", LocalDate.parse("2023-06-15"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), TimeSlots.Status.Booked));
        timeSlotRepository.save(new TimeSlots("TS150602", "PSY001", LocalDate.parse("2023-06-15"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"), TimeSlots.Status.Booked));
        timeSlotRepository.save(new TimeSlots("TS150603", "PSY001", LocalDate.parse("2023-06-15"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150604", "PSY001", LocalDate.parse("2023-06-15"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150605", "PSY001", LocalDate.parse("2023-06-15"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150606", "PSY001", LocalDate.parse("2023-06-15"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150607", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150608", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150609", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150610", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150611", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150612", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150613", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("11:00:00"), LocalTime.parse("11:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150614", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("11:30:00"), LocalTime.parse("12:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150615", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("12:00:00"), LocalTime.parse("12:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150616", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("12:30:00"), LocalTime.parse("13:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS150617", "PSY002", LocalDate.parse("2023-06-15"), LocalTime.parse("13:00:00"), LocalTime.parse("13:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS16 0601", "PSY001", LocalDate.parse("2023-06-16"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160602", "PSY001", LocalDate.parse("2023-06-16"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160603", "PSY001", LocalDate.parse("2023-06-16"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160604", "PSY001", LocalDate.parse("2023-06-16"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160605", "PSY001", LocalDate.parse("2023-06-16"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160606", "PSY001", LocalDate.parse("2023-06-16"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160607", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160608", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160609", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160610", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160611", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160612", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160613", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("11:00:00"), LocalTime.parse("11:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160614", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("11:30:00"), LocalTime.parse("12:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160615", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("12:00:00"), LocalTime.parse("12:30:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160616", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("12:30:00"), LocalTime.parse("13:00:00"), TimeSlots.Status.Available));
        timeSlotRepository.save(new TimeSlots("TS160617", "PSY002", LocalDate.parse("2023-06-16"), LocalTime.parse("13:00:00"), LocalTime.parse("13:30:00"), TimeSlots.Status.Available));

        // Initialize Programs
        programRepository.save(new Programs("PRG001", "Stress Management", Programs.Category.Wellness, "Program to help manage stress", 20, 4, Programs.Status.Activate, userRepository.findByUsername("staff_user").getUserId()));
        programRepository.save(new Programs("PRG002", "Anxiety Support Group", Programs.Category.Wellness, "Support group for individuals with anxiety", 15, 6, Programs.Status.Activate, userRepository.findByUsername("staff_user").getUserId()));
        programRepository.save(new Programs("PRG003", "Mindfulness Workshop", Programs.Category.Wellness, "Workshop on mindfulness techniques", 25, 3, Programs.Status.Activate, userRepository.findByUsername("staff_user").getUserId()));

        // Initialize Program Schedule
        programScheduleRepository.save(new ProgramSchedule("SCH001", "PRG001", "Monday", LocalTime.parse("10:00:00"), LocalTime.parse("11:30:00")));
        programScheduleRepository.save(new ProgramSchedule("SCH002", "PRG002", "Tuesday", LocalTime.parse("14:00:00"), LocalTime.parse("15:30:00")));
        programScheduleRepository.save(new ProgramSchedule("SCH003", "PRG003", "Wednesday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00")));

        // Initialize Program Participation
        programParticipationRepository.save(new ProgramParticipation("PP001", "S001", "PRG001", ProgramParticipation.Status.Completed, LocalDate.parse("2023-06-01"), LocalDate.parse("2023-06-30")));
        programParticipationRepository.save(new ProgramParticipation("PP002", "S002", "PRG002", ProgramParticipation.Status.InProgress, LocalDate.parse("2023-07-01"), LocalDate.parse("2023-08-15")));

        // Initialize Categories
        categoryRepository.save(new Categories("CAT001", Categories.MentalHealthCategory.Anxiety));
        categoryRepository.save(new Categories("CAT002", Categories.MentalHealthCategory.Stress));
        categoryRepository.save(new Categories("CAT003", Categories.MentalHealthCategory.Depression));

        // Initialize Surveys
        surveyRepository.save(new Surveys("SUR001", "Stress Survey", "Survey to assess stress levels", "CAT001", userRepository.findByUsername("john_doe").getUserId(), Surveys.Status.Finished));
        surveyRepository.save(new Surveys("SUR002", "Anxiety Assessment", "Assessment of anxiety symptoms", "CAT002", userRepository.findByUsername("jane_smith").getUserId(), Surveys.Status.Finished));
        surveyRepository.save(new Surveys("SUR003", "Depression Screening", "Screening for depression", "CAT003", userRepository.findByUsername("john_green").getUserId(), Surveys.Status.Finished));
        surveyRepository.save(new Surveys("SUR004", "Mood Assessment", "Assessment of mood", "CAT001", userRepository.findByUsername("john_doe").getUserId(), Surveys.Status.Unfinished));

        // Initialize Survey Questions
        surveyQuestionRepository.save(new SurveyQuestions("Q001", "SUR001", "How often do you feel stressed?", "CAT001"));
        surveyQuestionRepository.save(new SurveyQuestions("Q002", "SUR001", "How much does stress interfere with your daily life?", "CAT001"));
        surveyQuestionRepository.save(new SurveyQuestions("Q003", "SUR002", "Do you experience excessive worry or fear?", "CAT002"));
        surveyQuestionRepository.save(new SurveyQuestions("Q004", "SUR002", "How often do you have panic attacks?", "CAT002"));
        surveyQuestionRepository.save(new SurveyQuestions("Q005", "SUR003", "Do you feel sad or hopeless most of the time?", "CAT003"));
        surveyQuestionRepository.save(new SurveyQuestions("Q006", "SUR003", "Have you lost interest in activities you once enjoyed?", "CAT003"));
        surveyQuestionRepository.save(new SurveyQuestions("Q007", "SUR004", "How are you feeling today?", "CAT001"));
        surveyQuestionRepository.save(new SurveyQuestions("Q008", "SUR004", "Are you having a good day?", "CAT001"));

        // Initialize Answerss
        answersRepository.save(new Answers("A001", "Q001", "Never", 0));
        answersRepository.save(new Answers("A002", "Q001", "Sometimes", 1));
        answersRepository.save(new Answers("A003", "Q001", "Often", 2));
        answersRepository.save(new Answers("A004", "Q001", "Always", 3));
        answersRepository.save(new Answers("A005", "Q002", "Never", 0));
        answersRepository.save(new Answers("A006", "Q002", "Sometimes", 1));
        answersRepository.save(new Answers("A007", "Q002", "Moderately", 2));
        answersRepository.save(new Answers("A008", "Q002", "Very much", 3));
        answersRepository.save(new Answers("A009", "Q003", "Rarely", 0));
        answersRepository.save(new Answers("A010", "Q003", "Sometimes", 1));
        answersRepository.save(new Answers("A011", "Q003", "Often", 2));
        answersRepository.save(new Answers("A012", "Q003", "Always", 3));
        answersRepository.save(new Answers("A013", "Q004", "Never", 0));
        answersRepository.save(new Answers("A014", "Q004", "Once a month", 1));
        answersRepository.save(new Answers("A015", "Q004", "Once a week", 2));
        answersRepository.save(new Answers("A016", "Q004", "Multiple times a week", 3));
        answersRepository.save(new Answers("A017", "Q005", "Never", 0));
        answersRepository.save(new Answers("A018", "Q005", "Sometimes", 1));
        answersRepository.save(new Answers("A019", "Q005", "Often", 2));
        answersRepository.save(new Answers("A020", "Q005", "Always", 3));
        answersRepository.save(new Answers("A021", "Q006", "Never", 0));
        answersRepository.save(new Answers("A022", "Q006", "Sometimes", 1));
        answersRepository.save(new Answers("A023", "Q006", "Often", 2));
        answersRepository.save(new Answers("A024", "Q006", "Always", 3));
        answersRepository.save(new Answers("A025", "Q007", "Good", 0));
        answersRepository.save(new Answers("A026", "Q007", "Fair", 1));
        answersRepository.save(new Answers("A027", "Q007", "Bad", 2));
        answersRepository.save(new Answers("A028", "Q007", "Very bad", 3));
        answersRepository.save(new Answers("A029", "Q008", "Yes", 0));
        answersRepository.save(new Answers("A030", "Q008", "No", 1));
        answersRepository.save(new Answers("A031", "Q008", "Not sure", 2));
        answersRepository.save(new Answers("A032", "Q008", "Not at all", 3));

        // Initialize Survey Results
        surveyResultRepository.save(new SurveyResults("R001", "S001", "Q001", "A002"));
        surveyResultRepository.save(new SurveyResults("R002", "S001", "Q002", "A006"));
        surveyResultRepository.save(new SurveyResults("R003", "S001", "Q003", "A010"));
        surveyResultRepository.save(new SurveyResults("R004", "S001", "Q004", "A014"));
        surveyResultRepository.save(new SurveyResults("R005", "S001", "Q005", "A018"));
        surveyResultRepository.save(new SurveyResults("R006", "S001", "Q006", "A022"));

        surveyResultRepository.save(new SurveyResults("R007", "S002", "Q001", "A001"));
        surveyResultRepository.save(new SurveyResults("R008", "S002", "Q002", "A005"));
        surveyResultRepository.save(new SurveyResults("R009", "S002", "Q003", "A009"));
        surveyResultRepository.save(new SurveyResults("R010", "S002", "Q004", "A013"));
        surveyResultRepository.save(new SurveyResults("R011", "S002", "Q005", "A017"));
        surveyResultRepository.save(new SurveyResults("R012", "S002", "Q006", "A021"));

        surveyResultRepository.save(new SurveyResults("R013", "S003", "Q001", "A003"));
        surveyResultRepository.save(new SurveyResults("R014", "S003", "Q002", "A007"));
        surveyResultRepository.save(new SurveyResults("R015", "S003", "Q003", "A011"));
        surveyResultRepository.save(new SurveyResults("R016", "S003", "Q004", "A015"));
        surveyResultRepository.save(new SurveyResults("R017", "S003", "Q005", "A019"));
        surveyResultRepository.save(new SurveyResults("R018", "S003", "Q006", "A023"));


        // Initialize Student Notes
        studentNoteRepository.save(new StudentNotes("N001", "S001", "PSY001", "Student shows signs of stress", StudentNotes.NoteType.General, 2, 5, 7));
        studentNoteRepository.save(new StudentNotes("N002", "S002", "PSY002", "Student exhibits anxiety symptoms", StudentNotes.NoteType.Behavior, 3, 2, 5));
        studentNoteRepository.save(new StudentNotes("N003", "S003", "PSY001", "Student behavior is normal", StudentNotes.NoteType.Behavior, 1, 1, 1));

        // Initialize User Logs
        userLogRepository.save(new UserLogs("L001", userRepository.findByUsername("jane_smith").getUserId(), "192.168.0.1"));
        userLogRepository.save(new UserLogs("L002", userRepository.findByUsername("john_doe").getUserId(), "192.168.0.2"));

        // Initialize Blogs
        blogRepository.save(new Blog("B001", "Managing Stress", userRepository.findByUsername("dr_brown").getUserId(), "Tips for managing stress..."));
        blogRepository.save(new Blog("B002", "Overcoming Anxiety", userRepository.findByUsername("staff_user").getUserId(), "Strategies to cope with anxiety..."));

        // Initialize Appointments
        appointmentRepository.save(new Appointments("APP001", "TS150601", "S001", "PSY001", Appointments.Status.Scheduled, "https://example.com/meeting1", Appointments.AppointmentType.Online));
        appointmentRepository.save(new Appointments("APP002", "TS150602", "S002", "PSY002", Appointments.Status.Scheduled, "https://example.com/meeting2", Appointments.AppointmentType.Online));

        // Initialize Appointment History
        appointmentHistoryRepository.save(new AppointmentHistory("H001", "APP001", AppointmentHistory.Action.Created, AppointmentHistory.Status.Scheduled, userRepository.findByUsername("dr_brown").getUserId()));
        appointmentHistoryRepository.save(new AppointmentHistory("H002", "APP002", AppointmentHistory.Action.Created, AppointmentHistory.Status.Scheduled, userRepository.findByUsername("dr_blue").getUserId()));

        // Initialize Notifications
        notificationRepository.save(new Notifications("NOT001", userRepository.findByUsername("jane_smith").getUserId(), "Appointment Scheduled", "Your appointment is scheduled for 2023-06-15 at 10:00 AM", Notifications.Type.Appointment));
        notificationRepository.save(new Notifications("NOT002", userRepository.findByUsername("john_doe").getUserId(), "Survey Available", "A new survey is available for you to complete", Notifications.Type.Survey));
    }

    @Override
    public void run(String... args) {
        try {
            if (userRepository.count() != 0) {
                return;
            }
            this.initialize();
        } catch (Exception e) {
            e.initCause(e);
        }
    }
}