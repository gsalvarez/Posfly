-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-11-2017 a las 23:34:20
-- Versión del servidor: 10.1.28-MariaDB
-- Versión de PHP: 7.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `dbposfly`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `evento`
--

CREATE TABLE `evento` (
  `id_evento` int(10) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `lugar` varchar(50) NOT NULL,
  `fecha` date NOT NULL,
  `hora` varchar(10) NOT NULL,
  `descripcion` varchar(120) NOT NULL,
  `precio` double NOT NULL,
  `id_usuario` varchar(15) NOT NULL,
  `calificacion` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `evento`
--

INSERT INTO `evento` (`id_evento`, `nombre`, `lugar`, `fecha`, `hora`, `descripcion`, `precio`, `id_usuario`, `calificacion`) VALUES
(1, 'Evento uno', 'Poli', '2017-10-28', '10:20', 'Este evento es de prueba', 2000, 'gsalvarez', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `museo`
--

CREATE TABLE `museo` (
  `id_museo` int(10) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `id_usuario` varchar(15) NOT NULL,
  `descripcion` varchar(120) NOT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `museo`
--

INSERT INTO `museo` (`id_museo`, `nombre`, `id_usuario`, `descripcion`, `fecha`) VALUES
(1, 'Museo uno', 'gsalvarez', 'Esta anecdota es prueba', '2017-10-10');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` varchar(15) NOT NULL,
  `pass` varchar(30) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `apellido` varchar(20) NOT NULL,
  `correo` varchar(25) NOT NULL,
  `rol` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `pass`, `nombre`, `apellido`, `correo`, `rol`) VALUES
('gsalvarez', '123456', 'Gabriel', 'Alvarez', 'gabo05640@hotmail.com', 'admin');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `evento`
--
ALTER TABLE `evento`
  ADD PRIMARY KEY (`id_evento`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `museo`
--
ALTER TABLE `museo`
  ADD PRIMARY KEY (`id_museo`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `evento`
--
ALTER TABLE `evento`
  ADD CONSTRAINT `id_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`);

--
-- Filtros para la tabla `museo`
--
ALTER TABLE `museo`
  ADD CONSTRAINT `id_usuario2` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
