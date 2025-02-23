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
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `res_id` int NOT NULL AUTO_INCREMENT,
  `movie_name` varchar(45) NOT NULL,
  `rating` varchar(45) DEFAULT NULL,
  `movie_type` varchar(45) NOT NULL,
  `movie_runtime` varchar(45) NOT NULL,
  `movie_date` date NOT NULL,
  `movie_start` time NOT NULL,
  `movie_end` time NOT NULL,
  `movie_theater` varchar(50) NOT NULL,
  `seats` varchar(200) NOT NULL,
  `people` varchar(45) DEFAULT NULL,
  `resNumber` varchar(50) DEFAULT NULL,
  `totalprice` int DEFAULT NULL,
  `usePoint` int DEFAULT NULL,
  `savePoint` int DEFAULT NULL,
  `phone` char(13) DEFAULT NULL,
  `status` varchar(45) DEFAULT 'available',
  PRIMARY KEY (`res_id`),
  KEY `user_idx` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,'트랜스포머 ONE','all','IMAX','104','2024-09-19','11:10:00','12:54:00','IMAX관','B2, A3, A1','일반 1, 청소년 2','4247141563424707',86000,NULL,NULL,'010-1234-5678','available'),(2,'베테랑2','15','2D','118','2024-09-19','11:10:00','13:08:00','3관 1층','B2, A2, A1, B1, B3','일반 1, 청소년 2, 경로 2','1816285951461214',44000,NULL,NULL,'010-1234-1234','available'),(3,'탈주','12','3D','94','2024-09-20','11:22:00','12:56:00','4관 2층','A4, B4, B3','일반 1, 청소년 2','5646522704835925',39000,NULL,NULL,'010-1111-2222','available'),(10,'베테랑2','15','4D','118','2024-09-21','01:12:00','03:10:00','7관 5층','A2, A3','일반 2','9341429530149286',40000,NULL,NULL,'010-1234-5678','available'),(11,'베테랑2','15','4D','118','2024-09-21','03:31:00','05:29:00','7관 5층','A5, A3, A4','청소년 3','1231794178698570',54000,NULL,NULL,NULL,'available'),(12,'베테랑2','15','4D','118','2024-09-23','11:20:00','13:18:00','특별관(4D)','A1','청소년 1','5628250171014009',18000,NULL,NULL,NULL,'available'),(13,'베테랑2','15','4D','118','2024-09-23','11:20:00','13:18:00','특별관(4D)','A2','일반 1','9935895502182135',20000,NULL,NULL,NULL,'available'),(14,'탈주','12','3D','94','2024-09-24','08:22:00','09:56:00','4관 2층','A1, A2','일반 2','9289004490874572',30000,NULL,NULL,NULL,'available'),(15,'탈주','12','3D','94','2024-09-24','08:22:00','09:56:00','4관 2층','B3','일반 1','9615974979063487',15000,NULL,NULL,NULL,'available'),(16,'탈주','12','3D','94','2024-09-24','08:22:00','09:56:00','4관 2층','A4','일반 1','2288289865838458',15000,NULL,NULL,NULL,'available'),(17,'데드풀과 울버린','19','4DX','128','2024-09-25','11:00:00','13:08:00','특별관(4DX)','D6, D5, D7','일반 3','8004776882299579',75000,NULL,NULL,NULL,'available'),(18,'사랑의 하츄핑','all','4DX','86','2024-09-24','14:00:00','15:26:00','특별관(4DX)','D5, D7, D6','일반 3','6257883336918903',75000,NULL,NULL,NULL,'available'),(19,'베테랑2','15','4D','118','2024-09-24','11:20:00','13:18:00','7관 5층','E8, E7','일반 2','5127576395484618',40000,NULL,NULL,NULL,'available'),(20,'탈주','12','3D','94','2024-09-24','08:22:00','09:56:00','4관 2층','A3','일반 1','8866539605148476',15000,NULL,NULL,NULL,'available'),(21,'트랜스포머 ONE','all','3D','104','2024-09-25','11:25:00','13:09:00','4관 2층','B3, C4, C3','일반 2, 청소년 1','9523926921502280',42000,NULL,NULL,NULL,'available'),(22,'베테랑2','15','2D','118','2024-09-25','08:22:00','10:20:00','1관 1층','A1','일반 1','8602186055674089',10000,NULL,NULL,NULL,'available'),(23,'데드풀과 울버린','19','4DX','128','2024-09-25','11:00:00','13:08:00','특별관(4DX)','A1','일반 1','7808459374747476',25000,NULL,NULL,NULL,'available'),(24,'베테랑2','15','2D','118','2024-09-25','11:11:00','13:09:00','3관 1층','A5','일반 1','3934538178287831',10000,NULL,NULL,NULL,'available');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
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
