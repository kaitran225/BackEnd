package com.healthy.BackEnd.init;

import com.healthy.BackEnd.entity.*;
import com.healthy.BackEnd.repository.*;
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

    private void initialize() {
        userRepository.save(new Users("U000", "admin", "adminpass", "Admin User", "admin@example.com", "1111111111", Users.UserRole.MANAGER, Users.Gender.Male));

        Users user1 = new Users("U001", "john_doe", "password123", "John Doe", "john@example.com", "1234567890", Users.UserRole.STUDENT, Users.Gender.Male);
        userRepository.save(user1);

        Users user2 = new Users("U002", "jane_smith", "password456", "Jane Smith", "jane@example.com", "9876543210", Users.UserRole.PARENT, Users.Gender.Female);
        userRepository.save(user2);

        Users user3 = new Users("U003", "dr_brown", "password789", "Dr. Brown", "brown@example.com", "5555555555", Users.UserRole.PSYCHOLOGIST, Users.Gender.Male);
        userRepository.save(user3);

        Users user4 = new Users("U004", "staff_user", "staffpass", "Staff Member", "staff@example.com", "9999999999", Users.UserRole.MANAGER, Users.Gender.Female);
        userRepository.save(user4);

        Users user5 = new Users("U005", "alice_jones", "alicepass", "Alice Jones", "alice@example.com", "2222222222", Users.UserRole.STUDENT, Users.Gender.Female);
        userRepository.save(user5);

        Users user6 = new Users("U006", "john_jan", "password123", "John Jan", "jan@example.com", "1234567890", Users.UserRole.STUDENT, Users.Gender.Male);
        userRepository.save(user6);

        Users user7 = new Users("U007", "dr_blue", "password789", "Dr. Blue", "blue@example.com", "5555555555", Users.UserRole.PSYCHOLOGIST, Users.Gender.Male);
        userRepository.save(user7);

        // Initialize Students
        Students student1 = new Students("S001", user1.getUserId(), 10, "A", "Example High School", 5, 10, 2);
        studentRepository.save(student1);

        Students student2 = new Students("S002", user5.getUserId(), 9, "B", "Example High School", 3, 4, 7);
        studentRepository.save(student2);

        Students student3 = new Students("S003", user6.getUserId(), 9, "A", "Example High School", 2, 2, 1);
        studentRepository.save(student3);

        // Initialize Parents
        Parents parent1 = new Parents("P001", user2.getUserId(), student1.getStudentID());
        parentRepository.save(parent1);

        Parents parent2 = new Parents("P002", user5.getUserId(), student2.getStudentID());
        parentRepository.save(parent2);

        // Initialize Psychologists
        Psychologists psychologist1 = new Psychologists("PSY001", user3.getUserId(), "Child Psychology", 10, Psychologists.Status.Active);
        psychologistRepository.save(psychologist1);

        Psychologists psychologist2 = new Psychologists("PSY002", user7.getUserId(), "Adolescent Psychology", 8, Psychologists.Status.Active);
        psychologistRepository.save(psychologist2);

        // Initialize Time Slots
        TimeSlots timeSlot1 = new TimeSlots("TS150601", psychologist1.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), TimeSlots.Status.Booked);
        timeSlotRepository.save(timeSlot1);

        TimeSlots timeSlot2 = new TimeSlots("TS150602", psychologist1.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"), TimeSlots.Status.Booked);
        timeSlotRepository.save(timeSlot2);

        TimeSlots timeSlot3 = new TimeSlots("TS150603", psychologist1.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot3);

        TimeSlots timeSlot4 = new TimeSlots("TS150604", psychologist1.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot4);

        TimeSlots timeSlot5 = new TimeSlots("TS150605", psychologist1.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot5);

        TimeSlots timeSlot6 = new TimeSlots("TS150606", psychologist1.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot6);

        TimeSlots timeSlot7 = new TimeSlots("TS150607", psychologist2.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot7);

        TimeSlots timeSlot8 = new TimeSlots("TS150608", psychologist2.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot8);

        TimeSlots timeSlot9 = new TimeSlots("TS150609", psychologist2.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot9);

        TimeSlots timeSlot10 = new TimeSlots("TS150610", psychologist2.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot10);

        TimeSlots timeSlot11 = new TimeSlots("TS150611", psychologist2.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot11);

        TimeSlots timeSlot12 = new TimeSlots("TS150612", psychologist2.getPsychologistID(), LocalDate.parse("2023-06-15"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"), TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot12);

        // Initialize Programs
        Programs program1 = new Programs("PRG001", "Stress Management", Programs.Category.Wellness, "Program to help manage stress", 20, 4, Programs.Status.Activate, "U004");
        programRepository.save(program1);

        Programs program2 = new Programs("PRG002", "Anxiety Support Group", Programs.Category.Wellness, "Support group for individuals with anxiety", 15, 6, Programs.Status.Activate, "U004");
        programRepository.save(program2);

        Programs program3 = new Programs("PRG003", "Mindfulness Workshop", Programs.Category.Wellness, "Workshop on mindfulness techniques", 25, 3, Programs.Status.Activate, "U004");
        programRepository.save(program3);

        // Initialize Program Schedule
        ProgramSchedule schedule1 = new ProgramSchedule("SCH001", program1.getProgramID(), "Monday", LocalTime.parse("10:00:00"), LocalTime.parse("11:30:00"));
        programScheduleRepository.save(schedule1);

        ProgramSchedule schedule2 = new ProgramSchedule("SCH002", program2.getProgramID(), "Tuesday", LocalTime.parse("14:00:00"), LocalTime.parse("15:30:00"));
        programScheduleRepository.save(schedule2);

        ProgramSchedule schedule3 = new ProgramSchedule("SCH003", program3.getProgramID(), "Wednesday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00"));
        programScheduleRepository.save(schedule3);

        // Initialize Program Participation
        ProgramParticipation participation1 = new ProgramParticipation("PP001", student1.getStudentID(), program1.getProgramID(), ProgramParticipation.Status.Completed, LocalDate.parse("2023-06-01"), LocalDate.parse("2023-06-30"));
        programParticipationRepository.save(participation1);

        ProgramParticipation participation2 = new ProgramParticipation("PP002", student2.getStudentID(), program2.getProgramID(), ProgramParticipation.Status.InProgress, LocalDate.parse("2023-07-01"), LocalDate.parse("2023-08-15"));
        programParticipationRepository.save(participation2);

        // Initialize Categories
        Categories category1 = new Categories("CAT001", Categories.MentalHealthCategory.Anxiety);
        categoryRepository.save(category1);

        Categories category2 = new Categories("CAT002", Categories.MentalHealthCategory.Stress);
        categoryRepository.save(category2);

        Categories category3 = new Categories("CAT003", Categories.MentalHealthCategory.Depression);
        categoryRepository.save(category3);

        // Initialize Surveys
        Surveys survey1 = new Surveys("SUR001", "Stress Survey", "Survey to assess stress levels", category1.getCategoryID(), user3.getUserId(), Surveys.Status.Finished);
        surveyRepository.save(survey1);

        Surveys survey2 = new Surveys("SUR002", "Anxiety Assessment", "Assessment of anxiety symptoms", category2.getCategoryID(), user3.getUserId(), Surveys.Status.Unfinished);
        surveyRepository.save(survey2);

        Surveys survey3 = new Surveys("SUR003", "Depression Screening", "Screening for depression", category3.getCategoryID(), user7.getUserId(), Surveys.Status.Unfinished);
        surveyRepository.save(survey3);

        // Initialize Survey Questions
        SurveyQuestions question1 = new SurveyQuestions("Q001", survey1.getSurveyID(), "How often do you feel stressed?", category1.getCategoryID());
        surveyQuestionRepository.save(question1);

        SurveyQuestions question2 = new SurveyQuestions("Q002", survey1.getSurveyID(), "How much does stress interfere with your daily life?", category1.getCategoryID());
        surveyQuestionRepository.save(question2);

        SurveyQuestions question3 = new SurveyQuestions("Q003", survey2.getSurveyID(), "Do you experience excessive worry or fear?", category2.getCategoryID());
        surveyQuestionRepository.save(question3);

        SurveyQuestions question4 = new SurveyQuestions("Q004", survey2.getSurveyID(), "How often do you have panic attacks?", category2.getCategoryID());
        surveyQuestionRepository.save(question4);

        SurveyQuestions question5 = new SurveyQuestions("Q005", survey3.getSurveyID(), "Do you feel sad or hopeless most of the time?", category3.getCategoryID());
        surveyQuestionRepository.save(question5);

        SurveyQuestions question6 = new SurveyQuestions("Q006", survey3.getSurveyID(), "Have you lost interest in activities you once enjoyed?", category3.getCategoryID());
        surveyQuestionRepository.save(question6);

        // Initialize Answerss
        Answers answer1 = new Answers("A001", question1.getQuestionID(), "Never", 0);
        answersRepository.save(answer1);

        Answers answer2 = new Answers("A002", question1.getQuestionID(), "Sometimes", 1);
        answersRepository.save(answer2);

        Answers answer3 = new Answers("A003", question1.getQuestionID(), "Often", 2);
        answersRepository.save(answer3);

        Answers answer4 = new Answers("A004", question1.getQuestionID(), "Always", 3);
        answersRepository.save(answer4);

        Answers answer5 = new Answers("A005", question2.getQuestionID(), "Never", 0);
        answersRepository.save(answer5);

        Answers answer6 = new Answers("A006", question2.getQuestionID(), "Sometimes", 1);
        answersRepository.save(answer6);

        Answers answer7 = new Answers("A007", question2.getQuestionID(), "Moderately", 2);
        answersRepository.save(answer7);

        Answers answer8 = new Answers("A008", question2.getQuestionID(), "Very much", 3);
        answersRepository.save(answer8);

        Answers answer9 = new Answers("A009", question3.getQuestionID(), "Rarely", 0);
        answersRepository.save(answer9);

        Answers answer10 = new Answers("A010", question3.getQuestionID(), "Sometimes", 1);
        answersRepository.save(answer10);

        Answers answer11 = new Answers("A011", question3.getQuestionID(), "Often", 2);
        answersRepository.save(answer11);

        Answers answer12 = new Answers("A012", question3.getQuestionID(), "Always", 3);
        answersRepository.save(answer12);

        Answers answer13 = new Answers("A013", question4.getQuestionID(), "Never", 0);
        answersRepository.save(answer13);

        Answers answer14 = new Answers("A014", question4.getQuestionID(), "Once a month", 1);
        answersRepository.save(answer14);

        Answers answer15 = new Answers("A015", question4.getQuestionID(), "Once a week", 2);
        answersRepository.save(answer15);

        Answers answer16 = new Answers("A016", question4.getQuestionID(), "Multiple times a week", 3);
        answersRepository.save(answer16);

        Answers answer17 = new Answers("A017", question5.getQuestionID(), "Never", 0);
        answersRepository.save(answer17);

        Answers answer18 = new Answers("A018", question5.getQuestionID(), "Sometimes", 1);
        answersRepository.save(answer18);

        Answers answer19 = new Answers("A019", question5.getQuestionID(), "Often", 2);
        answersRepository.save(answer19);

        Answers answer20 = new Answers("A020", question5.getQuestionID(), "Always", 3);
        answersRepository.save(answer20);

        Answers answer21 = new Answers("A021", question6.getQuestionID(), "Never", 0);
        answersRepository.save(answer21);

        Answers answer22 = new Answers("A022", question6.getQuestionID(), "Sometimes", 1);
        answersRepository.save(answer22);

        Answers answer23 = new Answers("A023", question6.getQuestionID(), "Often", 2);
        answersRepository.save(answer23);

        Answers answer24 = new Answers("A024", question6.getQuestionID(), "Always", 3);
        answersRepository.save(answer24);

        // Initialize Survey Results
        SurveyResults result1 = new SurveyResults("R001", student1.getStudentID(), question1.getQuestionID(), answer2.getAnswerID());
        surveyResultRepository.save(result1);

        SurveyResults result2 = new SurveyResults("R002", student1.getStudentID(), question2.getQuestionID(), answer6.getAnswerID());
        surveyResultRepository.save(result2);

        // Initialize Student Notes
        StudentNotes note1 = new StudentNotes("N001", student1.getStudentID(), psychologist1.getPsychologistID(), "Student shows signs of stress", StudentNotes.NoteType.General, 2, 5, 7);
        studentNoteRepository.save(note1);

        StudentNotes note2 = new StudentNotes("N002", student2.getStudentID(), psychologist2.getPsychologistID(), "Student exhibits anxiety symptoms", StudentNotes.NoteType.Behavior, 3, 2, 5);
        studentNoteRepository.save(note2);

        StudentNotes note3 = new StudentNotes("N003", student3.getStudentID(), psychologist1.getPsychologistID(), "Student behavior is normal", StudentNotes.NoteType.Behavior, 1, 1, 1);
        studentNoteRepository.save(note3);

        // Initialize User Logs
        UserLogs log1 = new UserLogs("L001", user1.getUserId(), "192.168.0.1");
        userLogRepository.save(log1);

        UserLogs log2 = new UserLogs("L002", user2.getUserId(), "192.168.0.2");
        userLogRepository.save(log2);

        // Initialize Blogs
        Blog blog1 = new Blog("B001", "Managing Stress", user3.getUserId(), "Tips for managing stress...");
        blogRepository.save(blog1);

        Blog blog2 = new Blog("B002", "Overcoming Anxiety", user4.getUserId(), "Strategies to cope with anxiety...");
        blogRepository.save(blog2);

        // Initialize Appointments
        Appointments appointment1 = new Appointments("APP001", timeSlot1.getTimeSlotsID(), student1.getStudentID(), psychologist1.getPsychologistID(), "https://example.com/meeting1");
        appointmentRepository.save(appointment1);

        Appointments appointment2 = new Appointments("APP002", timeSlot2.getTimeSlotsID(), student2.getStudentID(), psychologist2.getPsychologistID(), "https://example.com/meeting2");
        appointmentRepository.save(appointment2);

        // Initialize Appointment History
        AppointmentHistory history1 = new AppointmentHistory("H001", appointment1.getAppointmentID(), AppointmentHistory.Action.Created, AppointmentHistory.Status.Scheduled, user3.getUserId());
        appointmentHistoryRepository.save(history1);

        AppointmentHistory history2 = new AppointmentHistory("H002", appointment2.getAppointmentID(), AppointmentHistory.Action.Created, AppointmentHistory.Status.Scheduled, user7.getUserId());
        appointmentHistoryRepository.save(history2);

        // Initialize Notifications
        Notifications notification1 = new Notifications("NOT001", user1.getUserId(), "Appointment Scheduled", "Your appointment is scheduled for 2023-06-15 at 10:00 AM", Notifications.Type.Appointment);
        notificationRepository.save(notification1);

        Notifications notification2 = new Notifications("NOT002", user2.getUserId(), "Survey Available", "A new survey is available for you to complete", Notifications.Type.Survey);
        notificationRepository.save(notification2);
    }

    @Override
    public void run(String... args) throws Exception {
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