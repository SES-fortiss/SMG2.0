-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 23. Mrz 2015 um 14:11
-- Server Version: 5.6.16
-- PHP-Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `fortiss2`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `gamificationmanager_end_users`
--

DROP TABLE IF EXISTS `gamificationmanager_end_users`;
CREATE TABLE IF NOT EXISTS `gamificationmanager_end_users` (
  `userManagerID` int(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `level` int(255) NOT NULL,
  `score` int(255) NOT NULL,
  `hexabusID` varchar(100) NOT NULL,
  `consumptionID` varchar(50) NOT NULL,
  PRIMARY KEY (`userManagerID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `gamificationmanager_groups`
--

DROP TABLE IF EXISTS `gamificationmanager_groups`;
CREATE TABLE IF NOT EXISTS `gamificationmanager_groups` (
  `id` int(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `level` int(255) NOT NULL,
  `score` int(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `gamificationmanager_group_memberships`
--

DROP TABLE IF EXISTS `gamificationmanager_group_memberships`;
CREATE TABLE IF NOT EXISTS `gamificationmanager_group_memberships` (
  `childID` int(255) NOT NULL,
  `parentID` int(255) NOT NULL,
  `childIsGroup` int(5) NOT NULL,
  PRIMARY KEY (`childID`,`childIsGroup`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
