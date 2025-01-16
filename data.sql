CREATE DATABASE SWP391Healthy;
USE SWP391Healthy;

CREATE TABLE Users (
    UserID VARCHAR(36) PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    FullName VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    PhoneNumber VARCHAR(15),
    Role ENUM('Student', 'Parent', 'Psychologist', 'Manager', 'Staff') NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Bảng phụ thuộc vào Users
CREATE TABLE Students (
    StudentID VARCHAR(36)  PRIMARY KEY,
    UserID VARCHAR(36) NOT NULL,
    Grade INT,
    Class VARCHAR(20),
    SchoolName VARCHAR(100),
    Gender ENUM('Male', 'Female', 'Other'),
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE Parents (
    ParentID VARCHAR(36)  PRIMARY KEY,
    UserID VARCHAR(36) NOT NULL,
    ChildID VARCHAR(36),
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ChildID) REFERENCES Students(StudentID) ON DELETE CASCADE
);

CREATE TABLE Psychologists (
    PsychologistID VARCHAR(36) PRIMARY KEY,
    UserID VARCHAR(36) NOT NULL,
    Specialization VARCHAR(100),
    YearsOfExperience INT,
    AvailableSlot VARCHAR(36),
    Status ENUM('Active', 'On Leave', 'Inactive') DEFAULT 'Active',
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (AvailableSlot) REFERENCES TimeSlots(TimeSlotID)
);



-- 3. Bảng chương trình và liên quan
CREATE TABLE Programs (
    ProgramID VARCHAR(36)  PRIMARY KEY,
    ProgramName VARCHAR(100) NOT NULL,
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
    Description TEXT,
    NumberParticipants INT,
    Duration INT,
    Status ENUM('Activate', 'Inactive') DEFAULT 'Activate',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ManagedByStaffID VARCHAR(36),
    FOREIGN KEY (ManagedByStaffID) REFERENCES Users(UserID)

);

CREATE TABLE ProgramSchedule (
    ScheduleID VARCHAR(36)  PRIMARY KEY,
    ProgramID VARCHAR(36) NOT NULL,
    DayOfWeek ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'),
    StartTime TIME,
    EndTime TIME,
    FOREIGN KEY (ProgramID) REFERENCES Programs(ProgramID) ON DELETE CASCADE
);

CREATE TABLE ProgramParticipation (
    ParticipationID VARCHAR(36)  PRIMARY KEY,
    StudentID VARCHAR(36) NOT NULL,
    ProgramID VARCHAR(36) NOT NULL,
    StartDate DATE,
    EndDate DATE,
    Status ENUM('In Progress', 'Completed', 'Cancelled') DEFAULT 'In Progress',
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE,
    FOREIGN KEY (ProgramID) REFERENCES Programs(ProgramID) ON DELETE CASCADE
);

-- 4. Bảng Categories và Surveys
CREATE TABLE Categories (
    CategoryID VARCHAR(36)  PRIMARY KEY,
    CategoryName ENUM('Stress', 'Anxiety', 'Depression') NOT NULL UNIQUE
);

CREATE TABLE Surveys (
    SurveyID VARCHAR(36)  PRIMARY KEY,
    SurveyName VARCHAR(100) NOT NULL,
    Description TEXT,
    CategoryID VARCHAR(36),
    CreatedBy VARCHAR(36) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Status ENUM('Unfinished', 'Finished', 'Cancelled') DEFAULT 'Unfinished',
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID),
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE SurveyQuestions (
    QuestionID VARCHAR(36)  PRIMARY KEY,
    SurveyID VARCHAR(36) NOT NULL,
    QuestionText TEXT NOT NULL,
    CategoryID VARCHAR(36),
    FOREIGN KEY (SurveyID) REFERENCES Surveys(SurveyID) ON DELETE CASCADE,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);

CREATE TABLE Answers (
    AnswerID VARCHAR(36)  PRIMARY KEY,
    QuestionID VARCHAR(36) NOT NULL,
    Answer TEXT NOT NULL,
    Score INT,
    FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions(QuestionID) ON DELETE CASCADE
);

CREATE TABLE SurveyResults (
    ResultID VARCHAR(36)  PRIMARY KEY,
    StudentID VARCHAR(36) NOT NULL,
    QuestionID VARCHAR(36) NOT NULL,
    AnswerID VARCHAR(36) NOT NULL,
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE,
    FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions(QuestionID) ON DELETE CASCADE,
    FOREIGN KEY (AnswerID) REFERENCES Answers(AnswerID) ON DELETE CASCADE
);

-- 5. Bảng ghi chú và nhật ký
CREATE TABLE StudentNotes (
    NoteID VARCHAR(36)  PRIMARY KEY,
    StudentID VARCHAR(36) NOT NULL,
    PsychologistID VARCHAR(36) NOT NULL,
    NoteText TEXT NOT NULL,
    NoteType ENUM('General', 'Behavior', 'Academic', 'Emotional') NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID),
    FOREIGN KEY (PsychologistID) REFERENCES Psychologists(PsychologistID)
);

CREATE TABLE UserLogs (
    LogID VARCHAR(36)  PRIMARY KEY,
    UserID VARCHAR(36) NOT NULL,
    LoginTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IPAddress VARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- 6. Bảng bài viết
CREATE TABLE Blog (
    BlogID VARCHAR(36) PRIMARY KEY,
    Title VARCHAR(100),
    CreatedBy VARCHAR(36),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Content TEXT,
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserID)
);

-- 7. Bảng lịch hẹn và thông báo
CREATE TABLE TimeSlots (
    TimeSlotID VARCHAR(36) PRIMARY KEY,
    SlotDate DATE NOT NULL,
    SlotTime INT NOT NULL,
    Status ENUM('Available', 'Booked') DEFAULT 'Available',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Appointments (
    AppointmentID VARCHAR(36) PRIMARY KEY,
    SlotTime INT,
    StudentID VARCHAR(36) NOT NULL,
    PsychologistID VARCHAR(36) NOT NULL,
    Status ENUM('Scheduled', 'Completed', 'Cancelled') DEFAULT 'Scheduled',
    Notes TEXT,
    MeetingLink VARCHAR(255),
    AppointmentType ENUM('Online', 'Offline') DEFAULT 'Offline',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (SlotTime) REFERENCES TimeSlots(TimeSlotID),
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE,
    FOREIGN KEY (PsychologistID) REFERENCES Psychologists(PsychologistID) ON DELETE CASCADE
);

CREATE TABLE AppointmentHistory (
    HistoryID VARCHAR(36)  PRIMARY KEY,
    AppointmentID VARCHAR(36) NOT NULL,
    Action ENUM('Created', 'Updated', 'Cancelled', 'Completed') NOT NULL,
    Status ENUM('Scheduled', 'Completed', 'Cancelled') NOT NULL,
    ChangedBy VARCHAR(36) NOT NULL,
    ChangeDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Notes TEXT,
    FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID) ON DELETE CASCADE,
    FOREIGN KEY (ChangedBy) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE Notifications (
    NotificationID VARCHAR(36)  PRIMARY KEY,
    UserID VARCHAR(36) NOT NULL,
    Title VARCHAR(255) NOT NULL,
    Message TEXT NOT NULL,
    Type ENUM('Appointment', 'Survey', 'Program', 'Done') NOT NULL,
    IsRead BOOLEAN DEFAULT FALSE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);



ALTER TABLE Students ADD FOREIGN KEY (UserID) REFERENCES Users (UserID);

ALTER TABLE Parents ADD FOREIGN KEY (UserID) REFERENCES Users (UserID);

ALTER TABLE Parents ADD FOREIGN KEY (ChildID) REFERENCES Students (StudentID);

ALTER TABLE Psychologists ADD FOREIGN KEY (UserID) REFERENCES Users (UserID);

ALTER TABLE Psychologists ADD FOREIGN KEY (AvailableSlot) REFERENCES TimeSlots (TimeSlotID);

ALTER TABLE Programs ADD FOREIGN KEY (ManagedByStaffID) REFERENCES Users (UserID);

ALTER TABLE ProgramSchedule ADD FOREIGN KEY (ProgramID) REFERENCES Programs (ProgramID);

ALTER TABLE ProgramParticipation ADD FOREIGN KEY (StudentID) REFERENCES Students (StudentID);

ALTER TABLE ProgramParticipation ADD FOREIGN KEY (ProgramID) REFERENCES Programs (ProgramID);

ALTER TABLE Surveys ADD FOREIGN KEY (CategoryID) REFERENCES Categories (CategoryID);

ALTER TABLE Surveys ADD FOREIGN KEY (CreatedBy) REFERENCES Psychologists (PsychologistID);

ALTER TABLE SurveyQuestions ADD FOREIGN KEY (SurveyID) REFERENCES Surveys (SurveyID);

ALTER TABLE SurveyQuestions ADD FOREIGN KEY (CategoryID) REFERENCES Categories (CategoryID);

ALTER TABLE Answers ADD FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions (QuestionID);

ALTER TABLE SurveyResults ADD FOREIGN KEY (StudentID) REFERENCES Students (StudentID);

ALTER TABLE SurveyResults ADD FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions (QuestionID);

ALTER TABLE SurveyResults ADD FOREIGN KEY (AnswerID) REFERENCES Answers (AnswerID);

ALTER TABLE StudentNotes ADD FOREIGN KEY (PsychologistID) REFERENCES Psychologists (PsychologistID);

ALTER TABLE StudentNotes ADD FOREIGN KEY (StudentID) REFERENCES Students (StudentID);

ALTER TABLE UserLogs ADD FOREIGN KEY (UserID) REFERENCES Users (UserID);

ALTER TABLE Blog ADD FOREIGN KEY (CreatedBy) REFERENCES Users (UserID);

ALTER TABLE Appointments ADD FOREIGN KEY (SlotTime) REFERENCES TimeSlots (TimeSlotID);

ALTER TABLE Appointments ADD FOREIGN KEY (StudentID) REFERENCES Students (StudentID);

ALTER TABLE Appointments ADD FOREIGN KEY (PsychologistID) REFERENCES Psychologists (PsychologistID);

ALTER TABLE AppointmentHistory ADD FOREIGN KEY (AppointmentID) REFERENCES Appointments (AppointmentID);

ALTER TABLE AppointmentHistory ADD FOREIGN KEY (ChangedBy) REFERENCES Users (UserID);

ALTER TABLE Notifications ADD FOREIGN KEY (UserID) REFERENCES Users (UserID);
