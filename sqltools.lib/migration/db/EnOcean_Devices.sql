-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 15. Mai 2015 um 15:08
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
-- Tabellenstruktur für Tabelle `enocean_devices`
--

CREATE TABLE IF NOT EXISTS `enocean_devices` (
  `uniqueID` int(11) NOT NULL AUTO_INCREMENT,
  `enoceanID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `Channel` int(11) NOT NULL,
  `Strategy` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `HRName` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `DeviceCode` int(11) NOT NULL,
  PRIMARY KEY (`uniqueID`),
  UNIQUE KEY `uniqueID` (`uniqueID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=52 ;

--
-- Daten für Tabelle `enocean_devices`
--

INSERT INTO `enocean_devices` (`uniqueID`, `enoceanID`, `Channel`, `Strategy`, `HRName`, `DeviceCode`) VALUES
(1, '001E4E19', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', 'Dijkstra_Halogen_Lampe', 5),
(2, '0100627F', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTKSensorStrategy', 'Left_Window_Room225', 151),
(3, '01007D14', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTKSensorStrategy', 'Right_Window_Room225', 151),
(4, '0003091A', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FBH55SensorStrategy', 'Room225_Britness', 5),
(6, '010066F4', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTR55DSensorStrategy', '', 13),
(7, '01006934', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTKSensorStrategy', '', 151),
(8, '01003F84', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTKSensorStrategy', '', 151),
(9, '001E4DE8', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', '', 5),
(10, '001E4DA8', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4SingleRockerStrategy', '', 5),
(11, '001E4DD8', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4SingleRockerStrategy', '', 5),
(12, '001E64E6', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', '', 5),
(13, '0021EB81', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', '', 5),
(14, '000321E6', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FAH60SensorStrategy', '', 5),
(15, '01003E48', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTR55DSensorStrategy', '', 13),
(16, '0003B078', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.BSCsOTSSensorStrategy', '', 13),
(17, '008758B7', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FWZ12SensorStrategy', '', 101),
(18, '00019005', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTKSensorStrategy', '', 101),
(19, '001E6596', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', '', 5),
(20, '001E6536', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', '', 5),
(21, '001E4DD0', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', '', 5),
(22, '0001860B', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTKSensorStrategy', '', 151),
(23, '01035241', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', '', 5),
(24, '010088C8', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.BSCsOTSSensorStrategy', '', 13),
(25, '0021EB8D', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', '', 5),
(26, '0087A835', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FWZ12SensorStrategy', '', 101),
(27, '0087DFF7', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FWZ12SensorStrategy', '', 101),
(28, '0087D5F9', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FWZ12SensorStrategy', '', 101),
(29, '01008831', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTR55DSensorStrategy', '', 13),
(30, '', 1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light1030ActorStrategy', 'office1030light', 5),
(31, '', 1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light5070ActorStrategy', 'office5070light', 5),
(32, '', 4, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.FortissBlindActorStrategy', 'meetingroomblinds', 136),
(33, '', 0, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light1030ActorStrategy', 'meetingroom1030light', 5),
(34, '', 0, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light5070ActorStrategy', 'meetingroom5070light', 5),
(35, '', 12, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.HeatingActorStrategy', 'heating_ch12', 132),
(36, '', 5, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.FortissBlindActorStrategy', 'officeblinds', 136),
(37, '', 3, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.SteckdosenleisteActorStrategy', 'Steckdosenleiste', 138),
(38, '', 8, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.SteckdosenleisteActorStrategy', 'PowerPlug', 138),
(39, '', 99, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.GenericPushRelease1030SwitchActorStrategy', 'test3', 145),
(40, '', 0, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.GenericPushRelease1030SwitchActorStrategy', 'test2', 145),
(41, '', 13, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light5070ActorStrategy', 'MeetingRoom-224_AC', 5),
(42, '', 100, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light1030ActorStrategy', 'testStrategy', 5),
(43, '', 98, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.SteckdosenleisteActorStrategy', 'testmitsteckdosenstrategy', 138),
(44, '', 0, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light1030ActorStrategy', 'testmit5070', 5),
(45, '', 101, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light5070ActorStrategy', 'testmit5070nochmal', 5),
(46, '', 102, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.GenericPushRelease1030SwitchActorStrategy', 'test4', 145),
(47, '0100E48', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTR55DSensorStrategy', 'Night_Reduction', 5),
(48, '1E657C', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', 'Halogeon/Fluorescent_Lamps_Dijkstra', 5),
(50, '1E656C', -1, 'org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy', 'Blinds_Dijkstra', 136);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
