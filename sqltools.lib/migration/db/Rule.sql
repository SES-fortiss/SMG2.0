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
-- Table structure for table `Rule`
--

CREATE TABLE IF NOT EXISTS `Rule` (
`RuleID` int(255) NOT NULL,
  `RuleName` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Cron` int(255) NOT NULL,
  `RuleCondition` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `UserID` int(255) NOT NULL,
  `ContainerID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `RuleType` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Rule`
--

INSERT INTO `Rule` (`RuleID`, `RuleName`, `Cron`, `RuleCondition`, `UserID`, `ContainerID`, `RuleType`) VALUES
(1, 'TestRule1', 1, ' condition ', 123, 'room225', 'notification'),
(2, 'TestRule2', 3, ' Boolean condition ', 789, 'room225', 'command');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Rule`
--
ALTER TABLE `Rule`
 ADD PRIMARY KEY (`RuleID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Rule`
--
ALTER TABLE `Rule`
MODIFY `RuleID` int(255) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
