-- phpMyAdmin SQL Dump
-- version 4.2.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Feb 18, 2015 at 11:18 AM
-- Server version: 5.5.38
-- PHP Version: 5.6.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `fortiss2`
--

-- --------------------------------------------------------

--
-- Table structure for table `Optimizer_Exchanges`
--

CREATE TABLE `Optimizer_Exchanges` (
  `value` double NOT NULL,
  `sequence` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `executetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Optimizer_Intervals`
--

CREATE TABLE `Optimizer_Intervals` (
  `date` datetime NOT NULL,
  `duration` double NOT NULL,
  `price` double NOT NULL,
  `consumption` double NOT NULL,
  `generation` double NOT NULL,
  `fromGrid` double NOT NULL,
  `toGrid` double NOT NULL,
  `charge` double NOT NULL,
  `discharge` double NOT NULL,
  `wastage` double NOT NULL,
  `demand` double NOT NULL,
  `supply` double NOT NULL,
  `capacity` double NOT NULL,
  `executetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Optimizer_Paths`
--

CREATE TABLE `Optimizer_Paths` (
  `chosen` int(11) NOT NULL,
  `exchange` int(11) NOT NULL,
  `lastDemand` int(11) NOT NULL,
  `lastSupply` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `sequence` int(11) NOT NULL,
  `executetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Optimizer_Periods`
--

CREATE TABLE `Optimizer_Periods` (
  `price` double NOT NULL,
  `possibleDemand` double NOT NULL,
  `leastSupply` double NOT NULL,
  `maximumSupply` double NOT NULL,
  `expectedStorage` double NOT NULL,
  `neededStorage` double NOT NULL,
  `exchange` double NOT NULL,
  `first` int(11) NOT NULL,
  `last` int(11) NOT NULL,
  `sequence` int(11) NOT NULL,
  `executetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Optimizer_Pools`
--

CREATE TABLE `Optimizer_Pools` (
  `value` double NOT NULL,
  `sequence` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `executetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Optimizer_Specification`
--

CREATE TABLE `Optimizer_Specification` (
  `chargeEfficiency` double NOT NULL,
  `dischargeEfficiency` double NOT NULL,
  `maximumCapacity` double NOT NULL,
  `initialCapacity` double NOT NULL,
  `maximumLineLoad` double NOT NULL,
  `maximumPowerOutput` double NOT NULL,
  `maximumPowerInput` double NOT NULL,
  `possibleFromBattery` tinyint(1) NOT NULL,
  `possibleFromGeneration` tinyint(1) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Optimizer_Specification`
--

INSERT INTO `Optimizer_Specification` (`chargeEfficiency`, `dischargeEfficiency`, `maximumCapacity`, `initialCapacity`, `maximumLineLoad`, `maximumPowerOutput`, `maximumPowerInput`, `possibleFromBattery`, `possibleFromGeneration`, `timestamp`) VALUES
(0.95, 0.95, 20000, 3000, 20000, 20000, 20000, 0, 0, '2015-01-18 14:22:42');

-- --------------------------------------------------------

--
-- Table structure for table `Optimizer_Suggestion`
--

CREATE TABLE `Optimizer_Suggestion` (
`id` int(11) NOT NULL,
  `chargeEfficiency` double NOT NULL,
  `dischargeEfficiency` double NOT NULL,
  `maximumCapacity` double NOT NULL,
  `initialCapacity` double NOT NULL,
  `maximumLineLoad` double NOT NULL,
  `maximumPowerOutput` double NOT NULL,
  `maximumPowerInput` double NOT NULL,
  `possibleFromBattery` tinyint(1) NOT NULL,
  `possibleFromGeneration` tinyint(1) NOT NULL,
  `executetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Optimizer_Exchanges`
--
ALTER TABLE `Optimizer_Exchanges`
 ADD PRIMARY KEY (`sequence`,`type`,`executetime`);

--
-- Indexes for table `Optimizer_Intervals`
--
ALTER TABLE `Optimizer_Intervals`
 ADD PRIMARY KEY (`date`,`executetime`);

--
-- Indexes for table `Optimizer_Paths`
--
ALTER TABLE `Optimizer_Paths`
 ADD PRIMARY KEY (`sequence`,`executetime`);

--
-- Indexes for table `Optimizer_Periods`
--
ALTER TABLE `Optimizer_Periods`
 ADD PRIMARY KEY (`sequence`,`executetime`);

--
-- Indexes for table `Optimizer_Pools`
--
ALTER TABLE `Optimizer_Pools`
 ADD PRIMARY KEY (`sequence`,`type`,`executetime`);

--
-- Indexes for table `Optimizer_Suggestion`
--
ALTER TABLE `Optimizer_Suggestion`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Optimizer_Suggestion`
--
ALTER TABLE `Optimizer_Suggestion`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;