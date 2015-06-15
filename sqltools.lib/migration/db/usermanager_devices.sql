-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 15. Mai 2015 um 11:00
-- Server Version: 5.5.32
-- PHP-Version: 5.4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `fortisstest`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `usermanager_devices`
--

CREATE TABLE IF NOT EXISTS `usermanager_devices` (
  `DeviceID` int(255) NOT NULL,
  `UserID` int(255) NOT NULL,
  `DeviceName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OS` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MACAddress` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PrimaryDevice` int(11) DEFAULT '0',
  `publicKey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `privateKey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LastSeen` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DeviceID`,`UserID`),
  UNIQUE KEY `DeviceID` (`DeviceID`),
  UNIQUE KEY `publicKey` (`publicKey`),
  KEY `publicKey_2` (`publicKey`),
  KEY `UserID` (`UserID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
