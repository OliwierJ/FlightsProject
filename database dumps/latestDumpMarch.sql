CREATE DATABASE  IF NOT EXISTS `flights_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `flights_project`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: flights_project
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `booking_no` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `priority_boarding` tinyint(1) DEFAULT NULL,
  `luggage_amount` int DEFAULT NULL,
  `tier` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`booking_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES ('522558','govie@setu.ie',1,3,'Deluxe'),('600920','freedom@gov.usa',1,30,'Deluxe'),('987654','a@a.a',0,2,'Standard');
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight`
--

DROP TABLE IF EXISTS `flight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flight` (
  `flight_id` int NOT NULL AUTO_INCREMENT,
  `departure_airport` varchar(255) DEFAULT NULL,
  `arrival_airport` varchar(255) DEFAULT NULL,
  `departure_time` datetime DEFAULT NULL,
  `arrival_time` datetime DEFAULT NULL,
  `aircraft` varchar(255) NOT NULL,
  `base_price` int DEFAULT NULL,
  PRIMARY KEY (`flight_id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight`
--

LOCK TABLES `flight` WRITE;
/*!40000 ALTER TABLE `flight` DISABLE KEYS */;
INSERT INTO `flight` VALUES (100,'Dublin','Barcelona','2025-03-04 14:00:00','2025-03-04 16:20:00','Boeing 737-800',50),(101,'Barcelona','Dublin','2025-03-06 11:50:00','2025-03-06 13:20:00','Boeing 737-800',50);
/*!40000 ALTER TABLE `flight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight_booking`
--

DROP TABLE IF EXISTS `flight_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flight_booking` (
  `flight_id` int NOT NULL,
  `booking_no` varchar(10) NOT NULL,
  `is_return` tinyint(1) NOT NULL,
  PRIMARY KEY (`flight_id`,`booking_no`),
  KEY `booking_no` (`booking_no`),
  CONSTRAINT `flight_booking_ibfk_1` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`flight_id`),
  CONSTRAINT `flight_booking_ibfk_2` FOREIGN KEY (`booking_no`) REFERENCES `booking` (`booking_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight_booking`
--

LOCK TABLES `flight_booking` WRITE;
/*!40000 ALTER TABLE `flight_booking` DISABLE KEYS */;
INSERT INTO `flight_booking` VALUES (100,'522558',0),(100,'600920',0),(100,'987654',0),(101,'522558',1),(101,'600920',1);
/*!40000 ALTER TABLE `flight_booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passenger`
--

DROP TABLE IF EXISTS `passenger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passenger` (
  `passenger_ID` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `booking_no` varchar(10) DEFAULT NULL,
  `title` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`passenger_ID`),
  KEY `booking_no` (`booking_no`),
  CONSTRAINT `passenger_ibfk_3` FOREIGN KEY (`booking_no`) REFERENCES `booking` (`booking_no`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passenger`
--

LOCK TABLES `passenger` WRITE;
/*!40000 ALTER TABLE `passenger` DISABLE KEYS */;
INSERT INTO `passenger` VALUES (3,'Mary','Smith','987654','Mrs'),(5,'Brandon','J','987654','Mr'),(6,'Norma','Foley','522558','Ms'),(7,'Simon','Harris','522558','Mr'),(10,'Donald','Trump','600920','Mr'),(11,'Kamala','Harris','600920','Mrs');
/*!40000 ALTER TABLE `passenger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat` (
  `seat_no` varchar(5) NOT NULL,
  `class` varchar(30) DEFAULT NULL,
  `flight_id` int NOT NULL,
  `passenger_id` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`seat_no`,`flight_id`),
  KEY `flight_id` (`flight_id`),
  CONSTRAINT `seat_ibfk_2` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`flight_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES ('01A','Economy',100,3),('03C','Economy',100,7),('03D','Economy',100,10),('03E','Economy',100,11),('10A','Economy',101,7),('10B','Economy',101,10),('10C','Economy',101,11),('26D','Economy',100,6),('26D','Economy',101,6),('30A','Economy',100,3),('30B','Economy',100,5);
/*!40000 ALTER TABLE `seat` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-11 20:56:20
