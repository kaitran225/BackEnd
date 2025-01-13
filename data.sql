
CREATE DATABASE DB;
USE DB;

CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    FullName VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    PhoneNumber VARCHAR(15),
    Role ENUM('Student', 'Parent', 'Psychologist', 'Manager') NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Students (
    StudentID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    -- REMOVED
        -- GradeLevel VARCHAR(20),
    -- MODIFIED
    Grade INT,
    Class VARCHAR(20),
    SchoolName VARCHAR(100),
    --
    Gender ENUM('Male', 'Female', 'Other'),
    Status ENUM('Active', 'Inactive') DEFAULT 'Active',
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE Parents (
    ParentID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    ChildID INT,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ChildID) REFERENCES Students(StudentID) ON DELETE CASCADE
);

CREATE TABLE Psychologists (
    PsychologistID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    Specialization VARCHAR(100),
    -- ADDED    
    YearsOfExperience INT,
    AvailableHours JSON,
    --
    Status ENUM('Active', 'On Leave', 'Inactive') DEFAULT 'Active',
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE Programs (
    ProgramID INT AUTO_INCREMENT PRIMARY KEY,
    ProgramName VARCHAR(100) NOT NULL,
    -- REMOVED
        -- Category ENUM('Cognitive', 'Social', 'Emotional', 'Physical') NOT NULL,
    -- MODIFIED
    Category ENUM(
        'Cognitive',
        'Social',
        'Emotional',
        'Physical',
        'Self Help',          
        'Wellness',            
        'Assessment',          
        'Support Group',                        
        'Life Skills',        
        'Prevention',        
        'Counseling'        
    ) NOT NULL,
    --
    Description TEXT,
    -- ADDED
    NumberParticipants INT,                 -- Maximum number of participants
--    MinAge INT,                          -- Minimum age requirement
--    MaxAge INT,                          -- Maximum age requirement
    Duration INT,                        -- Program duration day/week
    --
    Status ENUM('Activate', 'Inactive') DEFAULT 'Activate',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);
CREATE TABLE ProgramSchedule (
    ScheduleID INT AUTO_INCREMENT PRIMARY KEY,
    ProgramID INT NOT NULL,
    DayOfWeek ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'),
    StartTime TIME,
    EndTime TIME,
    FOREIGN KEY (ProgramID) REFERENCES Programs(ProgramID) ON DELETE CASCADE
);
CREATE TABLE ProgramParticipation (
    ParticipationID INT AUTO_INCREMENT PRIMARY KEY,
    StudentID INT NOT NULL,
    ProgramID INT NOT NULL,
    StartDate DATE,
    EndDate DATE,
    Status ENUM('In Progress', 'Completed', 'Cancelled') DEFAULT 'In Progress',
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE,
    FOREIGN KEY (ProgramID) REFERENCES Programs(ProgramID) ON DELETE CASCADE
);

-- Surveys management
-- Each survey can have multiple questions
CREATE TABLE Surveys (
    SurveyID INT AUTO_INCREMENT PRIMARY KEY,
    SurveyName VARCHAR(100) NOT NULL,
    Description TEXT,
    CreatedBy INT NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Status ENUM('Unfinished', 'Finished', 'Cancelled') DEFAULT 'Unfinished',
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE SurveyQuestions (
    QuestionID INT AUTO_INCREMENT PRIMARY KEY,
    SurveyID INT NOT NULL,
    QuestionText TEXT NOT NULL,
    FOREIGN KEY (SurveyID) REFERENCES Surveys(SurveyID) ON DELETE CASCADE
    FOREIGN KEY (AnswerSetID) REFERENCES SurveyAnswersSet(AnswerSetID) ON DELETE CASCADE
);
CREATE TABLE SurveyAnswersSet (
    AnswerSetID INT AUTO_INCREMENT PRIMARY KEY,
    QuestionID INT NOT NULL,
    TextAnswer TEXT,
    DepressionScore INT,
    AnxietyScore INT,
    StressScore INT,
    LowSelfEsteemScore INT,
    SocialAnxietyScore INT,
    SleepIssueScore INT,
    FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions(QuestionID) ON DELETE CASCADE
);

-- One survey can have multiple students and questions
-- One student can participate in multiple surveys
CREATE TABLE SurveyResponses (
    SurveyID INT NOT NULL,
    StudentID INT NOT NULL,
    QuestionID INT NOT NULL,
    Answer TEXT,
    SubmittedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(SurveyID, StudentID, QuestionID),
    FOREIGN KEY (SurveyID) REFERENCES Surveys(SurveyID) ON DELETE CASCADE,
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE,
    FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions(QuestionID) ON DELETE CASCADE
);
-- REMOVED
-- Multiple correct answers possible for one question
-- CREATE TABLE SurveyCorrectAnswers (
--     QuestionID INT,
--     CorrectAnswer TEXT NOT NULL,
--     PRIMARY KEY (QuestionID, CorrectAnswer),
--     FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions(QuestionID) ON DELETE CASCADE
-- ); -- Currently not used

CREATE TABLE UserLogs (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    LoginTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IPAddress VARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE Reports (
    ReportID INT AUTO_INCREMENT PRIMARY KEY,
    ReportName VARCHAR(100),
    GeneratedBy INT NOT NULL,
    GeneratedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Data JSON,
    FOREIGN KEY (GeneratedBy) REFERENCES Users(UserID) ON DELETE CASCADE
);
-- REMOVED
-- CREATE TABLE FixedTimeSlots (
--     SlotID INT AUTO_INCREMENT PRIMARY KEY,
--     StartTime TIME NOT NULL,
--     EndTime TIME NOT NULL,
--     Description VARCHAR(255) DEFAULT NULL,
--     CONSTRAINT chk_slot_time_valid CHECK (StartTime < EndTime)
-- ); --Currently not used

CREATE TABLE TimeSlots (
    TimeSlotID INT AUTO_INCREMENT PRIMARY KEY,
    SlotDate DATE NOT NULL,
    SlotID INT NOT NULL,
    PsychologistID INT NOT NULL,
    Status ENUM('Available', 'Booked') DEFAULT 'Available',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- REMOVED
    -- FOREIGN KEY (SlotID) REFERENCES FixedTimeSlots(SlotID) ON DELETE CASCADE, -- Currently not used
    FOREIGN KEY (PsychologistID) REFERENCES Psychologists(PsychologistID) ON DELETE CASCADE
);

CREATE TABLE Appointments (
    AppointmentID INT AUTO_INCREMENT PRIMARY KEY,
    TimeSlotID INT NOT NULL,
    StudentID INT NOT NULL,
    Status ENUM('Scheduled', 'Completed', 'Cancelled') DEFAULT 'Scheduled',
    Notes TEXT,
    MeetingLink VARCHAR(255),
    AppointmentType ENUM('Online', 'Offline') DEFAULT 'Offline',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (TimeSlotID) REFERENCES TimeSlots(TimeSlotID) ON DELETE CASCADE,
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE
);

CREATE TABLE AppointmentHistory (
    HistoryID INT AUTO_INCREMENT PRIMARY KEY,
    AppointmentID INT NOT NULL,
    OldStatus ENUM('Scheduled', 'Completed', 'Cancelled') NOT NULL,
    NewStatus ENUM('Scheduled', 'Completed', 'Cancelled') NOT NULL,
    ChangedBy INT NOT NULL,
    ChangeDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Notes TEXT DEFAULT NULL,
    FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID) ON DELETE CASCADE,
    FOREIGN KEY (ChangedBy) REFERENCES Psychologists(PsychologistID) ON DELETE CASCADE
);

-- Student notes for periodic assessments and feedback
CREATE TABLE StudentNotes (
    NoteID INT AUTO_INCREMENT PRIMARY KEY,
    StudentID INT NOT NULL,
    PsychologistID INT NOT NULL,
    NoteText TEXT NOT NULL,
    NoteType ENUM('General', 'Behavior', 'Academic', 'Emotional') NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID),
    FOREIGN KEY (PsychologistID) REFERENCES Psychologists(PsychologistID)
);

-- Notification management for email alerts
CREATE TABLE Notifications (
    NotificationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    Title VARCHAR(255) NOT NULL,
    Message TEXT NOT NULL,
    Type ENUM('Appointment', 'Survey', 'Program', 'done') NOT NULL,
    IsRead BOOLEAN DEFAULT FALSE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Progress tracking for support programs
CREATE TABLE StudentHealthRecords ( 
	-- MODIFIED Rename from StudentProgress
    RecordID INT AUTO_INCREMENT PRIMARY KEY,
    StudentID INT NOT NULL,
    PsychologistID INT NOT NULL,
    AssessmentDate DATE NOT NULL,
    -- REMOVED
        -- CognitiveScore DECIMAL(5,2),
        -- SocialScore DECIMAL(5,2),
        -- EmotionalScore DECIMAL(5,2),
        -- PhysicalScore DECIMAL(5,2),
    --
    -- MODIFIED
    -- Should be changed to a JSON object
    DepressionScore DECIMAL(5,2),
    AnxietyScore DECIMAL(5,2),
    StressScore DECIMAL(5,2),
    LowSelfEsteemScore DECIMAL(5,2),
    SocialAnxietyScore DECIMAL(5,2),    
    SleepIssueScore DECIMAL(5,2),  
    --
    Notes TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID),
    FOREIGN KEY (PsychologistID) REFERENCES Psychologists(PsychologistID)
);
-- ADDED


CREATE TABLE Feedback (
    FeedbackID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    RelatedTo ENUM('Program', 'Appointment', 'Psychologist', 'General') NOT NULL,
    RelatedID INT,                    
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

