-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Erstellungszeit: 25. Okt 2019 um 16:45
-- Server-Version: 10.3.14-MariaDB-1:10.3.14+maria~bionic
-- PHP-Version: 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `Loady`
--
CREATE DATABASE IF NOT EXISTS `Loady` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `Loady`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `car`
--

CREATE TABLE `car` (
  `id` int(11) NOT NULL,
  `capacity` double DEFAULT NULL,
  `count` int(11) NOT NULL,
  `max_power` double DEFAULT NULL,
  `plate` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `loadingport`
--

CREATE TABLE `loadingport` (
  `port` int(11) NOT NULL,
  `occupied` bit(1) NOT NULL,
  `loadingstation_station_nr` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `loadingstation`
--

CREATE TABLE `loadingstation` (
  `station_nr` int(11) NOT NULL,
  `owner` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `reservation`
--

CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `loadingport_port` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `session`
--

CREATE TABLE `session` (
  `id` int(11) NOT NULL,
  `current_percent` int(11) NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `power` int(11) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `car_id` int(11) DEFAULT NULL,
  `loadingport_port` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `card` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `weather`
--

CREATE TABLE `weather` (
  `id` int(11) NOT NULL,
  `clouds` int(11) NOT NULL,
  `light` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `pressure` int(11) NOT NULL,
  `sunrise` datetime NOT NULL,
  `sunset` datetime NOT NULL,
  `temperature` double NOT NULL,
  `temperature_max` double NOT NULL,
  `temperature_min` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `car`
--
ALTER TABLE `car`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKja1j4mm4rqlv6cnhgp1qqgtuj` (`user_id`);

--
-- Indizes für die Tabelle `loadingport`
--
ALTER TABLE `loadingport`
  ADD PRIMARY KEY (`port`),
  ADD KEY `FKd5iybc3ocqmc710y294y6y38v` (`loadingstation_station_nr`);

--
-- Indizes für die Tabelle `loadingstation`
--
ALTER TABLE `loadingstation`
  ADD PRIMARY KEY (`station_nr`);

--
-- Indizes für die Tabelle `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKqflqs0l9brx83ow3y74kuswwg` (`loadingport_port`),
  ADD KEY `FKm4oimk0l1757o9pwavorj6ljg` (`user_id`);

--
-- Indizes für die Tabelle `session`
--
ALTER TABLE `session`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_hxcel361kkffyn0uu73j2lrp2` (`car_id`),
  ADD UNIQUE KEY `UK_ilrgvkxy0tpaax6a78rj20q3o` (`loadingport_port`);

--
-- Indizes für die Tabelle `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `weather`
--
ALTER TABLE `weather`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `car`
--
ALTER TABLE `car`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `session`
--
ALTER TABLE `session`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `weather`
--
ALTER TABLE `weather`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `car`
--
ALTER TABLE `car`
  ADD CONSTRAINT `FKja1j4mm4rqlv6cnhgp1qqgtuj` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints der Tabelle `loadingport`
--
ALTER TABLE `loadingport`
  ADD CONSTRAINT `FKd5iybc3ocqmc710y294y6y38v` FOREIGN KEY (`loadingstation_station_nr`) REFERENCES `loadingstation` (`station_nr`);

--
-- Constraints der Tabelle `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `FKm4oimk0l1757o9pwavorj6ljg` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKqflqs0l9brx83ow3y74kuswwg` FOREIGN KEY (`loadingport_port`) REFERENCES `loadingport` (`port`);

--
-- Constraints der Tabelle `session`
--
ALTER TABLE `session`
  ADD CONSTRAINT `FK4jh5cpc0fdtyx27fc17vqmji6` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`),
  ADD CONSTRAINT `FK5ndmp98s6am40jkmp22ks9mvv` FOREIGN KEY (`loadingport_port`) REFERENCES `loadingport` (`port`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
