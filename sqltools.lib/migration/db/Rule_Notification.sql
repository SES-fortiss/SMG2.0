-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 30, 2015 at 11:53 AM
-- Server version: 5.6.21
-- PHP Version: 5.5.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `fortissTest`
--

-- --------------------------------------------------------

--
-- Table structure for table `Rule_Notification`
--

CREATE TABLE IF NOT EXISTS `Rule_Notification` (
`NotifyID` int(50) NOT NULL,
  `RuleID` int(50) NOT NULL,
  `NotifyType` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Content` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Rule_Notification`
--

INSERT INTO `Rule_Notification` (`NotifyID`, `RuleID`, `NotifyType`, `Content`) VALUES
(1, 1, 'Mail', 'Mail content '),
(2, 1, 'SMS', ' SMS content '),
(3, 1, 'Tweet', ' Tweet content ');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Rule_Notification`
--
ALTER TABLE `Rule_Notification`
 ADD PRIMARY KEY (`NotifyID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Rule_Notification`
--
ALTER TABLE `Rule_Notification`
MODIFY `NotifyID` int(50) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
