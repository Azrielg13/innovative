-- MySQL dump 10.13  Distrib 5.7.14, for osx10.11 (x86_64)
--
-- Host: localhost    Database: iisosnet_main
-- ------------------------------------------------------
-- Server version	5.7.14

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
  `CANCEL_REASON` varchar(160) DEFAULT NULL,
  `TIME_IN_D` datetime DEFAULT NULL,
  `TIME_OUT_D` datetime DEFAULT NULL,
  `PAY_FLAT_D` double DEFAULT NULL,
  `PAY_RATE_D` double DEFAULT NULL,
  `PAY_HOURS_D` double DEFAULT NULL,
  `PAY_MILEAGE_D` double DEFAULT NULL,
  `MILEAGE_D` double DEFAULT NULL,
  `PAY_MILEAGE_RATE_D` double DEFAULT NULL,
  `PAYING_TYPE_ID_D` int(11) DEFAULT NULL,
  `PAYSTUB_ID` int(11) DEFAULT NULL,
  `BILLING_FLAT_D` double DEFAULT NULL,
  `BILLING_RATE_D` double DEFAULT NULL,
  `BILLING_MILEAGE_D` double DEFAULT NULL,
  `BILLING_MILEAGE_RATE_D` double DEFAULT NULL,
  `BILLING_TYPE_ID_D` int(11) DEFAULT NULL,
  `INVOICE_ID` int(11) DEFAULT NULL,
  `ASSESSMENT_COMPLETE` tinyint(1) DEFAULT NULL,
  `ASSESSMENT_APPROVED` tinyint(1) DEFAULT NULL,
  `APPROVED_DATE` date DEFAULT NULL,
  `APPROVER_ID` int(11) DEFAULT NULL,
  `DATA_FILE_ID` int(11) DEFAULT NULL,
  `BILLED_HOURS_D` double DEFAULT NULL,
  `NURSE_CONFIRM_RES_ID` int(11) DEFAULT NULL,
  `NURSE_CONFIRM_TS` datetime DEFAULT NULL,
  `NURSE_CONFIRM_NOTES` varchar(512) DEFAULT NULL,
  `STATE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_file`
--

DROP TABLE IF EXISTS `data_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(32) NOT NULL,
  `TYPE_ID` int(11) NOT NULL,
  `data` mediumblob,
  `size` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=1668 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `data_file_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NURSE_ID` (`NURSE_ID`,`LIC_TYPE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `addr_unit` varchar(20) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `REFERRAL_SOURCE` varchar(100) DEFAULT NULL,
  `pay_flat` double DEFAULT NULL,
  `pay_rate` double DEFAULT NULL,
  `pay_flat_2hr_soc` double DEFAULT NULL,
  `pay_rate_2hr_soc` double DEFAULT NULL,
  `pay_flat_2hr_roc` double DEFAULT NULL,
  `pay_rate_2hr_roc` double DEFAULT NULL,
  `mileage_rate` double DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `START_OF_CARE_DATE` date DEFAULT NULL,
  `service_address` varchar(100) DEFAULT NULL,
  `PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `PRIMARY_PHONE_TYPE_ID` int(11) DEFAULT NULL,
  `ALT_CONTACT_NUMBER` varchar(20) DEFAULT NULL,
  `ALT_PHONE_TYPE_ID` int(11) DEFAULT NULL,
  `EMERGENCY_CONTACT` varchar(40) DEFAULT NULL,
  `EMERGENCY_CONTACT_PHONE` varchar(20) DEFAULT NULL,
  `EMERGENCY_CONTACT_PHONE_TYPE_ID` int(11) DEFAULT NULL,
  `service_addr_unit` varchar(20) DEFAULT NULL,
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
  `PATIENT_STATUS_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paystub`
--

DROP TABLE IF EXISTS `paystub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paystub` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NURSE_ID` int(11) NOT NULL,
  `STATUS_ID` int(11) NOT NULL,
  `NAME` varchar(64) DEFAULT NULL,
  `PAY_DATE` date NOT NULL,
  `GENERATION_TIME` datetime DEFAULT NULL,
  `LOGGED_HOURS` double DEFAULT NULL,
  `MILEAGE` int(11) DEFAULT NULL,
  `PAY_MILEAGE` double DEFAULT NULL,
  `LOGGED_HOURS_Y_T_D` double DEFAULT NULL,
  `MILEAGE_Y_T_D` int(11) DEFAULT NULL,
  `PAY_MILEAGE_Y_T_D` double DEFAULT NULL,
  `GROSS_PAY` double NOT NULL,
  `DEDUCTION` varchar(4096) DEFAULT NULL,
  `PRE_TAX_DEDUCTION` double DEFAULT NULL,
  `TAXABLE` double DEFAULT NULL,
  `TAX_TOTAL` double DEFAULT NULL,
  `POST_TAX_DEDUCTION` double DEFAULT NULL,
  `NON_TAX_WAGES` double DEFAULT NULL,
  `NET_PAY` double DEFAULT NULL,
  `GROSS_PAY_Y_T_D` double DEFAULT NULL,
  `PRE_TAX_DEDUCTION_Y_T_D` double DEFAULT NULL,
  `TAXABLE_Y_T_D` double DEFAULT NULL,
  `TAX_TOTAL_Y_T_D` double DEFAULT NULL,
  `POST_TAX_DEDUCTION_Y_T_D` double DEFAULT NULL,
  `NON_TAX_WAGES_Y_T_D` double DEFAULT NULL,
  `NET_PAY_Y_T_D` double DEFAULT NULL,
  `COMMENT` varchar(1024) DEFAULT NULL,
  `DATA` blob,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trans_hist`
--

DROP TABLE IF EXISTS `trans_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trans_hist` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TIMESTAMP` datetime NOT NULL,
  `TYPE_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `OBJECT` varchar(64) NOT NULL,
  `ROW_ID` int(11) NOT NULL,
  `DATA` blob,
  PRIMARY KEY (`ID`),
  KEY `I_USERID` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1718 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `NOTES` varchar(4096) DEFAULT NULL,
  `LAST_LOGIN` datetime DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_TYPEID` (`TYPE_ID`),
  CONSTRAINT `FK_TYPEID` FOREIGN KEY (`TYPE_ID`) REFERENCES `general_data` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `v_appointment`
--

DROP TABLE IF EXISTS `v_appointment`;
/*!50001 DROP VIEW IF EXISTS `v_appointment`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_appointment` AS SELECT 
 1 AS `ID`,
 1 AS `PATIENT_ID`,
 1 AS `NURSE_ID`,
 1 AS `START`,
 1 AS `END`,
 1 AS `CANCELLED`,
 1 AS `CANCEL_REASON`,
 1 AS `TIME_IN_D`,
 1 AS `TIME_OUT_D`,
 1 AS `PAY_FLAT_D`,
 1 AS `PAY_RATE_D`,
 1 AS `PAY_HOURS_D`,
 1 AS `PAY_MILEAGE_D`,
 1 AS `MILEAGE_D`,
 1 AS `PAY_MILEAGE_RATE_D`,
 1 AS `PAYING_TYPE_ID_D`,
 1 AS `PAYSTUB_ID`,
 1 AS `BILLING_FLAT_D`,
 1 AS `BILLING_RATE_D`,
 1 AS `BILLING_MILEAGE_D`,
 1 AS `BILLING_MILEAGE_RATE_D`,
 1 AS `BILLING_TYPE_ID_D`,
 1 AS `INVOICE_ID`,
 1 AS `ASSESSMENT_COMPLETE`,
 1 AS `ASSESSMENT_APPROVED`,
 1 AS `APPROVED_DATE`,
 1 AS `APPROVER_ID`,
 1 AS `DATA_FILE_ID`,
 1 AS `BILLED_HOURS_D`,
 1 AS `NURSE_CONFIRM_RES_ID`,
 1 AS `NURSE_CONFIRM_TS`,
 1 AS `NURSE_CONFIRM_NOTES`,
 1 AS `STATE`,
 1 AS `NURSE_NAME`,
 1 AS `PATIENT_NAME`,
 1 AS `VENDOR_ID`,
 1 AS `VENDOR_NAME`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_license`
--

DROP TABLE IF EXISTS `v_license`;
/*!50001 DROP VIEW IF EXISTS `v_license`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_license` AS SELECT 
 1 AS `ID`,
 1 AS `NURSE_ID`,
 1 AS `LIC_TYPE_ID`,
 1 AS `NUMBER`,
 1 AS `VALID_DATE`,
 1 AS `EXPIRATION_DATE`,
 1 AS `data_file_id`,
 1 AS `NURSE_NAME`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_nurse`
--

DROP TABLE IF EXISTS `v_nurse`;
/*!50001 DROP VIEW IF EXISTS `v_nurse`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_nurse` AS SELECT 
 1 AS `FIRST_NAME`,
 1 AS `LAST_NAME`,
 1 AS `FULL_NAME`,
 1 AS `EMAIL`,
 1 AS `USER_NAME`,
 1 AS `NOTES`,
 1 AS `LAST_LOGIN`,
 1 AS `ID`,
 1 AS `REG_DATE`,
 1 AS `address`,
 1 AS `addr_unit`,
 1 AS `latitude`,
 1 AS `longitude`,
 1 AS `PHONE_NUMBER`,
 1 AS `REFERRAL_SOURCE`,
 1 AS `pay_flat`,
 1 AS `pay_rate`,
 1 AS `pay_flat_2hr_soc`,
 1 AS `pay_rate_2hr_soc`,
 1 AS `pay_flat_2hr_roc`,
 1 AS `pay_rate_2hr_roc`,
 1 AS `mileage_rate`,
 1 AS `status_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_patient`
--

DROP TABLE IF EXISTS `v_patient`;
/*!50001 DROP VIEW IF EXISTS `v_patient`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_patient` AS SELECT 
 1 AS `ID`,
 1 AS `REFERRAL_DATE`,
 1 AS `REFERRAL_SOURCE_ID`,
 1 AS `NAME`,
 1 AS `MR_NUM`,
 1 AS `d_o_b`,
 1 AS `DIANOSIS_ID`,
 1 AS `THERAPY_TYPE_ID`,
 1 AS `I_V_ACCESS_ID`,
 1 AS `START_OF_CARE_DATE`,
 1 AS `service_address`,
 1 AS `PHONE_NUMBER`,
 1 AS `PRIMARY_PHONE_TYPE_ID`,
 1 AS `ALT_CONTACT_NUMBER`,
 1 AS `ALT_PHONE_TYPE_ID`,
 1 AS `EMERGENCY_CONTACT`,
 1 AS `EMERGENCY_CONTACT_PHONE`,
 1 AS `EMERGENCY_CONTACT_PHONE_TYPE_ID`,
 1 AS `service_addr_unit`,
 1 AS `latitude`,
 1 AS `longitude`,
 1 AS `BILLING_ID`,
 1 AS `RX`,
 1 AS `EST_LAST_DAY_OF_SERVICE`,
 1 AS `LABS`,
 1 AS `LABS_FREQUENCY`,
 1 AS `FIRST_RECERT_DUE`,
 1 AS `d_c_date`,
 1 AS `INFO_IN_S_O_S`,
 1 AS `SCHEDULING_PREFERENCE`,
 1 AS `REFERRAL_NOTE`,
 1 AS `REFERRAL_RESOLUTION_ID`,
 1 AS `REFERRAL_RESOLUTION_DATE`,
 1 AS `REFERRAL_RESOLUTION_NOTE`,
 1 AS `VENDOR_CONFIRMATION_DATE`,
 1 AS `NURSE_CONFIRMATION_DATE`,
 1 AS `PATIENT_CONFIRMATION_DATE`,
 1 AS `MEDS_DELIVERY_DATE`,
 1 AS `MEDS_CONFIRMATION_DATE`,
 1 AS `ACTIVE`,
 1 AS `DESCRIPTION`,
 1 AS `BILLING_RATE`,
 1 AS `BILLING_RATE_2HR_SOC`,
 1 AS `BILLING_RATE_2HR_ROC`,
 1 AS `BILLING_FLAT`,
 1 AS `BILLING_FLAT_2HR_SOC`,
 1 AS `BILLING_FLAT_2HR_ROC`,
 1 AS `MILEAGE_RATE`,
 1 AS `PATIENT_STATUS_ID`,
 1 AS `BILLING_VENDOR_NAME`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_user`
--

DROP TABLE IF EXISTS `v_user`;
/*!50001 DROP VIEW IF EXISTS `v_user`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_user` AS SELECT 
 1 AS `ID`,
 1 AS `TYPE_ID`,
 1 AS `EMAIL`,
 1 AS `FIRST_NAME`,
 1 AS `LAST_NAME`,
 1 AS `DISABLED`,
 1 AS `READ_ONLY`,
 1 AS `PASSWORD`,
 1 AS `NOTES`,
 1 AS `LAST_LOGIN`,
 1 AS `user_name`,
 1 AS `FULL_NAME`*/;
SET character_set_client = @saved_cs_client;

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
  `addr_unit` varchar(20) DEFAULT NULL,
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
  `MILEAGE_RATE` double DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `v_appointment`
--

/*!50001 DROP VIEW IF EXISTS `v_appointment`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `v_appointment` AS select `appointment`.`ID` AS `ID`,`appointment`.`PATIENT_ID` AS `PATIENT_ID`,`appointment`.`NURSE_ID` AS `NURSE_ID`,`appointment`.`START` AS `START`,`appointment`.`END` AS `END`,`appointment`.`CANCELLED` AS `CANCELLED`,`appointment`.`CANCEL_REASON` AS `CANCEL_REASON`,`appointment`.`TIME_IN_D` AS `TIME_IN_D`,`appointment`.`TIME_OUT_D` AS `TIME_OUT_D`,`appointment`.`PAY_FLAT_D` AS `PAY_FLAT_D`,`appointment`.`PAY_RATE_D` AS `PAY_RATE_D`,`appointment`.`PAY_HOURS_D` AS `PAY_HOURS_D`,`appointment`.`PAY_MILEAGE_D` AS `PAY_MILEAGE_D`,`appointment`.`MILEAGE_D` AS `MILEAGE_D`,`appointment`.`PAY_MILEAGE_RATE_D` AS `PAY_MILEAGE_RATE_D`,`appointment`.`PAYING_TYPE_ID_D` AS `PAYING_TYPE_ID_D`,`appointment`.`PAYSTUB_ID` AS `PAYSTUB_ID`,`appointment`.`BILLING_FLAT_D` AS `BILLING_FLAT_D`,`appointment`.`BILLING_RATE_D` AS `BILLING_RATE_D`,`appointment`.`BILLING_MILEAGE_D` AS `BILLING_MILEAGE_D`,`appointment`.`BILLING_MILEAGE_RATE_D` AS `BILLING_MILEAGE_RATE_D`,`appointment`.`BILLING_TYPE_ID_D` AS `BILLING_TYPE_ID_D`,`appointment`.`INVOICE_ID` AS `INVOICE_ID`,`appointment`.`ASSESSMENT_COMPLETE` AS `ASSESSMENT_COMPLETE`,`appointment`.`ASSESSMENT_APPROVED` AS `ASSESSMENT_APPROVED`,`appointment`.`APPROVED_DATE` AS `APPROVED_DATE`,`appointment`.`APPROVER_ID` AS `APPROVER_ID`,`appointment`.`DATA_FILE_ID` AS `DATA_FILE_ID`,`appointment`.`BILLED_HOURS_D` AS `BILLED_HOURS_D`,`appointment`.`NURSE_CONFIRM_RES_ID` AS `NURSE_CONFIRM_RES_ID`,`appointment`.`NURSE_CONFIRM_TS` AS `NURSE_CONFIRM_TS`,`appointment`.`NURSE_CONFIRM_NOTES` AS `NURSE_CONFIRM_NOTES`,`appointment`.`STATE` AS `STATE`,`user`.`FULL_NAME` AS `NURSE_NAME`,`patient`.`NAME` AS `PATIENT_NAME`,`patient`.`BILLING_ID` AS `VENDOR_ID`,`patient`.`BILLING_VENDOR_NAME` AS `VENDOR_NAME` from ((`appointment` join `v_user` `User`) join `v_patient` `Patient`) where ((`appointment`.`NURSE_ID` = `user`.`ID`) and (`appointment`.`PATIENT_ID` = `patient`.`ID`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_license`
--

/*!50001 DROP VIEW IF EXISTS `v_license`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `v_license` AS select `license`.`ID` AS `ID`,`license`.`NURSE_ID` AS `NURSE_ID`,`license`.`LIC_TYPE_ID` AS `LIC_TYPE_ID`,`license`.`NUMBER` AS `NUMBER`,`license`.`VALID_DATE` AS `VALID_DATE`,`license`.`EXPIRATION_DATE` AS `EXPIRATION_DATE`,`license`.`data_file_id` AS `data_file_id`,`v_user`.`FULL_NAME` AS `NURSE_NAME` from (`license` join `v_user`) where (`license`.`NURSE_ID` = `v_user`.`ID`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_nurse`
--

/*!50001 DROP VIEW IF EXISTS `v_nurse`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `v_nurse` AS select `user`.`FIRST_NAME` AS `FIRST_NAME`,`user`.`LAST_NAME` AS `LAST_NAME`,`user`.`FULL_NAME` AS `FULL_NAME`,`user`.`EMAIL` AS `EMAIL`,`user`.`user_name` AS `USER_NAME`,`user`.`NOTES` AS `NOTES`,`user`.`LAST_LOGIN` AS `LAST_LOGIN`,`nurse`.`ID` AS `ID`,`nurse`.`REG_DATE` AS `REG_DATE`,`nurse`.`address` AS `address`,`nurse`.`addr_unit` AS `addr_unit`,`nurse`.`latitude` AS `latitude`,`nurse`.`longitude` AS `longitude`,`nurse`.`PHONE_NUMBER` AS `PHONE_NUMBER`,`nurse`.`REFERRAL_SOURCE` AS `REFERRAL_SOURCE`,`nurse`.`pay_flat` AS `pay_flat`,`nurse`.`pay_rate` AS `pay_rate`,`nurse`.`pay_flat_2hr_soc` AS `pay_flat_2hr_soc`,`nurse`.`pay_rate_2hr_soc` AS `pay_rate_2hr_soc`,`nurse`.`pay_flat_2hr_roc` AS `pay_flat_2hr_roc`,`nurse`.`pay_rate_2hr_roc` AS `pay_rate_2hr_roc`,`nurse`.`mileage_rate` AS `mileage_rate`,`nurse`.`status_id` AS `status_id` from (`nurse` join `v_user` `User`) where (`nurse`.`ID` = `user`.`ID`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_patient`
--

/*!50001 DROP VIEW IF EXISTS `v_patient`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `v_patient` AS select `patient`.`ID` AS `ID`,`patient`.`REFERRAL_DATE` AS `REFERRAL_DATE`,`patient`.`REFERRAL_SOURCE_ID` AS `REFERRAL_SOURCE_ID`,`patient`.`NAME` AS `NAME`,`patient`.`MR_NUM` AS `MR_NUM`,`patient`.`d_o_b` AS `d_o_b`,`patient`.`DIANOSIS_ID` AS `DIANOSIS_ID`,`patient`.`THERAPY_TYPE_ID` AS `THERAPY_TYPE_ID`,`patient`.`I_V_ACCESS_ID` AS `I_V_ACCESS_ID`,`patient`.`START_OF_CARE_DATE` AS `START_OF_CARE_DATE`,`patient`.`service_address` AS `service_address`,`patient`.`PHONE_NUMBER` AS `PHONE_NUMBER`,`patient`.`PRIMARY_PHONE_TYPE_ID` AS `PRIMARY_PHONE_TYPE_ID`,`patient`.`ALT_CONTACT_NUMBER` AS `ALT_CONTACT_NUMBER`,`patient`.`ALT_PHONE_TYPE_ID` AS `ALT_PHONE_TYPE_ID`,`patient`.`EMERGENCY_CONTACT` AS `EMERGENCY_CONTACT`,`patient`.`EMERGENCY_CONTACT_PHONE` AS `EMERGENCY_CONTACT_PHONE`,`patient`.`EMERGENCY_CONTACT_PHONE_TYPE_ID` AS `EMERGENCY_CONTACT_PHONE_TYPE_ID`,`patient`.`service_addr_unit` AS `service_addr_unit`,`patient`.`latitude` AS `latitude`,`patient`.`longitude` AS `longitude`,`patient`.`BILLING_ID` AS `BILLING_ID`,`patient`.`RX` AS `RX`,`patient`.`EST_LAST_DAY_OF_SERVICE` AS `EST_LAST_DAY_OF_SERVICE`,`patient`.`LABS` AS `LABS`,`patient`.`LABS_FREQUENCY` AS `LABS_FREQUENCY`,`patient`.`FIRST_RECERT_DUE` AS `FIRST_RECERT_DUE`,`patient`.`d_c_date` AS `d_c_date`,`patient`.`INFO_IN_S_O_S` AS `INFO_IN_S_O_S`,`patient`.`SCHEDULING_PREFERENCE` AS `SCHEDULING_PREFERENCE`,`patient`.`REFERRAL_NOTE` AS `REFERRAL_NOTE`,`patient`.`REFERRAL_RESOLUTION_ID` AS `REFERRAL_RESOLUTION_ID`,`patient`.`REFERRAL_RESOLUTION_DATE` AS `REFERRAL_RESOLUTION_DATE`,`patient`.`REFERRAL_RESOLUTION_NOTE` AS `REFERRAL_RESOLUTION_NOTE`,`patient`.`VENDOR_CONFIRMATION_DATE` AS `VENDOR_CONFIRMATION_DATE`,`patient`.`NURSE_CONFIRMATION_DATE` AS `NURSE_CONFIRMATION_DATE`,`patient`.`PATIENT_CONFIRMATION_DATE` AS `PATIENT_CONFIRMATION_DATE`,`patient`.`MEDS_DELIVERY_DATE` AS `MEDS_DELIVERY_DATE`,`patient`.`MEDS_CONFIRMATION_DATE` AS `MEDS_CONFIRMATION_DATE`,`patient`.`ACTIVE` AS `ACTIVE`,`patient`.`DESCRIPTION` AS `DESCRIPTION`,`patient`.`BILLING_RATE` AS `BILLING_RATE`,`patient`.`BILLING_RATE_2HR_SOC` AS `BILLING_RATE_2HR_SOC`,`patient`.`BILLING_RATE_2HR_ROC` AS `BILLING_RATE_2HR_ROC`,`patient`.`BILLING_FLAT` AS `BILLING_FLAT`,`patient`.`BILLING_FLAT_2HR_SOC` AS `BILLING_FLAT_2HR_SOC`,`patient`.`BILLING_FLAT_2HR_ROC` AS `BILLING_FLAT_2HR_ROC`,`patient`.`MILEAGE_RATE` AS `MILEAGE_RATE`,`patient`.`PATIENT_STATUS_ID` AS `PATIENT_STATUS_ID`,`vendor`.`NAME` AS `BILLING_VENDOR_NAME` from (`patient` join `vendor`) where (`patient`.`BILLING_ID` = `vendor`.`ID`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_user`
--

/*!50001 DROP VIEW IF EXISTS `v_user`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `v_user` AS select `user`.`ID` AS `ID`,`user`.`TYPE_ID` AS `TYPE_ID`,`user`.`EMAIL` AS `EMAIL`,`user`.`FIRST_NAME` AS `FIRST_NAME`,`user`.`LAST_NAME` AS `LAST_NAME`,`user`.`DISABLED` AS `DISABLED`,`user`.`READ_ONLY` AS `READ_ONLY`,`user`.`PASSWORD` AS `PASSWORD`,`user`.`NOTES` AS `NOTES`,`user`.`LAST_LOGIN` AS `LAST_LOGIN`,`user`.`user_name` AS `user_name`,concat(`user`.`FIRST_NAME`,' ',`user`.`LAST_NAME`) AS `FULL_NAME` from `user` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-23 10:17:07
