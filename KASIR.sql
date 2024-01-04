-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 04, 2024 at 11:59 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `KASIR`
--

-- --------------------------------------------------------

--
-- Table structure for table `masakan`
--

CREATE TABLE `masakan` (
  `id_masakan` int(11) NOT NULL,
  `nama_masakan` varchar(50) NOT NULL,
  `harga` int(11) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `masakan`
--

INSERT INTO `masakan` (`id_masakan`, `nama_masakan`, `harga`, `status`) VALUES
(2, 'Nasi Goreng', 8000, 'Tersedia'),
(3, 'Capcay', 9000, 'Tersedia');

-- --------------------------------------------------------

--
-- Table structure for table `Meja`
--

CREATE TABLE `Meja` (
  `kode_meja` int(11) NOT NULL,
  `jml_kursi` int(11) NOT NULL,
  `harga_meja` int(11) NOT NULL,
  `status_meja` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Meja`
--

INSERT INTO `Meja` (`kode_meja`, `jml_kursi`, `harga_meja`, `status_meja`) VALUES
(1, 2, 10000, 'Tersedia'),
(2, 2, 10000, 'Terisi '),
(3, 4, 15000, 'Terisi '),
(4, 4, 15000, 'Tersedia'),
(5, 8, 30000, 'Terisi'),
(6, 8, 30000, 'Tersedia'),
(7, 8, 30000, 'Tersedia'),
(8, 8, 30000, 'Tersedia');

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pel` int(5) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `nama_pelanggan` varchar(30) NOT NULL,
  `id_masakan` int(11) NOT NULL,
  `kode_meja` int(11) NOT NULL,
  `jml_kursi` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `nama_masakan` varchar(30) NOT NULL,
  `harga` int(11) NOT NULL,
  `harga_meja` int(11) NOT NULL,
  `jumlah_beli` int(11) NOT NULL,
  `total_bayar` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `nama_pelanggan`, `id_masakan`, `kode_meja`, `jml_kursi`, `tanggal`, `nama_masakan`, `harga`, `harga_meja`, `jumlah_beli`, `total_bayar`) VALUES
(10, 'farel', 2, 1, 2, '2024-01-05', 'Nasi Goreng', 8000, 10000, 3, 34000),
(11, 'gurqon', 2, 3, 4, '2024-01-12', 'Nasi Goreng', 8000, 15000, 3, 39000),
(14, 'mikael', 2, 2, 2, '2024-01-02', 'Nasi Goreng', 8000, 10000, 3, 34000),
(16, 'kontl', 2, 5, 8, '2024-01-04', 'Nasi Goreng', 8000, 30000, 3, 54000);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `email` varchar(60) NOT NULL,
  `no_telp` varchar(30) NOT NULL,
  `level` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `username`, `password`, `email`, `no_telp`, `level`) VALUES
(1, 'Admin0', 'admin123', 'admin@pesanduduk', '0666', 'Admin'),
(2, 'Resto0', 'resto123', 'Pihak Resto', '', 'Admin'),
(5, 'tiyer', 'tiyer125', 'tiyerkomara@gmail.com', '081212345678', 'Admin'),
(16, 'jajang00', 'jajang123', 'email@yahoo.com', '091232123', 'Admin'),
(18, 'jojo00', 'admin123', 'jojo@yahoo.com', '01293123123', 'Pelanggan'),
(19, 'jojong', 'awok12', 'awok@aws.com', '0123912803', 'Pelanggan');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `masakan`
--
ALTER TABLE `masakan`
  ADD PRIMARY KEY (`id_masakan`);

--
-- Indexes for table `Meja`
--
ALTER TABLE `Meja`
  ADD PRIMARY KEY (`kode_meja`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pel`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD UNIQUE KEY `Id_meja` (`kode_meja`),
  ADD KEY `id_masakan` (`id_masakan`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `masakan`
--
ALTER TABLE `masakan`
  MODIFY `id_masakan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `Meja`
--
ALTER TABLE `Meja`
  MODIFY `kode_meja` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `pelanggan`
--
ALTER TABLE `pelanggan`
  MODIFY `id_pel` int(5) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
