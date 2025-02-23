-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: kiosk
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `play_info`
--

DROP TABLE IF EXISTS `play_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `play_info` (
  `play_info_id` int NOT NULL AUTO_INCREMENT,
  `movie_id` int NOT NULL,
  `theater_id` int NOT NULL,
  `movie_date` date DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  PRIMARY KEY (`play_info_id`),
  KEY `showmid_idx` (`movie_id`),
  KEY `theaterid_idx` (`theater_id`),
  CONSTRAINT `showmid` FOREIGN KEY (`movie_id`) REFERENCES `showmovie` (`movie_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `theaterid` FOREIGN KEY (`theater_id`) REFERENCES `theater` (`theater_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `play_info`
--

LOCK TABLES `play_info` WRITE;
/*!40000 ALTER TABLE `play_info` DISABLE KEYS */;
INSERT INTO `play_info` VALUES (1,5,10,'2024-09-19','11:10:00','12:54:00'),(2,31,9,'2024-09-20','11:10:00','13:18:00'),(3,3,7,'2024-09-21','01:12:00','03:10:00'),(4,3,7,'2024-09-21','03:31:00','05:29:00'),(43,3,8,'2024-09-23','11:20:00','13:18:00'),(44,20,5,'2024-09-22','01:17:00','02:51:00'),(45,31,9,'2024-09-23','12:12:00','14:20:00'),(46,20,4,'2024-09-24','08:22:00','09:56:00'),(49,31,9,'2024-09-25','11:00:00','13:08:00'),(51,5,5,'2024-09-25','01:28:00','03:12:00'),(52,5,4,'2024-09-25','11:25:00','13:09:00'),(53,3,1,'2024-09-25','08:22:00','10:20:00'),(54,3,2,'2024-09-25','08:22:00','10:20:00'),(55,3,3,'2024-09-25','11:11:00','13:09:00');
/*!40000 ALTER TABLE `play_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-25 13:59:24
