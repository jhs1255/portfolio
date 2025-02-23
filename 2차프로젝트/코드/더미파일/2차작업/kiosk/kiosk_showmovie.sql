CREATE DATABASE  IF NOT EXISTS `kiosk` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `kiosk`;
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
-- Table structure for table `showmovie`
--

DROP TABLE IF EXISTS `showmovie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `showmovie` (
  `id` int NOT NULL AUTO_INCREMENT,
  `movie_id` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `runtime` varchar(45) NOT NULL,
  `rating` varchar(45) NOT NULL,
  `movietype` varchar(45) NOT NULL,
  `poster` text,
  PRIMARY KEY (`id`),
  KEY `mname_idx` (`title`),
  KEY `resitmid_idx` (`movie_id`),
  CONSTRAINT `resitmid` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`movie_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `showmovie`
--

LOCK TABLES `showmovie` WRITE;
/*!40000 ALTER TABLE `showmovie` DISABLE KEYS */;
INSERT INTO `showmovie` VALUES (1,3,'베테랑2','118','15','2D','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png'),(2,3,'베테랑2','118','15','4D','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png'),(3,5,'트랜스포머 ONE','104','all','IMAX','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png'),(4,20,'탈주','94','12','3D','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png'),(5,31,'데드풀과 울버린','128','19','4DX','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png'),(6,5,'트랜스포머 ONE','104','all','3D','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png');
/*!40000 ALTER TABLE `showmovie` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-19  1:50:49
