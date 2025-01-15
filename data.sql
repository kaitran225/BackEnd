-- 1. Bảng chính chứa thông tin người dùng
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

-- 2. Bảng phụ thuộc vào Users ..
CREATE TABLE Students (
    StudentID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    Grade INT,
    Class VARCHAR(20),
    SchoolName VARCHAR(100),
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
    YearsOfExperience INT,
    AvailableHours JSON,
    Status ENUM('Active', 'On Leave', 'Inactive') DEFAULT 'Active',
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- 3. Bảng chương trình và liên quan
CREATE TABLE Programs (
    ProgramID INT AUTO_INCREMENT PRIMARY KEY,
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
    NumberParticipants INT, -- Maximum number of participants
    Duration INT, -- Program duration day/week
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

-- 4. Bảng khảo sát và câu hỏi
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
);

CREATE TABLE Categories (
    CategoryID INT AUTO_INCREMENT PRIMARY KEY,
    CategoryName ENUM('Stress', 'Anxiety', 'Depression') NOT NULL UNIQUE
);

CREATE TABLE SurveyCategories (
    SurveyID INT NOT NULL,
    CategoryID INT NOT NULL,
    PRIMARY KEY (SurveyID, CategoryID),
    FOREIGN KEY (SurveyID) REFERENCES Surveys(SurveyID) ON DELETE CASCADE,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID) ON DELETE CASCADE
);

CREATE TABLE QuestionCategories (
    QuestionID INT NOT NULL,
    CategoryID INT NOT NULL,
    PRIMARY KEY (QuestionID, CategoryID),
    FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions(QuestionID) ON DELETE CASCADE,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID) ON DELETE CASCADE
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

CREATE TABLE SurveyResults (
    ResultID INT AUTO_INCREMENT PRIMARY KEY,
    StudentID INT NOT NULL,
    PsychologistID INT,
    SurveyID INT NOT NULL,
    QuestionID INT,
    DepressionScore DECIMAL(5,2),
    AnxietyScore DECIMAL(5,2),
    StressScore DECIMAL(5,2),
    LowSelfEsteemScore DECIMAL(5,2),
    SocialAnxietyScore DECIMAL(5,2),
    SleepIssueScore DECIMAL(5,2),
    Score VARCHAR(50),
    AssessmentDate DATE NOT NULL,
    Notes TEXT,
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE,
    FOREIGN KEY (PsychologistID) REFERENCES Psychologists(PsychologistID) ON DELETE SET NULL,
    FOREIGN KEY (SurveyID) REFERENCES Surveys(SurveyID) ON DELETE CASCADE,
    FOREIGN KEY (QuestionID) REFERENCES SurveyQuestions(QuestionID) ON DELETE SET NULL
);

-- 5. Bảng ghi chú và nhật ký
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

CREATE TABLE UserLogs (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    LoginTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IPAddress VARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- 6. Bảng bài viết và bình luận
CREATE TABLE Blog (
    BlogID INT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(100),
    Username VARCHAR(50) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Content TEXT,
    FOREIGN KEY (Username) REFERENCES Users(Username) ON DELETE CASCADE
);



-- 7. Bảng lịch hẹn và thông báo
CREATE TABLE TimeSlots (
    TimeSlotID INT AUTO_INCREMENT PRIMARY KEY,
    SlotDate DATE NOT NULL,
    PsychologistID INT NOT NULL,
    Status ENUM('Available', 'Booked') DEFAULT 'Available',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PsychologistID) REFERENCES Psychologists(PsychologistID) ON DELETE CASCADE
);

CREATE TABLE Appointments (
    AppointmentID INT AUTO_INCREMENT PRIMARY KEY,
    TimeSlotID INT NOT NULL,
    StudentID INT NOT NULL,
    PsychologistID INT NOT NULL,
    Status ENUM('Scheduled', 'Completed', 'Cancelled') DEFAULT 'Scheduled',
    Notes TEXT,
    MeetingLink VARCHAR(255),
    AppointmentType ENUM('Online', 'Offline') DEFAULT 'Offline',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (PsychologistID) REFERENCES Psychologists(PsychologistID) ON DELETE CASCADE,
    FOREIGN KEY (TimeSlotID) REFERENCES TimeSlots(TimeSlotID) ON DELETE CASCADE,
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE
);

CREATE TABLE AppointmentHistory (
    HistoryID INT AUTO_INCREMENT PRIMARY KEY,
    AppointmentID INT NOT NULL,
    Action ENUM('Created', 'Updated', 'Cancelled', 'Completed') [not null],
    Status ENUM('Scheduled', 'Completed', 'Cancelled') NOT NULL,
    ChangedBy ENUM('Student', 'Psychologist', 'Admin') [not null]    ChangeDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Notes TEXT DEFAULT NULL,
    Timestamp TIMESTAMP [default: 'CURRENT_TIMESTAMP'],
    FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID) ON DELETE CASCADE,
    FOREIGN KEY (ChangedBy) REFERENCES Psychologists(PsychologistID) ON DELETE CASCADE
);

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