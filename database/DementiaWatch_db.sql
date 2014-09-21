-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 21, 2014 at 02:11 PM
-- Server version: 5.6.16
-- PHP Version: 5.5.9

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
  `carerID` int(5) NOT NULL AUTO_INCREMENT,
  `fName` varchar(20) NOT NULL,
  `lName` varchar(30) NOT NULL,
  `mobileNum` varchar(10) DEFAULT NULL,
  `contactNum` varchar(10) NOT NULL,
  PRIMARY KEY (`carerID`),
  UNIQUE KEY `carerID` (`carerID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `carers`
--

INSERT INTO `carers` (`carerID`, `fName`, `lName`, `mobileNum`, `contactNum`) VALUES
(4, 'j', 'j', '1', '1');

-- --------------------------------------------------------

--
-- Table structure for table `patientalerts`
--

CREATE TABLE IF NOT EXISTS `patientalerts` (
  `patientID` int(5) NOT NULL,
  `alertTime` time NOT NULL DEFAULT '00:00:00',
  `alertDate` date NOT NULL,
  `alertLat` decimal(20,10) NOT NULL,
  `alertLong` decimal(20,10) NOT NULL,
  PRIMARY KEY (`patientID`,`alertTime`),
  KEY `patientID` (`patientID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Triggers `patientalerts`
--
DROP TRIGGER IF EXISTS `insert_alerts`;
DELIMITER //
CREATE TRIGGER `insert_alerts` BEFORE INSERT ON `patientalerts`
 FOR EACH ROW BEGIN
UPDATE patients
SET patients.status = 'Distressed'
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
  PRIMARY KEY (`patientID`,`alertTime`),
  KEY `patientID` (`patientID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Triggers `patientbatteryalerts`
--
DROP TRIGGER IF EXISTS `insert_batteryalerts`;
DELIMITER //
CREATE TRIGGER `insert_batteryalerts` BEFORE INSERT ON `patientbatteryalerts`
 FOR EACH ROW BEGIN
UPDATE patients
SET patients.status = 'Battery Low'
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
  `collapseLong` decimal(20,10) NOT NULL,
  PRIMARY KEY (`patientID`,`collapseTime`),
  KEY `patientID` (`patientID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Triggers `patientcollapses`
--
DROP TRIGGER IF EXISTS `insert_collapses`;
DELIMITER //
CREATE TRIGGER `insert_collapses` BEFORE INSERT ON `patientcollapses`
 FOR EACH ROW BEGIN
UPDATE patients
SET patients.status = 'Collapsed'
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
  `fenceRadius` int(2) NOT NULL,
  PRIMARY KEY (`patientID`,`fenceLat`,`fenceLong`),
  KEY `patientID` (`patientID`)
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
  `retrievalDate` date NOT NULL,
  PRIMARY KEY (`patientID`),
  KEY `patientID` (`patientID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patientloc`
--

INSERT INTO `patientloc` (`patientID`, `patientLat`, `patientLong`, `retrievalTime`, `retrievalDate`) VALUES
(6, '-27.4976428700', '152.9736471000', '10:21:46', '2014-09-04');

--
-- Triggers `patientloc`
--
DROP TRIGGER IF EXISTS `insert_current_retrieval`;
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
  `pointDescription` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`patientID`,`pointName`),
  KEY `patientID` (`patientID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE IF NOT EXISTS `patients` (
  `patientID` int(5) NOT NULL AUTO_INCREMENT,
  `carerID` int(5) DEFAULT NULL,
  `fName` varchar(20) NOT NULL,
  `lName` varchar(30) NOT NULL,
  `gender` enum('Male','Female') NOT NULL DEFAULT 'Male',
  `age` int(3) NOT NULL,
  `bloodType` varchar(3) DEFAULT NULL,
  `medication` varchar(255) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `homeAddress` varchar(100) NOT NULL,
  `homeSuburb` varchar(20) NOT NULL,
  `contactNum` varchar(10) NOT NULL,
  `emergencyContactName` varchar(50) DEFAULT NULL,
  `emergencyContactAddress` varchar(100) DEFAULT NULL,
  `emergencyContactSuburb` varchar(20) DEFAULT NULL,
  `emergencyContactNum` varchar(10) NOT NULL,
  `uniqueKey` varchar(36) NOT NULL,
  PRIMARY KEY (`patientID`),
  UNIQUE KEY `patientID` (`patientID`),
  UNIQUE KEY `uniqueKey` (`uniqueKey`),
  KEY `carerID` (`carerID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`patientID`, `carerID`, `fName`, `lName`, `gender`, `age`, `bloodType`, `medication`, `status`, `homeAddress`, `homeSuburb`, `contactNum`, `emergencyContactName`, `emergencyContactAddress`, `emergencyContactSuburb`, `emergencyContactNum`, `uniqueKey`) VALUES
(6, 4, 'Josh', 'Johnston', 'Male', 123, 'O+', 'stoofs', 'fine', '123 fake street', 'fakeberg', '12345678', 'Dawn Johnston', '1234 fake street', 'fakeberg', '123456789', '1');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `userID` int(5) NOT NULL AUTO_INCREMENT,
  `patientID` int(5) DEFAULT NULL,
  `carerID` int(5) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `userName` char(15) NOT NULL,
  `userPass` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `userName` (`userName`),
  UNIQUE KEY `salt` (`salt`),
  UNIQUE KEY `email` (`email`),
  KEY `carerID` (`carerID`),
  KEY `patientID` (`patientID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `patientID`, `carerID`, `email`, `userName`, `userPass`, `salt`) VALUES
(4, NULL, 4, '1@1.com', 'j', '8161e767e1e85f0f37ad655caf1aad304073d8701bf2a9bbda60090e1e6cd125', '2e033eef-bcd2-4529-9111-74dff28aa093');

--
-- Triggers `users`
--
DROP TRIGGER IF EXISTS `check_patient_carer_ID`;
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
-- Constraints for dumped tables
--

--
-- Constraints for table `patientalerts`
--
ALTER TABLE `patientalerts`
  ADD CONSTRAINT `patientAlerts_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientbatteryalerts`
--
ALTER TABLE `patientbatteryalerts`
  ADD CONSTRAINT `patientbatteryalerts_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientcollapses`
--
ALTER TABLE `patientcollapses`
  ADD CONSTRAINT `patientCollapses_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientfences`
--
ALTER TABLE `patientfences`
  ADD CONSTRAINT `patientFences_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientloc`
--
ALTER TABLE `patientloc`
  ADD CONSTRAINT `patientLoc_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patientpoints`
--
ALTER TABLE `patientpoints`
  ADD CONSTRAINT `patientPoints_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `patients`
--
ALTER TABLE `patients`
  ADD CONSTRAINT `patients_ibfk_1` FOREIGN KEY (`carerID`) REFERENCES `carers` (`carerID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`carerID`) REFERENCES `carers` (`carerID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `users_ibfk_2` FOREIGN KEY (`patientID`) REFERENCES `patients` (`patientID`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
