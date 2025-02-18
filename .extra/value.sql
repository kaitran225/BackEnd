-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: swp391healthy
-- ------------------------------------------------------
-- Server version	8.4.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answers` (
  `AnswerID` varchar(36) NOT NULL,
  `Answer` text NOT NULL,
  `QuestionID` varchar(36) NOT NULL,
  `Score` int DEFAULT NULL,
  PRIMARY KEY (`AnswerID`),
  KEY `FKryr8kigvj5aw9w6ilkvmrht5q` (`QuestionID`),
  CONSTRAINT `FKryr8kigvj5aw9w6ilkvmrht5q` FOREIGN KEY (`QuestionID`) REFERENCES `surveyquestions` (`QuestionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
INSERT INTO `answers` VALUES ('A001','Never','Q001',0),('A002','Sometimes','Q001',1),('A003','Often','Q001',2),('A004','Always','Q001',3),('A005','Never','Q002',0),('A006','Sometimes','Q002',1),('A007','Moderately','Q002',2),('A008','Very much','Q002',3),('A009','Rarely','Q003',0),('A010','Sometimes','Q003',1),('A011','Often','Q003',2),('A012','Always','Q003',3),('A013','Never','Q004',0),('A014','Once a month','Q004',1),('A015','Once a week','Q004',2),('A016','Multiple times a week','Q004',3),('A017','Never','Q005',0),('A018','Sometimes','Q005',1),('A019','Often','Q005',2),('A020','Always','Q005',3),('A021','Never','Q006',0),('A022','Sometimes','Q006',1),('A023','Often','Q006',2),('A024','Always','Q006',3),('A025','Good','Q007',0),('A026','Fair','Q007',1),('A027','Bad','Q007',2),('A028','Very bad','Q007',3),('A029','Yes','Q008',0),('A030','No','Q008',1),('A031','Not sure','Q008',2),('A032','Not at all','Q008',3);
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `AppointmentID` varchar(36) NOT NULL,
  `CheckInTime` datetime(6) DEFAULT NULL,
  `CheckOutTime` datetime(6) DEFAULT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `Feedback` text,
  `PsychologistID` varchar(36) NOT NULL,
  `PsychologistNote` text,
  `Status` enum('Cancelled','Completed','InProgress','Scheduled') DEFAULT NULL,
  `StudentID` varchar(36) NOT NULL,
  `StudentNote` text,
  `TimeSlotsID` varchar(36) DEFAULT NULL,
  `UpdatedAt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`AppointmentID`),
  KEY `FKmily3f3tgg2d14poosui8nvyt` (`PsychologistID`),
  KEY `FK7j3kvglfjb20jbx6oubix5y9b` (`StudentID`),
  KEY `FKayb7iw3wolqlcv0mjvub5bw2h` (`TimeSlotsID`),
  CONSTRAINT `FK7j3kvglfjb20jbx6oubix5y9b` FOREIGN KEY (`StudentID`) REFERENCES `students` (`StudentID`),
  CONSTRAINT `FKayb7iw3wolqlcv0mjvub5bw2h` FOREIGN KEY (`TimeSlotsID`) REFERENCES `timeslots` (`TimeSlotsID`),
  CONSTRAINT `FKmily3f3tgg2d14poosui8nvyt` FOREIGN KEY (`PsychologistID`) REFERENCES `psychologists` (`PsychologistID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES ('APP001',NULL,NULL,'2025-02-18 11:21:00.019987',NULL,'PSY001',NULL,'Scheduled','S001',NULL,'TSPSY00120022501','2025-02-18 11:21:00.019987'),('APP002',NULL,NULL,'2025-02-18 11:21:00.026533',NULL,'PSY002',NULL,'Scheduled','S002',NULL,'TSPSY00221022501','2025-02-18 11:21:00.026533');
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `ArticleID` varchar(36) NOT NULL,
  `Content` text,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `CreatedBy` varchar(36) DEFAULT NULL,
  `Title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ArticleID`),
  KEY `FK4hogxqsoqr8iyerohcyrfrmvv` (`CreatedBy`),
  CONSTRAINT `FK4hogxqsoqr8iyerohcyrfrmvv` FOREIGN KEY (`CreatedBy`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES ('B001','Tips for managing stress...','2025-02-18 11:21:00.004723','US008','Managing Stress'),('B002','Strategies to cope with anxiety...','2025-02-18 11:21:00.010061','US009','Overcoming Anxiety');
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `CategoryID` varchar(36) NOT NULL,
  `CategoryName` enum('Anxiety','Depression','Stress') NOT NULL,
  PRIMARY KEY (`CategoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES ('CAT001','Anxiety'),('CAT002','Stress'),('CAT003','Depression');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `DepartmentID` varchar(36) NOT NULL,
  `Name` varchar(100) NOT NULL,
  PRIMARY KEY (`DepartmentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES ('DP01','Child & Adolescent Psychology'),('DP02','School Counseling'),('DP03','Behavioral Therapy'),('DP04','Trauma & Crisis Intervention'),('DP05','Family & Parent Counseling'),('DP06','Stress & Anxiety Management'),('DP07','Depression & Mood Disorders'),('DP08','Special Education Support'),('DP09','Social Skills & Peer Relation'),('DP10','Suicide Prevention & Intervention'),('DP11','Digital Well-beingIntervention');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `NotificationID` varchar(36) NOT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `IsRead` bit(1) DEFAULT NULL,
  `Message` text NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Type` enum('Appointment','Survey','Program','Done') NOT NULL,
  `UserID` varchar(36) NOT NULL,
  PRIMARY KEY (`NotificationID`),
  KEY `FK689wccaxeicnjy9vjaqks20ya` (`UserID`),
  CONSTRAINT `FK689wccaxeicnjy9vjaqks20ya` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES ('NOT001','2025-02-18 11:21:00.034982',NULL,'Your appointment is scheduled for 2023-06-15 at 10:00 AM','Appointment Scheduled','Appointment','US008'),('NOT002','2025-02-18 11:21:00.037536',NULL,'A new survey is available for you to complete','Survey Available','Survey','US003');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parents`
--

DROP TABLE IF EXISTS `parents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parents` (
  `ParentID` varchar(36) NOT NULL,
  `UserID` varchar(36) NOT NULL,
  PRIMARY KEY (`ParentID`),
  KEY `FKj0gwsij677b838ipqcmrclxhy` (`UserID`),
  CONSTRAINT `FKj0gwsij677b838ipqcmrclxhy` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parents`
--

LOCK TABLES `parents` WRITE;
/*!40000 ALTER TABLE `parents` DISABLE KEYS */;
INSERT INTO `parents` VALUES ('P001','US006'),('P002','US007');
/*!40000 ALTER TABLE `parents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programparticipation`
--

DROP TABLE IF EXISTS `programparticipation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programparticipation` (
  `ParticipationID` varchar(36) NOT NULL,
  `EndDate` date DEFAULT NULL,
  `ProgramID` varchar(36) NOT NULL,
  `StartDate` date NOT NULL,
  `Status` enum('Cancelled','Completed','Joined') NOT NULL,
  `StudentID` varchar(36) NOT NULL,
  PRIMARY KEY (`ParticipationID`),
  KEY `FK19bqmcxun4kulmgtpwlhlf65i` (`ProgramID`),
  KEY `FKcqkowhb5ekatyig1sjvrkj6ba` (`StudentID`),
  CONSTRAINT `FK19bqmcxun4kulmgtpwlhlf65i` FOREIGN KEY (`ProgramID`) REFERENCES `programs` (`ProgramID`),
  CONSTRAINT `FKcqkowhb5ekatyig1sjvrkj6ba` FOREIGN KEY (`StudentID`) REFERENCES `students` (`StudentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programparticipation`
--

LOCK TABLES `programparticipation` WRITE;
/*!40000 ALTER TABLE `programparticipation` DISABLE KEYS */;
INSERT INTO `programparticipation` VALUES ('PP001','2023-06-30','PRG001','2023-06-01','Completed','S001'),('PP002','2023-08-15','PRG002','2023-07-01','Joined','S002');
/*!40000 ALTER TABLE `programparticipation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programs`
--

DROP TABLE IF EXISTS `programs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programs` (
  `ProgramID` varchar(36) NOT NULL,
  `CreatedAt` datetime(6) NOT NULL,
  `DepartmentID` varchar(36) NOT NULL,
  `Description` text,
  `Duration` int DEFAULT NULL,
  `FacilitatorID` varchar(36) NOT NULL,
  `MeetingLink` varchar(255) DEFAULT NULL,
  `NumberParticipants` int DEFAULT NULL,
  `ProgramName` varchar(100) NOT NULL,
  `StartDate` date DEFAULT NULL,
  `Status` enum('Active','Inactive') NOT NULL,
  `AppointmentType` enum('Offline','Online') NOT NULL,
  PRIMARY KEY (`ProgramID`),
  KEY `FK4jmg7ygekgx5spttenbdmx1kx` (`DepartmentID`),
  KEY `FKsb6seu9egjvj1832rhwsi7awb` (`FacilitatorID`),
  CONSTRAINT `FK4jmg7ygekgx5spttenbdmx1kx` FOREIGN KEY (`DepartmentID`) REFERENCES `department` (`DepartmentID`),
  CONSTRAINT `FKsb6seu9egjvj1832rhwsi7awb` FOREIGN KEY (`FacilitatorID`) REFERENCES `psychologists` (`PsychologistID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programs`
--

LOCK TABLES `programs` WRITE;
/*!40000 ALTER TABLE `programs` DISABLE KEYS */;
INSERT INTO `programs` VALUES ('PRG001','2025-02-18 11:20:59.382283','DP06','Program to help manage stress',4,'PSY001','https://example.com/meeting1',20,'Stress Management','2025-02-13','Active','Online'),('PRG002','2025-02-18 11:20:59.423494','DP06','Support group for individuals with anxiety',6,'PSY002','https://example.com/meeting2',15,'Anxiety Support Group','2025-02-15','Active','Offline'),('PRG003','2025-02-18 11:20:59.452055','DP03','Workshop on mindfulness techniques',3,'PSY001','https://example.com/meeting3',25,'Mindfulness Workshop','2025-02-18','Active','Online'),('PRG004','2025-02-18 11:20:59.470469','DP07','Counseling for individuals with depression',2,'PSY002','https://example.com/meeting4',30,'Depression Counseling','2025-02-28','Active','Online');
/*!40000 ALTER TABLE `programs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programschedule`
--

DROP TABLE IF EXISTS `programschedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programschedule` (
  `ScheduleID` varchar(36) NOT NULL,
  `DayOfWeek` varchar(10) NOT NULL,
  `EndTime` time(6) NOT NULL,
  `ProgramID` varchar(36) NOT NULL,
  `StartTime` time(6) NOT NULL,
  PRIMARY KEY (`ScheduleID`),
  KEY `FK7cmhqcejobhnshmb1eeiok17u` (`ProgramID`),
  CONSTRAINT `FK7cmhqcejobhnshmb1eeiok17u` FOREIGN KEY (`ProgramID`) REFERENCES `programs` (`ProgramID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programschedule`
--

LOCK TABLES `programschedule` WRITE;
/*!40000 ALTER TABLE `programschedule` DISABLE KEYS */;
INSERT INTO `programschedule` VALUES ('SCH001','Monday','11:30:00.000000','PRG001','10:00:00.000000'),('SCH002','Tuesday','15:30:00.000000','PRG002','14:00:00.000000'),('SCH003','Wednesday','10:30:00.000000','PRG003','09:00:00.000000');
/*!40000 ALTER TABLE `programschedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programtags`
--

DROP TABLE IF EXISTS `programtags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programtags` (
  `ProgramId` varchar(36) NOT NULL,
  `TagId` varchar(36) NOT NULL,
  PRIMARY KEY (`ProgramId`,`TagId`),
  KEY `FKsf4tt0sadfbmidngtelynbbpl` (`TagId`),
  CONSTRAINT `FKqe3rsyoj9xkw039g2gl1wkfuh` FOREIGN KEY (`ProgramId`) REFERENCES `programs` (`ProgramID`),
  CONSTRAINT `FKsf4tt0sadfbmidngtelynbbpl` FOREIGN KEY (`TagId`) REFERENCES `tags` (`TagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programtags`
--

LOCK TABLES `programtags` WRITE;
/*!40000 ALTER TABLE `programtags` DISABLE KEYS */;
INSERT INTO `programtags` VALUES ('PRG001','TAG001'),('PRG001','TAG002'),('PRG001','TAG003'),('PRG002','TAG004'),('PRG002','TAG005'),('PRG002','TAG006'),('PRG003','TAG007'),('PRG003','TAG008'),('PRG003','TAG009'),('PRG004','TAG010'),('PRG004','TAG011'),('PRG004','TAG012');
/*!40000 ALTER TABLE `programtags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `psychologists`
--

DROP TABLE IF EXISTS `psychologists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `psychologists` (
  `PsychologistID` varchar(36) NOT NULL,
  `DepartmentID` varchar(36) DEFAULT NULL,
  `Status` enum('Active','Inactive','OnLeave') NOT NULL,
  `UserID` varchar(36) NOT NULL,
  `YearsOfExperience` int DEFAULT NULL,
  PRIMARY KEY (`PsychologistID`),
  KEY `FK6socmrflubw3g1vrdx8hm8vml` (`DepartmentID`),
  KEY `FKk8ad9rs4e4wbhcf7nxvf1c16n` (`UserID`),
  CONSTRAINT `FK6socmrflubw3g1vrdx8hm8vml` FOREIGN KEY (`DepartmentID`) REFERENCES `department` (`DepartmentID`),
  CONSTRAINT `FKk8ad9rs4e4wbhcf7nxvf1c16n` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `psychologists`
--

LOCK TABLES `psychologists` WRITE;
/*!40000 ALTER TABLE `psychologists` DISABLE KEYS */;
INSERT INTO `psychologists` VALUES ('PSY001','DP01','Active','US008',10),('PSY002','DP02','Active','US009',8);
/*!40000 ALTER TABLE `psychologists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refreshtoken`
--

DROP TABLE IF EXISTS `refreshtoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refreshtoken` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ExpiresAt` datetime(6) NOT NULL,
  `HashedToken` varchar(255) NOT NULL,
  `UserID` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpxtlubptdust15wrm4k48fr4i` (`UserID`),
  CONSTRAINT `FKpxtlubptdust15wrm4k48fr4i` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refreshtoken`
--

LOCK TABLES `refreshtoken` WRITE;
/*!40000 ALTER TABLE `refreshtoken` DISABLE KEYS */;
/*!40000 ALTER TABLE `refreshtoken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `StudentID` varchar(36) NOT NULL,
  `AnxietyScore` int DEFAULT NULL,
  `ClassName` varchar(20) DEFAULT NULL,
  `DepressionScore` int DEFAULT NULL,
  `Grade` int DEFAULT NULL,
  `ParentID` varchar(36) NOT NULL,
  `SchoolName` varchar(100) DEFAULT NULL,
  `StressScore` int DEFAULT NULL,
  `UserID` varchar(36) NOT NULL,
  PRIMARY KEY (`StudentID`),
  KEY `FKciyagwdb0nowj7f0dyoia1k3t` (`ParentID`),
  KEY `FKaengiastf6jkwmemijj3g2onv` (`UserID`),
  CONSTRAINT `FKaengiastf6jkwmemijj3g2onv` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`),
  CONSTRAINT `FKciyagwdb0nowj7f0dyoia1k3t` FOREIGN KEY (`ParentID`) REFERENCES `parents` (`ParentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('S001',5,'A',10,10,'P001','Example High School',2,'US003'),('S002',3,'B',4,9,'P002','Example High School',7,'US004'),('S003',2,'A',2,9,'P001','Example High School',1,'US005');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveyquestions`
--

DROP TABLE IF EXISTS `surveyquestions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surveyquestions` (
  `QuestionID` varchar(36) NOT NULL,
  `CategoryID` varchar(36) DEFAULT NULL,
  `QuestionText` text NOT NULL,
  `SurveyID` varchar(36) NOT NULL,
  PRIMARY KEY (`QuestionID`),
  KEY `FK3mu1ylyqne2p4vboi456v9phv` (`CategoryID`),
  KEY `FKb3qeg2ym9kg6byybvra3mql58` (`SurveyID`),
  CONSTRAINT `FK3mu1ylyqne2p4vboi456v9phv` FOREIGN KEY (`CategoryID`) REFERENCES `categories` (`CategoryID`),
  CONSTRAINT `FKb3qeg2ym9kg6byybvra3mql58` FOREIGN KEY (`SurveyID`) REFERENCES `surveys` (`SurveyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveyquestions`
--

LOCK TABLES `surveyquestions` WRITE;
/*!40000 ALTER TABLE `surveyquestions` DISABLE KEYS */;
INSERT INTO `surveyquestions` VALUES ('Q001','CAT001','How often do you feel stressed?','SUR001'),('Q002','CAT001','How much does stress interfere with your daily life?','SUR001'),('Q003','CAT002','Do you experience excessive worry or fear?','SUR002'),('Q004','CAT002','How often do you have panic attacks?','SUR002'),('Q005','CAT003','Do you feel sad or hopeless most of the time?','SUR003'),('Q006','CAT003','Have you lost interest in activities you once enjoyed?','SUR003'),('Q007','CAT001','How are you feeling today?','SUR004'),('Q008','CAT001','Are you having a good day?','SUR004');
/*!40000 ALTER TABLE `surveyquestions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveyresults`
--

DROP TABLE IF EXISTS `surveyresults`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surveyresults` (
  `ResultID` varchar(36) NOT NULL,
  `AnswerID` varchar(36) NOT NULL,
  `QuestionID` varchar(36) NOT NULL,
  `StudentID` varchar(36) NOT NULL,
  PRIMARY KEY (`ResultID`),
  KEY `FKjs72wj2vwxl45cg2walj44dg5` (`AnswerID`),
  KEY `FK8t68w18aa6owt2opherlwv9vk` (`QuestionID`),
  KEY `FK678llvs17bf7jg075j6lh9e1q` (`StudentID`),
  CONSTRAINT `FK678llvs17bf7jg075j6lh9e1q` FOREIGN KEY (`StudentID`) REFERENCES `students` (`StudentID`),
  CONSTRAINT `FK8t68w18aa6owt2opherlwv9vk` FOREIGN KEY (`QuestionID`) REFERENCES `surveyquestions` (`QuestionID`),
  CONSTRAINT `FKjs72wj2vwxl45cg2walj44dg5` FOREIGN KEY (`AnswerID`) REFERENCES `answers` (`AnswerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveyresults`
--

LOCK TABLES `surveyresults` WRITE;
/*!40000 ALTER TABLE `surveyresults` DISABLE KEYS */;
INSERT INTO `surveyresults` VALUES ('R001','A002','Q001','S001'),('R002','A006','Q002','S001'),('R003','A010','Q003','S001'),('R004','A014','Q004','S001'),('R005','A018','Q005','S001'),('R006','A022','Q006','S001'),('R007','A001','Q001','S002'),('R008','A005','Q002','S002'),('R009','A009','Q003','S002'),('R010','A013','Q004','S002'),('R011','A017','Q005','S002'),('R012','A021','Q006','S002'),('R013','A003','Q001','S003'),('R014','A007','Q002','S003'),('R015','A011','Q003','S003'),('R016','A015','Q004','S003'),('R017','A019','Q005','S003'),('R018','A023','Q006','S003');
/*!40000 ALTER TABLE `surveyresults` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveys`
--

DROP TABLE IF EXISTS `surveys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surveys` (
  `SurveyID` varchar(36) NOT NULL,
  `CategoryID` varchar(36) NOT NULL,
  `CreatedAt` datetime(6) NOT NULL,
  `CreatedBy` varchar(36) NOT NULL,
  `Description` text,
  `Status` enum('Unfinished','Finished','Cancelled') NOT NULL,
  `SurveyName` varchar(100) NOT NULL,
  PRIMARY KEY (`SurveyID`),
  KEY `FKk4lykft9ydt37leirifqmsalt` (`CategoryID`),
  KEY `FKo76oxa2c3q56tr77aknoeqjck` (`CreatedBy`),
  CONSTRAINT `FKk4lykft9ydt37leirifqmsalt` FOREIGN KEY (`CategoryID`) REFERENCES `categories` (`CategoryID`),
  CONSTRAINT `FKo76oxa2c3q56tr77aknoeqjck` FOREIGN KEY (`CreatedBy`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveys`
--

LOCK TABLES `surveys` WRITE;
/*!40000 ALTER TABLE `surveys` DISABLE KEYS */;
INSERT INTO `surveys` VALUES ('SUR001','CAT001','2025-02-18 11:20:59.583270','US003','Survey to assess stress levels','Finished','Stress Survey'),('SUR002','CAT002','2025-02-18 11:20:59.596567','US004','Assessment of anxiety symptoms','Finished','Anxiety Assessment'),('SUR003','CAT003','2025-02-18 11:20:59.608540','US005','Screening for depression','Finished','Depression Screening'),('SUR004','CAT001','2025-02-18 11:20:59.621271','US003','Assessment of mood','Unfinished','Mood Assessment');
/*!40000 ALTER TABLE `surveys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `TagID` varchar(36) NOT NULL,
  `TagName` varchar(100) NOT NULL,
  PRIMARY KEY (`TagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES ('TAG001','Stress'),('TAG002','Anxiety'),('TAG003','Mindfulness'),('TAG004','Depression'),('TAG005','EatingDisorder'),('TAG006','Addiction'),('TAG007','Self Care'),('TAG008','Sleep'),('TAG009','PhysicalHealth'),('TAG010','MentalHealth'),('TAG011','Peer Support'),('TAG012','Community'),('TAG013','Social Skill'),('TAG014','Exercise'),('TAG015','Relaxation'),('TAG016','Meditation'),('TAG017','Motivation'),('TAG018','Test Anxiety'),('TAG019','Test Depression'),('TAG020','Test Stress'),('TAG021','Academic Stress'),('TAG022','Work Stress'),('TAG023','Financial Stress'),('TAG024','Relationship Stress'),('TAG025','Self Improvement Stress'),('TAG026','Academic Relationship'),('TAG027','Work Relationship'),('TAG028','Financial Relationship'),('TAG029','Relationship'),('TAG030','Self Improvement'),('TAG031','Performance'),('TAG032','Confidence'),('TAG033','Self Esteem'),('TAG034','Self Awareness'),('TAG035','Self Discipline'),('TAG036','Self Reflection'),('TAG037','Self Management'),('TAG038','Resilience'),('TAG039','Coping Skills'),('TAG040','Problem Solving'),('TAG041','Decision Making'),('TAG042','Time Management'),('TAG043','Stress Management'),('TAG044','Emotional Intelligence'),('TAG045','Emotional Regulation'),('TAG046','Emotional Expression'),('TAG047','Productivity'),('TAG048','Boundaries'),('TAG049','Self Control'),('TAG050','Wellness'),('TAG051','Health'),('TAG052','Grief'),('TAG053','Support'),('TAG054','Healing'),('TAG055','Body Image'),('TAG056','Personal Development'),('TAG057','Self Acceptance'),('TAG058','Self Health'),('TAG059','Social Support'),('TAG060','Social Connectivity'),('TAG061','Social Interaction'),('TAG062','Community Engagement'),('TAG063','Community Involvement'),('TAG064','Relationship Building'),('TAG065','Relationship Health');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timeslots`
--

DROP TABLE IF EXISTS `timeslots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `timeslots` (
  `TimeSlotsID` varchar(36) NOT NULL,
  `CreatedAt` datetime(6) NOT NULL,
  `EndTime` time(6) NOT NULL,
  `SlotDate` date NOT NULL,
  `SlotNumber` int NOT NULL,
  `StartTime` time(6) NOT NULL,
  `Status` enum('Available','Booked') NOT NULL,
  `PsychologistID` varchar(36) NOT NULL,
  PRIMARY KEY (`TimeSlotsID`),
  KEY `FK9ece0eaadgo17qqksrlvif0gh` (`PsychologistID`),
  CONSTRAINT `FK9ece0eaadgo17qqksrlvif0gh` FOREIGN KEY (`PsychologistID`) REFERENCES `psychologists` (`PsychologistID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timeslots`
--

LOCK TABLES `timeslots` WRITE;
/*!40000 ALTER TABLE `timeslots` DISABLE KEYS */;
INSERT INTO `timeslots` VALUES ('TSPSY00120022501','2025-02-18 11:20:58.769282','08:30:00.000000','2025-02-20',1,'08:00:00.000000','Available','PSY001'),('TSPSY00120022502','2025-02-18 11:20:58.778022','09:00:00.000000','2025-02-20',2,'08:30:00.000000','Available','PSY001'),('TSPSY00120022503','2025-02-18 11:20:58.786562','09:30:00.000000','2025-02-20',3,'09:00:00.000000','Available','PSY001'),('TSPSY00120022504','2025-02-18 11:20:58.792274','10:00:00.000000','2025-02-20',4,'09:30:00.000000','Available','PSY001'),('TSPSY00120022505','2025-02-18 11:20:58.798846','10:30:00.000000','2025-02-20',5,'10:00:00.000000','Available','PSY001'),('TSPSY00120022506','2025-02-18 11:20:58.806882','11:00:00.000000','2025-02-20',6,'10:30:00.000000','Available','PSY001'),('TSPSY00121022501','2025-02-18 11:20:58.855619','08:30:00.000000','2025-02-21',1,'08:00:00.000000','Available','PSY001'),('TSPSY00121022502','2025-02-18 11:20:58.863543','09:00:00.000000','2025-02-21',2,'08:30:00.000000','Available','PSY001'),('TSPSY00121022503','2025-02-18 11:20:58.868918','09:30:00.000000','2025-02-21',3,'09:00:00.000000','Available','PSY001'),('TSPSY00121022504','2025-02-18 11:20:58.877704','10:00:00.000000','2025-02-21',4,'09:30:00.000000','Available','PSY001'),('TSPSY00121022505','2025-02-18 11:20:58.886903','10:30:00.000000','2025-02-21',5,'10:00:00.000000','Available','PSY001'),('TSPSY00121022506','2025-02-18 11:20:58.893135','11:00:00.000000','2025-02-21',6,'10:30:00.000000','Available','PSY001'),('TSPSY00220022501','2025-02-18 11:20:58.813853','08:30:00.000000','2025-02-20',1,'08:00:00.000000','Available','PSY002'),('TSPSY00220022502','2025-02-18 11:20:58.821488','09:00:00.000000','2025-02-20',2,'08:30:00.000000','Available','PSY002'),('TSPSY00220022503','2025-02-18 11:20:58.827465','09:30:00.000000','2025-02-20',3,'09:00:00.000000','Available','PSY002'),('TSPSY00220022504','2025-02-18 11:20:58.835194','10:00:00.000000','2025-02-20',4,'09:30:00.000000','Available','PSY002'),('TSPSY00220022505','2025-02-18 11:20:58.837712','10:30:00.000000','2025-02-20',5,'10:00:00.000000','Available','PSY002'),('TSPSY00220022506','2025-02-18 11:20:58.849480','11:00:00.000000','2025-02-20',6,'10:30:00.000000','Available','PSY002'),('TSPSY00221022501','2025-02-18 11:20:58.899078','08:30:00.000000','2025-02-21',1,'08:00:00.000000','Available','PSY002'),('TSPSY00221022502','2025-02-18 11:20:58.905737','09:00:00.000000','2025-02-21',2,'08:30:00.000000','Available','PSY002'),('TSPSY00221022503','2025-02-18 11:20:58.911933','09:30:00.000000','2025-02-21',3,'09:00:00.000000','Available','PSY002'),('TSPSY00221022504','2025-02-18 11:20:58.920151','10:00:00.000000','2025-02-21',4,'09:30:00.000000','Available','PSY002'),('TSPSY00221022505','2025-02-18 11:20:58.927763','10:30:00.000000','2025-02-21',5,'10:00:00.000000','Available','PSY002'),('TSPSY00221022506','2025-02-18 11:20:58.936802','11:00:00.000000','2025-02-21',6,'10:30:00.000000','Available','PSY002');
/*!40000 ALTER TABLE `timeslots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userlogs`
--

DROP TABLE IF EXISTS `userlogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userlogs` (
  `LogID` varchar(36) NOT NULL,
  `IPAddress` varchar(50) DEFAULT NULL,
  `LoginTime` datetime(6) DEFAULT NULL,
  `UserID` varchar(36) NOT NULL,
  PRIMARY KEY (`LogID`),
  KEY `FKct2vgnj582notleijgl3l3mkj` (`UserID`),
  CONSTRAINT `FKct2vgnj582notleijgl3l3mkj` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userlogs`
--

LOCK TABLES `userlogs` WRITE;
/*!40000 ALTER TABLE `userlogs` DISABLE KEYS */;
INSERT INTO `userlogs` VALUES ('L001','192.168.0.1','2025-02-18 11:20:59.988617','US003'),('L002','192.168.0.2','2025-02-18 11:20:59.996619','US004');
/*!40000 ALTER TABLE `userlogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
    `UserID` VARCHAR(36) NOT NULL,
    `Address` VARCHAR(100) DEFAULT NULL,
    `CreatedAt` DATETIME(6) NOT NULL,
    `Email` VARCHAR(100) DEFAULT NULL,
    `FullName` VARCHAR(100) NOT NULL,
    `Gender` ENUM('Male', 'Female', 'Other') DEFAULT NULL,
    `IsVerified` BIT(1) NOT NULL,
    `PasswordHash` VARCHAR(255) NOT NULL,
    `PhoneNumber` VARCHAR(15) DEFAULT NULL,
    `ResetToken` VARCHAR(255) DEFAULT NULL,
    `ResetTokenExpiry` DATETIME(6) DEFAULT NULL,
    `Role` ENUM('MANAGER', 'PARENT', 'PSYCHOLOGIST', 'STUDENT') NOT NULL,
    `TokenExpiration` DATETIME(6) NOT NULL,
    `UpdatedAt` DATETIME(6) NOT NULL,
    `Username` VARCHAR(50) NOT NULL,
    `VerificationToken` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`UserID`),
    UNIQUE KEY `UK9cw87ffd4i55ki0qpkwu63er` (`Username`),
    UNIQUE KEY `UKikxg9koctpmqub6afe9tlal14` (`Address`),
    UNIQUE KEY `UKjdfr6kjrxekx1j5vrr77rp44t` (`Email`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('US001','Street 123, Ho Chi Minh City','2025-02-18 11:20:57.838708','admin@example.com','Admin Admin','Male',_binary '\0','$2a$10$97pwT4BZNj5wlr3SEyFa2.PjvV56vXdNKs1w4Z0sIaB7xjwcMMJ0i','1111111111',NULL,NULL,'MANAGER','2025-02-18 23:50:57.838708','2025-02-18 11:20:57.838708','admin','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImFkbWluQGV4YW1wbGUuY29tIiwic3ViIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJpYXQiOjE3Mzk4NTI0NTcsImV4cCI6MTczOTkzODg1N30.ZKw9ZqgRTt6yfopOBGYQgZLqakzv3IsOlb5WcOJSuuk'),('US002','Street 202, Ho Chi Minh City','2025-02-18 11:20:57.972814','staff@example.com','Staff Member','Female',_binary '\0','$2a$10$d5dK9g3zIMTFpjRu7hRsOu3Ud3fZujFIfpw7Isy2kTbafO9yOQJVS','2222222222',NULL,NULL,'MANAGER','2025-02-18 23:50:57.972814','2025-02-18 11:20:57.972814','staff_member','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN0YWZmQGV4YW1wbGUuY29tIiwic3ViIjoic3RhZmZAZXhhbXBsZS5jb20iLCJpYXQiOjE3Mzk4NTI0NTcsImV4cCI6MTczOTkzODg1N30.2SOjV1KW2fKcoKrnstGR1xJGRl4xzmh3QV5NcP6Fqf0'),('US003','Street 456, Ho Chi Minh City','2025-02-18 11:20:58.075632','student@example.com','John Doe','Male',_binary '\0','$2a$10$4QjF.7wJVxCmkuMxXjzG1uOAXsqVBdyuW3KADk.aNqSSlGVCs9a8S','3333333333',NULL,NULL,'STUDENT','2025-02-18 23:50:58.075632','2025-02-18 11:20:58.075632','student_user','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN0dWRlbnRAZXhhbXBsZS5jb20iLCJzdWIiOiJzdHVkZW50QGV4YW1wbGUuY29tIiwiaWF0IjoxNzM5ODUyNDU3LCJleHAiOjE3Mzk5Mzg4NTd9.Bv3F8u-3ZGhCUhqfcxNAkztNBT99n_RUP0w_vpE4hyo'),('US004','Street 606, Ho Chi Minh City','2025-02-18 11:20:58.164834','student2@example.com','John Green','Male',_binary '\0','$2a$10$M3cS9ADMXBw7yP.hoNIeqeu4DPaoh5dBuAFHXFWeS0xIi45rKgX3y','4444444444',NULL,NULL,'STUDENT','2025-02-18 23:50:58.164834','2025-02-18 11:20:58.164834','student_user2','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN0dWRlbnQyQGV4YW1wbGUuY29tIiwic3ViIjoic3R1ZGVudDJAZXhhbXBsZS5jb20iLCJpYXQiOjE3Mzk4NTI0NTgsImV4cCI6MTczOTkzODg1OH0.2DAQKi9G6VAkdV-3OKEYPZwhcmYF69CnsUv8Hxhel-M'),('US005','Street 303, Ho Chi Minh City','2025-02-18 11:20:58.248548','student3@example.com','Alice Jones','Female',_binary '\0','$2a$10$tOyZoP6qnSPrHo7OxNi2Y.Nc9p3xvjdA4xlX2h.65bZCVwQGhwBNK','5555555555',NULL,NULL,'STUDENT','2025-02-18 23:50:58.248548','2025-02-18 11:20:58.248548','student_user3','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN0dWRlbnQzQGV4YW1wbGUuY29tIiwic3ViIjoic3R1ZGVudDNAZXhhbXBsZS5jb20iLCJpYXQiOjE3Mzk4NTI0NTgsImV4cCI6MTczOTkzODg1OH0.aMc0o2QQ8NWLHSpV-2W3zIEsulWiARwg40vWr_JQBGc'),('US006','Street 789, Ho Chi Minh City','2025-02-18 11:20:58.337342','parent@example.com','Jane Smith','Female',_binary '\0','$2a$10$foe8XSPJyRND8vtkRn9EfOjDi.VKtt083PPx3eBcmaX9rO7CNGy.S','6666666666',NULL,NULL,'PARENT','2025-02-18 23:50:58.337342','2025-02-18 11:20:58.337342','parent_user','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBhcmVudEBleGFtcGxlLmNvbSIsInN1YiI6InBhcmVudEBleGFtcGxlLmNvbSIsImlhdCI6MTczOTg1MjQ1OCwiZXhwIjoxNzM5OTM4ODU4fQ.d2erszj4WBQtzWbjWv0XMjuUA7Kca885IbwAnUY7yRo'),('US007','Street 404, Ho Chi Minh City','2025-02-18 11:20:58.423688','parent2@example.com','Bob Johnson','Male',_binary '\0','$2a$10$FvrUWqEqp8oybavQAv/LDeI3X7n22r7HBoxyABOSqLpILo6w8J./G','7777777777',NULL,NULL,'PARENT','2025-02-18 23:50:58.423688','2025-02-18 11:20:58.423688','parent_user2','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBhcmVudDJAZXhhbXBsZS5jb20iLCJzdWIiOiJwYXJlbnQyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM5ODUyNDU4LCJleHAiOjE3Mzk5Mzg4NTh9.Yc7DIsl3WWuUH2mYJulot1ajT_nvfYqUmAgHp46KUT8'),('US008','Street 101, Ho Chi Minh City','2025-02-18 11:20:58.533023','psychologist@example.com','Dr. Brown','Male',_binary '\0','$2a$10$gxSPlnQah4ZARyTWnU8QZ.rSuGUFRiQOJJXZV7RFTnLaXzZVFGVvm','8888888888',NULL,NULL,'PSYCHOLOGIST','2025-02-18 23:50:58.533023','2025-02-18 11:20:58.533023','psychologist_user','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBzeWNob2xvZ2lzdEBleGFtcGxlLmNvbSIsInN1YiI6InBzeWNob2xvZ2lzdEBleGFtcGxlLmNvbSIsImlhdCI6MTczOTg1MjQ1OCwiZXhwIjoxNzM5OTM4ODU4fQ.LWhibR_-TzOTR3anPLgDjKXHD4755O7Yd-mWcUsO6ss'),('US009','Street 505, Ho Chi Minh City','2025-02-18 11:20:58.618037','psychologist2@example.com','Dr. Blue','Male',_binary '\0','$2a$10$JCmLFGgnXYQLB5TcNBKPmeXIqrE3t3DimLJhNRZwvtXQfYjdxTT0m','9999999999',NULL,NULL,'PSYCHOLOGIST','2025-02-18 23:50:58.618037','2025-02-18 11:20:58.618037','psychologist_user2','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBzeWNob2xvZ2lzdDJAZXhhbXBsZS5jb20iLCJzdWIiOiJwc3ljaG9sb2dpc3QyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM5ODUyNDU4LCJleHAiOjE3Mzk5Mzg4NTh9.Zv3pqD9k4RcRycJH2XYrL-qXx6gnKYyRGiVDTHvkloc');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-18 11:27:40
