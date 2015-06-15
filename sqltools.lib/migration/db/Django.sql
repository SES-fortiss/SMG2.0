-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 19. Mrz 2015 um 12:59
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
CREATE DATABASE IF NOT EXISTS `django` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `django`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `auth_group`
--

DROP TABLE IF EXISTS `auth_group`;
CREATE TABLE IF NOT EXISTS `auth_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `auth_group_permissions`
--

DROP TABLE IF EXISTS `auth_group_permissions`;
CREATE TABLE IF NOT EXISTS `auth_group_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_id` (`group_id`,`permission_id`),
  KEY `auth_group_permissions_0e939a4f` (`group_id`),
  KEY `auth_group_permissions_8373b171` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `auth_permission`
--

DROP TABLE IF EXISTS `auth_permission`;
CREATE TABLE IF NOT EXISTS `auth_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `content_type_id` int(11) NOT NULL,
  `codename` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_type_id` (`content_type_id`,`codename`),
  KEY `auth_permission_417f1b1c` (`content_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31 ;

--
-- Daten für Tabelle `auth_permission`
--

INSERT INTO `auth_permission` (`id`, `name`, `content_type_id`, `codename`) VALUES
(1, 'Can add log entry', 1, 'add_logentry'),
(2, 'Can change log entry', 1, 'change_logentry'),
(3, 'Can delete log entry', 1, 'delete_logentry'),
(4, 'Can add permission', 2, 'add_permission'),
(5, 'Can change permission', 2, 'change_permission'),
(6, 'Can delete permission', 2, 'delete_permission'),
(7, 'Can add group', 3, 'add_group'),
(8, 'Can change group', 3, 'change_group'),
(9, 'Can delete group', 3, 'delete_group'),
(10, 'Can add user', 4, 'add_user'),
(11, 'Can change user', 4, 'change_user'),
(12, 'Can delete user', 4, 'delete_user'),
(13, 'Can add content type', 5, 'add_contenttype'),
(14, 'Can change content type', 5, 'change_contenttype'),
(15, 'Can delete content type', 5, 'delete_contenttype'),
(16, 'Can add session', 6, 'add_session'),
(17, 'Can change session', 6, 'change_session'),
(18, 'Can delete session', 6, 'delete_session'),
(19, 'Can add posts', 7, 'add_posts'),
(20, 'Can change posts', 7, 'change_posts'),
(21, 'Can delete posts', 7, 'delete_posts'),
(22, 'Can add realuser', 8, 'add_realuser'),
(23, 'Can change realuser', 8, 'change_realuser'),
(24, 'Can delete realuser', 8, 'delete_realuser'),
(25, 'Can add user profile', 9, 'add_userprofile'),
(26, 'Can change user profile', 9, 'change_userprofile'),
(27, 'Can delete user profile', 9, 'delete_userprofile'),
(28, 'Can add registration profile', 10, 'add_registrationprofile'),
(29, 'Can change registration profile', 10, 'change_registrationprofile'),
(30, 'Can delete registration profile', 10, 'delete_registrationprofile');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `auth_user`
--

DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE IF NOT EXISTS `auth_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(128) NOT NULL,
  `last_login` datetime NOT NULL,
  `is_superuser` tinyint(1) NOT NULL,
  `username` varchar(30) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `email` varchar(75) NOT NULL,
  `is_staff` tinyint(1) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `date_joined` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Daten für Tabelle `auth_user`
--

INSERT INTO `auth_user` (`id`, `password`, `last_login`, `is_superuser`, `username`, `first_name`, `last_name`, `email`, `is_staff`, `is_active`, `date_joined`) VALUES
(1, 'pbkdf2_sha256$12000$CMJyIVSwzbxz$Bt/w4GFFf8DMyxDhJXDyaPP63NjZMQ9OT5X+4FnH+IA=', '2014-09-09 13:46:42', 1, 'admin', '', '', 'jens.klinker@peyclon.de', 1, 1, '2014-09-09 13:01:49'),
(2, 'pbkdf2_sha256$12000$2DspeFcDgn6S$kYzSO4wbp7vXyeZY2166+vr0PilGRaxget9SpnzezNo=', '2014-09-09 13:47:52', 0, 'subject_1', 'Subject', '1', 'sub1@fortiss.org', 1, 1, '2014-09-09 13:47:52'),
(3, 'pbkdf2_sha256$12000$IzfvTqMuAOce$PrXRLMEleRGz+v5HZlNP4EcOc4CQR//8kZam22J8vj0=', '2014-11-12 15:18:35', 1, 'fortiss', '', '', 'fortiss@fortiss.de', 1, 1, '2014-11-12 15:18:14'),
(4, 'pbkdf2_sha256$12000$q6SLRpiMI2L8$KaZRRNHJr3W7MjFxBGdYHWZoGhowmanVgrfmxtlJk2s=', '2014-11-12 15:19:25', 0, 'subject2', '', '', '', 0, 1, '2014-11-12 15:19:25');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `auth_user_groups`
--

DROP TABLE IF EXISTS `auth_user_groups`;
CREATE TABLE IF NOT EXISTS `auth_user_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`group_id`),
  KEY `auth_user_groups_e8701ad4` (`user_id`),
  KEY `auth_user_groups_0e939a4f` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `auth_user_user_permissions`
--

DROP TABLE IF EXISTS `auth_user_user_permissions`;
CREATE TABLE IF NOT EXISTS `auth_user_user_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`permission_id`),
  KEY `auth_user_user_permissions_e8701ad4` (`user_id`),
  KEY `auth_user_user_permissions_8373b171` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `blog_posts`
--

DROP TABLE IF EXISTS `blog_posts`;
CREATE TABLE IF NOT EXISTS `blog_posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(100),
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `django_admin_log`
--

DROP TABLE IF EXISTS `django_admin_log`;
CREATE TABLE IF NOT EXISTS `django_admin_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_time` datetime NOT NULL,
  `object_id` longtext,
  `object_repr` varchar(200) NOT NULL,
  `action_flag` smallint(5) unsigned NOT NULL,
  `change_message` longtext NOT NULL,
  `content_type_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `django_admin_log_417f1b1c` (`content_type_id`),
  KEY `django_admin_log_e8701ad4` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Daten für Tabelle `django_admin_log`
--

INSERT INTO `django_admin_log` (`id`, `action_time`, `object_id`, `object_repr`, `action_flag`, `change_message`, `content_type_id`, `user_id`) VALUES
(1, '2014-09-09 13:47:52', '2', 'subject_1', 1, '', 4, 1),
(2, '2014-09-09 13:48:28', '2', 'subject_1', 2, 'Changed first_name, last_name, email and is_staff.', 4, 1),
(3, '2014-11-12 15:19:25', '4', 'subject2', 1, '', 4, 3);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `django_content_type`
--

DROP TABLE IF EXISTS `django_content_type`;
CREATE TABLE IF NOT EXISTS `django_content_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `app_label` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `django_content_type_app_label_284121e1d38aa81f_uniq` (`app_label`,`model`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Daten für Tabelle `django_content_type`
--

INSERT INTO `django_content_type` (`id`, `name`, `app_label`, `model`) VALUES
(1, 'log entry', 'admin', 'logentry'),
(2, 'permission', 'auth', 'permission'),
(3, 'group', 'auth', 'group'),
(4, 'user', 'auth', 'user'),
(5, 'content type', 'contenttypes', 'contenttype'),
(6, 'session', 'sessions', 'session'),
(7, 'posts', 'blog', 'posts'),
(8, 'realuser', 'joins', 'realuser'),
(9, 'user profile', 'joins', 'userprofile'),
(10, 'registration profile', 'registration', 'registrationprofile');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `django_migrations`
--

DROP TABLE IF EXISTS `django_migrations`;
CREATE TABLE IF NOT EXISTS `django_migrations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `applied` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Daten für Tabelle `django_migrations`
--

INSERT INTO `django_migrations` (`id`, `app`, `name`, `applied`) VALUES
(1, 'contenttypes', '0001_initial', '2014-09-09 13:00:56'),
(2, 'auth', '0001_initial', '2014-09-09 13:01:14'),
(3, 'admin', '0001_initial', '2014-09-09 13:01:18'),
(4, 'sessions', '0001_initial', '2014-09-09 13:01:19'),
(5, 'blog', '0001_initial', '2014-11-12 15:09:54'),
(6, 'blog', '0002_auto_20150310_1327', '2015-03-13 12:12:23'),
(7, 'joins', '0001_initial', '2015-03-13 12:12:24'),
(8, 'joins', '0002_userprofile', '2015-03-13 12:12:25'),
(9, 'joins', '0003_auto_20150114_1112', '2015-03-13 12:12:26'),
(10, 'joins', '0004_auto_20150116_1339', '2015-03-13 12:12:29');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `django_session`
--

DROP TABLE IF EXISTS `django_session`;
CREATE TABLE IF NOT EXISTS `django_session` (
  `session_key` varchar(40) NOT NULL,
  `session_data` longtext NOT NULL,
  `expire_date` datetime NOT NULL,
  PRIMARY KEY (`session_key`),
  KEY `django_session_de54fa62` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `django_session`
--

INSERT INTO `django_session` (`session_key`, `session_data`, `expire_date`) VALUES
('0t8c1wz8r8rogeimqfmpjzhz1c337yh8', 'YmQ0MjkzZWIzMmE4MzQzNjJhNmM0OGM2N2IwYWExYjY2ZmM5NDVkYzp7fQ==', '2014-09-23 13:19:54'),
('7yd9kq09khfmv1gd9o2zvalt2pgb1bf6', 'YmQ0MjkzZWIzMmE4MzQzNjJhNmM0OGM2N2IwYWExYjY2ZmM5NDVkYzp7fQ==', '2014-09-23 13:02:00'),
('ayu15i1ejahcyxtpvkclhnjhwlpngsy9', 'YmQ0MjkzZWIzMmE4MzQzNjJhNmM0OGM2N2IwYWExYjY2ZmM5NDVkYzp7fQ==', '2015-03-16 11:24:47'),
('c6tg0bbgpk37lwwmtkess8ze0ardho3d', 'YmQ0MjkzZWIzMmE4MzQzNjJhNmM0OGM2N2IwYWExYjY2ZmM5NDVkYzp7fQ==', '2014-09-23 13:44:52'),
('ebneg6grzocqcz75p7gqk9c41cius004', 'N2I4MDM5Y2RmMjk5OTA1NzYyYjY3YjRmNzNiNTFiMjdiNjhkYWFlNDp7Il9hdXRoX3VzZXJfaGFzaCI6IjE0YjYwYmZjM2EwN2ZhMmFlMjQxNGNlZTk4YTZkMjJiMGFkNzYyMWYiLCJfYXV0aF91c2VyX2lkIjoxLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCJ9', '2014-09-23 13:46:42'),
('hzawg0mfyqj923zjadpftcb3doj5tgcx', 'YmQ0MjkzZWIzMmE4MzQzNjJhNmM0OGM2N2IwYWExYjY2ZmM5NDVkYzp7fQ==', '2014-09-23 13:15:56'),
('ktliytapvafmnfdwxs5teomn8cnm9gww', 'YmQ0MjkzZWIzMmE4MzQzNjJhNmM0OGM2N2IwYWExYjY2ZmM5NDVkYzp7fQ==', '2014-11-26 15:19:33'),
('nuw1kaldj3524q5fi278l53kfy1ezgk7', 'YmQ0MjkzZWIzMmE4MzQzNjJhNmM0OGM2N2IwYWExYjY2ZmM5NDVkYzp7fQ==', '2014-09-23 13:21:36'),
('q0ekk6ep1im45yr2sxz3af5xh6a45a8s', 'YmQ0MjkzZWIzMmE4MzQzNjJhNmM0OGM2N2IwYWExYjY2ZmM5NDVkYzp7fQ==', '2014-09-23 13:16:22');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `joins_realuser`
--

DROP TABLE IF EXISTS `joins_realuser`;
CREATE TABLE IF NOT EXISTS `joins_realuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(30) NOT NULL,
  `lastname` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `joins_userprofile`
--

DROP TABLE IF EXISTS `joins_userprofile`;
CREATE TABLE IF NOT EXISTS `joins_userprofile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `website` varchar(200),
  `picture` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `registration_registrationprofile`
--

DROP TABLE IF EXISTS `registration_registrationprofile`;
CREATE TABLE IF NOT EXISTS `registration_registrationprofile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `activation_key` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `auth_group_permissions`
--
ALTER TABLE `auth_group_permissions`
  ADD CONSTRAINT `auth_group_permission_group_id_7ffee43972bd7f1a_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`),
  ADD CONSTRAINT `auth_group__permission_id_368eeb6666ec07a6_fk_auth_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`);

--
-- Constraints der Tabelle `auth_permission`
--
ALTER TABLE `auth_permission`
  ADD CONSTRAINT `auth__content_type_id_6b20c51720da873a_fk_django_content_type_id` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`);

--
-- Constraints der Tabelle `auth_user_groups`
--
ALTER TABLE `auth_user_groups`
  ADD CONSTRAINT `auth_user_groups_group_id_7d085966c94c6232_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`),
  ADD CONSTRAINT `auth_user_groups_user_id_27e8dc8fc3e5d802_fk_auth_user_id` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`);

--
-- Constraints der Tabelle `auth_user_user_permissions`
--
ALTER TABLE `auth_user_user_permissions`
  ADD CONSTRAINT `auth_user_user_permissi_user_id_76b8c3047a3326cf_fk_auth_user_id` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`),
  ADD CONSTRAINT `auth_user_u_permission_id_6be0cfd6e66a0bed_fk_auth_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`);

--
-- Constraints der Tabelle `django_admin_log`
--
ALTER TABLE `django_admin_log`
  ADD CONSTRAINT `django_admin_log_user_id_6fe8bbee29c0ae54_fk_auth_user_id` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`),
  ADD CONSTRAINT `djang_content_type_id_2556ca9ac1c31b73_fk_django_content_type_id` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`);

--
-- Constraints der Tabelle `joins_userprofile`
--
ALTER TABLE `joins_userprofile`
  ADD CONSTRAINT `joins_userprofile_user_id_6700435d_fk_auth_user_id` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`);

--
-- Constraints der Tabelle `registration_registrationprofile`
--
ALTER TABLE `registration_registrationprofile`
  ADD CONSTRAINT `user_id_refs_id_954d2985` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
