-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 10, 2021 at 10:30 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `imenik_eng`
--

-- --------------------------------------------------------

--
-- Table structure for table `adresa`
--

CREATE TABLE `adresa` (
  `id` int(11) NOT NULL,
  `mesto` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `adresa`
--

INSERT INTO `adresa` (`id`, `mesto`) VALUES
(5, 'Miami'),
(7, 'Berlin'),
(8, 'Rome'),
(9, 'Barcelona'),
(10, 'Moscow'),
(11, 'London'),
(12, 'Amsterdam'),
(13, 'Prague'),
(14, 'Las Vegas'),
(15, 'Chicago'),
(18, 'New York'),
(19, 'Los Angeles'),
(20, 'Jakarta'),
(21, 'Belgrade'),
(22, 'San Francisco');

-- --------------------------------------------------------

--
-- Table structure for table `osoba`
--

CREATE TABLE `osoba` (
  `id` int(11) NOT NULL,
  `ime` varchar(20) NOT NULL,
  `prezime` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `osoba`
--

INSERT INTO `osoba` (`id`, `ime`, `prezime`) VALUES
(16, 'Tom', 'Holland'),
(17, 'Jack', 'Davis'),
(18, 'Amelia', 'Stone'),
(20, 'Oscar', 'Hunt'),
(21, 'Justin', 'Bond'),
(22, 'Marsh', 'Males'),
(23, 'Alan', 'Lee'),
(24, 'Josephine', 'Anderson'),
(25, 'Richard', 'Rios'),
(26, 'Felicia', 'Davis'),
(27, 'Penelope', 'Bates'),
(28, 'Charlie', 'Hale'),
(29, 'Kristen', 'Stewart'),
(30, 'Itan', 'Hunt'),
(31, 'Emma', 'Watson'),
(32, 'Susan', 'Smith');

-- --------------------------------------------------------

--
-- Table structure for table `povezuje`
--

CREATE TABLE `povezuje` (
  `id` int(11) NOT NULL,
  `id_osoba` int(11) NOT NULL,
  `id_adresa` int(11) NOT NULL,
  `id_telefon` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `povezuje`
--

INSERT INTO `povezuje` (`id`, `id_osoba`, `id_adresa`, `id_telefon`) VALUES
(23, 16, 12, 23),
(24, 17, 9, 24),
(25, 18, 15, 25),
(27, 20, 13, 27),
(28, 21, 12, 28),
(29, 22, 8, 29),
(30, 23, 13, 30),
(31, 24, 10, 31),
(32, 25, 5, 32),
(33, 26, 14, 33),
(34, 27, 15, 34),
(35, 28, 18, 35),
(36, 29, 18, 36),
(39, 30, 20, 39),
(40, 31, 14, 40),
(41, 17, 21, 41),
(42, 31, 15, 42),
(43, 16, 21, 43),
(44, 18, 21, 44),
(45, 20, 21, 45),
(46, 21, 21, 46),
(47, 22, 21, 47),
(48, 23, 21, 48),
(49, 24, 21, 49),
(50, 25, 21, 50),
(51, 26, 21, 51),
(52, 27, 21, 52),
(53, 28, 21, 53),
(54, 29, 21, 54),
(55, 30, 21, 55),
(56, 31, 21, 56),
(57, 16, 9, 57),
(58, 17, 9, 58),
(59, 18, 9, 59),
(60, 20, 9, 60),
(61, 24, 9, 61),
(62, 25, 9, 62),
(63, 26, 9, 63),
(64, 27, 9, 64),
(65, 28, 9, 65),
(66, 29, 9, 66),
(68, 32, 22, 68);

-- --------------------------------------------------------

--
-- Table structure for table `prijava`
--

CREATE TABLE `prijava` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `prijava`
--

INSERT INTO `prijava` (`id`, `username`, `password`) VALUES
(1, 'Ognjen123', 'Ognjen123');

-- --------------------------------------------------------

--
-- Table structure for table `telefon`
--

CREATE TABLE `telefon` (
  `id` int(11) NOT NULL,
  `telefon` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `telefon`
--

INSERT INTO `telefon` (`id`, `telefon`) VALUES
(23, '111-1111-111'),
(24, '222-2222-222'),
(25, '333-1111-333'),
(27, '555-5555-55'),
(28, '666-6666-666'),
(29, '777-7777-777'),
(30, '888-8888-888'),
(31, '999-9999-999'),
(32, '123-1234-123'),
(33, '132-1324-132'),
(34, '124-1243-124'),
(35, '142-1423-142'),
(36, '143-1234-143'),
(39, '144-1432-144'),
(40, '145-1432-145'),
(41, '146-1432-146'),
(42, '147-1432-007'),
(43, '111-1111-211'),
(44, '111-1111-311'),
(45, '111-1111-411'),
(46, '111-1111-511'),
(47, '111-1111-611'),
(48, '111-1111-711'),
(49, '111-1111-811'),
(50, '111-1111-911'),
(51, '111-1111-212'),
(52, '111-1111-213'),
(53, '111-1111-214'),
(54, '111-1111-215'),
(55, '111-1111-216'),
(56, '111-1111-217'),
(57, '111-1111-218'),
(58, '111-1111-219'),
(59, '111-1111-221'),
(60, '111-1111-223'),
(61, '111-1111-224'),
(62, '111-1111-225'),
(63, '111-1111-226'),
(64, '111-1111-227'),
(65, '111-1111-228'),
(66, '111-1111-229'),
(68, '222-2222-333');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `adresa`
--
ALTER TABLE `adresa`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `osoba`
--
ALTER TABLE `osoba`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `povezuje`
--
ALTER TABLE `povezuje`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prijava`
--
ALTER TABLE `prijava`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `telefon`
--
ALTER TABLE `telefon`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `adresa`
--
ALTER TABLE `adresa`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `osoba`
--
ALTER TABLE `osoba`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `povezuje`
--
ALTER TABLE `povezuje`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;

--
-- AUTO_INCREMENT for table `prijava`
--
ALTER TABLE `prijava`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `telefon`
--
ALTER TABLE `telefon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
