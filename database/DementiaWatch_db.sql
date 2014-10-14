-- phpMyAdmin SQL Dump
-- version 4.2.10
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 14, 2014 at 03:32 AM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `dementiawatch_db`
--
CREATE DATABASE IF NOT EXISTS `dementiawatch_db` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `dementiawatch_db`;

-- --------------------------------------------------------

--
-- Table structure for table `carers`
--

CREATE TABLE IF NOT EXISTS `carers` (
`carerID` int(5) NOT NULL,
  `fName` varchar(20) NOT NULL,
  `lName` varchar(30) NOT NULL,
  `mobileNum` varchar(10) DEFAULT NULL,
  `contactNum` varchar(10) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `carers`
--

INSERT INTO `carers` (`carerID`, `fName`, `lName`, `mobileNum`, `contactNum`) VALUES
(4, 'jjo', 'jj', '12345678', '12345678');

-- --------------------------------------------------------

--
-- Table structure for table `patientalerts`
--

CREATE TABLE IF NOT EXISTS `patientalerts` (
  `patientID` int(5) NOT NULL,
  `alertTime` time NOT NULL DEFAULT '00:00:00',
  `alertDate` date NOT NULL,
  `alertLat` decimal(20,10) NOT NULL,
  `alertLong` decimal(20,10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patientalerts`
--

INSERT INTO `patientalerts` (`patientID`, `alertTime`, `alertDate`, `alertLat`, `alertLong`) VALUES
(6, '20:44:04', '2014-09-22', '0.0000000000', '0.0000000000'),
(8, '13:26:33', '2014-10-13', '0.0000000000', '0.0000000000');

--
-- Triggers `patientalerts`
--
DELIMITER //
CREATE TRIGGER `insert_alerts` BEFORE INSERT ON `patientalerts`
 FOR EACH ROW BEGIN
UPDATE patients
SET patients.status = 'DISTRESSED'
WHERE patients.patientID = NEW.patientID;

SET NEW.alertDate = DATE_FORMAT(now(),'%Y-%m-%d'),
	NEW.alertTime = TIME_FORMAT(CURTIME(), '%H:%i:%s');
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `patientbatteryalerts`
--

CREATE TABLE IF NOT EXISTS `patientbatteryalerts` (
  `patientID` int(5) NOT NULL,
  `alertTime` time NOT NULL DEFAULT '00:00:00',
  `alertDate` date NOT NULL,
  `batteryLevel` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patientbatteryalerts`
--

INSERT INTO `patientbatteryalerts` (`patientID`, `alertTime`, `alertDate`, `batteryLevel`) VALUES
(6, '20:50:15', '2014-09-22', '15%'),
(8, '13:25:13', '2014-10-13', '15%');

--
-- Triggers `patientbatteryalerts`
--
DELIMITER //
CREATE TRIGGER `insert_batteryalerts` BEFORE INSERT ON `patientbatteryalerts`
 FOR EACH ROW BEGIN
UPDATE patients
SET patients.status = 'BATTERY_LOW'
WHERE patients.patientID = NEW.patientID;

SET NEW.alertDate = DATE_FORMAT(now(),'%Y-%m-%d'),
	NEW.alertTime = TIME_FORMAT(CURTIME(), '%H:%i:%s');
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `patientcollapses`
--

CREATE TABLE IF NOT EXISTS `patientcollapses` (
  `patientID` int(5) NOT NULL,
  `collapseTime` time NOT NULL,
  `collapseDate` date NOT NULL,
  `collapseLat` decimal(20,10) NOT NULL,
  `collapseLong` decimal(20,10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patientcollapses`
--

INSERT INTO `patientcollapses` (`patientID`, `collapseTime`, `collapseDate`, `collapseLat`, `collapseLong`) VALUES
(6, '20:44:16', '2014-09-22', '0.0000000000', '0.0000000000');

--
-- Triggers `patientcollapses`
--
DELIMITER //
CREATE TRIGGER `insert_collapses` BEFORE INSERT ON `patientcollapses`
 FOR EACH ROW BEGIN
UPDATE patients
SET patients.status = 'FALLEN'
WHERE patients.patientID = NEW.patientID;

SET NEW.collapseDate = DATE_FORMAT(now(),'%Y-%m-%d'),
	NEW.collapseTime = TIME_FORMAT(CURTIME(), '%H:%i:%s');
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `patientfences`
--

CREATE TABLE IF NOT EXISTS `patientfences` (
  `patientID` int(5) NOT NULL,
  `fenceLat` decimal(20,10) NOT NULL,
  `fenceLong` decimal(20,10) NOT NULL,
  `radius` decimal(20,10) NOT NULL DEFAULT '30.0000000000'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `patientloc`
--

CREATE TABLE IF NOT EXISTS `patientloc` (
  `patientID` int(5) NOT NULL,
  `patientLat` decimal(20,10) NOT NULL,
  `patientLong` decimal(20,10) NOT NULL,
  `retrievalTime` time NOT NULL,
  `retrievalDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patientloc`
--

INSERT INTO `patientloc` (`patientID`, `patientLat`, `patientLong`, `retrievalTime`, `retrievalDate`) VALUES
(6, '-27.4976428700', '152.9736471000', '10:21:46', '2014-09-04');

--
-- Triggers `patientloc`
--
DELIMITER //
CREATE TRIGGER `insert_current_retrieval` BEFORE INSERT ON `patientloc`
 FOR EACH ROW SET NEW.retrievalDate = DATE_FORMAT(now(),'%Y-%m-%d'),
	NEW.retrievalTime = TIME_FORMAT(CURTIME(), '%H:%i:%s')
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `patientpoints`
--

CREATE TABLE IF NOT EXISTS `patientpoints` (
  `patientID` int(5) NOT NULL,
  `pointLat` decimal(20,10) NOT NULL,
  `pointLong` decimal(20,10) NOT NULL,
  `pointName` varchar(50) NOT NULL,
  `pointDescription` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE IF NOT EXISTS `patients` (
`patientID` int(5) NOT NULL,
  `carerID` int(5) DEFAULT NULL,
  `fName` varchar(20) NOT NULL,
  `lName` varchar(30) NOT NULL,
  `gender` enum('Male','Female') NOT NULL DEFAULT 'Male',
  `age` int(3) NOT NULL,
  `bloodType` enum('O_POS','O_NEG','A_POS','A_NEG','B_POS','B_NEG','AB_POS','AB_NEG') DEFAULT NULL,
  `medication` varchar(255) DEFAULT NULL,
  `status` enum('FINE','DISTRESSED','FALLEN','BATTERY_LOW','LOST') DEFAULT 'FINE',
  `homeAddress` varchar(100) NOT NULL,
  `homeSuburb` varchar(20) NOT NULL,
  `contactNum` varchar(10) NOT NULL,
  `emergencyContactName` varchar(50) DEFAULT NULL,
  `emergencyContactAddress` varchar(100) DEFAULT NULL,
  `emergencyContactSuburb` varchar(20) DEFAULT NULL,
  `emergencyContactNum` varchar(10) NOT NULL,
  `uniqueKey` varchar(36) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`patientID`, `carerID`, `fName`, `lName`, `gender`, `age`, `bloodType`, `medication`, `status`, `homeAddress`, `homeSuburb`, `contactNum`, `emergencyContactName`, `emergencyContactAddress`, `emergencyContactSuburb`, `emergencyContactNum`, `uniqueKey`) VALUES
(6, 4, 'Joshua', 'Johnston', 'Female', 123, 'A_NEG', 'stoofs', 'BATTERY_LOW', '123 fake street', 'fakeberg', '12345678', 'Dawn Johnston', '1234 fake street', 'fakeberg', '123456789', '1'),
(8, 4, 'jason', 'johnston', 'Male', 73, 'O_POS', 'hello', 'DISTRESSED', 'hello', 'hello', '12345678', 'hello', 'hello', 'hello', '12345678', '74ab0de7-d310-448b-8e77-f84b0ae6b01b');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
`userID` int(5) NOT NULL,
  `patientID` int(5) DEFAULT NULL,
  `carerID` int(5) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `userName` char(15) NOT NULL,
  `userPass` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `patientID`, `carerID`, `email`, `userName`, `userPass`, `salt`) VALUES
(4, NULL, 4, '123@j.com', 'j', '2ae0f5eecf88938e25f1f73aca933314e431c59988219d37708d398d03e592d9', '2e033eef-bcd2-4529-9111-74dff28aa093');

--
-- Triggers `users`
--
DELIMITER //
CREATE TRIGGER `check_patient_carer_ID` BEFORE INSERT ON `users`
 FOR EACH ROW IF	(
	(NEW.patientID IS NULL AND NEW.carerID IS NULL) || 
	(NEW.patientID IS NOT NULL AND NEW.carerID IS NOT NULL)
	)
THEN
	SIGNAL SQLSTATE '44000'
	SET MESSAGE_TEXT = 'user must have either a patient or carer ID, it cannot have both either';
END IF
//
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `carers`
--
ALTER TABLE `carers`
 ADD PRIMARY KEY (`carerID`), ADD UNIQUE KEY `carerID` (`carerID`);

--
-- Indexes for table `patientalerts`
--
ALTER TABLE `patientalerts`
 ADD PRIMARY KEY (`patientID`,`alertTime`), ADD KEY `patientID` (`patientID`);

--
-- Indexes for table `patientbatteryalerts`
--
ALTER TABLE `patientbatteryalerts`
 ADD PRIMARY KEY (`patientID`,`alertTime`), ADD KEY `patientID` (`patientID`);

--
-- Indexes for table `patientcollapses`
--
ALTER TABLE `patientcollapses`
 ADD PRIMARY KEY (`patientID`,`collapseTime`), ADD KEY `patientID` (`patientID`);

--
-- Indexes for table `patientfences`
--
ALTER TABLE `patientfences`
 ADD PRIMARY KEY (`patientID`,`fenceLat`,`fenceLong`), ADD KEY `patientID` (`patientID`);

--
-- Indexes for table `patientloc`
--
ALTER TABLE `patientloc`
 ADD PRIMARY KEY (`patientID`,`retrievalTime`), ADD KEY `patientID` (`patientID`);

--
-- Indexes for table `patientpoints`
--
ALTER TABLE `patientpoints`
 ADD PRIMARY KEY (`patientID`,`pointName`), ADD KEY `patientID` (`patientID`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
 ADD PRIMARY KEY (`patientID`), ADD UNIQUE KEY `patientID` (`patientID`), ADD UNIQUE KEY `uniqueKey` (`uniqueKey`), ADD KEY `carerID` (`carerID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`userID`), ADD UNIQUE KEY `userName` (`userName`), ADD UNIQUE KEY `salt` (`salt`), ADD UNIQUE KEY `email` (`email`), ADD KEY `carerID` (`carerID`), ADD KEY `patientID` (`patientID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `carers`
--
ALTER TABLE `carers`
MODIFY `carerID` int(5) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
MODIFY `patientID` int(5) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
MODIFY `userID` int(5) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `patientalerts`
--
ALTER TABLE `patientalerts`
ADD CONSTRAINT `patientalerts_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientbatteryalerts`
--
ALTER TABLE `patientbatteryalerts`
ADD CONSTRAINT `patientbatteryalerts_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientcollapses`
--
ALTER TABLE `patientcollapses`
ADD CONSTRAINT `patientcollapses_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientfences`
--
ALTER TABLE `patientfences`
ADD CONSTRAINT `patientfences_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientloc`
--
ALTER TABLE `patientloc`
ADD CONSTRAINT `patientloc_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientpoints`
--
ALTER TABLE `patientpoints`
ADD CONSTRAINT `patientpoints_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patients`
--
ALTER TABLE `patients`
ADD CONSTRAINT `patients_ibfk_1` FOREIGN KEY (`carerID`) REFERENCES `carers` (`carerID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
ADD CONSTRAINT `users_ibfk_2` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `users_ibfk_3` FOREIGN KEY (`carerID`) REFERENCES `carers` (`carerID`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
