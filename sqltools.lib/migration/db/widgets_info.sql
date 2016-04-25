-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 02. Mai 2015 um 21:12
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
-- Tabellenstruktur für Tabelle `widgets_info`
--

CREATE TABLE IF NOT EXISTS `widgets_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `info_text` varchar(2000) NOT NULL,
  `amount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `widgets_info_3184e41c` (`amount_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Daten für Tabelle `widgets_info`
--

INSERT INTO `widgets_info` (`id`, `info_text`, `amount_id`) VALUES
(1, 'Wenn Sie Ihren alten Kühlschrank der Energieeffizienzklasse B durch ein Gerät der Klasse A+++ austauschen, sparen Sie pro Jahr 160 Kilogramm CO2 und 74 Euro ein.', 1),
(2, 'Ob Tee oder Pasta – erhitzen Sie das Wasser im elektrischen Wasserkocher. Der ist schneller und benötigt weniger Energie als der Topf auf dem Herd. Wenn Sie an jedem Tag ein Liter Wasser kochen, sparen Sie jährlich 40 Euro beziehungsweise 90 Kilogramm CO2. ', 1),
(3, 'Sie sparen rund 46 Euro und 100 Kilogramm Kohlendioxid im Jahr, wenn Sie bei fünf Kochvorgängen pro Woche den Topfdeckel benutzen. ', 1),
(4, 'Wenn Sie Ihren Kühlschrank nur auf höchstens 7 Grad Celsius herunterkühlen lassen, sparen Sie gegenüber einer Kühlung auf 5 Grad Celsius rund 20 Kilogramm CO2 und 10 Euro jährlich.', 1),
(5, 'Bei 160 Waschgängen pro Jahr mit 40 statt 60 Grad sowie Verzicht auf Vorwäsche und Trockner können Sie 250 Kilogramm CO2 vermeiden. Gespartes Geld: 110 Euro. ', 1),
(6, 'Sparsame Spülmaschinen sind nicht nur tolle Küchenhelfer, sondern benötigen auch weniger Wasser als das Spülen mit der Hand. Laden Sie die Maschine möglichst voll, und schalten Sie aufs Sparprogramm. Geschirr dabei nicht vorspülen.So sparen bei 160 Spülmaschinengängen im Sparprogramm statt Handwäschen pro Jahr 80 Kilogramm CO2 und 35 Euro.', 1),
(7, 'Behalten Sie auch ohne Dauerlüftung einen klaren Kopf. Statt die Fenster stundenlang zu kippen, sollten Sie alle zwei bis drei Stunden stoßlüften. Dabei sind schon einige Minuten weit geöffnete Fenster ausreichend, um die gesamte Raumluft auszutauschen.Sie sparen bei dieser Methode 610 Kilogramm bzw. 180 Euro pro Jahr im Vergleich zu ständig gekippten Fenstern bei kalten Außentemperaturen.', 1),
(8, 'Wenn Sie die Temperatur in Ihren Wohnräumen nur um ein Grad senken, sparen Sie fünf bis zehn Prozent Heizenergie und damit eine Menge Geld. Ein angenehmes Wohnklima bekommen Sie gratis obendrauf.Senken Sie die Temperatur wie beschrieben sparen Sie im Jahr 450 Kilogramm CO2 und 135 Euro ein!', 1),
(9, 'Recyceln Sie Wertstoffe, und werfen Sie diese in die richtigen Behälter: Papier in die Altpapiertonne, Altglas in den Glascontainer, Kun ststoffe in den gelben Sack. Metalle gehören in den Wertstoffhof.Damit sparen Sie zwar kein Geld, vermeiden aber bei je 100 Kilogramm Altpapier, Altglas und Kunststoff sowie 1 Kilogramm Aluminium immerhin 100 Kilogramm CO2 im Jahr.', 1);

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `widgets_info`
--
ALTER TABLE `widgets_info`
  ADD CONSTRAINT `widgets_info_amount_id_e7999d2_fk_widgets_widgetdata_id` FOREIGN KEY (`amount_id`) REFERENCES `widgets_widgetdata` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
