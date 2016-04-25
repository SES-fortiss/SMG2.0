-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 02. Mai 2015 um 21:13
-- Server Version: 5.6.16
-- PHP-Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `django`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `widgets_widgetdata`
--

CREATE TABLE IF NOT EXISTS `widgets_widgetdata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount_co2` varchar(5) DEFAULT NULL,
  `amount_own` varchar(5) DEFAULT NULL,
  `timestamp` datetime NOT NULL,
  `price_today` decimal(5,2) NOT NULL,
  `price_tomorrow` decimal(5,2) NOT NULL,
  `energy_saver_of_the_week_first_name` varchar(30) NOT NULL,
  `energy_saver_of_the_week_last_name` varchar(30) NOT NULL,
  `energy_saver_of_the_month_first_name` varchar(30) NOT NULL,
  `energy_saver_of_the_month_last_name` varchar(30) NOT NULL,
  `battery_charging` tinyint(1) NOT NULL,
  `battery_used` tinyint(1) NOT NULL,
  `battery_stored` int(11) NOT NULL,
  `battery_loaded` int(11) NOT NULL,
  `laptop` int(11) NOT NULL,
  `earth` int(11) NOT NULL,
  `trees` int(11) NOT NULL,
  `low_consumption` int(11) NOT NULL,
  `high_consumption` int(11) NOT NULL,
  `yest_consumption` int(11) NOT NULL,
  `trend` int(11) NOT NULL,
  `streetlight` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Daten für Tabelle `widgets_widgetdata`
--

INSERT INTO `widgets_widgetdata` (`id`, `amount_co2`, `amount_own`, `timestamp`, `price_today`, `price_tomorrow`, `energy_saver_of_the_week_first_name`, `energy_saver_of_the_week_last_name`, `energy_saver_of_the_month_first_name`, `energy_saver_of_the_month_last_name`, `battery_charging`, `battery_used`, `battery_stored`, `battery_loaded`, `laptop`, `earth`, `trees`, `low_consumption`, `high_consumption`, `yest_consumption`, `trend`, `streetlight`) VALUES
(1, '12.66', '15', '2015-02-24 15:31:26', '24.91', '25.09', 'Mr. Weeky', 'Week', 'Mrs. Monthy', 'Month', 0, 0, 6720, 100, 4, 0, 13, 50, 300, 144, 1, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
