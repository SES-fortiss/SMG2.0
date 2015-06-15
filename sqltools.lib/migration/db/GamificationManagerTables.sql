-- phpMyAdmin SQL Dump
-- version 4.0.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 13, 2015 at 08:48 AM
-- Server version: 5.5.40-log
-- PHP Version: 5.5.20-pl0-gentoo

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
-- Table structure for table `GamificationManager_End_Users`
--

DROP TABLE IF EXISTS `GamificationManager_End_Users`;
CREATE TABLE IF NOT EXISTS `GamificationManager_End_Users` (
  `userManagerID` int(255) NOT NULL,
  `name` varchar(50) CHARACTER SET latin1 NOT NULL,
  `level` int(255) NOT NULL,
  `score` int(255) NOT NULL,
  `hexabusID` varchar(100) CHARACTER SET latin1 NOT NULL,
  `consumptionID` varchar(50) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`userManagerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `GamificationManager_Groups`
--

DROP TABLE IF EXISTS `GamificationManager_Groups`;
CREATE TABLE IF NOT EXISTS `GamificationManager_Groups` (
  `id` int(255) NOT NULL,
  `name` varchar(50) CHARACTER SET latin1 NOT NULL,
  `level` int(255) NOT NULL,
  `score` int(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `GamificationManager_Group_Memberships`
--

DROP TABLE IF EXISTS `GamificationManager_Group_Memberships`;
CREATE TABLE IF NOT EXISTS `GamificationManager_Group_Memberships` (
  `childID` int(255) NOT NULL,
  `parentID` int(255) NOT NULL,
  `childIsGroup` int(5) NOT NULL,
  PRIMARY KEY (`childID`,`childIsGroup`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
