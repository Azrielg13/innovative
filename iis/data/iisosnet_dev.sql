-- MySQL dump 10.13  Distrib 5.6.13, for osx10.7 (x86_64)
--
-- Host: localhost    Database: iisosnet_main
-- ------------------------------------------------------
-- Server version	5.6.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PATIENT_ID` int(11) NOT NULL,
  `NURSE_ID` int(11) DEFAULT NULL,
  `START` datetime NOT NULL,
  `END` datetime NOT NULL,
  `CANCELLED` tinyint(1) DEFAULT NULL,
  `TIME_IN` datetime DEFAULT NULL,
  `TIME_OUT` datetime DEFAULT NULL,
  `MILEAGE` smallint(6) DEFAULT NULL,
  `ASSESSMENT_COMPLETE` tinyint(1) DEFAULT NULL,
  `assessment_approved` tinyint(1) DEFAULT NULL,
  `approver_id` int(11) DEFAULT NULL,
  `approved_date` date DEFAULT NULL,
  `pay_rate` double DEFAULT NULL,
  `MILEAGE_RATE` double DEFAULT NULL,
  `INVOICE_ID` int(11) DEFAULT NULL,
  `PAYSTUB_ID` int(11) DEFAULT NULL,
  `BILLING_RATE` double DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1,1,5,'2013-08-16 08:00:00','2013-08-16 11:00:00',0,'1970-01-01 07:45:00','1970-01-01 11:00:00',50,1,1,NULL,NULL,0,NULL,8,NULL,75),(2,1,5,'2013-08-18 22:22:15','2013-08-19 01:22:15',0,'1970-01-01 07:00:00','1970-01-01 08:00:00',35,1,1,NULL,NULL,0,NULL,10,NULL,80),(3,1,5,'2013-10-11 10:00:00','2013-10-11 13:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,1,5,'2013-09-12 10:00:00','2013-09-12 13:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,1,5,'2013-09-13 00:00:00','2013-09-13 03:00:00',0,'1970-01-01 12:00:00','1970-01-01 16:00:00',20,1,1,NULL,NULL,0,NULL,8,NULL,75),(6,1,7,'2013-10-02 10:00:00','2013-10-02 13:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,1,6,'2013-10-27 10:00:00','2013-10-27 15:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,1,10,'2013-11-16 12:30:00','2013-11-16 18:00:00',0,'1970-01-01 12:45:00','1970-01-01 17:45:00',0,1,1,NULL,NULL,0,NULL,9,NULL,100),(9,2,6,'2013-11-29 00:59:34','2013-11-29 03:59:34',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,2,6,'2013-11-29 01:02:50','2013-11-29 04:02:50',0,'1970-01-01 10:12:00','1970-01-01 16:40:00',40,1,1,NULL,NULL,NULL,NULL,11,NULL,80),(11,2,6,'2013-11-29 01:09:02','2013-11-29 04:09:02',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(12,3,6,'2013-12-07 21:19:53','2013-12-08 00:19:53',0,'1970-01-01 10:15:00','1970-01-01 18:15:00',55,1,1,NULL,NULL,NULL,NULL,7,NULL,75),(13,3,NULL,'2013-02-18 08:48:48','2013-02-18 10:48:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(14,3,NULL,'2013-02-18 08:48:48','2013-02-18 10:48:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(15,3,NULL,'2013-02-18 08:48:48','2013-02-18 10:48:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(16,3,NULL,'2013-02-18 08:48:48','2013-02-18 10:48:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(17,3,NULL,'2013-02-18 08:48:48','2013-02-18 10:48:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(18,3,NULL,'2013-02-18 08:48:48','2013-02-18 10:48:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(19,3,NULL,'2013-02-18 08:48:48','2013-02-18 10:48:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(20,2,10,'2014-03-14 00:00:00','2014-03-14 03:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(21,2,6,'2014-03-04 17:15:00','2014-03-04 18:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(22,10,24,'2014-04-05 10:00:00','2014-04-05 13:00:00',0,'1970-01-01 09:00:00','1970-01-01 17:00:00',42,1,1,NULL,NULL,NULL,NULL,13,NULL,90),(23,10,6,'2014-04-16 00:00:00','2014-04-16 03:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(24,10,NULL,'2014-04-27 00:00:00','2014-04-27 03:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(25,2,NULL,'2014-04-20 00:00:00','2014-04-20 03:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(26,10,19,'2014-04-23 00:00:00','2014-04-23 03:00:00',0,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_entry`
--

DROP TABLE IF EXISTS `assessment_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assessment_entry` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APPOINTMENT_ID` int(11) NOT NULL,
  `ASSESSMENT_ID` int(11) NOT NULL,
  `VALUE_ID` int(11) DEFAULT NULL,
  `VALUE_STR` varchar(64) DEFAULT NULL,
  `ACK` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_entry`
--

LOCK TABLES `assessment_entry` WRITE;
/*!40000 ALTER TABLE `assessment_entry` DISABLE KEYS */;
INSERT INTO `assessment_entry` VALUES (14,6,927,NULL,'98.6F',0),(15,6,930,NULL,'5\'5\"',0),(16,6,931,NULL,'160Lbs',0),(17,6,926,NULL,'180/60',0),(18,6,928,NULL,'1000RR',0),(19,6,929,NULL,'Human Resources',0),(20,22,927,NULL,'86.5F',0),(21,22,928,NULL,'40bps',0),(22,22,929,NULL,'40',0),(23,22,930,NULL,'6\'2\"',0),(24,22,931,NULL,'720lbs',0),(25,22,926,NULL,'450/90',0),(26,22,952,955,NULL,0),(27,22,956,958,NULL,0),(28,22,960,961,NULL,0),(29,22,964,NULL,'This person is crazy.',0);
/*!40000 ALTER TABLE `assessment_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `general_data`
--

DROP TABLE IF EXISTS `general_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `general_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GROUP_ID` int(11) DEFAULT NULL,
  `IN_GROUP_ID` int(11) NOT NULL,
  `NAME` varchar(64) NOT NULL,
  `RANK` decimal(10,0) DEFAULT NULL,
  `ACTIVE` tinyint(1) DEFAULT '1',
  `DESCRIPTION` varchar(256) DEFAULT NULL,
  `DATA` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1522 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_data`
--

LOCK TABLES `general_data` WRITE;
/*!40000 ALTER TABLE `general_data` DISABLE KEYS */;
INSERT INTO `general_data` VALUES (829,NULL,3,'DIANOSIS',NULL,1,'DIANOSIS',NULL),(830,829,1,'Primary Immune Deficiencies',NULL,1,'Primary Immune Deficiencies',NULL),(831,829,2,'Chronic Inflammatory Demyelinating Polyneuropathy (CIDP)',NULL,1,'Chronic Inflammatory Demyelinating Polyneuropathy (CIDP)',NULL),(832,829,3,'Myasthenia Gravis',NULL,1,'Myasthenia Gravis',NULL),(833,829,4,'Multiple Sclerosis',NULL,1,'Multiple Sclerosis',NULL),(834,829,5,'Rheumatoid Arthritis',NULL,1,'Rheumatoid Arthritis',NULL),(835,829,6,'Polymyositis',NULL,1,'Polymyositis',NULL),(836,829,7,'Dermatomyositis',NULL,1,'Dermatomyositis',NULL),(837,829,8,'Stiff Person Syndrome',NULL,1,'Stiff Person Syndrome',NULL),(838,829,9,'Pemphigus and Pemphigoid',NULL,1,'Pemphigus and Pemphigoid',NULL),(839,829,10,'Hereditary Angio Edema',NULL,1,'Hereditary Angio Edema',NULL),(840,829,11,'Infection (Localized or General)',NULL,1,'Infection (Localized or General)',NULL),(841,829,12,'Gauchers Disease',NULL,1,'Gauchers Disease',NULL),(842,829,13,'Hemophilia A',NULL,1,'Hemophilia A',NULL),(843,829,14,'Transplant Services',NULL,1,'Transplant Services',NULL),(844,829,15,'Psoriasis',NULL,1,'Psoriasis',NULL),(845,829,16,'Osteoporosis',NULL,1,'Osteoporosis',NULL),(846,NULL,5,'IV_ACCESS',NULL,1,'IV_ACCESS',NULL),(847,846,1,'Peripheral IV',NULL,1,'Peripheral IV',NULL),(848,846,2,'PICC',NULL,1,'PICC',NULL),(849,846,3,'Port',NULL,1,'Port',NULL),(850,NULL,4,'THERAPY_TYPE',NULL,1,'THERAPY_TYPE',NULL),(851,850,1,'VPRIV',NULL,1,'VPRIV',NULL),(852,850,2,'CINRYZE (HAE)',NULL,1,'CINRYZE (HAE)',NULL),(853,850,3,'Berinert (HAE)',NULL,1,'Berinert (HAE)',NULL),(854,850,4,'Recombinant (Factor 8)',NULL,1,'Recombinant (Factor 8)',NULL),(855,850,5,'Rituxan',NULL,1,'Rituxan',NULL),(856,850,6,'Kalbitor (HAE)',NULL,1,'Kalbitor (HAE)',NULL),(857,850,7,'Gammaked IVIG',NULL,1,'Gammaked IVIG',NULL),(858,850,8,'Carimune NF IVIG',NULL,1,'Carimune NF IVIG',NULL),(859,850,9,'Flebogamma IVIG',NULL,1,'Flebogamma IVIG',NULL),(860,850,10,'Gammagard IVIG',NULL,1,'Gammagard IVIG',NULL),(861,850,11,'Gammagard S/D  IVIG',NULL,1,'Gammagard S/D  IVIG',NULL),(862,850,12,'Gamunex IVIG',NULL,1,'Gamunex IVIG',NULL),(863,850,13,'Octagam',NULL,1,'Octagam','IVIG'),(864,850,14,'Privigen',NULL,1,'Privigen','IVIG'),(865,850,15,'Gammagard scIG',NULL,1,'Gammagard scIG',NULL),(866,850,16,'Hizentra scIG',NULL,1,'Hizentra scIG',NULL),(867,850,17,'Solumedrol',NULL,1,'Solumedrol',NULL),(868,850,18,'Remicade',NULL,1,'Remicade',NULL),(869,850,19,'Vancomycin ABX',NULL,1,'Vancomycin ABX',NULL),(870,850,20,'Ampicillin ABX',NULL,1,'Ampicillin ABX',NULL),(871,850,21,'Cefazolin ABX',NULL,1,'Cefazolin ABX',NULL),(872,850,22,'Ceftazidime ABX',NULL,1,'Ceftazidime ABX',NULL),(873,850,23,'Daptomycin (Cubicin) ABX',NULL,1,'Daptomycin (Cubicin) ABX',NULL),(874,850,24,'Gentamycin ABX',NULL,1,'Gentamycin ABX',NULL),(875,850,25,'Invanz ABX',NULL,1,'Invanz ABX',NULL),(876,850,26,'Levaquin ABX',NULL,1,'Levaquin ABX',NULL),(877,850,27,'Rocephin ABX',NULL,1,'Rocephin ABX',NULL),(878,850,28,'Maxipime ABX',NULL,1,'Maxipime ABX',NULL),(879,850,29,'Nafcillin ABX',NULL,1,'Nafcillin ABX',NULL),(880,850,30,'Penicillin G ABX',NULL,1,'Penicillin G ABX',NULL),(881,850,31,'Tobramycin',NULL,1,'Tobramycin',NULL),(882,850,32,'Unasyn',NULL,1,'Unasyn',NULL),(883,850,33,'Zosyn',NULL,1,'Zosyn',NULL),(884,NULL,8,'PATIENT_STATE',NULL,1,'PATIENT_STATE',NULL),(885,884,1,'pending',NULL,1,'pending',NULL),(886,884,2,'active',NULL,1,'active',NULL),(887,884,3,'denied',NULL,1,'denied',NULL),(888,884,4,'closed',NULL,1,'closed',NULL),(889,NULL,9,'LICENSE',NULL,1,'LICENSE',NULL),(924,NULL,2,'ASS_CAT',NULL,1,'ASS_CAT',NULL),(925,924,1,'Vital Signs',2,1,NULL,NULL),(926,925,1,'B/P',2,1,NULL,'{\'type\': \'TEXT\', \'copies\': false}'),(927,925,2,'Temp',3,1,NULL,'{\'type\': \'TEXT\', \'copies\': false}'),(928,925,3,'RR',4,1,NULL,'{\'type\': \'TEXT\', \'copies\': false}'),(929,925,4,'HR',5,1,NULL,'{\'type\': \'TEXT\', \'copies\': false}'),(930,925,5,'Height',6,1,NULL,'{\'type\': \'TEXT\'}'),(931,925,6,'Weight',7,1,NULL,'{\'type\': \'TEXT\'}'),(932,925,7,'SOC/Recertification/FU',8,1,NULL,'{\'type\': \'TEXT\'}'),(933,925,8,'Diagnosis',9,1,NULL,'{\'type\': \'TEXT\'}'),(934,925,9,'Physician',10,1,NULL,'{\'type\': \'TEXT\'}'),(935,925,10,'MD Phone Number',11,1,NULL,'{\'type\': \'TEXT\'}'),(936,925,11,'Last Visit',12,1,NULL,'{\'type\': \'TEXT\', \'copies\': false}'),(937,925,12,'Order Change',13,1,NULL,'{\'type\': \'RADIO\'}'),(938,937,1,'Yes',2,1,NULL,NULL),(939,937,2,'No',3,1,NULL,NULL),(940,925,13,'Allergies',14,1,NULL,'{\'type\': \'TEXT\'}'),(941,924,2,'Behavioral Status',3,1,NULL,NULL),(942,941,14,'Behavioral Status',15,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(943,942,3,'NPA',4,1,NULL,NULL),(944,942,4,'Alert',5,1,NULL,NULL),(945,942,5,'Anxious',6,1,NULL,NULL),(946,942,6,'Lethargic',7,1,NULL,NULL),(947,942,7,'Depressed',8,1,NULL,NULL),(948,942,8,'Restless',9,1,NULL,NULL),(949,942,9,'Flat Effect',10,1,NULL,NULL),(950,942,10,'Confused',11,1,NULL,NULL),(951,942,11,'Unresponsive',12,1,NULL,NULL),(952,941,15,'Memory',16,1,NULL,'{\'type\': \'RADIO\'}'),(953,952,12,'Good',13,1,NULL,NULL),(954,952,13,'Fair',14,1,NULL,NULL),(955,952,14,'Poor',15,1,NULL,NULL),(956,941,16,'Orientation',17,1,NULL,'{\'type\': \'RADIO\'}'),(957,956,15,'Person',16,1,NULL,NULL),(958,956,16,'Place',17,1,NULL,NULL),(959,956,17,'Time',18,1,NULL,NULL),(960,941,17,'Speech',18,1,NULL,'{\'type\': \'RADIO\'}'),(961,960,18,'Slurred',19,1,NULL,NULL),(962,960,19,'Garbled',20,1,NULL,NULL),(963,960,20,'Aphasic',21,1,NULL,NULL),(964,941,18,'Comments',19,1,NULL,'{\'type\': \'TEXT\'}'),(965,924,3,'Senses',4,1,NULL,NULL),(966,965,1,'Hearing',2,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(967,966,1,'NPA',2,1,NULL,NULL),(968,966,2,'Diminished',3,1,NULL,NULL),(969,966,3,'R',4,1,'Right',NULL),(970,966,4,'L',5,1,'Left',NULL),(971,966,5,'Assistive Device',6,1,NULL,NULL),(972,965,2,'Vision',3,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(973,972,6,'NPA',7,1,NULL,NULL),(974,972,7,'Glasses/Contacts',8,1,NULL,NULL),(975,972,8,'Legally Blind',9,1,NULL,NULL),(976,972,9,'Blurred/Double Vision',10,1,NULL,NULL),(977,965,3,'Primary Language',4,1,NULL,'{\'type\': \'TEXT\'}'),(978,965,4,'Comments',5,1,NULL,'{\'type\': \'TEXT\'}'),(979,924,4,'Cardiovascular',5,1,NULL,NULL),(980,979,1,'Cardiovascular',2,1,NULL,'{\'type\': \'RADIO\'}'),(981,980,10,'NPA',11,1,NULL,NULL),(982,980,11,'Palpitations',12,1,NULL,NULL),(983,980,12,'Angina',13,1,NULL,NULL),(984,980,13,'Other',14,1,NULL,NULL),(985,979,2,'Peripheral Pulse',3,1,NULL,'{\'type\': \'RADIO\'}'),(986,985,14,'Present',15,1,NULL,NULL),(987,985,15,'Absent',16,1,NULL,NULL),(988,979,3,'Edema (Location/Amount)',4,1,NULL,'{\'type\': \'TEXT\'}'),(989,979,4,'Comments',5,1,NULL,'{\'type\': \'TEXT\'}'),(990,924,5,'Respiratory',6,1,NULL,NULL),(991,990,1,'Shortness of Breath',2,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(992,991,16,'NPA',17,1,NULL,NULL),(993,991,17,'None',18,1,NULL,NULL),(994,991,18,'Exertion',19,1,NULL,NULL),(995,991,19,'At Rest',20,1,NULL,NULL),(996,991,20,'Orthopnea',21,1,NULL,NULL),(997,990,2,'Cough',3,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(998,997,21,'Non-Productive',22,1,NULL,NULL),(999,997,22,'Productive',23,1,NULL,NULL),(1000,990,3,'Sputum Character',4,1,NULL,'{\'type\': \'TEXT\'}'),(1001,990,4,'Lung Sounds',5,1,NULL,'{\'type\': \'RADIO\'}'),(1002,1001,23,'Clear',24,1,NULL,NULL),(1003,1001,24,'Diminished',25,1,NULL,NULL),(1004,1001,25,'Wheezes',26,1,NULL,NULL),(1005,1001,26,'Crackies',27,1,NULL,NULL),(1006,990,5,'Oxygen Therapy',6,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(1007,1006,27,'None',28,1,NULL,NULL),(1008,1006,28,'NC',29,1,NULL,NULL),(1009,1006,29,'L/PM',30,1,NULL,NULL),(1010,1006,30,'Cont',31,1,NULL,NULL),(1011,1006,31,'PM',32,1,NULL,NULL),(1012,1006,32,'Trach',33,1,NULL,NULL),(1013,1006,33,'w/Cuff',34,1,NULL,NULL),(1014,1006,34,'TX',35,1,NULL,NULL),(1015,990,6,'Comments',7,1,NULL,'{\'type\': \'TEXT\'}'),(1016,924,6,'GI',7,1,NULL,NULL),(1017,1016,1,'GI',2,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(1018,1017,35,'NPA',36,1,NULL,NULL),(1019,1017,36,'Nausea',37,1,NULL,NULL),(1020,1017,37,'Vomiting',38,1,NULL,NULL),(1021,1017,38,'Anorexia',39,1,NULL,NULL),(1022,1017,39,'Dysphagia',40,1,NULL,NULL),(1023,1017,40,'Bleeding',41,1,NULL,NULL),(1024,1017,41,'Pain',42,1,NULL,NULL),(1025,1017,42,'Ascites',43,1,NULL,NULL),(1026,1017,43,'Constipation',44,1,NULL,NULL),(1027,1017,44,'Diarrhea',45,1,NULL,NULL),(1028,1017,45,'Distention',46,1,NULL,NULL),(1029,1016,2,'Ostomy Type',3,1,NULL,'{\'type\': \'TEXT\'}'),(1030,1016,3,'Bowel Sounds X4',4,1,NULL,'{\'type\': \'TEXT\'}'),(1031,1016,4,'Feeding Tube Type',5,1,NULL,'{\'type\': \'TEXT\'}'),(1032,1016,5,'Feeding Tube Site',6,1,NULL,'{\'type\': \'TEXT\'}'),(1033,1016,6,'Comments',7,1,NULL,'{\'type\': \'TEXT\'}'),(1034,924,7,'Endocrine',8,1,NULL,NULL),(1035,1034,1,'Endocrine',2,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(1036,1035,46,'NPA',47,1,NULL,NULL),(1037,1035,47,'Diabetic',48,1,NULL,NULL),(1038,1035,48,'Thyroid Disease',49,1,NULL,NULL),(1039,1035,49,'Other',50,1,NULL,NULL),(1040,1034,2,'Indicate Treatment',3,1,NULL,'{\'type\': \'TEXT\'}'),(1041,1034,3,'Finger Stick Glocose Precribed diet',4,1,NULL,'{\'type\': \'TEXT\'}'),(1042,1034,4,'Comments',5,1,NULL,'{\'type\': \'TEXT\'}'),(1043,924,8,'GU',9,1,NULL,NULL),(1044,1043,1,'GU',2,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(1045,1044,50,'NPA',51,1,NULL,NULL),(1046,1044,51,'Frequency',52,1,NULL,NULL),(1047,1044,52,'Urgency',53,1,NULL,NULL),(1048,1044,53,'Burning',54,1,NULL,NULL),(1049,1044,54,'Pain',55,1,NULL,NULL),(1050,1044,55,'Retention',56,1,NULL,NULL),(1051,1044,56,'Incontinence',57,1,NULL,NULL),(1052,1044,57,'Vaginal Bleeding',58,1,NULL,NULL),(1053,1043,2,'Catheter',3,1,NULL,'{\'type\': \'RADIO\'}'),(1054,1053,58,'None',59,1,NULL,NULL),(1055,1053,59,'External',60,1,NULL,NULL),(1056,1053,60,'Indwelling',61,1,NULL,NULL),(1057,1053,61,'S/P',62,1,NULL,NULL),(1058,1043,3,'Urine',4,1,NULL,'{\'type\': \'RADIO\'}'),(1059,1058,62,'Clear',63,1,NULL,NULL),(1060,1058,63,'Cloudy',64,1,NULL,NULL),(1061,1058,64,'Odor',65,1,NULL,NULL),(1062,1058,65,'Hematuria',66,1,NULL,NULL),(1063,1043,4,'Urine Color',5,1,NULL,'{\'type\': \'TEXT\'}'),(1064,1043,5,'Comments',6,1,NULL,'{\'type\': \'TEXT\'}'),(1065,924,9,'Neuromuscular',10,1,NULL,NULL),(1066,1065,1,'Neuromusular',2,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(1067,1066,66,'NPA',67,1,NULL,NULL),(1068,1065,2,'ROM Loss',3,1,NULL,'{\'type\': \'RADIO\'}'),(1069,1068,67,'None',68,1,NULL,NULL),(1070,1068,68,'R',69,1,NULL,NULL),(1071,1068,69,'L',70,1,NULL,NULL),(1072,1065,3,'Site',4,1,NULL,'{\'type\': \'TEXT\'}'),(1073,1065,4,'Mobility Problems',5,1,NULL,'{\'type\': \'ACK_TEXT\'}'),(1074,1065,5,'Amputation',6,1,NULL,'{\'type\': \'ACK_TEXT\'}'),(1075,1065,6,'Contractures',7,1,NULL,'{\'type\': \'ACK_TEXT\'}'),(1076,1065,7,'Sensation/Numbness/Tingling Location(s)',8,1,NULL,'{\'type\': \'TEXT\'}'),(1077,1065,8,'Describe',9,1,NULL,'{\'type\': \'TEXT\'}'),(1078,1065,9,'?',10,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(1079,1078,70,'Headache',71,1,NULL,NULL),(1080,1078,71,'Tremors',72,1,NULL,NULL),(1081,1078,72,'Seizures',73,1,NULL,NULL),(1082,1078,73,'Vertigo',74,1,NULL,NULL),(1083,1078,74,'Ataxia',75,1,NULL,NULL),(1084,1078,75,'Episodes of Unconsciousness',76,1,NULL,NULL),(1085,1065,10,'Comments',11,1,NULL,'{\'type\': \'TEXT\'}'),(1086,924,10,'Integumetary',11,1,NULL,NULL),(1087,1086,1,'Integumetary',2,1,NULL,'{\'type\': \'MULTI_CHECK\'}'),(1088,1087,76,'NPA',77,1,NULL,NULL),(1089,1087,77,'Rash',78,1,NULL,NULL),(1090,1087,78,'Jaundice',79,1,NULL,NULL),(1091,1087,79,'Poor Turgor',80,1,NULL,NULL),(1092,1087,80,'Pruritis',81,1,NULL,NULL),(1093,1087,81,'Brusing',82,1,NULL,NULL),(1094,1087,82,'Lesions',83,1,NULL,NULL),(1095,1087,83,'Staples',84,1,NULL,NULL),(1096,1087,84,'Sutures',85,1,NULL,NULL),(1097,1087,85,'Incision',86,1,NULL,NULL),(1098,1086,2,'Unusual Color',3,1,NULL,'{\'type\': \'ACK_TEXT\'}'),(1099,1086,3,'Wound Location(s)',4,1,NULL,'{\'type\': \'TEXT\'}'),(1100,1086,4,'Description',5,1,NULL,'{\'type\': \'TEXT\'}'),(1101,1086,5,'Dressing Change (Indicate type/drain)',6,1,NULL,'{\'type\': \'ACK_TEXT\'}'),(1102,1086,6,'Amount',7,1,NULL,'{\'type\': \'TEXT\'}'),(1103,1086,7,'Comments',8,1,NULL,'{\'type\': \'TEXT\'}'),(1104,924,11,'Pain',12,1,NULL,NULL),(1105,1104,1,'Pain',2,1,NULL,'{\'type\': \'ACK_TEXT\'}'),(1106,1104,2,'Pain Rating',3,1,NULL,'{\'type\': \'TEXT\'}'),(1107,1104,3,'Pain Rating at Rest',4,1,NULL,'{\'type\': \'TEXT\'}'),(1108,1104,4,'Pain Rating w/Activity',5,1,NULL,'{\'type\': \'TEXT\'}'),(1109,1104,5,'Describe Pain Quality',6,1,NULL,'{\'type\': \'TEXT\'}'),(1110,1104,6,'Other Relief Measures',7,1,NULL,'{\'type\': \'TEXT\'}'),(1111,1104,7,'Comments',8,1,NULL,'{\'type\': \'TEXT\'}'),(1112,924,12,'Teaching',13,1,NULL,NULL),(1113,1112,1,'Teaching',2,1,NULL,'{\'type\': \'RADIO\'}'),(1114,1113,86,'None',87,1,NULL,NULL),(1115,1113,87,'Patient',88,1,NULL,NULL),(1116,1113,88,'Primary Care Giver',89,1,NULL,NULL),(1117,1112,2,'Subject',3,1,NULL,'{\'type\': \'TEXTAREA\'}'),(1118,1112,3,'Response/Verification',4,1,NULL,'{\'type\': \'RADIO\'}'),(1119,1118,89,'None',90,1,NULL,NULL),(1120,1118,90,'Verbal',91,1,NULL,NULL),(1121,1118,91,'Demo',92,1,NULL,NULL),(1122,1112,4,'Level of Understanding',5,1,NULL,'{\'type\': \'RADIO\'}'),(1123,1122,92,'None',93,1,NULL,NULL),(1124,1122,93,'Partial',94,1,NULL,NULL),(1125,1122,94,'Complete',95,1,NULL,NULL),(1126,NULL,10,'NURSE_STATUS',NULL,1,'NURSE_STATUS',NULL),(1127,NULL,1,'UserType',NULL,1,'UserType',NULL),(1128,1127,1,'UserType_Admin',NULL,1,'UserType_Admin',NULL),(1129,1127,2,'UserType_Standard',NULL,1,'UserType_Standard',NULL),(1130,1126,1,'NURSE_STATUS_PENDING',NULL,1,'NURSE_STATUS_PENDING',NULL),(1131,1126,2,'NURSE_STATUS_ACTIVE',NULL,1,'NURSE_STATUS_ACTIVE',NULL),(1132,1126,3,'NURSE_STATUS_SUSPENDED',NULL,1,'NURSE_STATUS_SUSPENDED',NULL),(1133,1126,4,'NURSE_STATUS_INACTIVE',NULL,1,'NURSE_STATUS_INACTIVE',NULL),(1471,889,1,'1. GENERAL REQUIREMENTS',1,1,'1. GENERAL REQUIREMENTS',NULL),(1472,1471,1,'Resume',1,1,'Resume','{\'expires\': false}'),(1473,1471,2,'Pre-Screening Form',2,1,'Pre-Screening Form','{\'expires\': false}'),(1474,1471,3,'Independent Contractor Application',3,1,'Independent Contractor Application','{\'expires\': false}'),(1475,1471,4,'W9 Form',4,1,'W9 Form','{\'expires\': false}'),(1476,1471,5,'Authorization to Release',5,1,'Authorization to Release','{\'expires\': false}'),(1477,1471,6,'Reference Forms',6,1,'Reference Forms','{\'expires\': false}'),(1478,1471,7,'Background Check Consent',7,1,'Background Check Consent','{\'expires\': false}'),(1479,1471,8,'Background Check Report',8,1,'Background Check Report','{\'expires\': false}'),(1480,1471,9,'OIG/EPLS',9,1,'OIG/EPLS','{\'expires\': false}'),(1481,1471,10,'Sex Offender Search',10,1,'Sex Offender Search','{\'expires\': false}'),(1482,1471,11,'Independent Contract Agreement (ICA)',11,1,'Independent Contract Agreement (ICA)','{\'expires\': false}'),(1483,1471,12,'Confidentiality Agreement',12,1,'Confidentiality Agreement','{\'expires\': false}'),(1484,1471,13,'Personal Contact Form',13,1,'Personal Contact Form','{\'expires\': false}'),(1485,889,2,'2. LICENSURE/CREDENTIALS',2,1,'2. LICENSURE/CREDENTIALS',NULL),(1486,1485,1,'Driver License',1,1,'Driver License',NULL),(1487,1485,2,'RN License',2,1,'RN License',NULL),(1488,1485,3,'License Verification',3,1,'License Verification',NULL),(1489,1485,4,'BLS/CPR Card',4,1,'BLS/CPR Card',NULL),(1490,1485,5,'Malpractice/Professional Liability Insurance',5,1,'Malpractice/Professional Liability Insurance',NULL),(1491,1485,6,'Auto Insurance',6,1,'Auto Insurance',NULL),(1492,1485,7,'Other',7,1,'Other',NULL),(1493,889,3,'3. COMPETENCIES/ANNUAL REVIEW',3,1,'3. COMPETENCIES/ANNUAL REVIEW',NULL),(1494,1493,1,'IV Exam',1,1,'IV Exam','{\'expires\': false}'),(1495,1493,2,'Skills Checklist',2,1,'Skills Checklist',NULL),(1496,1493,3,'Specialty Test',3,1,'Specialty Test',NULL),(1497,1493,4,'IC/OSHA Attestation Form & Manual',4,1,'IC/OSHA Attestation Form & Manual',NULL),(1498,1493,5,'Handbook Acknowledgement',5,1,'Handbook Acknowledgement',NULL),(1499,1493,6,'Orientation Checklist',6,1,'Orientation Checklist','{\'expires\': false}'),(1500,889,4,'4. PERFORMANCE EVALUATION',4,1,'4. PERFORMANCE EVALUATION',NULL),(1501,1500,1,'Annual Evaluation',1,1,'Annual Evaluation',NULL),(1502,1500,2,'Client Evaluation',2,1,'Client Evaluation',NULL),(1503,889,5,'5. Other',5,1,'5. Other',NULL),(1504,1503,1,'Cleared Physical / Medical Release',1,1,'Cleared Physical / Medical Release',NULL),(1505,1503,2,'CNRI Certified',2,1,'CNRI Certified',NULL),(1506,1503,3,'Chemo Certified',3,1,'Chemo Certified',NULL),(1507,1503,4,'Compliance Manuals Sent',4,1,'Compliance Manuals Sent','{\'expires\': false}'),(1508,1503,5,'Consumer Affair Report',5,1,'Consumer Affair Report',NULL),(1509,1503,6,'Flu Vaccine Waiver',6,1,'Flu Vaccine Waiver',NULL),(1510,1503,7,'Hepatitis B Vaccine or Titer or Waiver',7,1,'Hepatitis B Vaccine or Titer or Waiver',NULL),(1511,1503,8,'Immunization History',8,1,'Immunization History',NULL),(1512,1503,9,'MMR Vaccien or Titer or Signed Decl.',9,1,'MMR Vaccien or Titer or Signed Decl.',NULL),(1513,1503,10,'PICC Line Certified',10,1,'PICC Line Certified',NULL),(1514,1503,11,'Performance Evaluation',11,1,'Performance Evaluation',NULL),(1515,1503,12,'RN Intro Packet Sent',12,1,'RN Intro Packet Sent','{\'expires\': false}'),(1516,1503,13,'TB Questionare',13,1,'TB Questionare',NULL),(1517,1503,14,'TB Test/Check X-ray',14,1,'TB Test/Check X-ray',NULL),(1518,1503,15,'Varicella Vaccine or Titer or Signed Decl.',15,1,'Varicella Vaccine or Titer or Signed Decl.',NULL),(1519,NULL,11,'PAYMENT_STATUS',NULL,1,'PAYMENT_STATUS',NULL),(1520,1519,2,'PAYMENT_STATUS_PAID',NULL,1,'PAYMENT_STATUS_PAID',NULL),(1521,1519,1,'PAYMENT_STATUS_UNPAID',NULL,1,'PAYMENT_STATUS_UNPAID',NULL);
/*!40000 ALTER TABLE `general_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `VENDOR_ID` int(11) NOT NULL,
  `NAME` varchar(64) NOT NULL,
  `GENERATION_TIME` datetime DEFAULT NULL,
  `DATA` blob,
  `status_id` int(11) DEFAULT NULL,
  `comment` varchar(1024) DEFAULT NULL,
  `TOTAL_DUE` double DEFAULT NULL,
  `TOTAL_PAID` double DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,1,'Test Invoice',NULL,'%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 854/Filter/FlateDecode>>stream\nxúçVMO‹0ºÁWX*8‡µù8‹(•¿‘¬™\'. UH⁄$Ä¯˜ı«sÿ‰ŸQµ“*…õ?OÊYaÑìcNëÖTˇ/—ﬂàì˜à˝ªΩàÑ$qŒ©$Y!Hñ™ãæäÓT˝\"bÍÊ]!gÇ)âS®õ\Zô~®Îß\"_ñãÇä$Lè’Ca qAQ1·4UÀIp›D\nöÊ¡≤Ò‘µjpQMYLyxOiñ–$ºtñHöâ`Yõ©6-ùºòüj0V\0A¿‘50÷@å{`Õ]XÉW˙p&Ø@ú—¢≠^\"úŸ+\"Œà3}2≈‹c;ÇX\0¨È+÷r0é. ∆pŸÿ^Ã¿j7 ™ïyåÄÕa\0òÏ¸ÔÅÚS>_∑\Z!3F∂;u•Í±:†R}∑˘ŒâPœ£√´∂Ìﬁ ±~´»U˚¯:‘]KÓ∫ÊuT√—ˆwtT#gí∆b_ÜêyÎÍá \0dÏ´è&É=mö≥≤Ø»e˜¢˛™≤ü…ÈS’>|h:Û√ª)_™≤≠ÜëÏ-∑ƒ~+Gk_õ∆[Íó0d∫∑Œ©«Ÿ¥[ªÉ ∞™≠=mQ¿‚,—)¡‘_ıPèD˜hÿÄÛJ®W™&K‹ﬁX™≠˚®¬‰3Ô∆≤áÆ\r¬«çyÆÊsœ€ù}ª¶Óe ú˙˙›vcŸêÀﬁ∆…¢|¸DzÙ<VnëÉÕso\0ÊSêå˘]Ö?UOﬁÙ0BÄû3•	…˝°xÓ“ı§©Ü·˛·Q	ﬂ[8gzËÒ¬◊uSïOˆ]»◊x*π>Ç|r÷\r∂i@˙D2!ÙπxÜ\rê˚8N]“%ïÛ°;ﬂÌjµ|ŸV$ûÁ]rãù¯SÊëñm„±\'ÙX«?–Ãu˘ÒXWÕŒ3HÀMnâùpâG	∏q¿πòMÄ©@9ex }\Z\nƒ?P«,cûë@\Zü·F\ZÃIÃ√ç%\\¿ëÛd—ß|Øt0O6íò“ç%R6âÃæ\'\"ÅúèaûÓL ™À∑¸eà3á|ûSá§ZÙ<ì©¥‡y˛<‡¿πÏ\r†gﬂ.ÇvµE÷r.mæ»ï…¯<G<)CAÚl(xPÜì¥ßbæ¥‘Â˘V}f˝Sår\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140301171944-08\'00\')/CreationDate(D:20140301171944-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001112 00000 n \n0000001205 00000 n \n0000000015 00000 n \n0000001293 00000 n \n0000000936 00000 n \n0000001356 00000 n \n0000001401 00000 n \ntrailer\n<</Root 6 0 R/ID [<6fa708d2c827451ae2409259608768f4><91fe48cefc0715a3fb8447c8486122a9>]/Info 7 0 R/Size 8>>\nstartxref\n1523\n%%EOF\n',NULL,NULL,NULL,NULL),(2,1,'Test Invoice',NULL,'%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 862/Filter/FlateDecode>>stream\nxúçVMOù@›Û+&©]»õÜw÷Zu°iı•+7ƒáJÉ–j¸˜ùè;¯‡ŒêÊ%/¿=ÁÃù√π(a‰òJd!’ˇ√KÙ7b‰=¢Dˇn/\".â»Y,IVpí•Í¢Ø¢;Uøà®∫y◊ÄÑs¬(èãîàÍ¶∆x¶Íz∆bû/ÀEÛ$LÍ!7eQƒ®ò∞8UÀIp›DÚ8ÕÉe…≈‘µjpQM©àYxOiñƒIxÈ,ëq∆Éem¶⁄¥t~∞b2|f®¡XS◊d¿X1Ó!Ä5w`\r^È√ôºqF[à∂zâpfØà8√W ŒÙ»spå3èÌ0F`∞¶ØHXÀ-¿8∫(√√ecwx}0;\0´›(´VÊu0:,\06á`≤ø Ou¯|›jÑÃ(ŸÓ‘ï™u@•˙nÛùÆû?FáWm€Ωïc˝Vë´ˆÒu®ªñ‹uÕÎ®.Ü£ÌÔË®FFd2|_ÜqêyÎÍá \0dÙ´è&É=mö≥≤Ø»e˜¢˛™≤ü…ÈS’>|h:ı√ª)_™≤≠ÜëÏ-∑ƒ~+G£bCŸÜSñxA ÂÑ∞Ñ\n |†Èﬁ:®g”ÆÌN~(„™v¥69¥Eã—DßS’C=›´aŒ+°^≠ö,q{c©∂Ó£rìOÃºÀ∫6W∞\\ÕÊû∑;˚ñM›ÀîyÏÎw€çeC.{+ãÚÒ^ËÙlXπE6œΩ\0òOARÍw˛T=y”/¿zŒî&&˜á¸πH◊ì¶\ZÜ˚#ÑßD%}o·úÍ·«_◊MU>Ÿw\r _„©d˙lÚ…Y7ÿ¶È…8◊ÁG‡6@Ïcë∫§ÀXŒáÔ|∑´’Úe[1œªd;ÒßÃ#∆74”C)<°«:.¯ÅfÆÀè«∫jvû)@ZnpKÙÑI<\nH¿ç»ù¿l\"∞\0L»cä—ß°@¸u‹RÍ	§Òn§Aùƒ<‹X¬IPO∂} ˜JÛd#â)›X\"•ì»<‡{\"h¡˘ÊÈŒ8¢∫|õ¡_Ü8√pà1¿Á9≈pH™Eœ3â¡êJûÁœúÀﬁ\0zˆÌ\"hW[d\r/Á“f·ã\\y‡ê,ÄœsÑ·¡ì2$œÜÇe8I{*ÊãK]ûo’Á÷?}à\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140301172248-08\'00\')/CreationDate(D:20140301172248-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001120 00000 n \n0000001213 00000 n \n0000000015 00000 n \n0000001301 00000 n \n0000000944 00000 n \n0000001364 00000 n \n0000001409 00000 n \ntrailer\n<</Root 6 0 R/ID [<5ca3a1086a5ca9f5189532b051a101b6><3be8c2a24100f138c19597c09e9098ee>]/Info 7 0 R/Size 8>>\nstartxref\n1531\n%%EOF\n',NULL,NULL,NULL,NULL),(3,1,'Test Invoice','2014-03-01 17:25:26','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 862/Filter/FlateDecode>>stream\nxúçVMOù@›Û+&©]»õÜw÷Zu°iı•+7ƒáJÉ–j¸˜ùè;¯‡ŒêÊ%/¿=ÁÃù√π(a‰òJd!’ˇ√KÙ7b‰=¢Dˇn/\".â»Y,IVpí•Í¢Ø¢;Uøà®∫y◊ÄÑs¬(èãîàÍ¶∆x¶Íz∆bû/ÀEÛ$LÍ!7eQƒ®ò∞8UÀIp›DÚ8ÕÉe…≈‘µjpQM©àYxOiñƒIxÈ,ëq∆Éem¶⁄¥t~∞b2|f®¡XS◊d¿X1Ó!Ä5w`\r^È√ôºqF[à∂zâpfØà8√W ŒÙ»spå3èÌ0F`∞¶ØHXÀ-¿8∫(√√ecwx}0;\0´›(´VÊu0:,\06á`≤ø Ou¯|›jÑÃ(ŸÓ‘ï™u@•˙nÛùÆû?FáWm€Ωïc˝Vë´ˆÒu®ªñ‹uÕÎ®.Ü£ÌÔË®FFd2|_ÜqêyÎÍá \0dÙ´è&É=mö≥≤Ø»e˜¢˛™≤ü…ÈS’>|h:ı√ª)_™≤≠ÜëÏ-∑ƒ~+G£bCŸÜSñxA ÂÑ∞Ñ\n |†Èﬁ:®g”ÆÌN~(„™v¥69¥Eã—DßS’C=›´aŒ+°^≠ö,q{c©∂Ó£rìOÃºÀ∫6W∞\\ÕÊû∑;˚ñM›ÀîyÏÎw€çeC.{+ãÚÒ^ËÙlXπE6œΩ\0òOARÍw˛T=y”/¿zŒî&&˜á¸πH◊ì¶\ZÜ˚#ÑßD%}o·úÍ·«_◊MU>Ÿw\r _„©d˙lÚ…Y7ÿ¶È…8◊ÁG‡6@Ïcë∫§ÀXŒáÔ|∑´’Úe[1œªd;ÒßÃ#∆74”C)<°«:.¯ÅfÆÀè«∫jvû)@ZnpKÙÑI<\nH¿ç»ù¿l\"∞\0L»cä—ß°@¸u‹RÍ	§Òn§Aùƒ<‹X¬IPO∂} ˜JÛd#â)›X\"•ì»<‡{\"h¡˘ÊÈŒ8¢∫|õ¡_Ü8√pà1¿Á9≈pH™Eœ3â¡êJûÁœúÀﬁ\0zˆÌ\"hW[d\r/Á“f·ã\\y‡ê,ÄœsÑ·¡ì2$œÜÇe8I{*ÊãK]ûo’Á÷?}à\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140301172526-08\'00\')/CreationDate(D:20140301172526-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001120 00000 n \n0000001213 00000 n \n0000000015 00000 n \n0000001301 00000 n \n0000000944 00000 n \n0000001364 00000 n \n0000001409 00000 n \ntrailer\n<</Root 6 0 R/ID [<d780ecce83733c5c4e1745769e6d97b3><2ba1e520327ff439ba5d4309f226f101>]/Info 7 0 R/Size 8>>\nstartxref\n1531\n%%EOF\n',NULL,NULL,NULL,NULL),(4,3,'First Invoice','2014-03-01 20:54:38','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 966/Filter/FlateDecode>>stream\nxúùWMO€@ΩÁW¨Tp`≥^ØÕ≠¥@ëZ‘B‘óLI‚÷ÈﬂÔ~Ãö¨g◊-	9û˜ﬁéüﬂLÄNé9aD’ ¸æ{ö˝ûqÚg∆à˝πæò	Ed)àÆ©K“5≥Sªò1™ !gÇö¢uW„B€õ∂Æ9’∏\\◊Ty∫47Ö+Àö¢b¡iï-VúŸs%hYeÀJ»°k”‡®Z2Iy˛ôJ]–\"¥.’\"[∂föáV¡^õÎÑ°„Lùícƒπá\0ﬁ‹	Ä7x¢è`Ú$Ì!÷Í1\"ò=!üÄ”\' 6‚ï¸áÌ1i˙ÑÑ∑‹&œóù›˘Û¡Ï<\0¨vÄ	£Û`s\0&Á∞E¿¡Sóa,Fe∞7GsKÿ£¢∑6SÙ∆fŒ\r∂f ¡‘E\\\rñf»¡–L9ÿâ ˇ≥ôøô\r~∫∞U•Y‹õ´í*i∂|i?Õœ9Ê˛√Ïr≥iwÀÌj◊êÀÕ√søj7‰¶]?oÕE¥¯9;™ìëZQ)ˆe∏\0ô]ª∫k@é¿^∞ˆ’9ÏÈ™ÖÉﬁ_ﬁXK`kΩZ>5\'‰|’ı[≤wƒòp¯qπ58&Áåœ„EÔN/òd<\Z>{◊Ãm=<©Ô˛´1´ŸlΩ5ÌQ¿‚¨∞©¿‘Ô´~µ%∂W«\\RBó6ÔX‚˙ S}=E.âòy≥]v–µC§∏íWfR0˜lsÔﬂ¨´\'ô™¢©~Ìvπ&ü:%èJÒQ€AK<∞qãÃ;\'\0∞îÇb,Ì:(¸j:≤≥/¿	:f*ì€CÒÿı§Ì»∫È˚€#Ñgƒ§{Ô‡äŸ1«Y≠õÂˇÆîjºT‹ÓÅ,ü|h{ﬂ4 S\"Zª-2/¿±2bÀ2$]QOﬂ¢Ω\'üÕß›¥°\"ˆêx$¡™9/ÌH D‰±Nà=“˘º‹5ùŸLkÛ2ªƒ ©0∏#}R(<H Ã‡¸Ñ1<X\0F	H*ƒF	ò-ƒÅ4^¢ç4Xêà£ç%Bº±ìâd#˙êÓâ‚\\#â!€XBíjïà˜ûà⁄´\"Æ˘à=ÒzŒe.‚XÁÕGRŸàs\ZE	‰#^&#ér/(√	GÙ7$iº>·X‚U	GÙ◊\'IL$\\≤A$N¯ ¢át\'wª-ƒ⁄›gW«PH.@„l∆PH¶G∆åÅê@å≥6f˛æ–…]:z»ê3Ã(PÒ9!N:\nŒ\n±híö˝äœ%dÙ\0Ÿo˜ï[ÄÉBÌvËŸ¬¸Oû˘∫ˇ\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140301205438-08\'00\')/CreationDate(D:20140301205438-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001224 00000 n \n0000001317 00000 n \n0000000015 00000 n \n0000001405 00000 n \n0000001048 00000 n \n0000001468 00000 n \n0000001513 00000 n \ntrailer\n<</Root 6 0 R/ID [<5783da7f7e690bdc9bb4cadf5ef287de><3344bfba7a239d8513bbbc9d7ce0382a>]/Info 7 0 R/Size 8>>\nstartxref\n1635\n%%EOF\n',1521,'have not heard from them',NULL,NULL),(5,1,'Test Invoice','2014-03-02 08:02:52','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 863/Filter/FlateDecode>>stream\nxúçVMO‹0ºÁWX*8‡µù8‹(•¿‘¬™\'. UH⁄$Ä¯˜ı«sÿ‰ŸQµ“*…õ?OÊYaÑìcNëÖTˇ/—ﬂàì˜à˝ªΩàÑ$qŒ©$Y!Hñ™ãæäÓT˝\"bÍÊ]!gÇ)âS®õ\Zô~®Îß\"_ñãÇä$Lè’Ca qAQ1·4UÀIp›D\nöÊ¡≤Ò‘µjpQMYLyxOiñ–$ºtñHöâ`Yõ©6-ùºòüj0V\0A¿‘50÷@å{`Õ]XÉW˙p&Ø@ú—¢≠^\"úŸ+\"Œà3}2≈‹c;ÇX\0¨È+÷r0é. ∆pŸÿ^Ã¿j7 ™ïyåÄÕa\0òÏ¸ÔÅÚS>_∑\Z!3F∂;u•Í±:†R}∑˘ŒâPœ£√´∂Ìﬁ ±~´»U˚¯:‘]KÓ∫ÊuT√—ˆwtT#gí∆b_ÜêyÎÍá \0dÏ´è&É=mö≥≤Ø»e˜¢˛™≤ü…ÈS’>|h:Û√ª)_™≤≠ÜëÏ-∑ƒ~+GcÒÜâç`<ÒÇ@‡À	·	SâÚÅ¶{Î†zúMª∂;˘°å´⁄—⁄‰–,ŒùL˝UıHtØÜ\r8ØÑzµj\"∞ƒÌç•⁄∫è*L>1Ûn,{Ë⁄ |‹òÁjé0˜º›Ÿ∑lÍ^¶Ã©Øﬂm7ñ\rπÏm¨, «OD°G–≥aÂ9ÿ<˜F\0`>…òﬂuP¯Sı‰Mø\0#Ë9Söò‹äÁ~ ]OöjÓèûïÙΩÖs¶á/|]7U˘dﬂ5Ä|çßíÎ≥!»\'g›`õ§O$BüÅ`ÿ\0Y∞è„‘%]R9æÛ›ÆVÀómE‚yﬁ%∑ÿâ?eâp±aô ÿz¨„ÇhÊ∫¸x¨´fÁô§Â&∑ƒN∏ƒ£Ä‹8`Å‹	Ã&¿T Åú2<à>\r‚®„ñ1œH çœp#\rÊ$Ê·∆.‡HÇy≤çËSæW:ò\'ILÈ∆)õDÊﬂë@Œ«0Ow&’Â€˛2ƒÜCå>œ)ÜCR-zûIÜTZ<p‡\\ˆ–≥oAª⁄\"kx9ó6_‰ ád|û#ûî° y6<(√I⁄S1_\\ÍÚ|´>∑˛é4\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140302080252-08\'00\')/CreationDate(D:20140302080252-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001121 00000 n \n0000001214 00000 n \n0000000015 00000 n \n0000001302 00000 n \n0000000945 00000 n \n0000001365 00000 n \n0000001410 00000 n \ntrailer\n<</Root 6 0 R/ID [<f5c337556d96f9a70d8e189ed34afc84><14bcbe8364db977a6dfd518d0286f945>]/Info 7 0 R/Size 8>>\nstartxref\n1532\n%%EOF\n',1521,NULL,600,NULL),(6,1,'Test Invoice','2014-03-02 08:05:50','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 863/Filter/FlateDecode>>stream\nxúçVMO‹0ºÁWX*8‡µù8‹(•¿‘¬™\'. UH⁄$Ä¯˜ı«sÿ‰ŸQµ“*…õ?OÊYaÑìcNëÖTˇ/—ﬂàì˜à˝ªΩàÑ$qŒ©$Y!Hñ™ãæäÓT˝\"bÍÊ]!gÇ)âS®õ\Zô~®Îß\"_ñãÇä$Lè’Ca qAQ1·4UÀIp›D\nöÊ¡≤Ò‘µjpQMYLyxOiñ–$ºtñHöâ`Yõ©6-ùºòüj0V\0A¿‘50÷@å{`Õ]XÉW˙p&Ø@ú—¢≠^\"úŸ+\"Œà3}2≈‹c;ÇX\0¨È+÷r0é. ∆pŸÿ^Ã¿j7 ™ïyåÄÕa\0òÏ¸ÔÅÚS>_∑\Z!3F∂;u•Í±:†R}∑˘ŒâPœ£√´∂Ìﬁ ±~´»U˚¯:‘]KÓ∫ÊuT√—ˆwtT#gí∆b_ÜêyÎÍá \0dÏ´è&É=mö≥≤Ø»e˜¢˛™≤ü…ÈS’>|h:Û√ª)_™≤≠ÜëÏ-∑ƒ~+GcÒÜâç`<ÒÇ@‡À	·	SâÚÅ¶{Î†zúMª∂;˘°å´⁄—⁄‰–,ŒùL˝UıHtØÜ\r8ØÑzµj\"∞ƒÌç•⁄∫è*L>1Ûn,{Ë⁄ |‹òÁjé0˜º›Ÿ∑lÍ^¶Ã©Øﬂm7ñ\rπÏm¨, «OD°G–≥aÂ9ÿ<˜F\0`>…òﬂuP¯Sı‰Mø\0#Ë9Söò‹äÁ~ ]OöjÓèûïÙΩÖs¶á/|]7U˘dﬂ5Ä|çßíÎ≥!»\'g›`õ§O$BüÅ`ÿ\0Y∞è„‘%]R9æÛ›ÆVÀómE‚yﬁ%∑ÿâ?eâp±aô ÿz¨„ÇhÊ∫¸x¨´fÁô§Â&∑ƒN∏ƒ£Ä‹8`Å‹	Ã&¿T Åú2<à>\r‚®„ñ1œH çœp#\rÊ$Ê·∆.‡HÇy≤çËSæW:ò\'ILÈ∆)õDÊﬂë@Œ«0Ow&’Â€˛2ƒÜCå>œ)ÜCR-zûIÜTZ<p‡\\ˆ–≥oAª⁄\"kx9ó6_‰ ád|û#ûî° y6<(√I⁄S1_\\ÍÚ|´>∑˛é4\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140302080639-08\'00\')/CreationDate(D:20140302080639-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001121 00000 n \n0000001214 00000 n \n0000000015 00000 n \n0000001302 00000 n \n0000000945 00000 n \n0000001365 00000 n \n0000001410 00000 n \ntrailer\n<</Root 6 0 R/ID [<89e5496b0eca6ca36ad65ce4f9d1d6db><7c3295394c3df4a6b15ee592fa0e2e0d>]/Info 7 0 R/Size 8>>\nstartxref\n1532\n%%EOF\n',1521,NULL,600,NULL),(7,1,'Test Invoice','2014-03-02 08:30:47','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 863/Filter/FlateDecode>>stream\nxúçVMO‹0ºÁWX*8‡µù8‹(•¿‘¬™\'. UH⁄$Ä¯˜ı«sÿ‰ŸQµ“*…õ?OÊYaÑìcNëÖTˇ/—ﬂàì˜à˝ªΩàÑ$qŒ©$Y!Hñ™ãæäÓT˝\"bÍÊ]!gÇ)âS®õ\Zô~®Îß\"_ñãÇä$Lè’Ca qAQ1·4UÀIp›D\nöÊ¡≤Ò‘µjpQMYLyxOiñ–$ºtñHöâ`Yõ©6-ùºòüj0V\0A¿‘50÷@å{`Õ]XÉW˙p&Ø@ú—¢≠^\"úŸ+\"Œà3}2≈‹c;ÇX\0¨È+÷r0é. ∆pŸÿ^Ã¿j7 ™ïyåÄÕa\0òÏ¸ÔÅÚS>_∑\Z!3F∂;u•Í±:†R}∑˘ŒâPœ£√´∂Ìﬁ ±~´»U˚¯:‘]KÓ∫ÊuT√—ˆwtT#gí∆b_ÜêyÎÍá \0dÏ´è&É=mö≥≤Ø»e˜¢˛™≤ü…ÈS’>|h:Û√ª)_™≤≠ÜëÏ-∑ƒ~+GcÒÜâç`<ÒÇ@‡À	·	SâÚÅ¶{Î†zúMª∂;˘°å´⁄—⁄‰–,ŒùL˝UıHtØÜ\r8ØÑzµj\"∞ƒÌç•⁄∫è*L>1Ûn,{Ë⁄ |‹òÁjé0˜º›Ÿ∑lÍ^¶Ã©Øﬂm7ñ\rπÏm¨, «OD°G–≥aÂ9ÿ<˜F\0`>…òﬂuP¯Sı‰Mø\0#Ë9Söò‹äÁ~ ]OöjÓèûïÙΩÖs¶á/|]7U˘dﬂ5Ä|çßíÎ≥!»\'g›`õ§O$BüÅ`ÿ\0Y∞è„‘%]R9æÛ›ÆVÀómE‚yﬁ%∑ÿâ?eâp±aô ÿz¨„ÇhÊ∫¸x¨´fÁô§Â&∑ƒN∏ƒ£Ä‹8`Å‹	Ã&¿T Åú2<à>\r‚®„ñ1œH çœp#\rÊ$Ê·∆.‡HÇy≤çËSæW:ò\'ILÈ∆)õDÊﬂë@Œ«0Ow&’Â€˛2ƒÜCå>œ)ÜCR-zûIÜTZ<p‡\\ˆ–≥oAª⁄\"kx9ó6_‰ ád|û#ûî° y6<(√I⁄S1_\\ÍÚ|´>∑˛é4\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140302083048-08\'00\')/CreationDate(D:20140302083048-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001121 00000 n \n0000001214 00000 n \n0000000015 00000 n \n0000001302 00000 n \n0000000945 00000 n \n0000001365 00000 n \n0000001410 00000 n \ntrailer\n<</Root 6 0 R/ID [<0554dc2cccea0df7d547d938c059fb5d><095dda3157001257beece284c882fb20>]/Info 7 0 R/Size 8>>\nstartxref\n1532\n%%EOF\n',1521,NULL,600,NULL),(8,3,'Payment','2014-03-02 08:32:09','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 966/Filter/FlateDecode>>stream\nxúùWMO€@ΩÁW¨Tp»f?º^õ[i)E¢àB‘óLI‚÷	©˙Ôª≥ÜıÏ∫\rBBéÁΩ∑„Á7`Ñì)\'å®ZôﬂwOì_N~O±?◊g°à,—µ uI∫frcjgFïAôb!·LPSî°Ój\\h{”÷5ß¢\ZñÎöä\"OóÊ¶peYST,8≠≤≈ä”\"{n°-´lY	Ÿwm\ZTK&)œ?S©Z‰è÷Ö¢ZdÀ÷LÛ–*¯¡ksù0‘aº\0ÇÄ©c2`¨É8˜¿õ;èÙLÅ£=ƒZ=D≥GDÇ·#ê`˙ƒFºíˇ∞›\"FMëñ[¿à·˘≤≥;>òùÄ’0bt^\0lŒ¿‰<\0∂¯#x ‡2å≈†ˆÊË`n	ª`PÙ÷fäﬁÿÃπ¡÷L9òZ¬¢à´¡“9ö);Q˘6ÛW≥¡OÊ∂™4#Û{sUR%Õñ/ÌßŸ\'NÑπˇ09<_Ø€›bª‹5‰|˝ºY∂kr”Æû∑Êbs4ˇ1ô’…H≠®Øe∏\0ô]ªºk@é¿^∞ˆ’9Ï…≤ÖÉﬁüﬂXK`kΩ\\<5«‰jÒÁ©YoS–√èã≠A09cb&/í ËÓ›1·3YIÅ˙œﬁ/s[˜œË˚æ26A”˛Ò<\nXú6ò˙mπYnâÌ’±óî–•M:ñ∏æÙT_OQÖÀ fﬁltÌ)Æ‰ïôÃ=]ﬂ˚wÍÍI¶™h™ﬂyª]¨»ÁŒá»£R¸B‘vƒl‹\"≥«Œ	\0,•†Kª\n?õéÏÏpBÄéô ≈‰ˆP<v“vd’l6∑Gœà…ı´É+f¸eπjﬂ˝ªP™ÒRqª≤|Ú°›¯¶ô—Bÿ=ëyé\rê{*ÀêtEU<wÛˆû\\òŸ#<NªiCEÏ>ÒHÇU3^⁄ëîâ»cù{§s±ÿ5ùŸI+Û2ªƒ ©0∏#}\\(<H Ã‡¸ò1<X\0F	H*ƒÔG	ò’\n-ƒÅ4^¢ç4Xêà£ç%Bº±ìâd#züÓë‚\\#â>€XBíjïà˜+¥Ω\"Æ˘Ä=Òz∆e.‚XÁÕGRŸàs\ZE	‰#^&#ér/(√	GÙ7$iÏüp,±W¬}ˇÑ#âëÑK÷ãƒ	ÔEtüÓ‰Óﬂƒ¡ÆED±vwáŸ’1í–8õ1íÈëqc $–„¨\rÄôø/tró2‰Ã3T|NàìáÇ3ÄBl\0\Zá$Üfø‚s	<@ˆ€˝@Â`ØPªz:7ˇ\r¸j∏Í\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140302083209-08\'00\')/CreationDate(D:20140302083209-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001224 00000 n \n0000001317 00000 n \n0000000015 00000 n \n0000001405 00000 n \n0000001048 00000 n \n0000001468 00000 n \n0000001513 00000 n \ntrailer\n<</Root 6 0 R/ID [<e2b28809d0c4e317f39cde3f7f3fd5b9><ec7885f37d0c7e98b128b59c5b909e39>]/Info 7 0 R/Size 8>>\nstartxref\n1635\n%%EOF\n',1521,NULL,543.75,NULL),(9,3,'Test getTotalDue','2014-03-02 08:40:35','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 853/Filter/FlateDecode>>stream\nxúçVMO‹0ΩÁWåTÙ@÷v‚|p+ÖR™Qv’óHµl⁄MX˛~˝1ÎåUH(…º˜<~~c-\'»Z™ˇ˜/…ﬂÑ√[¬@ˇ›^&BBVÒTBY(ı∞kí•™_&LΩºi@.p&“∫Ä¨¿∫©qQÍè∫^ÚTT”r]ß\"è”3ıQòrVß§òÛ¥äUÀyt›\\ä¥®¢e)≤±k’‡§Z∞,ÂÒ=eûÊÒ•À\\¶•àñµôj”“˘¡Î—pœPÉ±Ç¶Œ…†±b‹#\0kÓ¿\Z<”á3y‚å∂mı·ÃûqÜœ@úÈ3ê1ÊËò‡€qå¿`Müë∞ñ[ÄqtR6Ü«À∆Ó¯˙hvÄVªQV≠¯u4:.Ä6«hrø Ou˘ú≠4BñVÍI’3uA˙mÒÖÉPﬂì„´Ì∂€Øávﬂ¿’ˆÒµoª-,ªÕÎ†˙è´ﬂ…	RçLV 4á2\\†ÃækÔC@ê!∞w¨æöˆ¨Ìp°OWKÕ`lm†◊ÎóÊVM?¿S3¨∫aΩ9mBú„Ûı††,[0±åÁA∂˘·xŒTêB†Ò›\Zß>ó„fÌnî_Õv∞Ó8¥E!ã≥\\áÑRµ};ÄÓ’∞îP\'™ÅJ‹^[™≠á®¬ƒí2ó√zá]DàõÒJçÂ^lÏ·özê)´4‘Ø95¯∫≥i≤®?µûº¿Üï[p¥xﬁÑÖ$ca◊Q·O≥ÉΩ>\0#Ñhü)MLÓé≈ÛÆánõ¶ÔÔ><ÉÖ+¶gû.¸£›4Î\'{÷\n5^HÆØÑ(>wΩm\Zë!ëR}mD¿∞2aüdÖK∫L•?Ä´Óæ´!Óß]rãŸc‚âÁ^ËëÃëß:.ˆDÁ[˚7›[ÉAÚÛOt‹–vƒi.È7T†tﬁ,Pú\" SFGÅ–«q ¸#Æí»X`\Zà»{Æâ»(·ÁöJ∏l	à5°è—ûÈ¿5ëÉM%‰ª~∂D$“¢£—˚—.°∫põôü&∏§pÃ0¬˝†R8F’¢˝PR0∆“Ç˝\0¿ë+9ò¿¿æ]Ìjì¨—Â\\⁄,|í´\0ìÖp?GΩ$cA\nl(zG∆ìt†b~c©«ãï˙Åıu§Î5\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140302084035-08\'00\')/CreationDate(D:20140302084035-08\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001111 00000 n \n0000001204 00000 n \n0000000015 00000 n \n0000001292 00000 n \n0000000935 00000 n \n0000001355 00000 n \n0000001400 00000 n \ntrailer\n<</Root 6 0 R/ID [<bec79b37b0f33d6854022a4df59a0214><299c890159a72033fc8cf6ad47613dff>]/Info 7 0 R/Size 8>>\nstartxref\n1522\n%%EOF\n',1520,NULL,500,600),(10,3,'2014-04-09','2014-04-09 18:28:17','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 845/Filter/FlateDecode>>stream\nxúçV]O€0}œØ∞4‡°©Ìƒ˘‡mlå!1¥—jOºDFß“lI)◊ˆu®sÌh¢BiÓ9«◊«ÁZÂL∞Ö`ú©Z¡ˇáó‰o\"ÿ[¬ô˛ªªJ§bY%R≈ Z≤≤ÄáæMVPøJ8|y”Ä\\J&∏LÎÇe÷MM»Rø‘ıR§≤öñÎ:ïyúû¡Ki Yùíb.“*ZÑñÛË∫πíiQEÀJfc◊–‡§Z,Ò=eûÊÒ•À\\••åñµô∞iÂ¸ıh∏g®¡XASÁd–X1ÓÄ5w`\rûÈ√ô<qF[à∂zäpfœà8√g ŒÙ»stLäÄÌ8F`∞¶œHXÀ-¿8:)√„ecw|}4;@´›(C+~çé†Õq\0ö¸ÔÖÚ.üãµF®í≥ı#<A=É™–ﬂñ_ì˛)9ΩﬁÌ∫C≥ﬂZvΩ{z6›é≠∫ÌÎÜ≥ıÔdÅT#ìï*Õ‰±åê(sË6≠! »¯;V_M{±Èp°è◊+Õ‡lm†∑ÕK{≠ä|¡·Sá–ßüõ=Äxæ‰ıRCÉ l√99œ√J„wkº.«m⁄÷øÉSÌno}qhãBñ‡πé•˛‹õ=”Ω\Z6‚Çpñ0T‚Ó÷Rm=Dï&êîπ⁄7=vm!n&* Ω‹=⁄c5ı SUi®ﬂu∑o∂ÏkosdQ!~.k=sÅ\rÉ[Ïd˘‹ÑÖÁa◊Q·O€≥É>\0#Ñhü©LLÓOÂs?∞Æg€vÓœû3àˆ—¬◊”N˛∂Ÿ∂Õ/{÷\n5^(°/É(ü}Í€4\"C\"•î˙¬àÄa#d¬^dÖK∫Jï?zÎÓë›¿¯1·ß]	ãŸc‚âØñ¢“#ô\"Ou\\ÏâŒMsh{∏ñ∂pò}`àî⁄QyŒ9ù\"‡f!∞%\'‡ç¿ë \"Ât\Z}ú¬?Å,a˛<ç˜dçQ¬O6ïpÈ¶>ÇMËc∏g:cM$∆hœ·á˚HC!+:ÉüÌR™K∑˙iÑK\n«#‹è)ÖcP-⁄è$c(-ÿè_\0πìÉ˘Ï€%–Æ6â\Z]ŒÖÕ¬\'±\n¿1X˜cD·—[2ñ£¿Ü¢ód4HG\"Ê◊<^Æ·ß’?*Á≥\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140409182817-07\'00\')/CreationDate(D:20140409182817-07\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001103 00000 n \n0000001196 00000 n \n0000000015 00000 n \n0000001284 00000 n \n0000000927 00000 n \n0000001347 00000 n \n0000001392 00000 n \ntrailer\n<</Root 6 0 R/ID [<388163c30ce586f67f12119901d31c87><657ed35f327a21b116b4a5bac2869d8c>]/Info 7 0 R/Size 8>>\nstartxref\n1514\n%%EOF\n',1520,NULL,80,80),(11,7,'TEst Invoice','2014-04-09 18:34:55','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 861/Filter/FlateDecode>>stream\nxúçñOoú0≈Ô|\nKÕ!9Ñµç\rÀõn”TM‘fW9ÂÇ≤§°›@d£|˚˙œò,åç™H0øy6è7÷R¬»9#î»\\™ˇœ—ﬂàë◊à˝w{qIí%ã%…rN≤T]¥e¥QıÀà™õW\rŒ	£<ŒSí§P75∆3˝P◊3ÛÂ¥úÁ1·ˆD=‰¶ú‰1*\n/ÉEµe\\WHßÀ`YÚdÿµ⁄‡§ö“$f·wJ3ã“ôêq∆Éem¶ziÈ¸`˘`¯»P√XÑÄ©s2`¨Aå{∞ÊŒ\0÷‡ô}8ìgg¥E¥’S¬ô=#‚üAúÈ3»spå3èÌ0F`X”g$¨Â0éN ∆pŸÿ^Ã`µeµïqåÄÕa\0Lˆˇ{†¸Páœ«≠&dF…vßÆT=QT™Ôü·Í˘ctzU◊Õ°Ë´CIÆÍ«óÆjj≤iˆ/Ω∫ËŒ∂ø¢sh52I&„ÑÀ02á¶z(M@¶Åæ≥˙h2Ï≈›Ü\\m˘\\¥øuı†π!oäÁrE∂ÎÆ\'GL˘”OEØ0*4_p ÑÅ+¬4˜A√ΩıL=ŒÜ˜¥{ˇÆ¨*Îﬁ\Z„hKA£BÁ∑ﬁU]’ΩW”\rúWB}L5X‚ˆ∆∂⁄∫ØïõD‚ŒM_¥∞kC¯z∂TìÉ{◊ıŒ~WS˜v eÏ€Ô∂Èã=˘“⁄ Y ◊/xÆáŒÛ¬ -r≤xjç\0`>I©ﬂuP¯S∂‰†?Äz‹)MLÓO˘S€ë¶%˚≤ÎÓœOâ ˆ—¬K™«/|]ÌÀ‚ß˝÷\0˘6ûJ¶OÉ`?πh:ªi }\"Á˙ƒ|\0”\r»§˚<I]“e,«√˜≠Í˚}Iæ6Ou˝F¯8ÚíY|êbèt[p3óâ\'˜X«eÈ¨wª™$◊≈€cUÓwûA@ZnñËäq<\rH¿MHWÇ‚°¿0H@©x&P˚0®ˇD%äRœT ç˜|#çAbúo,·2é$®\'ﬁ®}à¯Ã∆·FC¿±Ñ‰É»8„G\"⁄Ç#“ç”ùq‘ÍÚmf\Z‚„c¿«9≈8$’“„LbRi·q˛<p‡hˆ–Ûﬁ.ÇvµI÷r.mü‰ ÉC≤\0Á„¡√2$œœ píéTÃœ,uπﬁ™ﬂXˇ\0+Ïô\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140409183455-07\'00\')/CreationDate(D:20140409183455-07\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001119 00000 n \n0000001212 00000 n \n0000000015 00000 n \n0000001300 00000 n \n0000000943 00000 n \n0000001363 00000 n \n0000001408 00000 n \ntrailer\n<</Root 6 0 R/ID [<f74b81785551251b4ec1cce8864fc49f><2d5e54e2ac3e0a4265fa0ca050c601d8>]/Info 7 0 R/Size 8>>\nstartxref\n1530\n%%EOF\n',1521,NULL,520,NULL),(12,2,'Get money','2014-04-09 19:40:08','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 850/Filter/FlateDecode>>stream\nxúçV]O€0}œØ∞4‡Å‘v‚8·m0∆ê6¥—j“$^\"\ZFß6ŸöP¥?\\á:◊é&$î‰ûs|}|ÆUJ9gÑQ	ıˇqó¸IyM(—˜7	$+Y*à¨8ëÖzÿ7…R’o™^^5 Áú0 ”™ YuSc\\Íè∫.Y Àiπ™Rû«Èô˙»M9´RTÃYZFã™Â<∫n.xZî—≤‡ŸÿµjpR-hñ≤¯û\nôßy|iôãTÚhYõ©6-ú¨\Z\r˜5+Ä `Íúk ∆=∞ÊŒ\0¨¡3}8ìg Œh—VOŒÏg¯ƒô>céq∞¡L\0÷Ù	kπG\'ecxºlÏéØf«`µe’ä_£„`s\0&á\0ˇ{°|SóœÂJ#Ñ§dµVO™û©™–oãèåpı˝)9Ωm€ÓPõCCn€ßó~”µdŸm_ı–ü≠~%Á@52ôi∆èeôC∑yl@Ü@ﬂ∞˙j2ÿÀMg“h\Z¿UvWÔör”d◊µÕﬂ¯ÙC=(Õ¥Zp Ú z{wAXNsZÖ@„ªuK}ñ„m◊_ïIM;XK⁄¢Ä≈hÆìÅ©ﬂ7˝f ∫W√\\PB£J?ñ∏ø≥T[Qπ…\"f.áz]Dàõ±RÕÊ^∑k{¢¶dä2\rıªÍÜzK>ÌmÑ,*ƒœy•«-∞aÂ9Y<Ôç\0¿B\nÇ“∞Î†ªŸìÉ>\0#hü)LLN˘Ûæ\'›ûlõæ8CxJT™è.©tºóÕ∂©⁄≥P®ÒB0}D˘‰™Îm”ÄâHŒı]9\0√»Ñ}û.È\"˛‰]5\r˘‹Ω*)?ÌÇY‰»è$ÙHäq$\'ë«:.ˆHÁG∑≠€uMﬁØÎ]$Â∆\0wT]PäÁ\0	∏Y@L:o∞\0å(Säß—«â@¸ìä¶–Å?H„-ŸHcîìç%\\∫±ëÅ`#˙Óô¸X#â1⁄XBÚQƒO˜ëà\0Zt8z?‹í#™ã∑ô˙iÜ%ÜCäÓÁ√!©ÌgÉ!ïÏÁ/\0é\\ ¡\0ˆÌ\"hWõd\r/Á“f·ì\\‡ê,Ä˚9¬Ë5R`C—[2û§#Û”J=^Ø‘Ô™ÁÖË@\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140409194008-07\'00\')/CreationDate(D:20140409194008-07\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001108 00000 n \n0000001201 00000 n \n0000000015 00000 n \n0000001289 00000 n \n0000000932 00000 n \n0000001352 00000 n \n0000001397 00000 n \ntrailer\n<</Root 6 0 R/ID [<8a85e789db0d7b2fadd26bc35077c646><a4d604d8d73db5d8de79c7ff54e9713d>]/Info 7 0 R/Size 8>>\nstartxref\n1519\n%%EOF\n',1520,'Sent us a check',720,720),(13,2,'Test Invoice','2014-04-15 20:34:24','%PDF-1.4\n%‚„œ”\n3 0 obj\n<</Length 848/Filter/FlateDecode>>stream\nxúçñOO‹0≈Ô˘ñ xm\'énÖRä‘\"\n´Jï∏Dl([-õvñØ_ˇáu∆é*$îd~ÛÏºº±ñNN9aD÷Rˇ|…˛fúºeåòøª´LHíWúJ¢jAT©/vmvØÎW”7o(Ñ ú	Zó$/°nk\\(Û–‘ß¢öñÎöä\"›ûÎá¬ñÛö¢b¡iï,Í-…u)hY%ÀR‰„Æı\'’íÂîßﬂ©T-“K´BR%íec¶~iÈ˝‡ıhx`®eú\0B¿‘90÷\"÷=8sg\0gÃ>º…3à7⁄!∆Í)·ÕûÒÜœ ﬁÙdå98&xƒv+0úÈ3ŒrXG\'ekx∫lÌNØfß∞⁄è≤ﬁJX£”`s\Z\0ìc¿ˇ(ﬂı·sæ4ÑTå,W˙J◊s}@ïÊnÒô°ü?e«◊€m∑oÜıæ%◊€ß◊~›m…}∑yÙE≤¸ùùB´ï…ï§π8î·dˆ›˙±µ\r\0ŸˆŒö£…≤ÁÎŒ-dh·jã›4/ÌY∂˝@‘ß¸Òßf–+\\.„EÅgÑ¨‡2ç˜Œ0˝Xç/È6~´}j∑És≈”éÇ.Œ\n‹˙c›ØbˆjªÅãJË/©\0K‹›∏VWèµ\nG‹y?4;ÿµ%bΩ9ØÙÿ‡ﬁÀÌ }T[èv ä∆ˆªÏÜfCæÏ\\äÎ/Dm&.Ú¬⁄-r¥xﬁY¿b\ní±∏Î†ß›ëΩ˘\0VË∞S⁄ò<ãÁ]O∫Ÿ¥}ˇpÇxFt∞Æòôuº∑ı¶m~πo\rPl„•‰Ê(HˆìãÆwõ2&¢Ñ0«E‚ÿn@&›ßyÈì.©áÔ¢m…◊ÓMKÖió‹ëc˜òx$°GíΩè‰$ÚX««È¸Ï6Õv’êè´Ê•èå\0íÚcÄwTü1ÜÁ\0	¯Y@\\yÅ`∞\0å®(√”Ä⁄«â@˝G5£∞Épê∆{≤ë∆(&K¯tc##¡FÌc∏gv∆\ZIå—∆Jå\"a∫D$¥%á£√≠jıÒ∂S?Õ∞¬8§0ßá§::Ã$Ü!ïÛÅár4Äë˜ˆt´M≤ÜóÛis¯$Wíxò#å\'è…Tê\"/î<%”I:P±øÆÙÂÂRˇ¥˙Œ∞È_\nendstream\nendobj\n5 0 obj\n<</Parent 4 0 R/Contents 3 0 R/Type/Page/Resources<</ProcSet [/PDF /Text /ImageB /ImageC /ImageI]/Font<</F1 1 0 R/F2 2 0 R>>>>/MediaBox[0 0 595 842]/Rotate 90>>\nendobj\n1 0 obj\n<</BaseFont/Helvetica-Bold/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n2 0 obj\n<</BaseFont/Helvetica/Type/Font/Encoding/WinAnsiEncoding/Subtype/Type1>>\nendobj\n4 0 obj\n<</ITXT(2.1.7)/Type/Pages/Count 1/Kids[5 0 R]>>\nendobj\n6 0 obj\n<</Type/Catalog/Pages 4 0 R>>\nendobj\n7 0 obj\n<</Producer(iText 2.1.7 by 1T3XT)/ModDate(D:20140415203424-07\'00\')/CreationDate(D:20140415203424-07\'00\')>>\nendobj\nxref\n0 8\n0000000000 65535 f \n0000001106 00000 n \n0000001199 00000 n \n0000000015 00000 n \n0000001287 00000 n \n0000000930 00000 n \n0000001350 00000 n \n0000001395 00000 n \ntrailer\n<</Root 6 0 R/ID [<55c4c1563b40591c83560c1e77ff9181><29f8bacbf91f52a8c7cf5afe754a472b>]/Info 7 0 R/Size 8>>\nstartxref\n1517\n%%EOF\n',1521,NULL,720,NULL);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `license`
--

DROP TABLE IF EXISTS `license`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `license` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NURSE_ID` int(11) NOT NULL,
  `LIC_TYPE_ID` int(11) NOT NULL,
  `NUMBER` varchar(32) DEFAULT NULL,
  `VALID_DATE` date DEFAULT NULL,
  `EXPIRATION_DATE` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `license`
--

LOCK TABLES `license` WRITE;
/*!40000 ALTER TABLE `license` DISABLE KEYS */;
INSERT INTO `license` VALUES (1,19,1472,'In','2014-04-15',NULL),(2,19,1486,'B08240502','2010-08-10','2014-08-10'),(3,19,1487,NULL,NULL,NULL),(4,19,1473,NULL,'2014-04-14',NULL),(5,19,1474,NULL,'2014-04-15',NULL),(6,19,1475,NULL,'2014-04-07',NULL);
/*!40000 ALTER TABLE `license` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nurse`
--

DROP TABLE IF EXISTS `nurse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nurse` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REG_DATE` date DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `REFERRAL_SOURCE` varchar(100) DEFAULT NULL,
  `pay_rate` double DEFAULT NULL,
  `pay_rate_2hr_soc` double DEFAULT NULL,
  `pay_rate_2hr_roc` double DEFAULT NULL,
  `mileage_rate` double DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nurse`
--

LOCK TABLES `nurse` WRITE;
/*!40000 ALTER TABLE `nurse` DISABLE KEYS */;
INSERT INTO `nurse` VALUES (5,NULL,'6683 Paso Fino St, Corona, CA 92880, USA',33.966819,-117.56195300000002,'909-736-0132','Eddie Mayfield',50,42,42.5,0.565,1131),(6,NULL,'1536 Valley Drive, Norco, CA',33.90876799999999,-117.523886,'909-800-0300','none',52,53,54,0.565,NULL),(7,NULL,'1536 Valley Drive, Norco, CA',33.90876799999999,-117.523886,NULL,'Eddie Mayfield',16.75,19,18,0.565,NULL),(9,NULL,'4740 Green River Road, Corona, California, Estados Unidos',33.8770894,-117.66077050000001,NULL,NULL,100,90,70,0.565,1131),(10,NULL,'6789 Fig Street, Riverside, California, Estados Unidos',33.9500317,-117.40298899999999,'951-123-4567',NULL,80,85,85,0.565,309),(11,NULL,'123 Boardway St, New York, NY',NULL,NULL,NULL,NULL,40,NULL,NULL,0.565,NULL),(12,NULL,'123 Boardway St, New York, NY',NULL,NULL,NULL,NULL,40,NULL,NULL,0.565,NULL),(13,NULL,'123 Boardway St, New York, NY',NULL,NULL,NULL,NULL,40,NULL,NULL,0.565,NULL),(14,NULL,'123 Boardway St, New York, NY',NULL,NULL,NULL,NULL,40,NULL,NULL,0.565,1131),(15,NULL,'123 Boardway St, New York, NY',NULL,NULL,NULL,NULL,40,NULL,NULL,0.565,NULL),(16,NULL,'123 Boardway St, New York, NY',NULL,NULL,NULL,NULL,40,NULL,NULL,0.565,NULL),(17,NULL,'123 Boardway St, New York, NY',NULL,NULL,NULL,NULL,40,NULL,NULL,0.565,NULL),(18,NULL,'1536 Valley Drive, Norco, CA, United States',33.90876799999999,-117.523886,NULL,'West Johnson',40,50,60,0.565,1131),(19,NULL,'Figueroa Street, Los Angeles, CA, United States',33.8045884,-118.28395840000002,NULL,'Lakers',400,500,600,0.565,1131),(23,NULL,'Riverside, CA, United States',33.9533487,-117.3961564,NULL,'ref',20,30,40,0.565,NULL),(24,NULL,'1536 Valley Drive, Norco, CA, United States',33.90876799999999,-117.523886,'951-123-4567','Google',40,90,70,0.565,1130),(25,NULL,'Detroit, MI, United States',42.331427,-83.0457538,NULL,'Motown',50,60,70,0.565,1130);
/*!40000 ALTER TABLE `nurse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REFERRAL_DATE` date DEFAULT NULL,
  `REFERRAL_SOURCE_ID` int(11) NOT NULL,
  `NAME` varchar(64) NOT NULL,
  `MR_NUM` varchar(16) DEFAULT NULL,
  `d_o_b` date DEFAULT NULL,
  `DIANOSIS_ID` int(11) DEFAULT NULL,
  `THERAPY_TYPE_ID` int(11) DEFAULT NULL,
  `I_V_ACCESS_ID` int(11) DEFAULT NULL,
  `START_OF_CARE` tinyint(1) DEFAULT NULL,
  `START_OF_CARE_DATE` date DEFAULT NULL,
  `service_address` varchar(100) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `BILLING_ID` int(11) DEFAULT NULL,
  `RX` varchar(128) DEFAULT NULL,
  `EST_LAST_DAY_OF_SERVICE` date DEFAULT NULL,
  `LABS` tinyint(1) DEFAULT NULL,
  `LABS_FREQUENCY` varchar(64) DEFAULT NULL,
  `FIRST_RECERT_DUE` date DEFAULT NULL,
  `d_c_date` date DEFAULT NULL,
  `INFO_IN_S_O_S` tinyint(1) DEFAULT NULL,
  `SCHEDULING_PREFERENCE` varchar(64) DEFAULT NULL,
  `REFERRAL_NOTE` varchar(1024) DEFAULT NULL,
  `REFERRAL_RESOLUTION_ID` int(11) DEFAULT NULL,
  `REFERRAL_RESOLUTION_DATE` date DEFAULT NULL,
  `REFERRAL_RESOLUTION_NOTE` varchar(512) DEFAULT NULL,
  `VENDOR_CONFIRMATION_DATE` date DEFAULT NULL,
  `NURSE_CONFIRMATION_DATE` date DEFAULT NULL,
  `PATIENT_CONFIRMATION_DATE` date DEFAULT NULL,
  `MEDS_DELIVERY_DATE` date DEFAULT NULL,
  `MEDS_CONFIRMATION_DATE` date DEFAULT NULL,
  `ACTIVE` tinyint(1) DEFAULT '1',
  `DESCRIPTION` varchar(256) DEFAULT NULL,
  `BILLING_RATE` double DEFAULT NULL,
  `BILLING_RATE_2HR_SOC` double DEFAULT NULL,
  `BILLING_RATE_2HR_ROC` double DEFAULT NULL,
  `BILLING_FLAT` double DEFAULT NULL,
  `BILLING_FLAT_2HR_SOC` double DEFAULT NULL,
  `BILLING_FLAT_2HR_ROC` double DEFAULT NULL,
  `MILEAGE_RATE` double DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,NULL,3,'Tod Lame','12345',NULL,18,57,47,0,'2013-08-19','212 W Mission Ct, Corona, CA 92882, USA',33.860343,-117.57081299999999,3,'300MG','2015-12-25',0,'none',NULL,NULL,1,'Monday - Thursday','this should update now',886,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'2013-10-23',7,'Little Johnny','1234567890',NULL,30,58,48,1,'2013-10-24','19540 Jamboree Road, Irvine, CA',33.6588778,-117.86035559999999,7,'500MG','2013-12-24',0,NULL,NULL,NULL,0,'TU/TH','This should work',886,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'2013-12-02',1,'Eddie Cane','T548-7369-1981',NULL,831,852,847,0,NULL,'212 W. Mission Ct, Corona, CA 92882, USA',33.860343,-117.570813,1,NULL,NULL,1,NULL,'2013-02-08',NULL,0,NULL,NULL,886,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'2013-12-02',1,'Eddie Cane','T548-7369-1981',NULL,831,NULL,NULL,0,NULL,'212 W. Mission Ct, Corona, CA 92882, USA',33.860343,-117.570813,1,NULL,NULL,1,NULL,'2013-02-08',NULL,0,NULL,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,'2013-12-02',1,'Eddie Cane','T548-7369-1981',NULL,831,NULL,NULL,0,NULL,'212 W. Mission Ct, Corona, CA 92882, USA',33.860343,-117.570813,1,NULL,NULL,1,NULL,'2013-02-08',NULL,0,NULL,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'2013-12-07',1,'Eddie Cane','T548-7369-1981',NULL,831,NULL,NULL,0,NULL,'212 W. Mission Ct, Corona, CA 92882, USA',33.860343,-117.570813,1,NULL,NULL,1,NULL,'2013-02-08',NULL,0,NULL,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,'2014-02-24',1,'Eddie Cane','T548-7369-1981',NULL,831,NULL,NULL,0,NULL,'212 W. Mission Ct, Corona, CA 92882, USA',33.860343,-117.570813,1,NULL,NULL,1,NULL,'2013-02-08',NULL,0,NULL,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,'2014-02-25',1,'Eddie Cane','T548-7369-1981',NULL,831,NULL,NULL,0,NULL,'212 W. Mission Ct, Corona, CA 92882, USA',33.860343,-117.570813,1,NULL,NULL,1,NULL,'2013-02-08',NULL,0,NULL,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,'2014-04-09',4,'Eddie Mack',NULL,'1981-02-08',832,875,848,1,'2014-04-11','1536 Valley Dr, Norco, CA 92860, USA',33.90876799999999,-117.523886,4,'300MG','2016-04-11',0,NULL,NULL,NULL,0,NULL,'Have to fix address.',885,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,'2014-04-09',2,'Cee Low',NULL,'1980-03-04',833,866,848,1,'2014-04-15',NULL,34.0550376,-117.75025649999998,2,'300MG','2014-06-19',0,NULL,NULL,NULL,0,'T & TH, Morning','Park in the back, knock twice. Don\'t use door bell',886,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(11,'2014-04-16',16,'Sick Sickly',NULL,'2004-04-16',832,857,848,1,'2014-04-22',NULL,NULL,NULL,16,'300MG',NULL,0,NULL,NULL,NULL,0,NULL,NULL,885,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_ID` int(11) NOT NULL,
  `EMAIL` varchar(64) NOT NULL,
  `FIRST_NAME` varchar(20) NOT NULL,
  `LAST_NAME` varchar(20) NOT NULL,
  `DISABLED` tinyint(1) DEFAULT NULL,
  `READ_ONLY` tinyint(1) DEFAULT NULL,
  `PASSWORD` varchar(128) DEFAULT NULL,
  `NOTES` varchar(256) DEFAULT NULL,
  `LAST_LOGIN` datetime DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1128,'eddiemay@gmail.com','Eddie','Mayfield',0,0,'6B7DE1B846CC2A047CE71E1214C3B6F7',NULL,'2014-04-21 13:47:00','eddiemay'),(5,1129,'lavernslater@yahoo.com','Lavern','Slater',0,0,NULL,'This is good.',NULL,'lavernslater'),(6,1129,'eddiemay@gmail.com','Eddie','Mayfield',0,0,NULL,NULL,NULL,'eddiemay'),(7,1129,'s912andrews@aol.com','Shalonda','Mayfield',0,0,NULL,'Good nurse',NULL,'s912andrews'),(8,1129,'nicole@yahoo.com','Nicole','Lawrente',0,0,NULL,'She is not a nurse.',NULL,'nicole'),(9,1129,'nicole@yahoo.com','Nicole','Lawrence',0,0,NULL,'She is not a nurse.',NULL,'nicole'),(10,1129,'jpowers@yahoo.com','Jim','Powers',0,0,NULL,'Not a nurse. Actually does not exist.',NULL,'jpowers'),(11,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(12,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(13,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(14,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(15,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(16,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(17,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(18,1129,'wesjohn@lakers.com','Westly','Johnson',0,0,NULL,'Good player, bad habits',NULL,'wesjohn'),(19,1129,'kobe@lakers.com','Kobe','Bryant',0,0,NULL,'#24 Missed the entire season, only played in 6 games.',NULL,'kobe'),(23,1129,'nick@pen.com','Nick','Pen',0,0,NULL,NULL,NULL,'nick'),(24,1129,'yolanda@example.com','Yolanda','Adams',0,0,NULL,'Mileage rate is sending them to jail',NULL,'yolanda'),(25,1129,'druffin@motown.com','David','Ruffin',0,0,NULL,'This should all work fine.',NULL,'druffin');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendor`
--

DROP TABLE IF EXISTS `vendor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(64) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `LATITUDE` double DEFAULT NULL,
  `LONGITUDE` double DEFAULT NULL,
  `PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `CONTACT_NAME` varchar(64) DEFAULT NULL,
  `CONTACT_NUMBER` varchar(20) DEFAULT NULL,
  `ACTIVE` tinyint(1) DEFAULT '1',
  `BILLING_RATE` double DEFAULT NULL,
  `BILLING_RATE_2HR_SOC` double DEFAULT NULL,
  `BILLING_RATE_2HR_ROC` double DEFAULT NULL,
  `BILLING_FLAT` double DEFAULT NULL,
  `BILLING_FLAT_2HR_SOC` double DEFAULT NULL,
  `BILLING_FLAT_2HR_ROC` double DEFAULT NULL,
  `fax_number` varchar(20) DEFAULT NULL,
  `notes` varchar(256) DEFAULT NULL,
  `contact_email` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendor`
--

LOCK TABLES `vendor` WRITE;
/*!40000 ALTER TABLE `vendor` DISABLE KEYS */;
INSERT INTO `vendor` VALUES (1,'AllCare Home Health Agency',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'Biofusion',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'Biofusion AIS',NULL,NULL,NULL,NULL,'James Dunn','951-514-3431',1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'Biofusion LA',NULL,NULL,NULL,NULL,NULL,NULL,1,105,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,'Bioscrip',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'Coram',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,'CVS Caremark',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,150,NULL,NULL,NULL,NULL,NULL),(8,'Grand Care',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,'Heralds Home Health',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,'IV Solutions',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(11,'LifeCare Solutions',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(12,'NORD National Organization of Rare Diseases',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(13,'Specialty Pharmacy Nursing Network',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(14,'Stellar Home Health',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(15,'Walgreens',NULL,NULL,NULL,NULL,NULL,NULL,1,75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(16,'Eddie\'s Health Services',NULL,33.90876799999999,-117.523886,'909-800-0300','Eddie Mayfield','909-800-0302',1,10,20,30,40,50,60,'909-800-0301','Hopefully this works first try','eddiemay@gmail.com'),(17,'Target RX','Target, Summit Avenue, Fontana, CA',34.152138,-117.47334599999999,'909-Target','Mr. Target','Mr-Target-number',1,10.1,20.2,30.3,40.4,50.5,60.6,'909-Target-fax','Got to get address working',NULL),(18,'Costco','444 Main Street, Houston, TX',29.7608985,-95.36190959999999,'909-Costco','Mr. Costco','Mr-Costco-Number',1,10.11,20.22,30.33,40.44,50.55,60.67,'909-Costco-fax','Costco is here',NULL);
/*!40000 ALTER TABLE `vendor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-25 16:16:04
