USE SWP391Healthy;
 -- Query to select all data from Users table
SELECT * FROM Users;

-- Query to select all data from Students table
SELECT * FROM Students;

-- Query to select all data from Parents table
SELECT * FROM Parents;

-- Query to select all data from Psychologists table
SELECT * FROM Psychologists;

-- Query to select all data from AvailableSlots table
SELECT * FROM AvailableSlots;

-- Query to select all data from TimeSlots table
SELECT * FROM TimeSlots;

-- Query to select all data from Programs table
SELECT * FROM Programs;

-- Query to select all data from ProgramSchedule table
SELECT * FROM ProgramSchedule;

-- Query to select all data from ProgramParticipation table
SELECT * FROM ProgramParticipation;

-- Query to select all data from Categories table
SELECT * FROM Categories;

-- Query to select all data from Surveys table
SELECT * FROM Surveys;

-- Query to select all data from SurveyQuestions table
SELECT * FROM SurveyQuestions;

-- Query to select all data from Answers table
SELECT * FROM Answers;

-- Query to select all data from SurveyResults table
SELECT * FROM SurveyResults;

-- Query to select all data from StudentNotes table
SELECT * FROM StudentNotes;

-- Query to select all data from UserLogs table
SELECT * FROM UserLogs;

-- Query to select all data from Blog table
SELECT * FROM Blog;

-- Query to select all data from Appointments table
SELECT * FROM Appointments;

-- Query to select all data from AppointmentHistory table
SELECT * FROM AppointmentHistory;

-- Query to select all data from Notifications table
SELECT * FROM Notifications;

-- Query to find available appointments for a specific psychologist
SELECT a.AppointmentID, a.SlotTime, a.StudentID, a.MeetingLink
FROM Appointments a
JOIN TimeSlots t ON a.SlotTime = t.TimeSlotsID
JOIN AvailableSlots ass ON t.AvailableSlotsID = a.AvailableSlotsID
WHERE ass.PsychologistID = 'PSY001' AND t.Status = 'Available';

-- Query to find all available slots for all psychologists and their names
-- 1 : 8h - 8h30
-- 2 : 8h30 - 9h
-- 3 : 9h - 9h30
-- 4 : 9h30 - 10h
-- 5 : 10h - 10h30
-- 6 : 10h30 - 11h
-- 7 : 11h - 11h30

-- 8 : 13h - 13h30
-- 9 : 13h30 - 14h
-- 10 : 14h - 14h30
-- 11 : 14h30 - 15h
-- 12 : 15h - 15h30
-- 13 : 15h30 - 16h
-- 14 : 16h - 16h30

SELECT t.SlotDate, t.StartTime, t.EndTime,
       CASE 
           WHEN t.StartTime = '08:00:00' THEN '1'
           WHEN t.StartTime = '08:30:00' THEN '2'
           WHEN t.StartTime = '09:00:00' THEN '3'
           WHEN t.StartTime = '09:30:00' THEN '4'
           WHEN t.StartTime = '10:00:00' THEN '5'
           WHEN t.StartTime = '10:30:00' THEN '6'
           WHEN t.StartTime = '11:00:00' THEN '7'
           WHEN t.StartTime = '13:00:00' THEN '8'
           WHEN t.StartTime = '13:30:00' THEN '9'
           WHEN t.StartTime = '14:00:00' THEN '10'
           WHEN t.StartTime = '14:30:00' THEN '11'
           WHEN t.StartTime = '15:00:00' THEN '12'
           WHEN t.StartTime = '15:30:00' THEN '13'
           WHEN t.StartTime = '16:00:00' THEN '14'
       END AS TimeSlot,
       p.PsychologistID, u.FullName
FROM TimeSlots t
JOIN Psychologists p ON t.PsychologistID = p.PsychologistID
JOIN Users u ON p.UserID = u.UserID
WHERE t.Status = 'Available';	

-- Query to find all available slots for all psychologists and their names
SELECT ass.AvailableSlotsID, t.SlotDate, t.SlotTime, a.AppointmentID, a.StudentID, p.PsychologistID, u.FullName
FROM AvailableSlots ass
JOIN TimeSlots t ON ass.AvailableSlotsID = t.AvailableSlotsID
JOIN Appointments a ON t.TimeSlotsID = a.SlotTime
JOIN Psychologists p ON ass.PsychologistID = p.PsychologistID
JOIN Users u ON p.UserID = u.UserID;


-- Query to find all psychologists and their details
SELECT p.PsychologistID, p.FullName, p.Specialization, p.YearsOfExperience, p.Status
FROM Psychologists p
JOIN Users u ON p.UserID = u.UserID; 
