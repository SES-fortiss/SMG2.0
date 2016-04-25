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
-- Table structure for table `Rule_Command`
--

CREATE TABLE IF NOT EXISTS `Rule_Command` (
`CommandID` int(50) NOT NULL,
  `RuleID` int(50) NOT NULL,
  `Content` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Rule_Command`
--

INSERT INTO `Rule_Command` (`CommandID`, `RuleID`, `Content`) VALUES
(1, 2, '"sendCommand(new DoubleCommand(10.0) ,new DeviceId("dev", "dummy"));"');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Rule_Command`
--
ALTER TABLE `Rule_Command`
 ADD PRIMARY KEY (`CommandID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Rule_Command`
--
ALTER TABLE `Rule_Command`
MODIFY `CommandID` int(50) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
