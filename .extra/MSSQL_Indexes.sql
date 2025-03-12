-- Indexes for Appointments table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_appointments_studentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_appointments_studentid ON Appointments(StudentID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_appointments_psychologistid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_appointments_psychologistid ON Appointments(PsychologistID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_appointments_timeslotsid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_appointments_timeslotsid ON Appointments(TimeSlotsID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_appointments_status'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_appointments_status ON Appointments(Status);
    END IF;
END $$;

-- Additional indexes for Appointments table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_appointments_createdat'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_appointments_createdat ON Appointments(CreatedAt);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_appointments_updatedat'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_appointments_updatedat ON Appointments(UpdatedAt);
    END IF;
END $$;

-- Indexes for Article table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_article_authorid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_article_authorid ON Article(AuthorID);
    END IF;
END $$;

-- Additional indexes for Article table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_article_createdat'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_article_createdat ON Article(CreatedAt);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_article_likes'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_article_likes ON Article(likes);
    END IF;
END $$;

-- Indexes for Categories table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_categories_categoryname'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_categories_categoryname ON Categories(CategoryName);
    END IF;
END $$;

-- Indexes for Comment table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_comment_programid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_comment_programid ON Comments(ProgramID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_comment_articleid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_comment_articleid ON Comments(ArticleID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_comment_surveyid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_comment_surveyid ON Comments(SurveyID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_comment_appointmentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_comment_appointmentid ON Comments(AppointmentID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_comment_userid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_comment_userid ON Comments(UserID);
    END IF;
END $$;

-- Additional indexes for Comment table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_comment_createdat'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_comment_createdat ON Comment(CreatedAt);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_comment_rating'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_comment_rating ON Comment(Rating);
    END IF;
END $$;

-- Indexes for Department table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_department_name'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_department_name ON Department(Name);
    END IF;
END $$;

-- Indexes for Feedback table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_feedback_appointmentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_feedback_appointmentid ON Feedback(AppointmentID);
    END IF;
END $$;

-- Indexes for Notifications table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_notifications_userid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_notifications_userid ON Notifications(UserID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_notifications_type'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_notifications_type ON Notifications(Type);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_notifications_isread'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_notifications_isread ON Notifications(IsRead);
    END IF;
END $$;

-- Additional indexes for Notifications table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_notifications_createdat'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_notifications_createdat ON Notifications(CreatedAt);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_notifications_appointmentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_notifications_appointmentid ON Notifications(AppointmentID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_notifications_programid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_notifications_programid ON Notifications(ProgramID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_notifications_surveyid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_notifications_surveyid ON Notifications(SurveyID);
    END IF;
END $$;

-- Indexes for Parents table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_parents_userid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_parents_userid ON Parents(UserID);
    END IF;
END $$;

-- Indexes for ProgramParticipation table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programparticipation_studentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programparticipation_studentid ON ProgramParticipation(StudentID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programparticipation_programid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programparticipation_programid ON ProgramParticipation(ProgramID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programparticipation_status'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programparticipation_status ON ProgramParticipation(Status);
    END IF;
END $$;

-- Indexes for Programs table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programs_programname'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programs_programname ON Programs(ProgramName);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programs_status'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programs_status ON Programs(Status);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programs_departmentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programs_departmentid ON Programs(DepartmentID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programs_facilitatorid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programs_facilitatorid ON Programs(FacilitatorID);
    END IF;
END $$;

-- Additional indexes for Programs table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programs_createdat'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programs_createdat ON Programs(CreatedAt);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programs_startdate'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programs_startdate ON Programs(StartDate);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programs_enddate'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programs_enddate ON Programs(EndDate);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_programs_rating'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_programs_rating ON Programs(Rating);
    END IF;
END $$;

-- Indexes for Psychologists table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_psychologists_userid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_psychologists_userid ON Psychologists(UserID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_psychologists_status'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_psychologists_status ON Psychologists(Status);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_psychologists_departmentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_psychologists_departmentid ON Psychologists(DepartmentID);
    END IF;
END $$;

-- Indexes for Students table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_students_userid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_students_userid ON Students(UserID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_students_parentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_students_parentid ON Students(ParentID);
    END IF;
END $$;

-- Additional indexes for Students table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_students_anxietyscore'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_students_anxietyscore ON Students(AnxietyScore);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_students_stressscore'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_students_stressscore ON Students(StressScore);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_students_depressionscore'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_students_depressionscore ON Students(DepressionScore);
    END IF;
END $$;

-- Indexes for SurveyQuestions table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyquestions_surveyid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyquestions_surveyid ON SurveyQuestions(SurveyID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyquestions_categoryid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyquestions_categoryid ON SurveyQuestions(CategoryID);
    END IF;
END $$;

-- Indexes for SurveyQuestionOptions table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyquestionoptions_questionid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyquestionoptions_questionid ON SurveyQuestionOptions(QuestionID);
    END IF;
END $$;

-- Indexes for SurveyQuestionOptionsChoices table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyquestionoptionschoices_questionid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyquestionoptionschoices_questionid ON SurveyQuestionOptionsChoices(QuestionID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyquestionoptionschoices_optionid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyquestionoptionschoices_optionid ON SurveyQuestionOptionsChoices(OptionID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyquestionoptionschoices_resultid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyquestionoptionschoices_resultid ON SurveyQuestionOptionsChoices(ResultID);
    END IF;
END $$;

-- Indexes for SurveyResult table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyresult_surveyid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyresult_surveyid ON SurveyResult(SurveyID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyresult_studentid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyresult_studentid ON SurveyResult(StudentID);
    END IF;
END $$;

-- Additional indexes for SurveyResult table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyresult_createdat'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyresult_createdat ON SurveyResult(CreatedAt);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyresult_result'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyresult_result ON SurveyResult(Result);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveyresult_maxscore'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveyresult_maxscore ON SurveyResult(MaxScore);
    END IF;
END $$;

-- Indexes for Surveys table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveys_categoryid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveys_categoryid ON Surveys(CategoryID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveys_createdby'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveys_createdby ON Surveys(CreatedBy);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveys_status'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveys_status ON Surveys(Status);
    END IF;
END $$;

-- Additional indexes for Surveys table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_surveys_createdat'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_surveys_createdat ON Surveys(CreatedAt);
    END IF;
END $$;

-- Indexes for Tags table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_tags_tagname'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_tags_tagname ON Tags(TagName);
    END IF;
END $$;

-- Indexes for TimeSlots table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_timeslots_psychologistid'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_timeslots_psychologistid ON TimeSlots(PsychologistID);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_timeslots_slotdate'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_timeslots_slotdate ON TimeSlots(SlotDate);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_timeslots_status'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_timeslots_status ON TimeSlots(Status);
    END IF;
END $$;

-- Additional indexes for TimeSlots table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_timeslots_starttime'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_timeslots_starttime ON TimeSlots(StartTime);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_timeslots_endtime'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_timeslots_endtime ON TimeSlots(EndTime);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_timeslots_booked'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_timeslots_booked ON TimeSlots(booked);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_timeslots_maxcapacity'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_timeslots_maxcapacity ON TimeSlots(max_capacity);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'idx_timeslots_currentbookings'
        AND n.nspname = 'defaultdb'
    ) THEN
        CREATE INDEX idx_timeslots_currentbookings ON TimeSlots(current_bookings);
    END IF;
END $$; 