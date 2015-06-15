-- phpMyAdmin SQL Dump
-- version 4.2.12
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 21, 2014 at 02:32 PM
-- Server version: 10.0.14-MariaDB-log
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `fortissTest`
--

-- --------------------------------------------------------

--
-- Table structure for table `ContainerManager_Devices`
--

CREATE TABLE IF NOT EXISTS `ContainerManager_Specs` (
  `DevId` varchar(256) NOT NULL,
  `DeviceType` varchar(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `SMGDeviceType` varchar(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `AllowedUserProfile` int(11) NOT NULL DEFAULT '0',
  `MinUpdateRate` double NOT NULL,
  `MaxUpdateRate` double NOT NULL,
  `AcceptsCommands` int(11) NOT NULL DEFAULT '0',
  `HasValue` int(11) NOT NULL DEFAULT '0',
  `RangeMin` double NOT NULL,
  `RangeMax` double NOT NULL,
  `RangeStep` double NOT NULL,
  `CommandMinRange` double NOT NULL DEFAULT '-1',
  `CommandMaxRange` double NOT NULL DEFAULT '-1',
  `CommandRangeStep` double NOT NULL DEFAULT '-1',
  `CommandRangeStepType` varchar(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'NONE',
  `HumanReadableName` varchar(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Description` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ContainerManager_Devices`
--
ALTER TABLE `ContainerManager_Specs`
 ADD PRIMARY KEY (`DevId`), ADD UNIQUE KEY `DevId` (`DevId`), ADD KEY `DeviceType` (`DeviceType`), ADD KEY `SMGDeviceType` (`SMGDeviceType`), ADD KEY `AllowedUserProfile` (`AllowedUserProfile`);

