USE SWP391Healthy;

-- Insert sample data into Users table
INSERT INTO Users (UserID, Username, PasswordHash, FullName, Email, PhoneNumber, Role)
VALUES 
    ('U001', 'john_doe', 'password123', 'John Doe', 'john@example.com', '1234567890', 'Student'),
    ('U002', 'jane_smith', 'password456', 'Jane Smith', 'jane@example.com', '9876543210', 'Parent'),
    ('U003', 'dr_brown', 'password789', 'Dr. Brown', 'brown@example.com', '5555555555', 'Psychologist'),
    ('U004', 'admin', 'adminpass', 'Admin User', 'admin@example.com', '1111111111', 'Manager'),
    ('U005', 'staff_user', 'staffpass', 'Staff Member', 'staff@example.com', '9999999999', 'Staff'),
    ('U006', 'alice_jones', 'alicepass', 'Alice Jones', 'alice@example.com', '2222222222', 'Student'),
    ('U007', 'bob_wilson', 'bobpass', 'Bob Wilson', 'bob@example.com', '3333333333', 'Parent'),
    ('U008', 'dr_green', 'greenpass', 'Dr. Green', 'green@example.com', '4444444444', 'Psychologist'),
    ('U009', 'manager2', 'manager2pass', 'Manager 2', 'manager2@example.com', '6666666666', 'Manager'),
    ('U010', 'staff2', 'staff2pass', 'Staff 2', 'staff2@example.com', '7777777777', 'Staff'),
    ('U011', 'charlie_davis', 'charliepass', 'Charlie Davis', 'charlie@example.com', '8888888888', 'Student'),
    ('U012', 'emma_smith', 'emmapass', 'Emma Smith', 'emma@example.com', '1112223333', 'Student'),
    ('U013', 'liam_johnson', 'liampass', 'Liam Johnson', 'liam@example.com', '2223334444', 'Student'),
    ('U014', 'olivia_brown', 'oliviapass', 'Olivia Brown', 'olivia@example.com', '3334445555', 'Student'),
    ('U015', 'noah_davis', 'noahpass', 'Noah Davis', 'noah@example.com', '4445556666', 'Student'),
    ('U016', 'ava_miller', 'avapass', 'Ava Miller', 'ava@example.com', '5556667777', 'Student'),
    ('U017', 'ethan_wilson', 'ethanpass', 'Ethan Wilson', 'ethan@example.com', '6667778888', 'Student'),
    ('U018', 'sophia_moore', 'sophiapass', 'Sophia Moore', 'sophia@example.com', '7778889999', 'Student'),
    ('U019', 'jackson_taylor', 'jacksonpass', 'Jackson Taylor', 'jackson@example.com', '8889990000', 'Parent'),
    ('U020', 'isabella_anderson', 'isabellapass', 'Isabella Anderson', 'isabella@example.com', '9990001111', 'Parent'),
    ('U021', 'aiden_thomas', 'aidenpass', 'Aiden Thomas', 'aiden@example.com', '0001112222', 'Parent'),
    ('U022', 'mia_jackson', 'miapass', 'Mia Jackson', 'mia@example.com', '1112223333', 'Parent'),
    ('U023', 'william_white', 'williampass', 'William White', 'william@example.com', '2223334444', 'Parent'),
    ('U024', 'charlotte_harris', 'charlottepass', 'Charlotte Harris', 'charlotte@example.com', '3334445555', 'Parent'),
    ('U025', 'james_martin', 'jamespass', 'James Martin', 'james@example.com', '4445556666', 'Parent'),
    ('U026', 'amelia_thompson', 'ameliapass', 'Amelia Thompson', 'amelia@example.com', '5556667777', 'Parent'),
    ('U027', 'dr_gray', 'graypass', 'Dr. Gray', 'gray@example.com', '6667778888', 'Psychologist'),
    ('U028', 'dr_blue', 'bluepass', 'Dr. Blue', 'blue@example.com', '7778889999', 'Psychologist'),
    ('U029', 'dr_red', 'redpass', 'Dr. Red', 'red@example.com', '8889990000', 'Psychologist'),
    ('U030', 'dr_orange', 'orangepass', 'Dr. Orange', 'orange@example.com', '9990001111', 'Psychologist'),
    ('U031', 'dr_purple', 'purplepass', 'Dr. Purple', 'purple@example.com', '0001112222', 'Psychologist'),
    ('U032', 'dr_pink', 'pinkpass', 'Dr. Pink', 'pink@example.com', '1112223333', 'Psychologist'),
    ('U033', 'dr_yellow', 'yellowpass', 'Dr. Yellow', 'yellow@example.com', '2223334444', 'Psychologist'),
    ('U034', 'dr_black', 'blackpass', 'Dr. Black', 'black@example.com', '3334445555', 'Psychologist');

-- Insert sample data into Students table
INSERT INTO Students (StudentID, UserID, Grade, Class, SchoolName, Gender)
VALUES
    ('S001', 'U001', 10, 'A', 'Example High School', 'Male'),
    ('S002', 'U006', 9, 'B', 'Example High School', 'Female'),
    ('S003', 'U011', 11, 'C', 'Example High School', 'Male'),
    ('S004', 'U012', 10, 'A', 'Example High School', 'Female'),
    ('S005', 'U013', 9, 'B', 'Example High School', 'Male'),
    ('S006', 'U014', 11, 'C', 'Example High School', 'Female'),
    ('S007', 'U015', 10, 'A', 'Example High School', 'Male'),
    ('S008', 'U016', 9, 'B', 'Example High School', 'Female'),
    ('S009', 'U017', 11, 'C', 'Example High School', 'Male'),
    ('S010', 'U018', 10, 'A', 'Example High School', 'Female');

-- Insert sample data into Parents table
INSERT INTO Parents (ParentID, UserID, ChildID)
VALUES
    ('P001', 'U002', 'S001'),
    ('P002', 'U007', 'S002'),
    ('P003', 'U019', 'S003'),
    ('P004', 'U020', 'S004'),
    ('P005', 'U021', 'S005'),
    ('P006', 'U022', 'S006'),
    ('P007', 'U023', 'S007'),
    ('P008', 'U024', 'S008'),
    ('P009', 'U025', 'S009'),
    ('P010', 'U026', 'S010');

-- Insert sample data into Psychologists table
INSERT INTO Psychologists (PsychologistID, UserID, Specialization, YearsOfExperience, AvailableSlot, Status)
VALUES
    ('PSY001', 'U003', 'Child Psychology', 10, 'TS001', 'Active'),
    ('PSY002', 'U008', 'Adolescent Psychology', 8, 'TS002', 'Active'),
    ('PSY003', 'U027', 'Family Therapy', 12, 'TS003', 'Active'),
    ('PSY004', 'U028', 'Cognitive Behavioral Therapy', 6, 'TS004', 'Active'),
    ('PSY005', 'U029', 'Trauma Counseling', 9, 'TS005', 'Active'),
    ('PSY006', 'U030', 'Addiction Counseling', 7, 'TS006', 'Active'),
    ('PSY007', 'U031', 'Grief Counseling', 11, 'TS007', 'Active'),
    ('PSY008', 'U032', 'Couples Therapy', 5, 'TS008', 'Active'),
    ('PSY009', 'U033', 'Anxiety Disorders', 10, 'TS009', 'Active'),
    ('PSY010', 'U034', 'Depression Treatment', 8, 'TS010', 'Active');

-- Insert sample data into Programs table
INSERT INTO Programs (ProgramID, ProgramName, Category, Description, NumberParticipants, Duration, ManagedByStaffID)
VALUES
    ('PRG001', 'Stress Management', 'Wellness', 'Program to help manage stress', 20, 4, 'U005'),
    ('PRG002', 'Anxiety Support Group', 'Support Group', 'Support group for individuals with anxiety', 15, 6, 'U010'),
    ('PRG003', 'Mindfulness Workshop', 'Wellness', 'Workshop on mindfulness techniques', 25, 3, 'U005'),
    ('PRG004', 'Social Skills Training', 'Life Skills', 'Training program for improving social skills', 18, 8, 'U010'),
    ('PRG005', 'Anger Management', 'Emotional', 'Program to help manage anger and emotions', 12, 5, 'U005'),
    ('PRG006', 'Study Skills Workshop', 'Academic', 'Workshop on effective study techniques', 30, 2, 'U010'),
    ('PRG007', 'Self-Esteem Building', 'Emotional', 'Program to build self-esteem and confidence', 16, 6, 'U005'),
    ('PRG008', 'Substance Abuse Prevention', 'Prevention', 'Program to prevent substance abuse among students', 22, 4, 'U010'),
    ('PRG009', 'Conflict Resolution Training', 'Life Skills', 'Training program for resolving conflicts effectively', 14, 7, 'U005'),
    ('PRG010', 'Grief Support Group', 'Support Group', 'Support group for individuals dealing with grief', 10, 8, 'U010'),
    ('PRG011', 'Assertiveness Training', 'Life Skills', 'Training program to develop assertiveness skills', 20, 6, 'U005'),
    ('PRG012', 'Eating Disorder Support', 'Support Group', 'Support group for individuals with eating disorders', 12, 10, 'U010'),
    ('PRG013', 'Parenting Skills Workshop', 'Life Skills', 'Workshop on effective parenting techniques', 25, 4, 'U005'),
    ('PRG014', 'Trauma Recovery Group', 'Support Group', 'Group therapy for individuals recovering from trauma', 8, 12, 'U010'),
    ('PRG015', 'Stress Reduction Techniques', 'Wellness', 'Program teaching stress reduction techniques', 18, 5, 'U005');

-- Insert sample data into ProgramSchedule table
INSERT INTO ProgramSchedule (ScheduleID, ProgramID, DayOfWeek, StartTime, EndTime)
VALUES
    ('SCH001', 'PRG001', 'Monday', '10:00:00', '11:30:00'),
    ('SCH002', 'PRG002', 'Tuesday', '14:00:00', '15:30:00'),
    ('SCH003', 'PRG003', 'Wednesday', '09:00:00', '10:30:00'),
    ('SCH004', 'PRG004', 'Thursday', '16:00:00', '17:30:00'),
    ('SCH005', 'PRG005', 'Friday', '11:00:00', '12:30:00'),
    ('SCH006', 'PRG006', 'Monday', '13:00:00', '14:30:00'),
    ('SCH007', 'PRG007', 'Tuesday', '10:00:00', '11:30:00'),
    ('SCH008', 'PRG008', 'Wednesday', '15:00:00', '16:30:00'),
    ('SCH009', 'PRG009', 'Thursday', '09:00:00', '10:30:00'),
    ('SCH010', 'PRG010', 'Friday', '14:00:00', '15:30:00');

-- Insert sample data into ProgramParticipation table
INSERT INTO ProgramParticipation (ParticipationID, StudentID, ProgramID, StartDate, EndDate)
VALUES
    ('PP001', 'S001', 'PRG001', '2023-06-01', '2023-06-30'),
    ('PP002', 'S002', 'PRG002', '2023-07-01', '2023-08-15'),
    ('PP003', 'S003', 'PRG003', '2023-08-01', '2023-08-31'),
    ('PP004', 'S004', 'PRG004', '2023-09-01', '2023-10-31'),
    ('PP005', 'S005', 'PRG005', '2023-11-01', '2023-12-15'),
    ('PP006', 'S006', 'PRG006', '2024-01-01', '2024-01-31'),
    ('PP007', 'S007', 'PRG007', '2024-02-01', '2024-03-15'),
    ('PP008', 'S008', 'PRG008', '2024-04-01', '2024-04-30'),
    ('PP009', 'S009', 'PRG009', '2024-05-01', '2024-06-30'),
    ('PP010', 'S010', 'PRG010', '2024-07-01', '2024-08-31');

-- Insert sample data into Categories table
INSERT INTO Categories (CategoryID, CategoryName)
VALUES
    ('CAT001', 'Stress'),
    ('CAT002', 'Anxiety'),
    ('CAT003', 'Depression'),
    ('CAT004', 'Relationships'),
    ('CAT005', 'Self-Esteem'),
    ('CAT006', 'Anger Management'),
    ('CAT007', 'Substance Abuse'),
    ('CAT008', 'Grief'),
    ('CAT009', 'Trauma'),
    ('CAT010', 'Academic Performance');

-- Insert sample data into Surveys table
INSERT INTO Surveys (SurveyID, SurveyName, Description, CategoryID, CreatedBy)
VALUES
    ('SUR001', 'Stress Survey', 'Survey to assess stress levels', 'CAT001', 'U003'),
    ('SUR002', 'Anxiety Assessment', 'Assessment of anxiety symptoms', 'CAT002', 'U008'),
    ('SUR003', 'Depression Screening', 'Screening for depression', 'CAT003', 'U027'),
    ('SUR004', 'Relationship Satisfaction Survey', 'Survey on relationship satisfaction', 'CAT004', 'U028'),
    ('SUR005', 'Self-Esteem Questionnaire', 'Questionnaire to measure self-esteem', 'CAT005', 'U029'),
    ('SUR006', 'Anger Management Assessment', 'Assessment of anger management skills', 'CAT006', 'U030'),
    ('SUR007', 'Substance Abuse Risk Survey', 'Survey to assess risk of substance abuse', 'CAT007', 'U031'),
    ('SUR008', 'Grief Inventory', 'Inventory to assess grief reactions', 'CAT008', 'U032'),
    ('SUR009', 'Trauma Symptom Checklist', 'Checklist of trauma symptoms', 'CAT009', 'U033'),
    ('SUR010', 'Academic Performance Survey', 'Survey on academic performance and study habits', 'CAT010', 'U034'),
    ('SUR011', 'Substance Abuse Assessment', 'Assessment of substance abuse behaviors', 'CAT007', 'U035'),
    ('SUR012', 'Grief Coping Survey', 'Survey on coping strategies for grief', 'CAT008', 'U036'),
    ('SUR013', 'Trauma Impact Questionnaire', 'Questionnaire assessing the impact of trauma', 'CAT009', 'U037'),
    ('SUR014', 'Study Habits Inventory', 'Inventory of study habits and techniques', 'CAT010', 'U038'),
    ('SUR015', 'Social Support Assessment', 'Assessment of perceived social support', 'CAT004', 'U039');

-- Insert sample data into SurveyQuestions table
INSERT INTO SurveyQuestions (QuestionID, SurveyID, QuestionText, CategoryID)
VALUES
    ('Q001', 'SUR001', 'How often do you feel stressed?', 'CAT001'),
    ('Q002', 'SUR001', 'How much does stress interfere with your daily life?', 'CAT001'),
    ('Q003', 'SUR002', 'Do you experience excessive worry or fear?', 'CAT002'),
    ('Q004', 'SUR002', 'How often do you have panic attacks?', 'CAT002'),
    ('Q005', 'SUR003', 'Do you feel sad or hopeless most of the time?', 'CAT003'),
    ('Q006', 'SUR003', 'Have you lost interest in activities you once enjoyed?', 'CAT003'),
    ('Q007', 'SUR004', 'How satisfied are you with your current relationships?', 'CAT004'),
    ('Q008', 'SUR004', 'Do you feel supported by your partner or loved ones?', 'CAT004'),
    ('Q009', 'SUR005', 'Do you have a positive self-image?', 'CAT005'),
    ('Q010', 'SUR005', 'How confident are you in your abilities?', 'CAT005'),
    ('Q011', 'SUR006', 'How often do you have angry outbursts?', 'CAT006'),
    ('Q012', 'SUR006', 'Do you have difficulty controlling your temper?', 'CAT006'),
    ('Q013', 'SUR007', 'Have you ever used illegal substances?', 'CAT007'),
    ('Q014', 'SUR007', 'Do you feel the need to use substances to cope?', 'CAT007'),
    ('Q015', 'SUR008', 'How much has your grief impacted your daily functioning?', 'CAT008'),
    ('Q016', 'SUR008', 'Do you avoid reminders of your loss?', 'CAT008'),
    ('Q017', 'SUR009', 'Do you experience flashbacks of the traumatic event?', 'CAT009'),
    ('Q018', 'SUR009', 'Do you feel detached from others since the trauma?', 'CAT009'),
    ('Q019', 'SUR010', 'How often do you procrastinate on assignments?', 'CAT010'),
    ('Q020', 'SUR010', 'Do you have a regular study schedule?', 'CAT010');

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
    ('A008', 'Q002', 'Severely', 3),
    ('A009', 'Q003', 'Yes', 1),
    ('A010', 'Q003', 'No', 0),
    ('A011', 'Q004', 'Never', 0),
    ('A012', 'Q004', 'Rarely', 1),
    ('A013', 'Q004', 'Sometimes', 2),
    ('A014', 'Q004', 'Often', 3),
    ('A015', 'Q005', 'Yes', 1),
    ('A016', 'Q005', 'No', 0),
    ('A017', 'Q006', 'Yes', 1),
    ('A018', 'Q006', 'No', 0),
    ('A019', 'Q007', 'Very dissatisfied', 0),
    ('A020', 'Q007', 'Somewhat dissatisfied', 1),
    ('A021', 'Q007', 'Somewhat satisfied', 2),
    ('A022', 'Q007', 'Very satisfied', 3),
    ('A023', 'Q008', 'Never', 0),
    ('A024', 'Q008', 'Sometimes', 1),
    ('A025', 'Q008', 'Often', 2),
    ('A026', 'Q008', 'Always', 3),
    ('A027', 'Q009', 'Strongly disagree', 0),
    ('A028', 'Q009', 'Disagree', 1),
    ('A029', 'Q009', 'Agree', 2),
    ('A030', 'Q009', 'Strongly agree', 3),
    ('A031', 'Q010', 'Not at all confident', 0),
    ('A032', 'Q010', 'Somewhat confident', 1),
    ('A033', 'Q010', 'Moderately confident', 2),
    ('A034', 'Q010', 'Very confident', 3);

-- Insert sample data into SurveyResults table
INSERT INTO SurveyResults (ResultID, StudentID, QuestionID, AnswerID)
VALUES
    ('R001', 'S001', 'Q001', 'A002'),
    ('R002', 'S001', 'Q002', 'A006'),
    ('R003', 'S002', 'Q003', 'A009'),
    ('R004', 'S002', 'Q004', 'A012'),
    ('R005', 'S003', 'Q005', 'A015'),
    ('R006', 'S003', 'Q006', 'A018'),
    ('R007', 'S004', 'Q007', 'A021'),
    ('R008', 'S004', 'Q008', 'A025'),
    ('R009', 'S005', 'Q009', 'A029'),
    ('R010', 'S005', 'Q010', 'A033');

-- Insert sample data into StudentNotes table
INSERT INTO StudentNotes (NoteID, StudentID, PsychologistID, NoteText, NoteType)
VALUES
    ('N001', 'S001', 'PSY001', 'Student shows signs of stress', 'General'),
    ('N002', 'S002', 'PSY002', 'Student exhibits anxiety symptoms', 'Behavior'),
    ('N003', 'S003', 'PSY003', 'Student struggles with depression', 'Emotional'),
    ('N004', 'S004', 'PSY004', 'Student has relationship issues', 'General'),
    ('N005', 'S005', 'PSY005', 'Student lacks self-confidence', 'Emotional');

-- Insert sample data into UserLogs table
INSERT INTO UserLogs (LogID, UserID, IPAddress)
VALUES
    ('L001', 'U001', '192.168.0.1'),
    ('L002', 'U002', '192.168.0.2'),
    ('L003', 'U003', '192.168.0.3'),
    ('L004', 'U004', '192.168.0.4'),
    ('L005', 'U005', '192.168.0.5');

-- Insert sample data into Blog table
INSERT INTO Blog (BlogID, Title, CreatedBy, Content)
VALUES
    ('B001', 'Managing Stress', 'U003', 'Tips for managing stress...'),
    ('B002', 'Overcoming Anxiety', 'U004', 'Strategies to cope with anxiety...'),
    ('B003', 'Dealing with Depression', 'U005', 'Ways to combat depression...'),
    ('B004', 'Improving Relationships', 'U006', 'Advice for healthy relationships...'),
    ('B005', 'Building Self-Confidence', 'U007', 'Techniques to boost self-confidence...');

-- Insert sample data into TimeSlots table
INSERT INTO TimeSlots (TimeSlotID, SlotDate, SlotTime)
VALUES
    ('TS001', '2023-06-15', 1000),
    ('TS002', '2023-06-16', 1100),
    ('TS003', '2023-06-17', 1200),
    ('TS004', '2023-06-18', 1300),
    ('TS005', '2023-06-19', 1400);

-- Insert sample data into Appointments table
INSERT INTO Appointments (AppointmentID, SlotTime, StudentID, PsychologistID, MeetingLink)
VALUES
    ('APP001', 1000, 'S001', 'PSY001', 'https://example.com/meeting1'),
    ('APP002', 1100, 'S002', 'PSY002', 'https://example.com/meeting2'),
    ('APP003', 1200, 'S003', 'PSY003', 'https://example.com/meeting3'),
    ('APP004', 1300, 'S004', 'PSY004', 'https://example.com/meeting4'),
    ('APP005', 1400, 'S005', 'PSY005', 'https://example.com/meeting5');

-- Insert sample data into AppointmentHistory table
INSERT INTO AppointmentHistory (HistoryID, AppointmentID, Action, Status, ChangedBy)
VALUES
    ('H001', 'APP001', 'Created', 'Scheduled', 'U003'),
    ('H002', 'APP002', 'Created', 'Scheduled', 'U004'),
    ('H003', 'APP003', 'Created', 'Scheduled', 'U005'),
    ('H004', 'APP004', 'Created', 'Scheduled', 'U006'),
    ('H005', 'APP005', 'Created', 'Scheduled', 'U007');

-- Insert sample data into Notifications table
INSERT INTO Notifications (NotificationID, UserID, Title, Message, Type)
VALUES
    ('NOT001', 'U001', 'Appointment Scheduled', 'Your appointment is scheduled for 2023-06-15 at 10:00 AM', 'Appointment'),
    ('NOT002', 'U002', 'Survey Available', 'A new survey is available for you to complete', 'Survey'),
    ('NOT003', 'U003', 'Program Reminder', 'Reminder: Your program starts tomorrow at 9:00 AM', 'Program'),
    ('NOT004', 'U004', 'Appointment Completed', 'Your appointment has been marked as completed', 'Done'),
    ('NOT005', 'U005', 'Survey Reminder', 'Please complete the survey by the end of the week', 'Survey');