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
	SELECT ass.AvailableSlotsID, t.SlotDate, t.SlotTime, p.PsychologistID, u.FullName
	FROM AvailableSlots ass
	JOIN TimeSlots t ON ass.AvailableSlotsID = t.AvailableSlotsID
	JOIN Psychologists p ON ass.PsychologistID = p.PsychologistID
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
