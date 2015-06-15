-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 11, 2014 at 03:54 PM
-- Server version: 5.6.17-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.3

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
-- Table structure for table `AppKeys`
--

DROP TABLE IF EXISTS `AppKeys`;
CREATE TABLE IF NOT EXISTS `AppKeys` (
  `publicKey` varchar(255) NOT NULL,
  `privateKey` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `devId` varchar(80) DEFAULT NULL,
  `lastSeen` datetime DEFAULT NULL,
  `devName` varchar(255) DEFAULT NULL,
  `os` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`publicKey`),
  UNIQUE KEY `publicKey` (`publicKey`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `AppKeys`
--

INSERT INTO `AppKeys` (`publicKey`, `privateKey`, `userId`, `devId`, `lastSeen`, `devName`, `os`) VALUES
('68a45057-5734-4dad-9f86-ab9e32c4506e', 'jg9e65dui5272c45uds3qrf3b8gc71crjq4raq43', 5, '123456789', '2014-01-13 16:26:30', 'Unit-Testing', '');

-- --------------------------------------------------------

--
-- Table structure for table `UserManager_Contacts`
--

DROP TABLE IF EXISTS `UserManager_Contacts`;
CREATE TABLE IF NOT EXISTS `UserManager_Contacts` (
  `UserID` int(255) NOT NULL,
  `EMail` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PrimaryContactInfo` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`UserID`),
  KEY `UserID` (`UserID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `UserManager_Contacts`
--

INSERT INTO `UserManager_Contacts` (`UserID`, `EMail`, `Phone`, `PrimaryContactInfo`) VALUES
(1, 'duchon@fortiss.org', '004989360352230', ''),
(13, 'testetestetstet@test.de', '00000000000', '123456');

-- --------------------------------------------------------

--
-- Table structure for table `UserManager_Devices`
--

DROP TABLE IF EXISTS `UserManager_Devices`;
CREATE TABLE IF NOT EXISTS `UserManager_Devices` (
  `DeviceID` int(255) NOT NULL AUTO_INCREMENT,
  `UserID` int(255) NOT NULL,
  `DeviceName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `OS` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `MACAddress` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `PrimaryDevice` int(11) NOT NULL DEFAULT '0',
  `publicKey` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `privateKey` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `LastSeen` datetime NOT NULL,
  PRIMARY KEY (`DeviceID`),
  UNIQUE KEY `publicKey` (`publicKey`),
  KEY `publicKey_2` (`publicKey`),
  KEY `UserID` (`UserID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=123456790 ;

--
-- Dumping data for table `UserManager_Devices`
--

INSERT INTO `UserManager_Devices` (`DeviceID`, `UserID`, `DeviceName`, `OS`, `MACAddress`, `PrimaryDevice`, `publicKey`, `privateKey`, `LastSeen`) VALUES
(123456789, 2, 'Unit-Testing', '', '000111222', 0, '68a45057-5734-4dad-9f86-ab9e32c4506e', 'jg9e65dui5272c45uds3qrf3b8gc71crjq4raq43', '2014-03-14 14:05:55'),
(111000, 0, '', '', '', 0, '', '', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `UserManager_Profiles`
--

DROP TABLE IF EXISTS `UserManager_Profiles`;
CREATE TABLE IF NOT EXISTS `UserManager_Profiles` (
  `ProfileName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `UserID` int(255) NOT NULL,
  `ProfileItem` int(11) NOT NULL,
  `ProfileValue` double NOT NULL,
  PRIMARY KEY (`ProfileName`,`UserID`,`ProfileItem`),
  KEY `profileIndex` (`ProfileName`,`UserID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `UserManager_Profiles`
--

INSERT INTO `UserManager_Profiles` (`ProfileName`, `UserID`, `ProfileItem`, `ProfileValue`) VALUES
('summer', 14, 152, 0),
('summer', 14, 136, 9.1),
('summer', 14, 5, 44.23),
('summer', 14, 12, 40.3),
('summer', 14, 13, 76.2),
('summer', 14, 151, 2),
('summer', 22, 152, 0),
('summer', 22, 136, 7),
('summer', 22, 5, 55.1),
('summer', 22, 12, 10),
('summer', 22, 13, 25),
('summer', 22, 151, 1),
('winter', 32, 152, 0),
('winter', 32, 136, 7),
('winter', 32, 5, 55.1),
('winter', 32, 12, 10),
('winter', 32, 13, 25),
('winter', 32, 151, 1),
('test', 18, 13, 18),
('test', 18, 12, 65),
('test', 18, 5, 400);

-- --------------------------------------------------------

--
-- Table structure for table `UserManager_Role`
--

DROP TABLE IF EXISTS `UserManager_Role`;
CREATE TABLE IF NOT EXISTS `UserManager_Role` (
  `RoleID` int(255) NOT NULL AUTO_INCREMENT,
  `RoleName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Permission` varchar(512) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`RoleID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=8 ;

--
-- Dumping data for table `UserManager_Role`
--

INSERT INTO `UserManager_Role` (`RoleID`, `RoleName`, `Permission`) VALUES
(1, 'God', ''),
(2, 'SystemAdministrator', ''),
(3, 'RoleAdministrator', ''),
(4, 'RuleAdministrator', ''),
(5, 'FacilityManager', ''),
(6, 'Employee', ''),
(7, 'Guest', '');

-- --------------------------------------------------------

--
-- Table structure for table `UserManager_Rooms`
--

DROP TABLE IF EXISTS `UserManager_Rooms`;
CREATE TABLE IF NOT EXISTS `UserManager_Rooms` (
  `ContainerID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `UserID` int(255) NOT NULL,
  `RoleID` int(255) NOT NULL DEFAULT '7',
  `CurrentLocation` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ContainerID`,`UserID`),
  KEY `UserID` (`UserID`),
  KEY `UserID_2` (`UserID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `UserManager_Rooms`
--

INSERT INTO `UserManager_Rooms` (`ContainerID`, `UserID`, `RoleID`, `CurrentLocation`) VALUES
('10', 1, 7, 0),
('1', 1, 7, 0),
('13', 16, 7, 0),
('13', 18, 7, 1),
('11', 18, 7, 1),
('testroomId', 18, 7, 0);

-- --------------------------------------------------------

--
-- Table structure for table `UserManager_Users`
--

DROP TABLE IF EXISTS `UserManager_Users`;
CREATE TABLE IF NOT EXISTS `UserManager_Users` (
  `UserID` int(255) NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(512) COLLATE utf8_unicode_ci NOT NULL,
  `Nickname` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `RoleID` int(255) NOT NULL,
  `LoggedIn` tinyint(4) NOT NULL,
  `ActiveProfile` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LastSeen` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `UserID` (`UserID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=19 ;

--
-- Dumping data for table `UserManager_Users`
--

INSERT INTO `UserManager_Users` (`UserID`, `Username`, `Password`, `Nickname`, `RoleID`, `LoggedIn`, `ActiveProfile`, `LastSeen`) VALUES
(1, 'user', '*94BDCEBE19083CE2A1F959FD02F964C7AF4CFC29', 'Testuser', 6, 1, 'spring', '2014-08-26 12:03:54'),
(14, 'test000', '$2a$10$FXL79zhQsxP1q0TlHRgGzeROeY/sktiOJTB7GiPsoGPjgzKiN0rgq', 'fisrtTest', 7, 1, 'summer', '2014-08-19 11:47:57'),
(13, 'test213', '$2a$10$fVJ29OFGiBBKA02kP5gvc.vHMkj07yXUzWQ.rXXAQs8WB6YEs5T4e', 'test1', 3, 0, 'summer', '2014-08-21 09:02:46'),
(12, 'test21', '$2a$10$gdzCbQf0ZtgTyTVzgJ9kQuYSISrDyFVoS7W0YWd2pTBU7U1UnGG6S', 'test1', 3, 1, '', '0000-00-00 00:00:00'),
(11, 'test1', '$2a$10$aULPA8a03cvOyJOuC7z3fO9qhPQJ/3d6p.Gy4gB7szfKG3e3N4Fka', 'test1', 3, 1, 'winter', '2014-08-07 14:35:58'),
(15, 'andi', 'e60fbddd86e545e6f7084f9c44e4cb508f09abce', 'andi', 6, 0, NULL, '2014-11-13 13:39:45'),
(16, 'fortiss', '*0BEEC7B5EA3F0FDBC95D0DD47F3C5BC275DA8A33', 'fortiss', 6, 0, NULL, '2014-11-13 09:20:28'),
(18, 'peter2', 'fd4cef7a4e607f1fcc92ad6329a6df2df99a4e8', 'peter2', 1, 0, 'test', '2014-12-02 10:39:44');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
