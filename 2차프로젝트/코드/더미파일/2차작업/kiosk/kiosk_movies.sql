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
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
  `movie_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `poster` text,
  `runtime` int DEFAULT NULL,
  `rating` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`movie_id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (1,'우리가 끝이야','드라마,멜로/로맨스','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',130,'15'),(2,'수유천','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',111,'15'),(3,'베테랑2','액션,범죄','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',118,'15'),(4,'행복의 나라','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',124,'12'),(5,'트랜스포머 ONE','애니메이션,액션,어드벤처','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',104,'all'),(6,'안녕, 할부지','다큐멘터리,애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',95,'all'),(7,'브레드이발소: 빵스타의 탄생','애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',77,'all'),(8,'1923 간토대학살','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',117,'12'),(9,'조커: 폴리 아 되','범죄,드라마,뮤지컬','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',138,'15'),(10,'와일드 로봇','애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',102,'all'),(11,'필사의 추격','코미디,액션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',109,'15'),(12,'아침바다 갈매기는','드라마,가족','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',113,'12'),(13,'비틀쥬스 비틀쥬스','코미디,판타지,공포(호러)','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',105,'12'),(14,'마녀들의 카니발','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',82,'12'),(15,'해야 할 일','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',100,'12'),(16,'캐시 아웃','액션,범죄','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',90,'12'),(17,'문경','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',111,'12'),(18,'[마이 아티 필름] 온앤오프 : 뷰티풀 뷰티풀','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',45,'all'),(19,'빅토리','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',120,'12'),(20,'탈주','액션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',94,'12'),(21,'더 라이언: 사막의 생존자들','액션,스릴러','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',86,'15'),(22,'에이리언: 로물루스','공포(호러),SF','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',119,'15'),(23,'쥬라기캅스 극장판: 전설의 고대생물을 찾아라','애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',75,'all'),(24,'그리고 목련이 필때면','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',84,'12'),(25,'트위스터스','액션,어드벤처,드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',112,'12'),(26,'베베핀 플레이타임','애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',61,'all'),(27,'자유로운 사람은 혼자 남는다','미스터리,판타지','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',107,'15'),(28,'하이재킹','범죄,액션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',100,'12'),(29,'조선인 여공의 노래','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',83,'all'),(30,'사랑의 하츄핑','애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',86,'all'),(31,'데드풀과 울버린','액션,코미디,SF','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',128,'19'),(32,'땅에 쓰는 시','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',113,'all'),(33,'헬로카봇 올스타 스페셜','애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',67,'all'),(34,'공즉시색 3','멜로/로맨스,코미디','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',72,'19'),(35,'슈퍼배드 4','애니메이션,액션,어드벤처,코미디','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',94,'all'),(36,'더 납작 엎드릴게요','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',63,'all'),(37,'노무현과 바보들: 못다한 이야기','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',98,'12'),(38,'플라이 미 투 더 문','멜로/로맨스,코미디','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',132,'12'),(39,'막걸리가 알려줄거야 ','드라마,SF,코미디','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',91,'all'),(40,'정직한 사람들','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',78,'12'),(41,'다큐 황은정 : 스마트폰이 뭐길래','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',70,'12'),(42,'목화솜 피는 날','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',90,'12'),(43,'댓글부대','범죄,드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',109,'15'),(44,'도토리','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',74,'15'),(45,'인사이드 아웃 2','애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',96,'all'),(46,'감독판 김일성의 아이들','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',88,'12'),(47,'판문점','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',82,'12'),(48,'몽키맨','액션,스릴러','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',121,'19'),(49,'대치동 스캔들','드라마,멜로/로맨스','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',109,'15'),(50,'원더랜드','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',113,'12'),(51,'양치기','드라마,스릴러','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',94,'15'),(52,'나쁜 녀석들: 라이드 오어 다이','액션,코미디','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',115,'15'),(53,'소풍','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',114,'12'),(54,'범죄도시4','액션,범죄','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',109,'15'),(55,'다시 김대중-함께 합시다','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',84,'12'),(56,'1980','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',99,'12'),(57,'이프: 상상의 친구','코미디,드라마,가족','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',104,'all'),(58,'퓨리오사: 매드맥스 사가','액션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',148,'15'),(59,'스턴트맨','액션,코미디,멜로/로맨스','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',126,'15'),(60,'혹성탈출: 새로운 시대','액션,SF','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',145,'12'),(61,'챌린저스','드라마,멜로/로맨스','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',131,'15'),(62,'여행자의 필요','드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',90,'12'),(63,'바람의 세월','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',104,'12'),(64,'아서','어드벤처,드라마','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',107,'12'),(65,'8인의 용의자들','드라마,미스터리,범죄','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',105,'15'),(66,'쿵푸팬더4','애니메이션,액션,코미디','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',94,'all'),(67,'유미의 세포들 더 무비','애니메이션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',93,'all'),(68,'세월: 라이프 고즈 온','다큐멘터리','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',99,'all'),(69,'고질라 X 콩: 뉴 엠파이어','액션,어드벤처,SF','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',115,'12'),(70,'랜드 오브 배드','액션,전쟁','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',113,'15'),(71,'듄: 파트2','액션','C:/Users/HP/JAVA/kioskproject/src/fxml/image/movie.png',166,'12');
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
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
