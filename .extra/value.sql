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
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `AppointmentID` varchar(36) NOT NULL,
  `CancellationReason` text,
  `CheckInTime` datetime(6) DEFAULT NULL,
  `CheckOutTime` datetime(6) DEFAULT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `PsychologistID` varchar(36) NOT NULL,
  `PsychologistNote` text,
  `Rating` int DEFAULT NULL,
  `Status` enum('CANCELLED','COMPLETED','IN_PROGRESS','SCHEDULED') DEFAULT NULL,
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
  `likes` int NOT NULL,
  `Title` varchar(100) DEFAULT NULL,
  `AuthorID` varchar(36) NOT NULL,
  PRIMARY KEY (`ArticleID`),
  KEY `FK6lu0nhcalisyhm83bb7kcwlhn` (`AuthorID`),
  CONSTRAINT `FK6lu0nhcalisyhm83bb7kcwlhn` FOREIGN KEY (`AuthorID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES
  ('ATC001','Anxiety disorders represent a group of mental health conditions characterized by excessive worry, fear, and related behavioral disturbances. These disorders affect approximately 264 million people worldwide, making them among the most common mental health issues globally. While occasional anxiety is a normal part of life, anxiety disorders involve persistent, intense fear or worry about everyday situations that can significantly impact daily functioning.\n\nTypes of anxiety disorders include generalized anxiety disorder (GAD), panic disorder, specific phobias, social anxiety disorder, agoraphobia, and separation anxiety. Each presents with unique symptoms, though they share common threads of excessive, irrational fear and distress. Generalized anxiety disorder involves persistent worrying about various aspects of life, while panic disorder features recurrent panic attacks - sudden episodes of intense fear accompanied by physical symptoms like heart palpitations, shortness of breath, and dizziness. Social anxiety disorder centers on fear of social situations and potential judgment from others, whereas specific phobias involve intense fear of particular objects or situations.','2025-03-16 17:37:35.594729',0,'Understanding Anxiety Disorders: Causes, Symptoms, and Treatment Options','UID001'),
  ('ATC002','Depression, clinically known as major depressive disorder, represents one of the most prevalent mental health challenges of our time, affecting approximately 264 million people globally across all age groups. Far beyond merely feeling sad or experiencing temporary low mood, depression manifests as a serious and often debilitating condition that impacts every aspect of an individual\'s life - from thought patterns and emotional experiences to physical health and social functioning. In recent years, our understanding of depression has evolved significantly, revealing its complex nature as a multifaceted condition with biological, psychological, and social dimensions.\n\nThe hallmark symptoms of depression extend well beyond persistent sadness. Individuals with depression frequently experience profound loss of interest or pleasure in previously enjoyable activities (anhedonia), significant changes in appetite and weight, disrupted sleep patterns (either insomnia or hypersomnia), psychomotor agitation or retardation noticeable to others, fatigue or energy loss, feelings of worthlessness or excessive guilt, diminished ability to think or concentrate, and recurrent thoughts of death or suicide. For a clinical diagnosis, these symptoms must persist for at least two weeks and cause significant distress or impairment in daily functioning.','2025-03-16 17:37:35.607084',0,'Depression in the Modern Age: Recognition, Management, and Recovery Paths','UID001'),
  ('ATC003','Stress represents one of the most ubiquitous experiences in human life, yet its mechanisms and implications remain widely misunderstood. While commonly experienced as an unpleasant emotional state, stress actually constitutes a complex physiological response system that has evolved over millions of years to help organisms survive threats. Understanding the intricate science behind stress can provide valuable insights into its effects on physical and mental health, along with more effective strategies for management in our fast-paced modern world.\n\nFrom an evolutionary perspective, the stress response developed as a survival mechanism, preparing our ancestors for fight or flight when confronted with predators or other immediate dangers. This response mobilizes resources to deal with threats efficiently - increasing alertness, accelerating heart rate, elevating blood pressure, and directing energy to muscles. While this system served hunter-gatherers well when facing acute physical threats, today\'s chronic stressors often involve psychological pressures like work deadlines, financial concerns, relationship difficulties, and information overload.','2025-03-16 17:37:35.618493',0,'The Science of Stress: Understanding Our Body\'s Response System','UID001'),
  ('ATC004','Obsessive-Compulsive Disorder (OCD) is a chronic mental health condition characterized by unwanted, intrusive thoughts (obsessions) and repetitive behaviors or mental acts (compulsions) performed to alleviate anxiety. Affecting approximately 1-2% of the population, OCD manifests in various forms including contamination fears, need for symmetry, taboo thoughts, and checking behaviors. The disorder exists on a spectrum, with some individuals experiencing mild symptoms while others face severe impairment in daily functioning.\n\nRecent neuroscientific research has identified specific brain circuit abnormalities in OCD patients, particularly in the cortico-striato-thalamo-cortical loops. Treatment typically combines cognitive-behavioral therapy (CBT) with exposure and response prevention (ERP), along with SSRIs as first-line pharmacological interventions. Emerging therapies include deep brain stimulation for treatment-resistant cases and virtual reality exposure therapy for complex contamination fears.','2025-03-16 17:37:35.629098',0,'Understanding OCD: Breaking the Cycle of Intrusive Thoughts and Compulsions','UID001'),
  ('ATC005','Post-Traumatic Stress Disorder (PTSD) develops in some individuals following exposure to traumatic events such as combat, assault, accidents, or natural disasters. Characterized by intrusive memories, hypervigilance, avoidance behaviors, and negative alterations in mood and cognition, PTSD affects approximately 3.5% of U.S. adults annually. The disorder demonstrates complex interactions between neurobiological changes and psychological processes, particularly in memory consolidation and fear conditioning pathways.\n\nEffective treatments include trauma-focused psychotherapies like prolonged exposure (PE) therapy and cognitive processing therapy (CPT). Neuroimaging studies show that successful treatment correlates with normalization of amygdala hyperactivity and increased prefrontal cortex regulation. Recent advances include MDMA-assisted psychotherapy in clinical trials and the use of neurofeedback to modulate fear responses.','2025-03-16 17:37:35.638939',0,'PTSD: Navigating Trauma and the Path to Healing','UID001'),
  ('ATC006','Mindfulness-based interventions have gained significant empirical support in recent decades as effective adjuncts to traditional mental health treatments. Rooted in Buddhist meditation practices, mindfulness involves purposeful, non-judgmental attention to present-moment experiences. Research demonstrates its efficacy in reducing symptoms of anxiety, depression, and stress while improving emotional regulation and cognitive flexibility.\n\nNeuroscientific studies reveal that regular mindfulness practice increases gray matter density in the prefrontal cortex and hippocampus while decreasing amygdala volume. Clinical applications include Mindfulness-Based Stress Reduction (MBSR) and Mindfulness-Based Cognitive Therapy (MBCT), both shown to reduce relapse rates in recurrent depression. Emerging research explores its role in pain management, addiction treatment, and attention deficit disorders.','2025-03-16 17:37:35.648714',0,'The Power of Mindfulness: Ancient Practice in Modern Mental Health Care','UID001'),
  ('ATC007','Eating disorders represent complex mental health conditions characterized by severe disturbances in eating behaviors and related thoughts/emotions. The main types include anorexia nervosa, bulimia nervosa, binge-eating disorder, and avoidant/restrictive food intake disorder (ARFID). These disorders have the highest mortality rate of any mental illness, emphasizing the critical need for early intervention and comprehensive treatment approaches.\n\nBiological factors play a significant role, with heritability estimates ranging from 28-74%. Neuroendocrine abnormalities in hunger/satiety signaling and altered reward processing in the brain contribute to symptom maintenance. Treatment requires multidisciplinary care including medical stabilization, nutritional rehabilitation, and psychotherapy. Family-Based Treatment (FBT) has shown particular efficacy for adolescent anorexia cases.','2025-03-16 17:37:35.660792',0,'Eating Disorders: Beyond the Surface of Food and Body Image','UID001'),
  ('ATC008','Sleep and mental health share a complex bidirectional relationship, with sleep disturbances both contributing to and resulting from psychiatric conditions. Chronic insomnia increases the risk of developing depression by a factor of 10, while 50-80% of psychiatric patients experience sleep problems. The REM sleep phase appears particularly crucial for emotional processing and memory consolidation.\n\nCognitive-Behavioral Therapy for Insomnia (CBT-I) has emerged as the gold standard treatment, addressing maladaptive sleep behaviors and cognitive distortions about sleep. Recent discoveries about the glymphatic system\'s role in clearing neural waste during sleep have opened new research avenues into neurodegenerative aspects of chronic sleep deprivation and mental illness.','2025-03-16 17:37:35.670402',0,'The Mental Health-Sleep Connection: Understanding Bidirectional Impacts','UID001'),
  ('ATC009','Personality disorders represent enduring patterns of inner experience and behavior that deviate markedly from cultural expectations, leading to significant distress or impairment. The DSM-5 recognizes 10 specific personality disorders organized into three clusters: A (odd/eccentric), B (dramatic/erratic), and C (anxious/fearful). Borderline Personality Disorder (BPD) remains the most researched, with dialectical behavior therapy (DBT) showing particular efficacy in reducing self-harm and emotional dysregulation.\n\nNeurobiological research highlights altered frontolimbic circuitry in BPD patients, particularly in emotional regulation and impulse control. The dimensional approach in ICD-11 marks a paradigm shift, assessing personality functioning across five trait domains rather than categorical diagnoses. Early intervention programs for emerging personality disorders in adolescents show promise in improving long-term outcomes.','2025-03-16 17:37:35.679704',0,'Personality Disorders: Understanding Complex Patterns of Behavior','UID001'),
  ('ATC010','The digital revolution has transformed mental health care delivery while introducing new psychological challenges. Teletherapy platforms and mental health apps increase accessibility, particularly for rural populations. However, excessive screen time, social media comparison, and cyberbullying correlate with increased anxiety and depression rates, especially among adolescents.\n\nEmerging technologies like AI-powered chatbots and virtual reality exposure therapy show promise in supplementing traditional treatments. However, digital phenotyping and predictive algorithms raise ethical concerns regarding privacy and diagnostic accuracy. The WHO\'s digital mental health intervention guidelines emphasize the need for evidence-based digital tools with human oversight.','2025-03-16 17:37:35.691817',0,'Digital Age Mental Health: Navigating Technology\'s Double-Edged Sword','UID001'),
  ('ATC011','Adverse Childhood Experiences (ACEs) demonstrate a strong dose-response relationship with adult mental health outcomes. The landmark ACE study revealed that individuals with ≥4 ACEs have 4-12x increased risk for depression, suicide attempts, and substance abuse. Epigenetic mechanisms help explain how early trauma becomes biologically embedded, altering stress response systems and neural development.\n\nTrauma-informed care approaches emphasize safety, trustworthiness, and empowerment in therapeutic settings. Evidence-based interventions like Trauma-Focused CBT and eye movement desensitization and reprocessing (EMDR) help process traumatic memories. Recent research focuses on resilience factors and post-traumatic growth trajectories.','2025-03-16 17:37:35.701137',0,'Childhood Trauma and Adult Mental Health: Breaking the Cycle','UID001'),
  ('ATC012','Bipolar disorder is characterized by extreme mood swings between manic/hypomanic and depressive episodes. The condition affects approximately 2.8% of U.S. adults, with onset typically occurring in late adolescence or early adulthood. Accurate diagnosis remains challenging, with average delays of 5-10 years between symptom onset and proper treatment.\n\nMood stabilizers like lithium remain first-line treatments, demonstrating neuroprotective effects and suicide risk reduction. Psychoeducation programs focusing on early warning sign detection significantly improve outcomes. Emerging research explores the role of circadian rhythm disruptions and light therapy in managing bipolar depression.','2025-03-16 17:37:35.710486',0,'Bipolar Disorder: Managing the Peaks and Valleys','UID001'),
  ('ATC013','Addiction is a complex brain disorder characterized by compulsive substance use despite harmful consequences. The DSM-5 recognizes substance-related disorders across 10 drug classes, while behavioral addictions like gambling disorder are increasingly acknowledged. Neuroimaging studies reveal addiction-related changes in the mesolimbic dopamine system, prefrontal cortex, and stress response systems.\n\nEffective treatment combines medication-assisted treatment (MAT) with behavioral therapies like contingency management and motivational interviewing. The harm reduction model emphasizes meeting patients where they are, while neuroscience-informed approaches target impaired decision-making processes. Recent advances include deep brain stimulation for opioid use disorder and digital recovery support platforms.','2025-03-16 17:37:35.719736',0,'The Psychology of Addiction: Beyond Chemical Dependence','UID001'),
  ('ATC014','MBSR programs demonstrate 31% reduction in stress symptoms and 22% improvement in emotional regulation according to meta-analyses. The 8-week protocol combines body scanning, sitting meditation, and yoga to enhance present-moment awareness. Neuroimaging studies show increased gray matter density in the hippocampus and decreased amygdala activation following MBSR training.\n\nClinical applications now extend beyond stress reduction to chronic pain management and cancer care. Recent adaptations include digital MBSR platforms with comparable efficacy to in-person programs. Research challenges include standardizing teacher competency and addressing cultural adaptation needs in non-Western populations.','2025-03-16 17:37:35.728811',0,'Mindfulness-Based Stress Reduction: Science and Practice','UID002'),
  ('ATC015','Schizophrenia affects approximately 1% of the global population, characterized by psychotic symptoms like hallucinations and delusions, along with cognitive impairments. Early detection and intervention can significantly improve long-term outcomes.\n\nThe prodromal phase often precedes a full psychotic episode by several years. Early intervention programs using antipsychotics and psychosocial support have been shown to delay or prevent the onset of full-blown psychosis. Neuroinflammatory processes and genetic risk factors continue to be key research areas.','2025-03-16 17:37:35.739790',0,'Schizophrenia: Advances in Early Detection and Intervention','UID001'),
  ('ATC016','Autism Spectrum Disorder (ASD) is a neurodevelopmental condition affecting 1 in 54 children in the U.S. ASD is characterized by difficulties in social communication and repetitive behaviors, with significant variability in symptom severity. The neurodiversity movement advocates for recognizing and valuing neurological differences rather than viewing ASD as a deficit.\n\nInterventions like Applied Behavior Analysis (ABA) remain controversial, with debates over their effectiveness and ethical implications. Emerging treatments focus on sensory integration therapies and supporting individuals\' unique strengths. Innovations in assistive technologies are improving communication for nonverbal individuals, while inclusive education models promote integration in mainstream settings.','2025-03-16 17:37:35.751046',0,'Autism Spectrum Disorder: Rethinking Neurodiversity','UID001'),
  ('ATC017','Obsessive-compulsive disorder (OCD) affects 1-2% of the population, with symptoms typically emerging in childhood or adolescence. OCD is characterized by recurrent intrusive thoughts (obsessions) and repetitive behaviors (compulsions) aimed at reducing distress.\n\nCognitive-behavioral therapy (CBT) with exposure and response prevention (ERP) is the gold-standard treatment for OCD, showing significant symptom reduction. Selective serotonin reuptake inhibitors (SSRIs) remain first-line pharmacotherapy, with augmentation strategies like deep brain stimulation reserved for treatment-resistant cases. Current research explores the role of inflammation and microbiome-gut-brain axis in OCD pathophysiology.','2025-03-16 17:37:35.776956',0,'OCD: Breaking the Cycle of Obsessions and Compulsions','UID001'),
  ('ATC018','Eating disorders like anorexia nervosa, bulimia nervosa, and binge-eating disorder affect approximately 9% of the global population, with a higher prevalence among adolescents. Treatment often requires multidisciplinary approaches combining psychotherapy, nutritional support, and medical management.\n\nCognitive-behavioral therapy (CBT) remains the most effective psychological intervention, while family-based therapy (FBT) has been successful in treating younger patients with anorexia nervosa. New research on the gut-brain axis is exploring how gut microbiota imbalances may contribute to eating disorder pathology. Virtual reality-based therapies are also being tested for body image distortion in patients with eating disorders.','2025-03-16 17:37:35.786283',0,'Eating Disorders: Navigating the Complexities of Treatment','UID001'),
  ('ATC019','Stroke is a leading cause of disability worldwide, with approximately 15 million people experiencing a stroke each year. Advances in neuroplasticity research highlight the brain\'s capacity to reorganize and recover function after stroke through rehabilitation therapies.\n\nConstraint-induced movement therapy (CIMT) and task-specific training have been shown to enhance motor recovery by promoting neuroplastic changes in affected brain areas. Non-invasive brain stimulation techniques like transcranial magnetic stimulation (TMS) are also being investigated as adjunctive treatments for post-stroke recovery. Emerging technologies like brain-computer interfaces (BCIs) hold promise for assisting patients with severe motor impairments.','2025-03-16 17:37:35.798609',0,'The Role of Neuroplasticity in Post-Stroke Recovery','UID001'),
  ('ATC020','Alzheimer\'s disease is the most common form of dementia, affecting over 50 million people worldwide. Current treatments primarily target symptom management, with acetylcholinesterase inhibitors and NMDA receptor antagonists being the most commonly prescribed drugs.\n\nRecent advances in amyloid-targeting therapies, including monoclonal antibodies like aducanumab, have sparked debate over their clinical efficacy and safety. Non-pharmacological interventions, such as cognitive stimulation therapy, physical exercise, and diet, are increasingly recognized for their neuroprotective effects. Ongoing research is also investigating the role of neuroinflammation and metabolic dysfunction in Alzheimer\'s pathology.','2025-03-16 17:37:35.807893',0,'Alzheimer\'s Disease: New Insights and Therapeutic Approaches','UID001');
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articletags`
--

DROP TABLE IF EXISTS `articletags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articletags` (
  `ArticleId` varchar(36) NOT NULL,
  `TagId` varchar(36) NOT NULL,
  PRIMARY KEY (`ArticleId`,`TagId`),
  KEY `FK211863rqolg9dby033aq5csuo` (`TagId`),
  CONSTRAINT `FK1p80k2gpjsimmemlg107oy41a` FOREIGN KEY (`ArticleId`) REFERENCES `article` (`ArticleID`),
  CONSTRAINT `FK211863rqolg9dby033aq5csuo` FOREIGN KEY (`TagId`) REFERENCES `tags` (`TagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articletags`
--

LOCK TABLES `articletags` WRITE;
/*!40000 ALTER TABLE `articletags` DISABLE KEYS */;
INSERT INTO `articletags` VALUES
('ATC013','TAG001')
,('ATC001','TAG002'),
('ATC007','TAG002')
,('ATC009','TAG002'),
('ATC010','TAG002')
,('ATC014','TAG002'),
('ATC016','TAG002')
,('ATC017','TAG002'),
('ATC018','TAG002')
,('ATC019','TAG002'),
('ATC011','TAG003')
,('ATC020','TAG003'),
('ATC015','TAG004')
,('ATC008','TAG005'),
('ATC012','TAG005')
,('ATC014','TAG005'),
('ATC016','TAG005')
,('ATC017','TAG005'),
('ATC018','TAG005')
,('ATC020','TAG007'),
('ATC002','TAG008')
,('ATC012','TAG008'),
('ATC003','TAG010')
,('ATC002','TAG011'),
('ATC001','TAG013')
,('ATC002','TAG013'),
('ATC003','TAG013')
,('ATC013','TAG014'),
('ATC004','TAG017')
,('ATC019','TAG019'),
('ATC004','TAG021')
,('ATC007','TAG024'),
('ATC005','TAG025')
,('ATC009','TAG025'),
('ATC006','TAG026')
,('ATC002','TAG027'),
('ATC005','TAG030')
,('ATC015','TAG034'),
('ATC010','TAG037')
,('ATC006','TAG040'),
,('ATC008','TAG043')
,('ATC001','TAG060');
/*!40000 ALTER TABLE `articletags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `CategoryID` varchar(36) NOT NULL,
  `CategoryName` enum('ANXIETY','DEPRESSION','STRESS') NOT NULL,
  PRIMARY KEY (`CategoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES ('CAT001','STRESS')
,('CAT002','DEPRESSION')
,('CAT003','ANXIETY');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `AppointmentID` varchar(36) DEFAULT NULL,
  `ArticleID` varchar(36) DEFAULT NULL,
  `content` text NOT NULL,
  `createdAt` datetime(6) DEFAULT NULL,
  `ProgramID` varchar(36) DEFAULT NULL,
  `Rating` int DEFAULT NULL,
  `SurveyID` varchar(36) DEFAULT NULL,
  `Article` varchar(36) DEFAULT NULL,
  `UserID` varchar(36) NOT NULL,
  `ParentCommentID` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoav7rdyjaf6oawhs06ga6ekd1` (`AppointmentID`),
  KEY `FKmhbf6s5paxfvy7630cbvufmvo` (`Article`),
  KEY `FKqrwtn1chcp7ocoaummlh3bf39` (`UserID`),
  KEY `FK278qt707s8c9ip47fh8pwqtc3` (`ParentCommentID`),
  KEY `FKscm9u96d2vbmku532q313k1uu` (`ProgramID`),
  KEY `FKgfyd4yb9t9sytdwwcmt0mik8b` (`SurveyID`),
  CONSTRAINT `FK278qt707s8c9ip47fh8pwqtc3` FOREIGN KEY (`ParentCommentID`) REFERENCES `comments` (`id`),
  CONSTRAINT `FKgfyd4yb9t9sytdwwcmt0mik8b` FOREIGN KEY (`SurveyID`) REFERENCES `surveys` (`SurveyID`),
  CONSTRAINT `FKmhbf6s5paxfvy7630cbvufmvo` FOREIGN KEY (`Article`) REFERENCES `article` (`ArticleID`),
  CONSTRAINT `FKoav7rdyjaf6oawhs06ga6ekd1` FOREIGN KEY (`AppointmentID`) REFERENCES `appointments` (`AppointmentID`),
  CONSTRAINT `FKqrwtn1chcp7ocoaummlh3bf39` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`),
  CONSTRAINT `FKscm9u96d2vbmku532q313k1uu` FOREIGN KEY (`ProgramID`) REFERENCES `programs` (`ProgramID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `default_time_slots`
--

DROP TABLE IF EXISTS `default_time_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `default_time_slots` (
  `slotId` varchar(255) NOT NULL,
  `endTime` time(6) NOT NULL,
  `period` varchar(255) NOT NULL,
  `startTime` time(6) NOT NULL,
  PRIMARY KEY (`slotId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `default_time_slots`
--

LOCK TABLES `default_time_slots` WRITE;
/*!40000 ALTER TABLE `default_time_slots` DISABLE KEYS */;
INSERT INTO `default_time_slots` VALUES
('AFTERNOON-00','13:30:00.000000','Afternoon','13:00:00.000000'),
('AFTERNOON-01','14:00:00.000000','Afternoon','13:30:00.000000'),
('AFTERNOON-02','14:30:00.000000','Afternoon','14:00:00.000000'),
('AFTERNOON-03','15:00:00.000000','Afternoon','14:30:00.000000'),
('AFTERNOON-04','15:30:00.000000','Afternoon','15:00:00.000000'),
('AFTERNOON-05','16:00:00.000000','Afternoon','15:30:00.000000'),
('AFTERNOON-06','16:30:00.000000','Afternoon','16:00:00.000000'),
('AFTERNOON-07','17:00:00.000000','Afternoon','16:30:00.000000'),
('MORNING-00','08:30:00.000000','Morning','08:00:00.000000'),
('MORNING-01','09:00:00.000000','Morning','08:30:00.000000'),
('MORNING-02','09:30:00.000000','Morning','09:00:00.000000'),
('MORNING-03','10:00:00.000000','Morning','09:30:00.000000'),
('MORNING-04','10:30:00.000000','Morning','10:00:00.000000'),
('MORNING-05','11:00:00.000000','Morning','10:30:00.000000');
/*!40000 ALTER TABLE `default_time_slots` ENABLE KEYS */;
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
INSERT INTO `department` VALUES
('DPT001','Child & Adolescent Psychology'),
('DPT002','School Counseling'),
('DPT003','Behavioral Therapy'),
('DPT004','Trauma & Crisis Intervention'),
('DPT005','Family & Parent Counseling'),
('DPT006','Stress & Anxiety Management'),
('DPT007','Depression & Mood Disorders'),
('DPT008','Special Education Support'),
('DPT009','Social Skills & Peer Relation'),
('DPT010','Suicide Prevention & Intervention'),
('DPT011','Digital Well-being Intervention');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `Comment` text,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `Rating` int DEFAULT NULL,
  `AppointmentID` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5iy6h8l31n2f00kwdewg3n07b` (`AppointmentID`),
  CONSTRAINT `FK5iy6h8l31n2f00kwdewg3n07b` FOREIGN KEY (`AppointmentID`) REFERENCES `appointments` (`AppointmentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_schedule`
--

DROP TABLE IF EXISTS `notification_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification_schedule` (
  `id` varchar(255) NOT NULL,
  `notification_day` tinyint DEFAULT NULL,
  `notification_time` time(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `notification_schedule_chk_1` CHECK ((`notification_day` between 0 and 6))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_schedule`
--

LOCK TABLES `notification_schedule` WRITE;
/*!40000 ALTER TABLE `notification_schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `NotificationID` varchar(36) NOT NULL,
  `AppointmentID` varchar(36) DEFAULT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `IsRead` bit(1) DEFAULT NULL,
  `Message` text NOT NULL,
  `ProgramID` varchar(36) DEFAULT NULL,
  `SurveyID` varchar(36) DEFAULT NULL,
  `Title` varchar(255) NOT NULL,
  `Type` enum('APPOINTMENT','DONE','NOTIFICATION_TYPE','ON_LEAVE','PROGRAM','SURVEY') NOT NULL,
  `UserID` varchar(36) NOT NULL,
  PRIMARY KEY (`NotificationID`),
  KEY `FKgc9xl1x8fpo955vsq8kb3b566` (`AppointmentID`),
  KEY `FKnnjwgcebdpf2w50ecjmasadya` (`ProgramID`),
  KEY `FKk7ft32q0hcu6vwgdndm15o5ji` (`SurveyID`),
  KEY `FK689wccaxeicnjy9vjaqks20ya` (`UserID`),
  CONSTRAINT `FK689wccaxeicnjy9vjaqks20ya` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`),
  CONSTRAINT `FKgc9xl1x8fpo955vsq8kb3b566` FOREIGN KEY (`AppointmentID`) REFERENCES `appointments` (`AppointmentID`),
  CONSTRAINT `FKk7ft32q0hcu6vwgdndm15o5ji` FOREIGN KEY (`SurveyID`) REFERENCES `surveys` (`SurveyID`),
  CONSTRAINT `FKnnjwgcebdpf2w50ecjmasadya` FOREIGN KEY (`ProgramID`) REFERENCES `programs` (`ProgramID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES
('NOT001',NULL,'2025-03-16 17:37:35.822607',_binary '\0','Your appointment is scheduled',NULL,NULL,'Appointment Scheduled','APPOINTMENT','UID003'),
('NOT002',NULL,'2025-03-16 17:37:35.834160',_binary '\0','You have a new appointment',NULL,NULL,'New Appointment','APPOINTMENT','UID007'),
('NOT003',NULL,'2025-03-16 17:37:35.846637',_binary '\0','You have a new survey',NULL,NULL,'New Survey','SURVEY','UID007'),
('NOT004',NULL,'2025-03-16 17:37:35.857175',_binary '\0','You have a new program',NULL,NULL,'New Program','PROGRAM','UID007');
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
INSERT INTO `parents` VALUES ('PRT001','UID005')
,('PRT002','UID006');
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
  `Status` enum('CANCELLED','COMPLETED','JOINED') NOT NULL,
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
  `EndDate` date DEFAULT NULL,
  `FacilitatorID` varchar(36) NOT NULL,
  `MeetingLink` varchar(255) DEFAULT NULL,
  `NumberParticipants` int DEFAULT NULL,
  `ProgramName` varchar(100) NOT NULL,
  `Rating` int DEFAULT NULL,
  `StartDate` date DEFAULT NULL,
  `Status` enum('ACTIVE','COMPLETED','DELETED','INACTIVE','IN_PROGRESS') NOT NULL,
  `AppointmentType` enum('OFFLINE','ONLINE') NOT NULL,
  `CreatedByUser` varchar(36) NOT NULL,
  PRIMARY KEY (`ProgramID`),
  KEY `FKd5ofntotwoedguak122qijufi` (`CreatedByUser`),
  KEY `FK4jmg7ygekgx5spttenbdmx1kx` (`DepartmentID`),
  KEY `FKsb6seu9egjvj1832rhwsi7awb` (`FacilitatorID`),
  CONSTRAINT `FK4jmg7ygekgx5spttenbdmx1kx` FOREIGN KEY (`DepartmentID`) REFERENCES `department` (`DepartmentID`),
  CONSTRAINT `FKd5ofntotwoedguak122qijufi` FOREIGN KEY (`CreatedByUser`) REFERENCES `users` (`UserID`),
  CONSTRAINT `FKsb6seu9egjvj1832rhwsi7awb` FOREIGN KEY (`FacilitatorID`) REFERENCES `psychologists` (`PsychologistID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programs`
--

LOCK TABLES `programs` WRITE;
/*!40000 ALTER TABLE `programs` DISABLE KEYS */;
INSERT INTO `programs` VALUES
('PRG001','2025-03-16 17:37:32.923708','DPT006','Program to help manage stress',4,'2025-03-20','PSY001','https://example.com/meeting1',20,'Stress Management',NULL,'2025-03-16','ACTIVE','ONLINE','UID001'),
,('PRG003','2025-03-16 17:37:33.242454','DPT003','Workshop on mindfulness techniques',3,'2025-04-02','PSY001','https://example.com/meeting3',25,'Mindfulness Workshop',NULL,'2025-03-30','ACTIVE','ONLINE','UID001'),
,('PRG005','2025-03-16 17:37:33.417356','DPT007','Counseling for individuals with depression',2,'2025-04-08','PSY002','https://example.com/meeting4',30,'Depression Counseling',NULL,'2025-04-06','ACTIVE','ONLINE','UID001'),
('PRG006','2025-03-16 17:37:33.516140','DPT007','Counseling for individuals with depression',2,'2025-04-01','PSY002','https://example.com/meeting4',30,'Depression Counseling',NULL,'2025-03-30','ACTIVE','ONLINE','UID001'),
('PRG007','2025-03-16 17:37:33.597885','DPT007','Counseling for individuals with depression',2,'2025-03-25','PSY001','https://example.com/meeting4',30,'Depression Counseling',NULL,'2025-03-23','ACTIVE','ONLINE','UID001'),
('PRG008','2025-03-16 17:37:33.664325','DPT007','Counseling for individuals with depression',2,'2025-04-01','PSY002','https://example.com/meeting4',30,'Depression Counseling',NULL,'2025-03-30','ACTIVE','ONLINE','UID001'),
('PRG009','2025-03-16 17:37:33.736547','DPT007','Counseling for individuals with depression',2,'2025-03-18','PSY001','https://example.com/meeting4',30,'Depression Counseling',NULL,'2025-03-16','ACTIVE','ONLINE','UID001'),
('PRG010','2025-03-16 17:37:33.808222','DPT007','Counseling for individuals with depression',2,'2025-03-25','PSY001','https://example.com/meeting4',30,'Depression Counseling',NULL,'2025-03-23','ACTIVE','ONLINE','UID001');
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
  `Program` varchar(36) NOT NULL,
  PRIMARY KEY (`ScheduleID`),
  KEY `FKnq04h0tks92lngk23in256c6d` (`Program`),
  CONSTRAINT `FKnq04h0tks92lngk23in256c6d` FOREIGN KEY (`Program`) REFERENCES `programs` (`ProgramID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programschedule`
--

LOCK TABLES `programschedule` WRITE;
/*!40000 ALTER TABLE `programschedule` DISABLE KEYS */;
INSERT INTO `programschedule` VALUES
('SCH001','Monday','11:30:00.000000','PRG001','10:00:00.000000','PRG001'),
('SCH002','Tuesday','15:30:00.000000','PRG002','14:00:00.000000','PRG002'),
('SCH003','Wednesday','11:00:00.000000','PRG003','09:00:00.000000','PRG003'),
('SCH004','Thursday','12:00:00.000000','PRG004','10:00:00.000000','PRG004'),
('SCH005','Friday','15:00:00.000000','PRG005','13:00:00.000000','PRG005'),
('SCH006','Monday','10:00:00.000000','PRG006','08:00:00.000000','PRG006'),
('SCH007','Tuesday','13:00:00.000000','PRG007','11:00:00.000000','PRG007'),
('SCH008','Wednesday','14:00:00.000000','PRG008','12:00:00.000000','PRG008'),
('SCH009','Thursday','16:00:00.000000','PRG009','14:00:00.000000','PRG009'),
('SCH010','Friday','18:00:00.000000','PRG010','16:00:00.000000','PRG010');
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
INSERT INTO `programtags` VALUES
('PRG001','TAG001')
,('PRG001','TAG002')
,('PRG001','TAG003'),
('PRG002','TAG004')
,('PRG002','TAG005')
,('PRG002','TAG006'),
('PRG003','TAG007')
,('PRG003','TAG008')
,('PRG003','TAG009'),
('PRG004','TAG010')
,('PRG005','TAG010')
,('PRG006','TAG010'),
('PRG007','TAG010')
,('PRG008','TAG010')
,('PRG009','TAG010'),
('PRG010','TAG010')
,('PRG004','TAG011')
,('PRG005','TAG011'),
('PRG006','TAG011')
,('PRG007','TAG011')
,('PRG008','TAG011'),
('PRG009','TAG011')
,('PRG010','TAG011')
,('PRG004','TAG012'),
('PRG005','TAG012')
,('PRG006','TAG012')
,('PRG007','TAG012'),
('PRG008','TAG012')
,('PRG009','TAG012')
,('PRG010','TAG012');
/*!40000 ALTER TABLE `programtags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `psychologist_kpi`
--

DROP TABLE IF EXISTS `psychologist_kpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `psychologist_kpi` (
  `id` varchar(255) NOT NULL,
  `achieved_slots` int DEFAULT NULL,
  `month` int NOT NULL,
  `psychologist_id` varchar(255) DEFAULT NULL,
  `target_slots` int DEFAULT NULL,
  `year` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `psychologist_kpi`
--

LOCK TABLES `psychologist_kpi` WRITE;
/*!40000 ALTER TABLE `psychologist_kpi` DISABLE KEYS */;
/*!40000 ALTER TABLE `psychologist_kpi` ENABLE KEYS */;
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
  `Status` enum('ACTIVE','DELETED','INACTIVE','ON_LEAVE') NOT NULL,
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
INSERT INTO `psychologists` VALUES ('PSY001','DPT001','ACTIVE','UID003',10)
,('PSY002','DPT007','ACTIVE','UID004',8);
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
-- Table structure for table `resettokens`
--

DROP TABLE IF EXISTS `resettokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resettokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ExpiresAt` datetime(6) NOT NULL,
  `HashedToken` varchar(255) NOT NULL,
  `UserID` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr4ep844po8w05gs6f2niidgfh` (`UserID`),
  CONSTRAINT `FKr4ep844po8w05gs6f2niidgfh` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resettokens`
--

LOCK TABLES `resettokens` WRITE;
/*!40000 ALTER TABLE `resettokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `resettokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `StudentID` varchar(36) NOT NULL,
  `AnxietyScore` decimal(5,2) DEFAULT NULL,
  `ClassName` varchar(20) DEFAULT NULL,
  `DepressionScore` decimal(5,2) DEFAULT NULL,
  `Grade` int DEFAULT NULL,
  `ParentID` varchar(36) DEFAULT NULL,
  `SchoolName` varchar(100) DEFAULT NULL,
  `StressScore` decimal(5,2) DEFAULT NULL,
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
INSERT INTO `students` VALUES
('STU001',0.00,'A',0.00,10,'PRT001','Example High School',0.00,'UID007'),
('STU002',0.00,'B',0.00,9,'PRT002','Example High School',0.00,'UID008'),
('STU003',0.00,'A',0.00,9,'PRT001','Example High School',0.00,'UID009');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveyquestionoptions`
--

DROP TABLE IF EXISTS `surveyquestionoptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surveyquestionoptions` (
  `OptionID` varchar(36) NOT NULL,
  `OptionText` text NOT NULL,
  `QuestionID` varchar(36) NOT NULL,
  `Score` int DEFAULT NULL,
  PRIMARY KEY (`OptionID`),
  KEY `FK4q67dge0qfux5g9homnyd9mp8` (`QuestionID`),
  CONSTRAINT `FK4q67dge0qfux5g9homnyd9mp8` FOREIGN KEY (`QuestionID`) REFERENCES `surveyquestions` (`QuestionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveyquestionoptions`
--

LOCK TABLES `surveyquestionoptions` WRITE;
/*!40000 ALTER TABLE `surveyquestionoptions` DISABLE KEYS */;
INSERT INTO `surveyquestionoptions` VALUES
('SQO001','Never','SQR001',0),
('SQO002','Almost Never','SQR001',1),
('SQO003','Sometimes','SQR001',2),
('SQO004','Fairly Often','SQR001',3),
('SQO005','Very Often','SQR001',4),
('SQO006','Never','SQR002',0),
('SQO007','Almost Never','SQR002',1),
('SQO008','Sometimes','SQR002',2),
('SQO009','Fairly Often','SQR002',3),
('SQO010','Very Often','SQR002',4),
('SQO011','Never','SQR003',0),
('SQO012','Almost Never','SQR003',1),
('SQO013','Sometimes','SQR003',2),
('SQO014','Fairly Often','SQR003',3),
('SQO015','Very Often','SQR003',4),
('SQO016','Never','SQR004',4),
('SQO017','Almost Never','SQR004',3),
('SQO018','Sometimes','SQR004',2),
('SQO019','Fairly Often','SQR004',1),
('SQO020','Very Often','SQR004',0),
('SQO021','Never','SQR005',4),
('SQO022','Almost Never','SQR005',3),
('SQO023','Sometimes','SQR005',2),
('SQO024','Fairly Often','SQR005',1),
('SQO025','Very Often','SQR005',0)
,('SQO026','Never','SQR006',0),
('SQO027','Almost Never','SQR006',1),
('SQO028','Sometimes','SQR006',2),
('SQO029','Fairly Often','SQR006',3),
('SQO030','Very Often','SQR006',4),
('SQO031','Never','SQR007',4),
('SQO032','Almost Never','SQR007',3),
('SQO033','Sometimes','SQR007',2),
('SQO034','Fairly Often','SQR007',1),
('SQO035','Very Often','SQR007',0),
('SQO036','Never','SQR008',4),
('SQO037','Almost Never','SQR008',3),
('SQO038','Sometimes','SQR008',2),
('SQO039','Fairly Often','SQR008',1),
('SQO040','Very Often','SQR008',0),
('SQO041','Never','SQR009',0),
('SQO042','Almost Never','SQR009',1),
('SQO043','Sometimes','SQR009',2),
('SQO044','Fairly Often','SQR009',3),
('SQO045','Very Often','SQR009',4),
('SQO046','Never','SQR010',0),
('SQO047','Almost Never','SQR010',1)
,('SQO048','Sometimes','SQR010',2),
('SQO049','Fairly Often','SQR010',3),
('SQO050','Very Often','SQR010',4),
('SQO051','Not at all','SQR011',0),
('SQO052','Several days','SQR011',1),
('SQO053','More than half the days','SQR011',2),
('SQO054','Nearly every day','SQR011',3),
('SQO055','Not at all','SQR012',0),
('SQO056','Several days','SQR012',1),
('SQO057','More than half the days','SQR012',2),
('SQO058','Nearly every day','SQR012',3),
('SQO059','Not at all','SQR013',0),
('SQO060','Several days','SQR013',1),
('SQO061','More than half the days','SQR013',2)
,('SQO062','Nearly every day','SQR013',3),
('SQO063','Not at all','SQR014',0),
('SQO064','Several days','SQR014',1),
('SQO065','More than half the days','SQR014',2),
('SQO066','Nearly every day','SQR014',3),
('SQO067','Not at all','SQR015',0),
('SQO068','Several days','SQR015',1),
('SQO069','More than half the days','SQR015',2),
('SQO070','Nearly every day','SQR015',3),
('SQO071','Not at all','SQR016',0),
('SQO072','Several days','SQR016',1),
('SQO073','More than half the days','SQR016',2),
('SQO074','Nearly every day','SQR016',3),
('SQO075','Not at all','SQR017',0),
('SQO076','Several days','SQR017',1),
('SQO077','More than half the days','SQR017',2),
('SQO078','Nearly every day','SQR017',3),
('SQO079','Not at all','SQR018',0),
('SQO080','Several days','SQR018',1),
('SQO081','More than half the days','SQR018',2),
('SQO082','Nearly every day','SQR018',3),
('SQO083','Not at all','SQR019',0)
,('SQO084','Several days','SQR019',1),
('SQO085','More than half the days','SQR019',2),
('SQO086','Nearly every day','SQR019',3),
('SQO087','Not at all','SQR020',0),
('SQO088','Several days','SQR020',1),
('SQO089','More than half the days','SQR020',2),
('SQO090','Nearly every day','SQR020',3),
('SQO091','Not at all','SQR021',0),
('SQO092','Several days','SQR021',1),
('SQO093','More than half the days','SQR021',2),
('SQO094','Nearly every day','SQR021',3),
('SQO095','Not at all','SQR022',0),
('SQO096','Several days','SQR022',1),
('SQO097','More than half the days','SQR022',2),
('SQO098','Nearly every day','SQR022',3),
('SQO099','Not at all','SQR023',0),
('SQO100','Several days','SQR023',1),
('SQO101','More than half the days','SQR023',2),
('SQO102','Nearly every day','SQR023',3),
('SQO103','Not at all','SQR024',0),
('SQO104','Several days','SQR024',1),
('SQO105','More than half the days','SQR024',2),
('SQO106','Nearly every day','SQR024',3),
('SQO107','Not at all','SQR025',0)
,(
'SQO108','Several days','SQR025',1),
('SQO109','More than half the days','SQR025',2),
('SQO110','Nearly every day','SQR025',3),
('SQO111','Not at all','SQR026',0),
('SQO112','Several days','SQR026',1),
('SQO113','More than half the days','SQR026',2),
('SQO114','Nearly every day','SQR026',3),
('SQO115','Not at all','SQR027',0),
('SQO116','Several days','SQR027',1),
('SQO117','More than half the days','SQR027',2),
('SQO118','Nearly every day','SQR027',3),
('SQO119','Not at all','SQR028',0),
('SQO120','Several days','SQR028',1),
('SQO121','More than half the days','SQR028',2),
('SQO122','Nearly every day','SQR028',3);
/*!40000 ALTER TABLE `surveyquestionoptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveyquestionoptionschoices`
--

DROP TABLE IF EXISTS `surveyquestionoptionschoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surveyquestionoptionschoices` (
  `OptionsChoices` varchar(36) NOT NULL,
  `CreatedAt` datetime(6) NOT NULL,
  `OptionID` varchar(36) NOT NULL,
  `QuestionID` varchar(36) NOT NULL,
  `ResultID` varchar(36) NOT NULL,
  PRIMARY KEY (`OptionsChoices`),
  KEY `FK3qehlh5up2l2r9vewfkrwl24w` (`OptionID`),
  KEY `FK6qx6b2f8wgqsmntyotpqwu39g` (`QuestionID`),
  KEY `FK350lkbwomn7xo1h3sss4ye7xj` (`ResultID`),
  CONSTRAINT `FK350lkbwomn7xo1h3sss4ye7xj` FOREIGN KEY (`ResultID`) REFERENCES `surveyresult` (`ResultID`),
  CONSTRAINT `FK3qehlh5up2l2r9vewfkrwl24w` FOREIGN KEY (`OptionID`) REFERENCES `surveyquestionoptions` (`OptionID`),
  CONSTRAINT `FK6qx6b2f8wgqsmntyotpqwu39g` FOREIGN KEY (`QuestionID`) REFERENCES `surveyquestions` (`QuestionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveyquestionoptionschoices`
--

LOCK TABLES `surveyquestionoptionschoices` WRITE;
/*!40000 ALTER TABLE `surveyquestionoptionschoices` DISABLE KEYS */;
INSERT INTO `surveyquestionoptionschoices` VALUES
('SQC001001002','2025-03-16 17:37:35.357021','SQO002','SQR001','SRS001'),
('SQC001002006','2025-03-16 17:37:35.368576','SQO006','SQR002','SRS001'),
('SQC001003012','2025-03-16 17:37:35.378445','SQO012','SQR003','SRS001'),
('SQC001004017','2025-03-16 17:37:35.387837','SQO017','SQR004','SRS001'),
('SQC001005021','2025-03-16 17:37:35.398632','SQO021','SQR005','SRS001'),
('SQC001006026','2025-03-16 17:37:35.409902','SQO026','SQR006','SRS001'),
('SQC001007031','2025-03-16 17:37:35.420751','SQO031','SQR007','SRS001'),
('SQC001008036','2025-03-16 17:37:35.431781','SQO036','SQR008','SRS001'),
('SQC001009041','2025-03-16 17:37:35.441172','SQO041','SQR009','SRS001'),
('SQC001010046','2025-03-16 17:37:35.451190','SQO046','SQR010','SRS001');
/*!40000 ALTER TABLE `surveyquestionoptionschoices` ENABLE KEYS */;
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
INSERT INTO `surveyquestions` VALUES
('SQR001','CAT001','In the last month, how often have you been upset because something that happened was unexpected?','SUV001'),
('SQR002','CAT001','In the last month, how often have you felt that you were unable to control the important things in your life?','SUV001'),
('SQR003','CAT001','In the last month, how often have you felt nervous and stressed?','SUV001'),
('SQR004','CAT001','In the last month, how often have you felt confident about your ability to handle your personal problems?','SUV001'),
('SQR005','CAT001','In the last month, how often have you felt that things were going your way?','SUV001')
('SQR006','CAT001','In the last month, how often have you found that you could not cope with all the things you had to do?','SUV001'),
('SQR007','CAT001','In the last month, how often have you been able to control irritations in your life?','SUV001'),
('SQR008','CAT001','In the last month, how often have you felt that you were on top of things?','SUV001'),
('SQR009','CAT001','In the last month, how often have you been angered because of things that happened that were outside of your control?','SUV001'),
('SQR010','CAT001','In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?','SUV001'),
('SQR011','CAT002','Feeling nervous, anxious, or on edge?','SUV002')
,('SQR012','CAT002','Not being able to stop or control worrying?','SUV002'),
('SQR013','CAT002','Trouble relaxing','SUV002')
,('SQR014','CAT002','Do you feel sad or hopeless most of the time?','SUV002'),
('SQR015','CAT002','Being so restless that it\'s hard to sit still?','SUV002'),
('SQR016','CAT002','Becoming easily annoyed or irritable?','SUV002'),
('SQR017','CAT002','Feeling afraid as if something awful might happen?','SUV002'),
('SQR018','CAT003','Little interest or pleasure in doing things?','SUV003'),
('SQR019','CAT003','Feeling down, depressed, or hopeless?','SUV003'),
('SQR020','CAT003','Trouble falling or staying asleep, or sleeping too much?','SUV003'),
('SQR021','CAT003','Feeling tired or having little energy?','SUV003'),
('SQR022','CAT003','Poor appetite or overeating?','SUV003'),
'SQR023','CAT003','Feeling bad about yourself — or that you are a failure or have let yourself or your family down?','SUV003'),
('SQR024','CAT003','Trouble concentrating on things, such as reading the newspaper or watching television?','SUV003'),
('SQR025','CAT003','Moving or speaking so slowly that other people could have noticed? Or so fidgety or restless that you have been moving a lot more than usual?','SUV003'),
('SQR026','CAT003','Thoughts that you would be better off dead, or thoughts of hurting yourself in some way?','SUV003'),
('SQR027','CAT001','How are you feeling today?','SUV004'),
('SQR028','CAT001','Are you having a good day?','SUV004');
/*!40000 ALTER TABLE `surveyquestions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveyresult`
--

DROP TABLE IF EXISTS `surveyresult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surveyresult` (
  `ResultID` varchar(36) NOT NULL,
  `CreatedAt` datetime(6) NOT NULL,
  `MaxScore` int DEFAULT NULL,
  `Result` int DEFAULT NULL,
  `StudentID` varchar(36) NOT NULL,
  `SurveyID` varchar(36) NOT NULL,
  PRIMARY KEY (`ResultID`),
  KEY `FK3vqby6ohalw8g63q5gg3f4tr9` (`StudentID`),
  KEY `FKcspdsjw75p9bcy2x3uahgsm99` (`SurveyID`),
  CONSTRAINT `FK3vqby6ohalw8g63q5gg3f4tr9` FOREIGN KEY (`StudentID`) REFERENCES `students` (`StudentID`),
  CONSTRAINT `FKcspdsjw75p9bcy2x3uahgsm99` FOREIGN KEY (`SurveyID`) REFERENCES `surveys` (`SurveyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveyresult`
--

LOCK TABLES `surveyresult` WRITE;
/*!40000 ALTER TABLE `surveyresult` DISABLE KEYS */;
INSERT INTO `surveyresult` VALUES ('SRS001','2025-03-16 17:37:35.347944',40,17,'STU001','SUV001');
/*!40000 ALTER TABLE `surveyresult` ENABLE KEYS */;
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
  `Details` text,
  `Duration` varchar(100) DEFAULT NULL,
  `Status` enum('ACTIVE','CANCELLED','INACTIVE') NOT NULL,
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
INSERT INTO `surveys` VALUES
('SUV001','CAT001','2025-03-16 17:37:33.842583','UID003','Survey to assess stress levels',NULL,NULL,'ACTIVE','Stress Survey'),
('SUV002','CAT002','2025-03-16 17:37:33.853647','UID004','Assessment of anxiety symptoms',NULL,NULL,'ACTIVE','Anxiety Assessment'),
,('SUV004','CAT001','2025-03-16 17:37:33.873269','UID003','Assessment of mood',NULL,NULL,'INACTIVE','Mood Assessment');
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
INSERT INTO `tags` VALUES ('TAG001','STRESS')
,('TAG002','TEST STRESS')
,('TAG003','ACADEMIC STRESS'),
('TAG004','WORK STRESS')
,('TAG005','FINANCIAL STRESS')
,('TAG006','RELATIONSHIP STRESS'),
('TAG007','SELF IMPROVEMENT STRESS')
,('TAG008','STRESS MANAGEMENT')
,('TAG009','ANXIETY'),
('TAG010','TEST ANXIETY')
,('TAG011','DEPRESSION')
,('TAG012','TEST DEPRESSION'),
('TAG013','MENTAL HEALTH')
,('TAG014','EMOTIONAL INTELLIGENCE')
,('TAG015','EMOTIONAL REGULATION'),
('TAG016','EMOTIONAL EXPRESSION')
,('TAG017','MINDFULNESS')
,('TAG018','MEDITATION')
,('TAG019','RELAXATION'),
('TAG020','SLEEP')
,('TAG021','WELLNESS')
,('TAG022','HEALTH')
,('TAG023','SELF HEALTH')
,('TAG024','SELF CARE'),
('TAG025','SELF IMPROVEMENT')
,('TAG026','SELF AWARENESS')
,('TAG027','SELF DISCIPLINE')
,('TAG028','SELF REFLECTION'),
('TAG029','SELF MANAGEMENT')
,('TAG030','SELF CONTROL')
,('TAG031','SELF ACCEPTANCE')
,('TAG032','SELF ESTEEM'),
('TAG033','CONFIDENCE')
,('TAG034','MOTIVATION')
,('TAG035','PRODUCTIVITY')
,('TAG036','RESILIENCE'),
('TAG037','COPING SKILLS')
,('TAG038','PROBLEM SOLVING')
,('TAG039','DECISION MAKING')
,('TAG040','TIME MANAGEMENT'),
('TAG041','BOUNDARIES')
,('TAG042','PERSONAL DEVELOPMENT')
,('TAG043','RELATIONSHIP')
,('TAG044','RELATIONSHIP HEALTH'),
('TAG045','RELATIONSHIP BUILDING')
,('TAG046','ACADEMIC RELATIONSHIP')
,('TAG047','WORK RELATIONSHIP'),
('TAG048','FINANCIAL RELATIONSHIP')
,('TAG049','SOCIAL SUPPORT')
,('TAG050','SOCIAL CONNECTIVITY'),
('TAG051','SOCIAL INTERACTION')
,('TAG052','COMMUNITY')
,('TAG053','COMMUNITY ENGAGEMENT')
,('TAG054','COMMUNITY INVOLVEMENT'),
('TAG055','PEER SUPPORT')
,('TAG056','PHYSICAL HEALTH')
,('TAG057','EXERCISE')
,('TAG058','ADDICTION')
,('TAG059','EATING DISORDER'),
('TAG060','BODY IMAGE')
,('TAG061','SUPPORT')
,('TAG062','HEALING')
,('TAG063','GRIEF');
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
  `booked` bit(1) DEFAULT NULL,
  `CreatedAt` datetime(6) NOT NULL,
  `current_bookings` int NOT NULL,
  `default_slot_id` varchar(255) DEFAULT NULL,
  `EndTime` time(6) NOT NULL,
  `max_capacity` int NOT NULL,
  `SlotDate` date NOT NULL,
  `SlotNumber` int NOT NULL,
  `StartTime` time(6) NOT NULL,
  `Status` enum('AVAILABLE','BOOKED','UNAVAILABLE') NOT NULL,
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
INSERT INTO `timeslots` VALUES
('TS-PSY001-2025-03-17-MORNING-03',_binary '\0','2025-03-16 17:37:32.802935',0,NULL,'10:00:00.000000',3,'2025-03-17',0,'09:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-17-MORNING-04',_binary '\0','2025-03-16 17:37:32.802935',0,NULL,'10:30:00.000000',3,'2025-03-17',0,'10:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-17-MORNING-05',_binary '\0','2025-03-16 17:37:32.802935',0,NULL,'11:00:00.000000',3,'2025-03-17',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-20-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.697686',0,NULL,'14:00:00.000000',3,'2025-03-20',0,'13:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-20-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.697686',0,NULL,'14:30:00.000000',3,'2025-03-20',0,'14:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-20-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.697686',0,NULL,'15:00:00.000000',3,'2025-03-20',0,'14:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-20-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.697686',0,NULL,'15:30:00.000000',3,'2025-03-20',0,'15:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-20-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.697686',0,NULL,'16:00:00.000000',3,'2025-03-20',0,'15:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-20-AFTERNOON-06',_binary '\0','2025-03-16 17:37:33.697686',0,NULL,'16:30:00.000000',3,'2025-03-20',0,'16:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-24-MORNING-03',_binary '\0','2025-03-16 17:37:32.823295',0,NULL,'10:00:00.000000',3,'2025-03-24',0,'09:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-24-MORNING-04',_binary '\0','2025-03-16 17:37:32.823295',0,NULL,'10:30:00.000000',3,'2025-03-24',0,'10:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-24-MORNING-05',_binary '\0','2025-03-16 17:37:32.823295',0,NULL,'11:00:00.000000',3,'2025-03-24',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-25-AFTERNOON-00',_binary '\0','2025-03-16 17:37:33.566536',0,NULL,'13:30:00.000000',3,'2025-03-25',0,'13:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-25-MORNING-05',_binary '\0','2025-03-16 17:37:33.566536',0,NULL,'11:00:00.000000',3,'2025-03-25',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-27-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.714680',0,NULL,'14:00:00.000000',3,'2025-03-27',0,'13:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-27-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.714680',0,NULL,'14:30:00.000000',3,'2025-03-27',0,'14:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-27-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.714680',0,NULL,'15:00:00.000000',3,'2025-03-27',0,'14:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-27-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.714680',0,NULL,'15:30:00.000000',3,'2025-03-27',0,'15:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-27-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.714680',0,NULL,'16:00:00.000000',3,'2025-03-27',0,'15:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-27-AFTERNOON-06',_binary '\0','2025-03-16 17:37:33.714680',0,NULL,'16:30:00.000000',3,'2025-03-27',0,'16:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-28-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.769377',0,NULL,'16:00:00.000000',3,'2025-03-28',0,'15:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-28-AFTERNOON-06',_binary '\0','2025-03-16 17:37:33.769377',0,NULL,'16:30:00.000000',3,'2025-03-28',0,'16:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-28-AFTERNOON-07',_binary '\0','2025-03-16 17:37:33.769377',0,NULL,'17:00:00.000000',3,'2025-03-28',0,'16:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-31-MORNING-03',_binary '\0','2025-03-16 17:37:32.846270',0,NULL,'10:00:00.000000',3,'2025-03-31',0,'09:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-31-MORNING-04',_binary '\0','2025-03-16 17:37:32.846270',0,NULL,'10:30:00.000000',3,'2025-03-31',0,'10:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-03-31-MORNING-05',_binary '\0','2025-03-16 17:37:32.846270',0,NULL,'11:00:00.000000',3,'2025-03-31',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-01-AFTERNOON-00',_binary '\0','2025-03-16 17:37:33.583454',0,NULL,'13:30:00.000000',3,'2025-04-01',0,'13:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-01-MORNING-05',_binary '\0','2025-03-16 17:37:33.583454',0,NULL,'11:00:00.000000',3,'2025-04-01',0,'10:30:00.000000','UNAVAILABLE','PSY001')
,('TS-PSY001-2025-04-02-MORNING-01',_binary '\0','2025-03-16 17:37:33.177969',0,NULL,'09:00:00.000000',3,'2025-04-02',0,'08:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-02-MORNING-02',_binary '\0','2025-03-16 17:37:33.177969',0,NULL,'09:30:00.000000',3,'2025-04-02',0,'09:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-02-MORNING-03',_binary '\0','2025-03-16 17:37:33.177969',0,NULL,'10:00:00.000000',3,'2025-04-02',0,'09:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-02-MORNING-04',_binary '\0','2025-03-16 17:37:33.177969',0,NULL,'10:30:00.000000',3,'2025-04-02',0,'10:00:00.000000','UNAVAILABLE','PSY001')
,('TS-PSY001-2025-04-02-MORNING-05',_binary '\0','2025-03-16 17:37:33.177969',0,NULL,'11:00:00.000000',3,'2025-04-02',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-04-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.787395',0,NULL,'16:00:00.000000',3,'2025-04-04',0,'15:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-04-AFTERNOON-06',_binary '\0','2025-03-16 17:37:33.787395',0,NULL,'16:30:00.000000',3,'2025-04-04',0,'16:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-04-AFTERNOON-07',_binary '\0','2025-03-16 17:37:33.787395',0,NULL,'17:00:00.000000',3,'2025-04-04',0,'16:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-07-MORNING-03',_binary '\0','2025-03-16 17:37:32.884733',0,NULL,'10:00:00.000000',3,'2025-04-07',0,'09:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-07-MORNING-04',_binary '\0','2025-03-16 17:37:32.884733',0,NULL,'10:30:00.000000',3,'2025-04-07',0,'10:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-07-MORNING-05',_binary '\0','2025-03-16 17:37:32.884733',0,NULL,'11:00:00.000000',3,'2025-04-07',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-09-MORNING-01',_binary '\0','2025-03-16 17:37:33.195167',0,NULL,'09:00:00.000000',3,'2025-04-09',0,'08:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-09-MORNING-02',_binary '\0','2025-03-16 17:37:33.195167',0,NULL,'09:30:00.000000',3,'2025-04-09',0,'09:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-09-MORNING-03',_binary '\0','2025-03-16 17:37:33.195167',0,NULL,'10:00:00.000000',3,'2025-04-09',0,'09:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-09-MORNING-04',_binary '\0','2025-03-16 17:37:33.195167',0,NULL,'10:30:00.000000',3,'2025-04-09',0,'10:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-09-MORNING-05',_binary '\0','2025-03-16 17:37:33.195167',0,NULL,'11:00:00.000000',3,'2025-04-09',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-10-MORNING-03',_binary '\0','2025-03-16 17:37:33.283999',0,NULL,'10:00:00.000000',3,'2025-04-10',0,'09:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-10-MORNING-04',_binary '\0','2025-03-16 17:37:33.283999',0,NULL,'10:30:00.000000',3,'2025-04-10',0,'10:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-10-MORNING-05',_binary '\0','2025-03-16 17:37:33.283999',0,NULL,'11:00:00.000000',3,'2025-04-10',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
,('TS-PSY001-2025-04-16-MORNING-02',_binary '\0','2025-03-16 17:37:33.212661',0,NULL,'09:30:00.000000',3,'2025-04-16',0,'09:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-16-MORNING-03',_binary '\0','2025-03-16 17:37:33.212661',0,NULL,'10:00:00.000000',3,'2025-04-16',0,'09:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-16-MORNING-04',_binary '\0','2025-03-16 17:37:33.212661',0,NULL,'10:30:00.000000',3,'2025-04-16',0,'10:00:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-16-MORNING-05',_binary '\0','2025-03-16 17:37:33.212661',0,NULL,'11:00:00.000000',3,'2025-04-16',0,'10:30:00.000000','UNAVAILABLE','PSY001'),
('TS-PSY001-2025-04-17-MORNING-03',_binary '\0','2025-03-16 17:37:33.304024',0,NULL,'10:00:00.000000',3,'2025-04-17',0,'09:30:00.000000','UNAVAILABLE','PSY001')
,('TS-PSY001-2025-04-17-MORNING-04',_binary '\0','2025-03-16 17:37:33.304024',0,NULL,'10:30:00.000000',3,'2025-04-17',0,'10:00:00.000000','UNAVAILABLE','PSY001')
,('TS-PSY001-2025-04-17-MORNING-05',_binary '\0','2025-03-16 17:37:33.304024',0,NULL,'11:00:00.000000',3,'2025-04-17',0,'10:30:00.000000','UNAVAILABLE','PSY001')
,('TS-PSY002-2025-03-25-AFTERNOON-01',_binary '\0','2025-03-16 17:37:32.991134',0,NULL,'14:00:00.000000',3,'2025-03-25',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-25-AFTERNOON-02',_binary '\0','2025-03-16 17:37:32.992134',0,NULL,'14:30:00.000000',3,'2025-03-25',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-25-AFTERNOON-03',_binary '\0','2025-03-16 17:37:32.992134',0,NULL,'15:00:00.000000',3,'2025-03-25',0,'14:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-25-AFTERNOON-04',_binary '\0','2025-03-16 17:37:32.992134',0,NULL,'15:30:00.000000',3,'2025-03-25',0,'15:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-25-AFTERNOON-05',_binary '\0','2025-03-16 17:37:32.992134',0,NULL,'16:00:00.000000',3,'2025-03-25',0,'15:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-31-MORNING-00',_binary '\0','2025-03-16 17:37:33.457064',0,NULL,'08:30:00.000000',3,'2025-03-31',0,'08:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-31-MORNING-01',_binary '\0','2025-03-16 17:37:33.457064',0,NULL,'09:00:00.000000',3,'2025-03-31',0,'08:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-31-MORNING-02',_binary '\0','2025-03-16 17:37:33.457064',0,NULL,'09:30:00.000000',3,'2025-03-31',0,'09:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-31-MORNING-03',_binary '\0','2025-03-16 17:37:33.457064',0,NULL,'10:00:00.000000',3,'2025-03-31',0,'09:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-03-31-MORNING-04',_binary '\0','2025-03-16 17:37:33.457064',0,NULL,'10:30:00.000000',3,'2025-03-31',0,'10:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-01-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.014426',0,NULL,'14:00:00.000000',3,'2025-04-01',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-01-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.014426',0,NULL,'14:30:00.000000',3,'2025-04-01',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-01-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.014426',0,NULL,'15:00:00.000000',3,'2025-04-01',0,'14:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-01-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.014426',0,NULL,'15:30:00.000000',3,'2025-04-01',0,'15:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-01-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.014426',0,NULL,'16:00:00.000000',3,'2025-04-01',0,'15:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-02-AFTERNOON-00',_binary '\0','2025-03-16 17:37:33.630121',0,NULL,'13:30:00.000000',3,'2025-04-02',0,'13:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-02-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.630121',0,NULL,'14:00:00.000000',3,'2025-04-02',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-02-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.630121',0,NULL,'14:30:00.000000',3,'2025-04-02',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-07-MORNING-00',_binary '\0','2025-03-16 17:37:33.479666',0,NULL,'08:30:00.000000',3,'2025-04-07',0,'08:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-07-MORNING-01',_binary '\0','2025-03-16 17:37:33.479666',0,NULL,'09:00:00.000000',3,'2025-04-07',0,'08:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-07-MORNING-02',_binary '\0','2025-03-16 17:37:33.479666',0,NULL,'09:30:00.000000',3,'2025-04-07',0,'09:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-07-MORNING-03',_binary '\0','2025-03-16 17:37:33.479666',0,NULL,'10:00:00.000000',3,'2025-04-07',0,'09:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-07-MORNING-04',_binary '\0','2025-03-16 17:37:33.479666',0,NULL,'10:30:00.000000',3,'2025-04-07',0,'10:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-08-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.037905',0,NULL,'14:00:00.000000',3,'2025-04-08',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-08-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.037905',0,NULL,'14:30:00.000000',3,'2025-04-08',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-08-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.037905',0,NULL,'15:00:00.000000',3,'2025-04-08',0,'14:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-08-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.037905',0,NULL,'15:30:00.000000',3,'2025-04-08',0,'15:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-08-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.037905',0,NULL,'16:00:00.000000',3,'2025-04-08',0,'15:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-09-AFTERNOON-00',_binary '\0','2025-03-16 17:37:33.646539',0,NULL,'13:30:00.000000',3,'2025-04-09',0,'13:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-09-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.646539',0,NULL,'14:00:00.000000',3,'2025-04-09',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-09-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.646539',0,NULL,'14:30:00.000000',3,'2025-04-09',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-11-AFTERNOON-00',_binary '\0','2025-03-16 17:37:33.367896',0,NULL,'13:30:00.000000',3,'2025-04-11',0,'13:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-11-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.367896',0,NULL,'14:00:00.000000',3,'2025-04-11',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-11-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.367896',0,NULL,'14:30:00.000000',3,'2025-04-11',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-11-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.367896',0,NULL,'15:00:00.000000',3,'2025-04-11',0,'14:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-11-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.367896',0,NULL,'15:30:00.000000',3,'2025-04-11',0,'15:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-15-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.056360',0,NULL,'14:00:00.000000',3,'2025-04-15',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-15-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.056360',0,NULL,'14:30:00.000000',3,'2025-04-15',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-15-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.056360',0,NULL,'15:00:00.000000',3,'2025-04-15',0,'14:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-15-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.056360',0,NULL,'15:30:00.000000',3,'2025-04-15',0,'15:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-15-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.056360',0,NULL,'16:00:00.000000',3,'2025-04-15',0,'15:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-18-AFTERNOON-00',_binary '\0','2025-03-16 17:37:33.388984',0,NULL,'13:30:00.000000',3,'2025-04-18',0,'13:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-18-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.388984',0,NULL,'14:00:00.000000',3,'2025-04-18',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-18-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.389988',0,NULL,'14:30:00.000000',3,'2025-04-18',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-18-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.389988',0,NULL,'15:00:00.000000',3,'2025-04-18',0,'14:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-18-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.389988',0,NULL,'15:30:00.000000',3,'2025-04-18',0,'15:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-22-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.076372',0,NULL,'14:00:00.000000',3,'2025-04-22',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-22-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.076372',0,NULL,'14:30:00.000000',3,'2025-04-22',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-22-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.076372',0,NULL,'15:00:00.000000',3,'2025-04-22',0,'14:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-22-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.076372',0,NULL,'15:30:00.000000',3,'2025-04-22',0,'15:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-22-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.076372',0,NULL,'16:00:00.000000',3,'2025-04-22',0,'15:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-29-AFTERNOON-01',_binary '\0','2025-03-16 17:37:33.098559',0,NULL,'14:00:00.000000',3,'2025-04-29',0,'13:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-29-AFTERNOON-02',_binary '\0','2025-03-16 17:37:33.098559',0,NULL,'14:30:00.000000',3,'2025-04-29',0,'14:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-29-AFTERNOON-03',_binary '\0','2025-03-16 17:37:33.098559',0,NULL,'15:00:00.000000',3,'2025-04-29',0,'14:30:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-29-AFTERNOON-04',_binary '\0','2025-03-16 17:37:33.098559',0,NULL,'15:30:00.000000',3,'2025-04-29',0,'15:00:00.000000','UNAVAILABLE','PSY002')
,('TS-PSY002-2025-04-29-AFTERNOON-05',_binary '\0','2025-03-16 17:37:33.098559',0,NULL,'16:00:00.000000',3,'2025-04-29',0,'15:30:00.000000','UNAVAILABLE','PSY002');
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
INSERT INTO `userlogs` VALUES ('LOG001','192.168.0.1','2025-03-16 17:37:35.510539','UID003')
,('LOG002','244.178.44.111','2025-03-16 17:37:35.519972','UID008')
,('LOG003','38.0.101.76','2025-03-16 17:37:35.530417','UID003')
,('LOG004','89.0.142.86','2025-03-16 17:37:35.539735','UID006');
/*!40000 ALTER TABLE `userlogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `UserID` varchar(36) NOT NULL,
  `Address` varchar(100) DEFAULT NULL,
  `CreatedAt` datetime(6) NOT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `FullName` varchar(100) NOT NULL,
  `Gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `HashedID` varchar(255) NOT NULL,
  `isActive` bit(1) DEFAULT NULL,
  `isDeleted` bit(1) DEFAULT NULL,
  `IsVerified` bit(1) NOT NULL,
  `PasswordHash` varchar(255) NOT NULL,
  `PhoneNumber` varchar(15) DEFAULT NULL,
  `Role` enum('MANAGER','PARENT','PSYCHOLOGIST','STUDENT') NOT NULL,
  `TokenExpiration` datetime(6) NOT NULL,
  `UpdatedAt` datetime(6) NOT NULL,
  `VerificationToken` varchar(255) NOT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `UKjdfr6kjrxekx1j5vrr77rp44t` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('UID001','Street 123, Ho Chi Minh City','2025-03-16 17:37:31.734724','admin@example.com','Admin Admin','MALE','JY2NyRbbjOosr7bDzQywJG7-BhQh29g-w6NQQoyr2k8',_binary '',_binary '\0',_binary '','$2a$10$2LbBiKXBgSYfPu179yWBN.HgXgv1vl92dmkNoNB9RLE.XWIMAK2xq','1111111111','MANAGER','2025-03-17 06:07:31.734724','2025-03-16 17:37:31.768804','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImFkbWluQGV4YW1wbGUuY29tIiwic3ViIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJpYXQiOjE3NDIxMjE0NTEsImV4cCI6MTc0MjIwNzg1MX0.EQpjVWIMD2Jg6fj5e89ughVUTollWLV4W8YbsOuWNT8')
,('UID002','Street 202, Ho Chi Minh City','2025-03-16 17:37:31.851484','staff@example.com','Staff Member','FEMALE','eTxws2YSw50SKtoDBra-JxMnnpBFcZdzcuTHaeeEtyo',_binary '',_binary '\0',_binary '','$2a$10$nN3kNBkdJ8bYDYP6OfBazu03O4wjHrwTpIuSniElvzSnbVxoJT026','2222222222','MANAGER','2025-03-17 06:07:31.851484','2025-03-16 17:37:31.863160','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN0YWZmQGV4YW1wbGUuY29tIiwic3ViIjoic3RhZmZAZXhhbXBsZS5jb20iLCJpYXQiOjE3NDIxMjE0NTEsImV4cCI6MTc0MjIwNzg1MX0.nYE-kBNajlKDhKkS9lli2y-HhQAZcotrBl6oeY7QSwM')
,('UID003','Street 101, Ho Chi Minh City','2025-03-16 17:37:31.949015','psychologist@example.com','Dr. Brown','MALE','WNNo6_kP9DwnDBBl4ZCBYBkwrQhDkSf2KEbTA4Vq4Y8',_binary '',_binary '\0',_binary '','$2a$10$YCw1aC4zub/gqT5bVHk2tuSLFOFtTEbZvfVomS8brptYGTYmMToU2','0912345671','PSYCHOLOGIST','2025-03-17 06:07:31.949015','2025-03-16 17:37:31.960042','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBzeWNob2xvZ2lzdEBleGFtcGxlLmNvbSIsInN1YiI6InBzeWNob2xvZ2lzdEBleGFtcGxlLmNvbSIsImlhdCI6MTc0MjEyMTQ1MSwiZXhwIjoxNzQyMjA3ODUxfQ.k0pp-3auvlTe2NZjQ59dA-wR0dCY2hNeooYzxFwfMe8')
,('UID004','Street 505, Ho Chi Minh City','2025-03-16 17:37:32.034385','psychologist2@example.com','Dr. Blue','MALE','c5b9qoaEdJcI1W9otUPN5T_rRHzvIZEq-JfWy3vgb5I',_binary '',_binary '\0',_binary '','$2a$10$FXDFgDWX/bk9YQx6bVSEjO75uyXRnIPovYjNWA38ZgGsOwvaXGT6W','0912345672','PSYCHOLOGIST','2025-03-17 06:07:32.034385','2025-03-16 17:37:32.046190','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBzeWNob2xvZ2lzdDJAZXhhbXBsZS5jb20iLCJzdWIiOiJwc3ljaG9sb2dpc3QyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzQyMTIxNDUxLCJleHAiOjE3NDIyMDc4NTF9.eh7Q_6vymbim3WfJiBOA67NnBXW9Y7SAOvxHM7KFOpo')
,('UID005','Street 789, Ho Chi Minh City','2025-03-16 17:37:32.120570','parent@example.com','Jane Smith','FEMALE','7pzSo-rW1cw7WwvDyxlZJUTRq_VlAKXZ64cSkiDqkjY',_binary '',_binary '\0',_binary '','$2a$10$BUstnwX/Ax/Mjcarjpb3s..sSAqN9M/SPgBWfsLJKAZfSbzfCD3lO','0812345671','PARENT','2025-03-17 06:07:32.120570','2025-03-16 17:37:32.132222','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBhcmVudEBleGFtcGxlLmNvbSIsInN1YiI6InBhcmVudEBleGFtcGxlLmNvbSIsImlhdCI6MTc0MjEyMTQ1MiwiZXhwIjoxNzQyMjA3ODUyfQ.Usf5a2iAFlB76_5_9L5ms95204BUzTQ4zFujPBgukDo')
,('UID006','Street 404, Ho Chi Minh City','2025-03-16 17:37:32.207943','parent2@example.com','Bob Johnson','MALE','2sKAr_ur5OSCuYygg_rYzqjFuVSrFeSLXElcFCcnz4w',_binary '',_binary '\0',_binary '','$2a$10$W4Fbk6be1GlPodiISvFwCuA0L0W34uMKTBPfhP2I1fDjCMfekDtLK','0812345672','PARENT','2025-03-17 06:07:32.207943','2025-03-16 17:37:32.218239','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InBhcmVudDJAZXhhbXBsZS5jb20iLCJzdWIiOiJwYXJlbnQyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzQyMTIxNDUyLCJleHAiOjE3NDIyMDc4NTJ9.0iwCXiK4MJYWK_ia2rkH8cLlRiIklZDDESDlQb8GAv8')
,('UID007','Street 456, Ho Chi Minh City','2025-03-16 17:37:32.297131','student@example.com','John Doe','MALE','YWuzXTHQpoQNLVrf6s3ll56pmhirX6e7YzRgAp4gcX4',_binary '',_binary '\0',_binary '','$2a$10$wrM9r.IvfsM20UbN7F8WPOHnN2lQ8Xq5vcESdvparja4y3jefihNS','0512345671','STUDENT','2025-03-17 06:07:32.297131','2025-03-16 17:37:32.308060','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN0dWRlbnRAZXhhbXBsZS5jb20iLCJzdWIiOiJzdHVkZW50QGV4YW1wbGUuY29tIiwiaWF0IjoxNzQyMTIxNDUyLCJleHAiOjE3NDIyMDc4NTJ9.SiQaj3ZHNoApcMZ7vuZsjdP-uFCaStbfqc1B-FPrOgQ')
,('UID008','Street 606, Ho Chi Minh City','2025-03-16 17:37:32.380584','student2@example.com','John Green','MALE','BHVBp_yKsfPOBrD9huPGaddwqXoZe3oojfPzMa1GcN8',_binary '',_binary '\0',_binary '','$2a$10$xuepG7LNig7/LGnpZPAM8.KiCR.46NN73MbFyXNZMh.mnI2/zIFSi','0512345672','STUDENT','2025-03-17 06:07:32.380584','2025-03-16 17:37:32.391410','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN0dWRlbnQyQGV4YW1wbGUuY29tIiwic3ViIjoic3R1ZGVudDJAZXhhbXBsZS5jb20iLCJpYXQiOjE3NDIxMjE0NTIsImV4cCI6MTc0MjIwNzg1Mn0.4gwV4pwzbX7JgdujP1HG4H36UrUAIM6wXie23D9MjXI')
,('UID009','Street 303, Ho Chi Minh City','2025-03-16 17:37:32.464626','student3@example.com','Alice Jones','FEMALE','zJ1MhL4AFbWmJmwjwLHUdMQwykKUzFlfSVj4k8bL2Lk',_binary '',_binary '\0',_binary '','$2a$10$7/WJpWHMvgV/CRymQZ.ShekBbVv7CM/XsZ8UFqnOggs8d7aXDw0Fq','0512345673','STUDENT','2025-03-17 06:07:32.464626','2025-03-16 17:37:32.474891','eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN0dWRlbnQzQGV4YW1wbGUuY29tIiwic3ViIjoic3R1ZGVudDNAZXhhbXBsZS5jb20iLCJpYXQiOjE3NDIxMjE0NTIsImV4cCI6MTc0MjIwNzg1Mn0.fgpuPXwlqHJHB7gxANLGqPTBgo1jyhCHo7WPe_BrwDM');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'swp391healthy'
--

--
-- Dumping routines for database 'swp391healthy'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-16 17:43:30