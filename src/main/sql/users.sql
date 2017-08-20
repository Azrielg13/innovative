
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
  `password_d` varchar(128) DEFAULT NULL,
  `NOTES` varchar(4096) DEFAULT NULL,
  `LAST_LOGIN` datetime DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_TYPEID` (`TYPE_ID`),
  CONSTRAINT `FK_TYPEID` FOREIGN KEY (`TYPE_ID`) REFERENCES `general_data` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1128,'eddiemay@gmail.com','Eddie','Mayfield',0,0,'6B7DE1B846CC2A047CE71E1214C3B6F7',NULL,'2016-08-02 22:47:04','eddiemay'),(5,1129,'lavernslater@yahoo.com','Lavern','Slater',0,0,'4CA9D3DCD2B6843E62D75EB191887CF2','This is good.',NULL,'lavernslater'),(6,1129,'eddiemay@gmail.com','Eddie','Mayfield',0,0,NULL,NULL,NULL,'eddiemay'),(7,1129,'s912andrews@aol.com','Shalonda','Mayfield',0,0,NULL,'Good nurse',NULL,'s912andrews'),(8,1129,'nicole@yahoo.com','Nicole','Lawrente',0,0,NULL,'She is not a nurse.',NULL,'nicole'),(9,1129,'nicole@yahoo.com','Nicole','Lawrence',0,0,NULL,'She is not a nurse.',NULL,'nicole'),(10,1129,'jpowers@yahoo.com','Jim','Powers',0,0,NULL,'Not a nurse. Actually does not exist.',NULL,'jpowers'),(11,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(12,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(13,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(14,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(15,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(16,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(17,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(18,1129,'wesjohn@lakers.com','Westly','Johnson',0,0,'0FFB5CC0EE5648AA55290F0F89F5F8D8','Good player, bad habits',NULL,'wesjohn'),(19,1129,'kobe@lakers.com','Kobe','Bryant',0,0,NULL,'#24 Missed the entire season, only played in 6 games.',NULL,'kobe'),(23,1129,'nick@pen.com','Nick','Pen',0,0,NULL,NULL,NULL,'nick'),(24,1129,'yolanda@example.com','Yolanda','Adams',0,0,NULL,'Mileage rate is sending them to jail',NULL,'yolanda'),(25,1129,'druffin@motown.com','David','Ruffin',0,0,NULL,'This should all work fine.',NULL,'druffin'),(27,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(28,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(29,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(30,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(31,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(32,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(33,1129,'betty@example.com','Nurse','Betty',0,0,NULL,NULL,NULL,'betty'),(38,1129,'nointernet@example.com','Good','Job',0,0,NULL,NULL,NULL,'nointernet'),(39,1129,'s912andrews@aol.com','Shalonda','Mayfield',0,0,NULL,'hope this works',NULL,'s912andrews'),(40,1129,'test@example.com','Test','User',0,0,'yoface','I hope this works',NULL,'test'),(41,1129,'test2@example.com','Test','User 2',0,0,'179AD45C6CE2CB97CF1029E212046E81','I hope this works',NULL,'test2'),(42,1129,'nopass@example.com','No','Password',0,0,'D41D8CD98F00B204E9800998ECF8427E','Testing creating a user with no password.',NULL,'nopass'),(43,1129,'nopass2@example.com','No','Pasword 2',0,0,NULL,'No password test 2',NULL,'nopass2'),(44,1129,'mismatch@example.com','Pass','Mismatch',0,0,'202CB962AC59075B964B07152D234B70','Should not process.',NULL,'mismatch'),(45,1129,'badpass@example.com','Bad','Pass',0,0,'202CB962AC59075B964B07152D234B70','Bad pass',NULL,'badpass'),(46,1128,'newadmin@example.com','New','Admin',0,0,'971EF6D73B20A633967633686C8E124A',NULL,NULL,'newadmin'),(48,1129,'juantwo@gmail.com','Juan','Two',0,0,NULL,NULL,NULL,'juantwo'),(49,1129,'eddiemay@gmail.com','Eddie Mack','Mayfield',0,0,NULL,'Web developer',NULL,'eddiemay'),(50,1129,'sal@joker.com','Sal','Loser',0,0,NULL,'Testing',NULL,'sal');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1128,'eddiemay@gmail.com','Eddie','Mayfield',0,0,'6B7DE1B846CC2A047CE71E1214C3B6F7',NULL,'2016-08-02 22:47:04','eddiemay');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

