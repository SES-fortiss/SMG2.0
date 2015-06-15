-- phpMyAdmin SQL Dump
-- version 4.0.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 07, 2014 at 02:54 PM
-- Server version: 5.1.70-log
-- PHP Version: 5.5.10-pl0-gentoo

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
-- Table structure for table `ContainerManager_Devices`
--

CREATE TABLE IF NOT EXISTS `ContainerManager_Devices` (
  `DeviceID` int(11) NOT NULL,
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
  `Description` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`DeviceID`),
  UNIQUE KEY `DeviceID` (`DeviceID`),
  KEY `DeviceType` (`DeviceType`),
  KEY `SMGDeviceType` (`SMGDeviceType`),
  KEY `AllowedUserProfile` (`AllowedUserProfile`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ContainerManager_Devices`
--

INSERT INTO `ContainerManager_Devices` (`DeviceID`, `DeviceType`, `SMGDeviceType`, `AllowedUserProfile`, `MinUpdateRate`, `MaxUpdateRate`, `AcceptsCommands`, `HasValue`, `RangeMin`, `RangeMax`, `RangeStep`, `CommandMinRange`, `CommandMaxRange`, `CommandRangeStep`, `CommandRangeStepType`, `HumanReadableName`, `Description`) VALUES
(1, 'ACCELEROMETER', 'Accelerometer', 0, 1000, 3600000, 0, 0, 0, 50, 0.1, -1, -1, -1, 'NONE', '3-Axis Accelerometer', 'Measures the acceleration force in m/s^2 that is applied to a device on all three physical axes (x, y, and z), including the force of gravity.'),
(13, 'AMBIENT_TEMPERATURE', 'Temperature', 1, 1000, 3600000, 0, 0, -50, 100, 0.1, -1, -1, -1, 'NONE', 'Thermometer', 'Ambient temperature sensor in Celsius'),
(15, 'GAME_ROTATION_VECTOR', '', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', 'Uncalibrated rotation sensor', 'Identical to TYPE_ROTATION_VECTOR except that it doesn''t use the geomagnetic field. Therefore the Y axis doesn''t point north, but instead to some other reference, that reference is allowed to drift by the same order of magnitude as the gyroscope drift around the Z axis.'),
(20, 'GEOMAGNETIC_ROTATION_VECTOR', '', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', 'Geo-magnetic rotation sensor', 'Similar to TYPE_ROTATION_VECTOR, but using a magnetometer instead of using a gyroscope. This sensor uses lower power than the other rotation vectors, because it doesn''t use the gyroscope. However, it is more noisy and will work best outdoors.'),
(9, 'GRAVITY', 'Gravity', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', 'Gravity sensor', 'Gravity sensor'),
(10, 'LINEAR_LINEAR_ACCELERATION', 'Acceleration', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(2, 'MAGNETIC_FIELD', '', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(14, 'MAGNETIC_FIELD_UNCALIBRATED', 'MagneticField', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(6, 'PRESSURE', 'Pressure', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(8, 'PROXIMITY', 'Proximity', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(12, 'RELATIVE_HUMIDITY', 'Humidity', 1, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(11, 'ROTATION_VECTOR', '', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(17, 'SIGNIFICANT_MOTION', 'SignificantMotion', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(19, 'STEP_COUNTER', '', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(18, 'STEP_DETECTOR', '', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(5, 'LIGHT', 'Brightness', 1, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'LINEAR', '', ''),
(101, '', 'ConsumptionPowermeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(102, '', 'ConsumptionVoltmeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(103, '', 'ConsumptionAmperemeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(104, '', 'ConsumptionPowermeterAggregated', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(111, '', 'ProductionPowermeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(112, '', 'ProductionVoltmeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(113, '', 'ProductionAmperemeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(114, '', 'ProductionPowermeterAggregated', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(121, '', 'FeedPowerMeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(122, '', 'FeedVoltmeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(123, '', 'FeedAmperemeter', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(124, '', 'FeedPowerMeterAggregated', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(132, '', 'Heating', 0, 1000, 3600000, 1, 0, 0, 100, 1, 0, 100, 1, 'LINEAR', '', ''),
(135, '', 'Cooling', 0, 1000, 3600000, 1, 0, 0, 100, 1, 0, 100, 1, 'LINEAR', '', ''),
(136, '', 'Blinds', 1, 1000, 3600000, 1, 0, 0, 100, 1, 0, 100, 1, 'LINEAR', '', ''),
(137, '', 'Balance', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(138, '', 'Powerplug', 0, 1000, 3600000, 1, 0, 0, 1, 1, 0, 1, 1, 'LINEAR', '', ''),
(151, '', 'Window', 1, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(152, '', 'Door', 1, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(153, '', 'Occupancy', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(139, '', 'Noise', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(140, '', 'Battery', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(141, '', 'Calculator', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(142, '', 'Frequency', 0, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),
(143, '', 'LightSimple', 0, 1000, 3600000, 1, 0, 0, 1, 1, 0, 1, 1, 'LINEAR', '', ''),
(144, '', 'LightDimmable', 0, 1000, 3600000, 1, 0, 1, 100, 1, 0, 100, 1, 'LINEAR', '', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
