-- Indexes for Appointments table
CREATE INDEX idx_appointments_studentid ON Appointments(StudentID);
CREATE INDEX idx_appointments_psychologistid ON Appointments(PsychologistID);
CREATE INDEX idx_appointments_timeslotsid ON Appointments(TimeSlotsID);
CREATE INDEX idx_appointments_status ON Appointments(Status);
CREATE INDEX idx_appointments_createdat ON Appointments(CreatedAt);
CREATE INDEX idx_appointments_updatedat ON Appointments(UpdatedAt);

-- Indexes for Article table
CREATE INDEX idx_article_authorid ON Article(AuthorID);
CREATE INDEX idx_article_createdat ON Article(CreatedAt);
CREATE INDEX idx_article_likes ON Article(likes);

-- Indexes for Categories table
CREATE INDEX idx_categories_categoryname ON Categories(CategoryName);

-- Indexes for Comment table
CREATE INDEX idx_comment_programid ON Comments(ProgramID);
CREATE INDEX idx_comment_articleid ON Comments(ArticleID);
CREATE INDEX idx_comment_surveyid ON Comments(SurveyID);
CREATE INDEX idx_comment_appointmentid ON Comments(AppointmentID);
CREATE INDEX idx_comment_userid ON Comments(UserID);
CREATE INDEX idx_comment_createdat ON Comment(CreatedAt);
CREATE INDEX idx_comment_rating ON Comment(Rating);

-- Indexes for Department table
CREATE INDEX idx_department_name ON Department(Name);

-- Indexes for Feedback table
CREATE INDEX idx_feedback_appointmentid ON Feedback(AppointmentID);

-- Indexes for Notifications table
CREATE INDEX idx_notifications_userid ON Notifications(UserID);
CREATE INDEX idx_notifications_type ON Notifications(Type);
CREATE INDEX idx_notifications_isread ON Notifications(IsRead);
CREATE INDEX idx_notifications_createdat ON Notifications(CreatedAt);
CREATE INDEX idx_notifications_appointmentid ON Notifications(AppointmentID);
CREATE INDEX idx_notifications_programid ON Notifications(ProgramID);
CREATE INDEX idx_notifications_surveyid ON Notifications(SurveyID);

-- Indexes for Parents table
CREATE INDEX idx_parents_userid ON Parents(UserID);

-- Indexes for ProgramParticipation table
CREATE INDEX idx_programparticipation_studentid ON ProgramParticipation(StudentID); 
CREATE INDEX idx_programparticipation_programid ON ProgramParticipation(ProgramID);
CREATE INDEX idx_programparticipation_status ON ProgramParticipation(Status);

-- Indexes for Programs table
CREATE INDEX idx_programs_programname ON Programs(ProgramName);
CREATE INDEX idx_programs_status ON Programs(Status);
CREATE INDEX idx_programs_departmentid ON Programs(DepartmentID);
CREATE INDEX idx_programs_facilitatorid ON Programs(FacilitatorID);
CREATE INDEX idx_programs_createdat ON Programs(CreatedAt);
CREATE INDEX idx_programs_startdate ON Programs(StartDate);
CREATE INDEX idx_programs_enddate ON Programs(EndDate);
CREATE INDEX idx_programs_rating ON Programs(Rating);

-- Indexes for Psychologists table
CREATE INDEX idx_psychologists_userid ON Psychologists(UserID);
CREATE INDEX idx_psychologists_status ON Psychologists(Status);
CREATE INDEX idx_psychologists_departmentid ON Psychologists(DepartmentID);

-- Indexes for Students table
CREATE INDEX idx_students_userid ON Students(UserID);
CREATE INDEX idx_students_parentid ON Students(ParentID);
CREATE INDEX idx_students_anxietyscore ON Students(AnxietyScore);
CREATE INDEX idx_students_stressscore ON Students(StressScore);
CREATE INDEX idx_students_depressionscore ON Students(DepressionScore);

-- Indexes for SurveyQuestions table
CREATE INDEX idx_surveyquestions_surveyid ON SurveyQuestions(SurveyID);
CREATE INDEX idx_surveyquestions_categoryid ON SurveyQuestions(CategoryID);

-- Indexes for SurveyQuestionOptions table
CREATE INDEX idx_surveyquestionoptions_questionid ON SurveyQuestionOptions(QuestionID);

-- Indexes for SurveyQuestionOptionsChoices table
CREATE INDEX idx_surveyquestionoptionschoices_questionid ON SurveyQuestionOptionsChoices(QuestionID);
CREATE INDEX idx_surveyquestionoptionschoices_optionid ON SurveyQuestionOptionsChoices(OptionID);
CREATE INDEX idx_surveyquestionoptionschoices_resultid ON SurveyQuestionOptionsChoices(ResultID);

-- Indexes for SurveyResult table
CREATE INDEX idx_surveyresult_surveyid ON SurveyResult(SurveyID);
CREATE INDEX idx_surveyresult_studentid ON SurveyResult(StudentID);
CREATE INDEX idx_surveyresult_createdat ON SurveyResult(CreatedAt);
CREATE INDEX idx_surveyresult_result ON SurveyResult(Result);
CREATE INDEX idx_surveyresult_maxscore ON SurveyResult(MaxScore);


-- Indexes for Surveys table
CREATE INDEX idx_surveys_categoryid ON Surveys(CategoryID);
CREATE INDEX idx_surveys_createdby ON Surveys(CreatedBy);
CREATE INDEX idx_surveys_status ON Surveys(Status);
CREATE INDEX idx_surveys_createdat ON Surveys(CreatedAt);


-- Indexes for Tags table
CREATE INDEX idx_tags_tagname ON Tags(TagName);

-- Indexes for TimeSlots table
CREATE INDEX idx_timeslots_psychologistid ON TimeSlots(PsychologistID);
CREATE INDEX idx_timeslots_slotdate ON TimeSlots(SlotDate);
CREATE INDEX idx_timeslots_status ON TimeSlots(Status); 
CREATE INDEX idx_timeslots_starttime ON TimeSlots(StartTime);
CREATE INDEX idx_timeslots_endtime ON TimeSlots(EndTime);
CREATE INDEX idx_timeslots_booked ON TimeSlots(booked);
CREATE INDEX idx_timeslots_maxcapacity ON TimeSlots(max_capacity);
CREATE INDEX idx_timeslots_currentbookings ON TimeSlots(current_bookings); 