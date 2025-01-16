USE SWP391Healthy;

-- Insert sample data into Users table
INSERT INTO Users (UserID, Username, PasswordHash, FullName, Email, PhoneNumber, Role)
VALUES 
    ('U000', 'admin', 'adminpass', 'Admin User', 'admin@example.com', '1111111111', 'Manager'),
    ('U001', 'john_doe', 'password123', 'John Doe', 'john@example.com', '1234567890', 'Student'),
    ('U002', 'jane_smith', 'password456', 'Jane Smith', 'jane@example.com', '9876543210', 'Parent'),
    ('U003', 'dr_brown', 'password789', 'Dr. Brown', 'brown@example.com', '5555555555', 'Psychologist'),
    ('U004', 'staff_user', 'staffpass', 'Staff Member', 'staff@example.com', '9999999999', 'Staff'),
    ('U005', 'alice_jones', 'alicepass', 'Alice Jones', 'alice@example.com', '2222222222', 'Student'),
	('U006', 'john_jan', 'password123', 'John Jan', 'jan@example.com', '1234567890', 'Student'),
    ('U007', 'dr_blue', 'password789', 'Dr. Blue', 'blue@example.com', '5555555555', 'Psychologist');
    
-- Insert sample data into Students table
INSERT INTO Students (StudentID, UserID, Grade, Class, SchoolName, Gender)
VALUES
    ('S001', 'U001', 10, 'A', 'Example High School', 'Male'),
    ('S002', 'U006', 9, 'B', 'Example High School', 'Female');
    
-- Insert sample data into Parents table
INSERT INTO Parents (ParentID, UserID, ChildID)
VALUES
    ('P001', 'U002', 'S001'),
    ('P002', 'U005', 'S002');

-- Insert sample data into Psychologists table

    
-- Insert sample data into TimeSlots table
INSERT INTO TimeSlots (TimeSlotID, SlotDate, SlotTime)
VALUES
    ('TS150601', '2023-06-15', 1),
    ('TS150602', '2023-06-15', 2),
    ('TS150603', '2023-06-15', 3),
    ('TS150604', '2023-06-15', 4),
    ('TS150605', '2023-06-15', 5),
    ('TS150606', '2023-06-15', 6);
    
INSERT INTO Psychologists (PsychologistID, UserID, Specialization, YearsOfExperience, AvailableSlot, Status)
VALUES
    ('PSY001', 'U003', 'Child Psychology', 10, 'TS150601', 'Active'),
    ('PSY002', 'U007', 'Adolescent Psychology', 8, 'TS150604', 'Active');

-- Insert sample data into Programs table
INSERT INTO Programs (ProgramID, ProgramName, Category, Description, NumberParticipants, Duration, ManagedByStaffID)
VALUES
    ('PRG001', 'Stress Management', 'Wellness', 'Program to help manage stress', 20, 4, 'U004'),
    ('PRG002', 'Anxiety Support Group', 'Support Group', 'Support group for individuals with anxiety', 15, 6, 'U004'),
    ('PRG003', 'Mindfulness Workshop', 'Wellness', 'Workshop on mindfulness techniques', 25, 3, 'U004');

-- Insert sample data into ProgramSchedule table
INSERT INTO ProgramSchedule (ScheduleID, ProgramID, DayOfWeek, StartTime, EndTime)
VALUES
    ('SCH001', 'PRG001', 'Monday', '10:00:00', '11:30:00'),
    ('SCH002', 'PRG002', 'Tuesday', '14:00:00', '15:30:00'),
    ('SCH003', 'PRG003', 'Wednesday', '09:00:00', '10:30:00');

-- Insert sample data into ProgramParticipation table
INSERT INTO ProgramParticipation (ParticipationID, StudentID, ProgramID, StartDate, EndDate)
VALUES
    ('PP001', 'S001', 'PRG001', '2023-06-01', '2023-06-30'),
    ('PP002', 'S002', 'PRG002', '2023-07-01', '2023-08-15');

-- Insert sample data into Categories table
INSERT INTO Categories (CategoryID, CategoryName)
VALUES
    ('CAT001', 'Stress'),
    ('CAT002', 'Anxiety'),
    ('CAT003', 'Depression');

-- Insert sample data into Surveys table
INSERT INTO Surveys (SurveyID, SurveyName, Description, CategoryID, CreatedBy)
VALUES
    ('SUR001', 'Stress Survey', 'Survey to assess stress levels', 'CAT001', 'U003'),
    ('SUR002', 'Anxiety Assessment', 'Assessment of anxiety symptoms', 'CAT002', 'U003'),
    ('SUR003', 'Depression Screening', 'Screening for depression', 'CAT003', 'U007');

-- Insert sample data into SurveyQuestions table
INSERT INTO SurveyQuestions (QuestionID, SurveyID, QuestionText, CategoryID)
VALUES
    ('Q001', 'SUR001', 'How often do you feel stressed?', 'CAT001'),
    ('Q002', 'SUR001', 'How much does stress interfere with your daily life?', 'CAT001'),
    ('Q003', 'SUR002', 'Do you experience excessive worry or fear?', 'CAT002'),
    ('Q004', 'SUR002', 'How often do you have panic attacks?', 'CAT002'),
    ('Q005', 'SUR003', 'Do you feel sad or hopeless most of the time?', 'CAT003'),
    ('Q006', 'SUR003', 'Have you lost interest in activities you once enjoyed?', 'CAT003');

-- Insert sample data into Answers table
INSERT INTO Answers (AnswerID, QuestionID, Answer, Score)
VALUES
    ('A001', 'Q001', 'Never', 0),
    ('A002', 'Q001', 'Sometimes', 1),
    ('A003', 'Q001', 'Often', 2),
    ('A004', 'Q001', 'Always', 3),
    ('A005', 'Q002', 'Not at all', 0),
    ('A006', 'Q002', 'A little', 1),
    ('A007', 'Q002', 'Moderately', 2),
    ('A008', 'Q002', 'Very much', 3),
    ('A009', 'Q003', 'Rarely', 0),
    ('A010', 'Q003', 'Sometimes', 1),
    ('A011', 'Q003', 'Often', 2),
    ('A012', 'Q003', 'Always', 3),
    ('A013', 'Q004', 'Never', 0),
    ('A014', 'Q004', 'Once a month', 1),
    ('A015', 'Q004', 'Once a week', 2),
    ('A016', 'Q004', 'Multiple times a week', 3),
    ('A017', 'Q005', 'Never', 0),
    ('A018', 'Q005', 'Sometimes', 1),
    ('A019', 'Q005', 'Often', 2),
    ('A020', 'Q005', 'Always', 3),
    ('A021', 'Q006', 'Never', 0),
    ('A022', 'Q006', 'Sometimes', 1),
    ('A023', 'Q006', 'Often', 2),
    ('A024', 'Q006', 'Always', 3);


-- Insert sample data into SurveyResults table
INSERT INTO SurveyResults (ResultID, StudentID, QuestionID, AnswerID)
VALUES
    ('R001', 'S001', 'Q001', 'A002'),
    ('R002', 'S001', 'Q002', 'A006');

-- Insert sample data into StudentNotes table
INSERT INTO StudentNotes (NoteID, StudentID, PsychologistID, NoteText, NoteType)
VALUES
    ('N001', 'S001', 'PSY001', 'Student shows signs of stress', 'General'),
    ('N002', 'S002', 'PSY002', 'Student exhibits anxiety symptoms', 'Behavior');

-- Insert sample data into UserLogs table
INSERT INTO UserLogs (LogID, UserID, IPAddress)
VALUES
    ('L001', 'U001', '192.168.0.1'),
    ('L002', 'U002', '192.168.0.2');

-- Insert sample data into Blog table
INSERT INTO Blog (BlogID, Title, CreatedBy, Content)
VALUES
    ('B001', 'Managing Stress', 'U003', 'Tips for managing stress...'),
    ('B002', 'Overcoming Anxiety', 'U004', 'Strategies to cope with anxiety...');

-- Insert sample data into Appointments table
INSERT INTO Appointments (AppointmentID, SlotTime, StudentID, PsychologistID, MeetingLink)
VALUES
    ('APP001', 1000, 'S001', 'PSY001', 'https://example.com/meeting1'),
    ('APP002', 1100, 'S002', 'PSY002', 'https://example.com/meeting2');

-- Insert sample data into AppointmentHistory table
INSERT INTO AppointmentHistory (HistoryID, AppointmentID, Action, Status, ChangedBy)
VALUES
    ('H001', 'APP001', 'Created', 'Scheduled', 'U003'),
    ('H002', 'APP002', 'Created', 'Scheduled', 'U004');

-- Insert sample data into Notifications table
INSERT INTO Notifications (NotificationID, UserID, Title, Message, Type)
VALUES
    ('NOT001', 'U001', 'Appointment Scheduled', 'Your appointment is scheduled for 2023-06-15 at 10:00 AM', 'Appointment'),
    ('NOT002', 'U002', 'Survey Available', 'A new survey is available for you to complete', 'Survey');