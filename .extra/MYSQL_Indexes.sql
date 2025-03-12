USE defaultdb;
-- DELIMITER //

-- CREATE PROCEDURE CreateIndexIfNotExists(
--     IN tableName VARCHAR(64),
--     IN indexName VARCHAR(64),
--     IN indexDefinition VARCHAR(255)
-- )
-- BEGIN
--     DECLARE indexExists INT DEFAULT 0;

--     SELECT COUNT(*)
--     INTO indexExists
--     FROM information_schema.statistics
--     WHERE table_schema = DATABASE()
--     AND table_name = tableName
--     AND index_name = indexName;

--     IF indexExists = 0 THEN
--         SET @query = CONCAT('CREATE INDEX ', indexName, ' ON ', tableName, ' ', indexDefinition);
--         PREPARE stmt FROM @query;
--         EXECUTE stmt;
--         DEALLOCATE PREPARE stmt;
--     END IF;
-- END //

-- DELIMITER ;

-- Indexes for Appointments table
CALL CreateIndexIfNotExists('Appointments', 'idx_appointments_studentid', '(StudentID)');
CALL CreateIndexIfNotExists('Appointments', 'idx_appointments_psychologistid', '(PsychologistID)');
CALL CreateIndexIfNotExists('Appointments', 'idx_appointments_timeslotsid', '(TimeSlotsID)');
CALL CreateIndexIfNotExists('Appointments', 'idx_appointments_status', '(Status)');
CALL CreateIndexIfNotExists('Appointments', 'idx_appointments_createdat', '(CreatedAt)');
CALL CreateIndexIfNotExists('Appointments', 'idx_appointments_updatedat', '(UpdatedAt)');

-- Indexes for Article table
CALL CreateIndexIfNotExists('Article', 'idx_article_authorid', '(AuthorID)');
CALL CreateIndexIfNotExists('Article', 'idx_article_createdat', '(CreatedAt)');
CALL CreateIndexIfNotExists('Article', 'idx_article_likes', '(likes)');

-- Indexes for Categories table
CALL CreateIndexIfNotExists('Categories', 'idx_categories_categoryname', '(CategoryName)');

-- Indexes for Comment table
CALL CreateIndexIfNotExists('Comments', 'idx_comment_programid', '(ProgramID)');
CALL CreateIndexIfNotExists('Comments', 'idx_comment_articleid', '(ArticleID)');
CALL CreateIndexIfNotExists('Comments', 'idx_comment_surveyid', '(SurveyID)');
CALL CreateIndexIfNotExists('Comments', 'idx_comment_appointmentid', '(AppointmentID)');
CALL CreateIndexIfNotExists('Comments', 'idx_comment_userid', '(UserID)');
CALL CreateIndexIfNotExists('Comments', 'idx_comment_createdat', '(CreatedAt)');
CALL CreateIndexIfNotExists('Comments', 'idx_comment_rating', '(Rating)');

-- Indexes for Department table
CALL CreateIndexIfNotExists('Department', 'idx_department_name', '(Name)');

-- Indexes for Feedback table
CALL CreateIndexIfNotExists('Feedback', 'idx_feedback_appointmentid', '(AppointmentID)');

-- Indexes for Notifications table
CALL CreateIndexIfNotExists('Notifications', 'idx_notifications_userid', '(UserID)');
CALL CreateIndexIfNotExists('Notifications', 'idx_notifications_type', '(Type)');
CALL CreateIndexIfNotExists('Notifications', 'idx_notifications_isread', '(IsRead)');
CALL CreateIndexIfNotExists('Notifications', 'idx_notifications_createdat', '(CreatedAt)');
CALL CreateIndexIfNotExists('Notifications', 'idx_notifications_appointmentid', '(AppointmentID)');
CALL CreateIndexIfNotExists('Notifications', 'idx_notifications_programid', '(ProgramID)');
CALL CreateIndexIfNotExists('Notifications', 'idx_notifications_surveyid', '(SurveyID)');

-- Indexes for Parents table
CALL CreateIndexIfNotExists('Parents', 'idx_parents_userid', '(UserID)');

-- Indexes for ProgramParticipation table
CALL CreateIndexIfNotExists('Programparticipation', 'idx_programparticipation_studentid', '(StudentID)');
CALL CreateIndexIfNotExists('Programparticipation', 'idx_programparticipation_programid', '(ProgramID)');
CALL CreateIndexIfNotExists('Programparticipation', 'idx_programparticipation_status', '(Status)');

-- Indexes for Programs table
CALL CreateIndexIfNotExists('Programs', 'idx_programs_programname', '(ProgramName)');
CALL CreateIndexIfNotExists('Programs', 'idx_programs_status', '(Status)');
CALL CreateIndexIfNotExists('Programs', 'idx_programs_departmentid', '(DepartmentID)');
CALL CreateIndexIfNotExists('Programs', 'idx_programs_facilitatorid', '(FacilitatorID)');
CALL CreateIndexIfNotExists('Programs', 'idx_programs_createdat', '(CreatedAt)');
CALL CreateIndexIfNotExists('Programs', 'idx_programs_startdate', '(StartDate)');
CALL CreateIndexIfNotExists('Programs', 'idx_programs_enddate', '(EndDate)');
CALL CreateIndexIfNotExists('Programs', 'idx_programs_rating', '(Rating)');

-- Indexes for Psychologists table
CALL CreateIndexIfNotExists('Psychologists', 'idx_psychologists_userid', '(UserID)');
CALL CreateIndexIfNotExists('Psychologists', 'idx_psychologists_status', '(Status)');
CALL CreateIndexIfNotExists('Psychologists', 'idx_psychologists_departmentid', '(DepartmentID)');

-- Indexes for Students table
CALL CreateIndexIfNotExists('Students', 'idx_students_userid', '(UserID)');
CALL CreateIndexIfNotExists('Students', 'idx_students_parentid', '(ParentID)');
CALL CreateIndexIfNotExists('Students', 'idx_students_anxietyscore', '(AnxietyScore)');
CALL CreateIndexIfNotExists('Students', 'idx_students_stressscore', '(StressScore)');
CALL CreateIndexIfNotExists('Students', 'idx_students_depressionscore', '(DepressionScore)');

-- Indexes for SurveyQuestions table
CALL CreateIndexIfNotExists('Surveyquestions', 'idx_surveyquestions_surveyid', '(SurveyID)');
CALL CreateIndexIfNotExists('Surveyquestions', 'idx_surveyquestions_categoryid', '(CategoryID)');

-- Indexes for SurveyQuestionOptions table
CALL CreateIndexIfNotExists('Surveyquestionoptions', 'idx_surveyquestionoptions_questionid', '(QuestionID)');

-- Indexes for SurveyQuestionOptionsChoices table
CALL CreateIndexIfNotExists('Surveyquestionoptionschoices', 'idx_surveyquestionoptionschoices_questionid', '(QuestionID)');
CALL CreateIndexIfNotExists('Surveyquestionoptionschoices', 'idx_surveyquestionoptionschoices_optionid', '(OptionID)');
CALL CreateIndexIfNotExists('Surveyquestionoptionschoices', 'idx_surveyquestionoptionschoices_resultid', '(ResultID)');

-- Indexes for SurveyResult table
CALL CreateIndexIfNotExists('Surveyresult', 'idx_surveyresult_surveyid', '(SurveyID)');
CALL CreateIndexIfNotExists('Surveyresult', 'idx_surveyresult_studentid', '(StudentID)');
CALL CreateIndexIfNotExists('Surveyresult', 'idx_surveyresult_createdat', '(CreatedAt)');
CALL CreateIndexIfNotExists('Surveyresult', 'idx_surveyresult_result', '(Result)');
CALL CreateIndexIfNotExists('Surveyresult', 'idx_surveyresult_maxscore', '(MaxScore)');

-- Indexes for Surveys table
CALL CreateIndexIfNotExists('Surveys', 'idx_surveys_categoryid', '(CategoryID)');
CALL CreateIndexIfNotExists('Surveys', 'idx_surveys_createdby', '(CreatedBy)');
CALL CreateIndexIfNotExists('Surveys', 'idx_surveys_status', '(Status)');
CALL CreateIndexIfNotExists('Surveys', 'idx_surveys_createdat', '(CreatedAt)');

-- Indexes for Tags table
CALL CreateIndexIfNotExists('Tags', 'idx_tags_tagname', '(TagName)');

-- Indexes for TimeSlots table
CALL CreateIndexIfNotExists('Timeslots', 'idx_timeslots_psychologistid', '(PsychologistID)');
CALL CreateIndexIfNotExists('Timeslots', 'idx_timeslots_slotdate', '(SlotDate)');
CALL CreateIndexIfNotExists('Timeslots', 'idx_timeslots_status', '(Status)');
CALL CreateIndexIfNotExists('Timeslots', 'idx_timeslots_starttime', '(StartTime)');
CALL CreateIndexIfNotExists('Timeslots', 'idx_timeslots_endtime', '(EndTime)');
CALL CreateIndexIfNotExists('Timeslots', 'idx_timeslots_booked', '(booked)');
CALL CreateIndexIfNotExists('Timeslots', 'idx_timeslots_maxcapacity', '(Maxcapacity)');
CALL CreateIndexIfNotExists('Timeslots', 'idx_timeslots_currentbookings', '(Currentbookings)'); 