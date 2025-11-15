/*
 Navicat Premium Dump SQL

 Source Server         : MariaDB
 Source Server Type    : MariaDB
 Source Server Version : 100432 (10.4.32-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : brand_service_db

 Target Server Type    : MariaDB
 Target Server Version : 100432 (10.4.32-MariaDB)
 File Encoding         : 65001

 Date: 15/11/2025 15:19:45
*/

CREATE DATABASE IF NOT EXISTS brand_service_db;
USE brand_service_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for brands
-- ----------------------------
DROP TABLE IF EXISTS `brands`;
CREATE TABLE `brands`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `active_status` tinyint(1) NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of brands
-- ----------------------------
INSERT INTO `brands` VALUES (8, 'Thiên Long', 1, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (9, 'Deli', 1, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (10, 'Hồng Hà', 2, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (11, 'Colokit', 2, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (12, 'KLONG', 3, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (13, 'Plus', 3, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (14, 'Pentel', 4, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (15, '3M (Post-it, Scotch)', 4, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (16, 'Staedtler', 5, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (17, 'Faber-Castell', 5, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (18, 'Bến Nghé', 6, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (19, 'Double A', 7, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (20, 'PaperOne', 7, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (21, 'Casio', 8, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (22, 'Tombow', 9, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (23, 'Zebra', 9, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (24, 'Pilot', 10, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');
INSERT INTO `brands` VALUES (25, 'Kokuyo', 10, 1, '2025-10-20 13:44:51.000000', '2025-10-20 13:44:51.000000');

SET FOREIGN_KEY_CHECKS = 1;
