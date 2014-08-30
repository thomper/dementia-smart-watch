-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Aug 15, 2014 at 02:17 PM
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

INSERT INTO `carers` (`fName`, `lName`, `mobileNum`, `contactNum`) VALUES
('louise', 'elliot', 0011223344, 0011223344),
('Paul', 'brand', 11223344, 22334455),
('jason', 'london', 33445566, 33445566);

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE IF NOT EXISTS `patients` (
  `patientID` int(5) NOT NULL AUTO_INCREMENT,
  `carerID` int(5) DEFAULT NULL,
  `fName` varchar(20) NOT NULL,
  `lName` varchar(30) NOT NULL,
  `gender` enum('Male', 'Female') DEFAULT 'Male' NOT NULL, 
  `age` int(3) NOT NULL,
  `bloodType` varchar(3),
  `medication` varchar(255),
  `status` char(10),
  `homeAddress` varchar(100) NOT NULL,
  `homeSuburb` varchar(20) NOT NULL,
  `contactNum` varchar(10) NOT NULL,
  `emergencyContactName` varchar(50),
  `emergencyContactAddress` varchar(100),
  `emergencyContactSuburb` varchar(20),
  `emergencyContactNum` varchar(10) NOT NULL,
  `uniqueKey` int(6) NOT NULL,
  PRIMARY KEY (`patientID`),
  UNIQUE KEY `patientID` (`patientID`),
  UNIQUE KEY `uniqueKey` (`uniqueKey`),
  KEY `carerID` (`carerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

INSERT INTO `patients` (`carerID`, `fName`, `lName`, `gender`, `age`, `bloodType`, `status`, `homeAddress`, `homeSuburb`, `contactNum`, `emergencyContactName`, `emergencyContactAddress`, `emergencyContactSuburb`, `emergencyContactNum`, `uniqueKey`) VALUES
(1, 'adam', 'langley', 'Male', 73, 'B', 'fine', '79 Evelyn Street', 'Grange', 9988776655, 'louise elliot', null, null, 0011223344, 111111),
(1, 'aaron', 'ramsey', 'Male', 88, 'A', 'fine', '322 Moggill Road', 'Indooroopilly', 8877665544, 'louise elliot', null, null, 0011223344, 222222),
(2, 'geoff', 'free', 'Male', 64, 'O', 'fine', '79 Evelyn Street', 'Grange', 7766554433, 'Paul brand', null, null, 2233445566, 333333),
(2, 'jessica', 'langley', 'Female', 99, 'AB', 'fine', '79 Evelyn Street', 'Grange', 6655443322, 'Paul Brand', null, null, 2233445566, 444444),
(3, 'dawn', 'summers', 'Female', 73, 'A+', 'fine', '79 Evelyn Street', 'Grange', 5544332211, 'jason london', null, null, 3344556677, 555555);

-- --------------------------------------------------------

--
-- Table structure for table `patientLoc`
--

CREATE TABLE IF NOT EXISTS `patientLoc` (
  `patientID` int(5) NOT NULL,
  `patientLat` decimal(20,10) NOT NULL,
  `patientLong` decimal(20,10) NOT NULL,
  PRIMARY KEY (`patientID`),
  KEY `patientID` (`patientID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `patientLoc` (`patientID`, `patientLat`, `patientLong`) VALUES
(1, -27.4752990800, 152.9760412000),
(2, -27.5385548200, 153.0802628000),
(3, -27.4976428700, 152.9736471000);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

INSERT INTO `users` (`patientID`, `carerID`, `email`, `userName`, `userPass`, `salt`) VALUES
(null, 1, 'hello@gmail.com', 'hello123', 'hello123', 123),
(null, 2, 'hello123@gmail.com', 'hello', 'hello', 111),
(null, 3, 'hello234@gmail.com', 'hello234', 'hello234', 234);

CREATE TABLE IF NOT EXISTS `patientCollapses` (
	`patientID` int(5) NOT NULL,
	`collapseTime` time NOT NULL,
	`collapseDate` date NOT NULL,
	`collapseLat` decimal(20,10) NOT NULL,
	`collapseLong` decimal(20,10) NOT NULL,
	PRIMARY KEY (`patientID`, `collapseTime`),
	KEY `patientID` (`patientID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
	
CREATE TABLE IF NOT EXISTS `patientAlerts` (
	`patientID` int(5) NOT NULL,
	`alertTime` time NOT NULL,
	`alertDate` date NOT NULL,
	`alertLat` decimal(20,10) NOT NULL,
	`alertLong` decimal(20,10) NOT NULL,
	PRIMARY KEY (`patientID`, `alertTime`),
	KEY `patientID` (`patientID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `patientFences` (
	`patientID` int(5) NOT NULL,
	`fenceLat` decimal(20,10) NOT NULL,
	`fenceLong` decimal(20,10) NOT NULL,
	`fenceRadius` int(2) NOT NULL,
	PRIMARY KEY (`patientID`, `fenceLat`, `fenceLong`),
	KEY `patientID` (`patientID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `patientPoints` (
	`patientID` int(5) NOT NULL,
	`pointLat` decimal(20,10) NOT NULL,
	`pointLong` decimal(20,10) NOT NULL,
	`pointName` varchar(50) NOT NULL,
	`pointDescription` varchar(255),
	PRIMARY KEY (`patientID`, `pointName`),
	KEY `patientID` (`patientID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
	
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
	SET MESSAGE_TEXT = 'user cannot have both patient and carer ID';
END IF
//
DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
