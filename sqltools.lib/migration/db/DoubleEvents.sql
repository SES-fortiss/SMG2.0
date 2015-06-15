-- phpMyAdmin SQL Dump
-- version 4.0.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 05, 2014 at 11:56 AM
-- Server version: 5.5.39-log
-- PHP Version: 5.5.16-pl0-gentoo

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `fortiss2`
--

-- --------------------------------------------------------

--
-- Table structure for table `DoubleEvents`
--

DROP TABLE IF EXISTS `DoubleEvents`;
CREATE TABLE IF NOT EXISTS `DoubleEvents` (
  `devid` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `wrapperid` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `value` double NOT NULL,
  `maxAbsError` double NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  UNIQUE KEY `uniq` (`devid`,`wrapperid`,`timestamp`),
  KEY `time` (`timestamp`),
  KEY `devid` (`devid`,`wrapperid`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
