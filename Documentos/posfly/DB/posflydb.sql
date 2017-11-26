-- MySQL dump 10.14  Distrib 5.5.56-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: posflydb
-- ------------------------------------------------------
-- Server version	5.5.56-MariaDB

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
-- Table structure for table `evento`
--

DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento` (
  `nombre` varchar(30) NOT NULL,
  `lugar` varchar(30) NOT NULL,
  `fecha` varchar(10) NOT NULL,
  `hora` varchar(10) NOT NULL,
  `descripcion` varchar(120) NOT NULL,
  `precio` varchar(6) NOT NULL,
  `id_usuario` varchar(15) NOT NULL,
  `calificacion` varchar(5) NOT NULL,
  PRIMARY KEY (`nombre`),
  KEY `fk_user` (`id_usuario`),
  CONSTRAINT `evento_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evento`
--

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
INSERT INTO `evento` VALUES ('Evento De Cuenta','Poli','24/11/17','16:18','Este evento es creado en video','2000.','Cuenta1','0'),('Evento Dos','Poli','23/11/17','21:05','Este es el segundo evento','3000','idprueba','0'),('Evento Uno Editado','Poli','27/11/17','19:03','Este es el primer evento modificado','2000','idprueba','0');
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forgot`
--

DROP TABLE IF EXISTS `forgot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forgot` (
  `correo` varchar(40) NOT NULL,
  `codigo` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forgot`
--

LOCK TABLES `forgot` WRITE;
/*!40000 ALTER TABLE `forgot` DISABLE KEYS */;
INSERT INTO `forgot` VALUES ('gabo05640@hotmail.com','FKS5sN'),('gabo05640@hotmail.com','zF3Ut9'),('gabo05640@hotmail.com','bPOQ3W'),('gabo05640@hotmail.com','nGJcXm'),('gabo05640@hotmail.com','wEyZa0'),('gabo05640@hotmail.com','mY7d39'),('gabo05640@hotmail.com','4nvcRo'),('gabo05640@hotmail.com','cTzuEa'),('gabo05640@hotmail.com','KOytwB'),('gabo05640@hotmail.com','DwEeBZ'),('gabo05640@hotmail.com','nDYVzy'),('gabo05640@hotmail.com','G1YVOa'),('gabo05640@hotmail.com','71mWrs'),('gabo05640@hotmail.com','ZR8eHg'),('gabo05640@hotmail.com','ftByqs'),('gabo05640@hotmail.com','9Zmx7F'),('gabo05640@hotmail.com','P6Fsnb'),('gabo05640@hotmail.com','Ouebi3'),('gabo05640@hotmail.com','VnRCmJ'),('gabo05640@hotmail.com','craXmL'),('gabo05640@hotmail.com','SWYtXR'),('gabo05640@hotmail.com','jCJNV0'),('gabo05640@hotmail.com','yoHujN'),('gabo05640@hotmail.com','A1H9Cz'),('gabo05640@hotmail.com','wS6CpM');
/*!40000 ALTER TABLE `forgot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `museo`
--

DROP TABLE IF EXISTS `museo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `museo` (
  `id_museo` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `id_usuario` varchar(15) NOT NULL,
  `descripcion` varchar(140) NOT NULL,
  `fecha` varchar(10) NOT NULL,
  `likes` int(5) NOT NULL,
  `unlikes` int(5) NOT NULL,
  `anonimo` varchar(5) NOT NULL,
  PRIMARY KEY (`id_museo`),
  KEY `fk_userm` (`id_usuario`),
  CONSTRAINT `museo_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `museo`
--

LOCK TABLES `museo` WRITE;
/*!40000 ALTER TABLE `museo` DISABLE KEYS */;
INSERT INTO `museo` VALUES (11,'Anécdota Dos','idprueba','Esta es la segunda anécdota','22/11/17',0,0,'false'),(12,'Anécdota Tres','idprueba','Es la tercera anécdota','08/11/17',0,0,'true'),(13,'Otra Anecdota','Cuenta1','Descripción de  anecodta','02/11/17',0,0,'true');
/*!40000 ALTER TABLE `museo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id_usuario` varchar(15) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `apellido` varchar(30) NOT NULL,
  `correo` varchar(40) NOT NULL,
  `pass` varchar(40) NOT NULL,
  `rol` varchar(5) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('cuenta1','Cuenta2','Perezzz','cuentaghghh@hotmail.com','f1887d3f9e6ee7a32fe5e76f4ab80d63','user'),('gsalvarez','GabrielSantiago','Álvarez','gabo0b@hotmail.com','e10adc3949ba59abbe56e057f20f883e','admin'),('idcuenta','Cuenta','Perez','idcuentas@hotmail.com','f1887d3f9e6ee7a32fe5e76f4ab80d63','user'),('idprueba','Prueba','Perez','prueba@probando.com','c893bad68927b457dbed39460e6afd62','user');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-25 19:34:25
