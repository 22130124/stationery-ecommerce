/*
 Navicat Premium Dump SQL

 Source Server         : MariaDB
 Source Server Type    : MariaDB
 Source Server Version : 100432 (10.4.32-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : category_service_db

 Target Server Type    : MariaDB
 Target Server Version : 100432 (10.4.32-MariaDB)
 File Encoding         : 65001

 Date: 15/11/2025 15:19:26
*/

CREATE DATABASE IF NOT EXISTS category_service_db;
USE category_service_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_id` int(11) NULL DEFAULT NULL,
  `slug` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `active_status` tinyint(1) NULL DEFAULT 1,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE,
  UNIQUE INDEX `slug`(`slug`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE,
  CONSTRAINT `FKsaok720gsu4u2wrgbk10b5n8d` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, 'Bút', NULL, 'but', 1, '2025-10-20 13:03:22', '2025-10-20 13:03:24');
INSERT INTO `categories` VALUES (2, 'Bút bi', 1, 'but-bi', 1, '2025-10-20 13:03:38', '2025-10-20 13:03:38');
INSERT INTO `categories` VALUES (3, 'Bút chì', 1, 'but-chi', 1, '2025-10-20 13:03:56', '2025-10-20 13:03:56');
INSERT INTO `categories` VALUES (4, 'Bút lông', 1, 'but-long', 1, '2025-10-20 13:04:40', '2025-10-20 13:04:40');
INSERT INTO `categories` VALUES (5, 'Bút dạ quang', 1, 'but-da-quang', 1, '2025-10-20 13:05:01', '2025-10-20 13:05:01');
INSERT INTO `categories` VALUES (6, 'Sản phẩm về giấy', NULL, 'san-pham-ve-giay', 1, '2025-10-20 13:05:41', '2025-10-20 13:05:41');
INSERT INTO `categories` VALUES (7, 'Sổ các loại', 6, 'so-cac-loai', 1, '2025-10-20 13:06:02', '2025-10-20 13:06:02');
INSERT INTO `categories` VALUES (8, 'Tập - Vở', 6, 'tap-vo', 1, '2025-10-20 13:06:21', '2025-10-20 13:06:21');
INSERT INTO `categories` VALUES (9, 'Giấy ghi chú', 6, 'giay-ghi-chu', 1, '2025-10-20 13:06:42', '2025-10-20 13:06:44');
INSERT INTO `categories` VALUES (10, 'Nhãn vở - Nhãn tên', 6, 'nhan-vo-nhan-ten', 1, '2025-10-20 13:07:13', '2025-10-20 13:07:13');
INSERT INTO `categories` VALUES (11, 'Giấy kiểm tra', 6, 'giay-kiem-tra', 1, '2025-10-20 13:07:32', '2025-10-20 13:07:32');
INSERT INTO `categories` VALUES (12, 'Dụng cụ vẽ', NULL, 'dung-cu-ve', 1, '2025-10-20 13:07:54', '2025-10-20 13:07:54');
INSERT INTO `categories` VALUES (13, 'Cọ vẽ', 12, 'co-ve', 1, '2025-10-20 13:08:29', '2025-10-20 13:08:31');
INSERT INTO `categories` VALUES (14, 'Màu vẽ', 12, 'mau-ve', 1, '2025-10-20 13:08:47', '2025-10-20 13:08:47');
INSERT INTO `categories` VALUES (15, 'Khay vẽ', 12, 'khay-ve', 1, '2025-10-20 13:09:19', '2025-10-20 13:09:19');
INSERT INTO `categories` VALUES (16, 'Giá vẽ - Khung vẽ', 12, 'gia-ve-khung-ve', 1, '2025-10-20 13:09:58', '2025-10-20 13:09:58');
INSERT INTO `categories` VALUES (17, 'Tập vẽ - Giấy vẽ', 12, 'tap-ve-giay-ve', 1, '2025-10-20 13:10:21', '2025-10-20 13:10:21');
INSERT INTO `categories` VALUES (18, 'Dụng cụ học tập', NULL, 'dung-cu-hoc-tap', 1, '2025-10-20 13:11:18', '2025-10-20 13:11:18');
INSERT INTO `categories` VALUES (19, 'Gôm - Tẩy', 18, 'gom-tay', 1, '2025-10-20 13:13:27', '2025-10-20 13:13:27');
INSERT INTO `categories` VALUES (20, 'Bóp viết - Hộp bút', 18, 'bop-viet-hop-but', 1, '2025-10-20 13:13:27', '2025-10-20 13:13:27');
INSERT INTO `categories` VALUES (21, 'Gọt bút chì', 18, 'got-but-chi', 1, '2025-10-20 13:13:27', '2025-10-20 13:13:27');
INSERT INTO `categories` VALUES (22, 'Thước', 18, 'thuoc', 1, '2025-10-20 13:13:27', '2025-10-20 13:13:27');
INSERT INTO `categories` VALUES (23, 'Bảng viết', 18, 'bang-viet', 1, '2025-10-20 13:13:27', '2025-10-20 13:13:27');
INSERT INTO `categories` VALUES (24, 'Compa', 18, 'compa', 1, '2025-10-20 13:13:27', '2025-10-20 13:13:27');
INSERT INTO `categories` VALUES (25, 'Phấn - Hộp đựng phấn', 18, 'phan-hop-dung-phan', 1, '2025-10-20 13:13:27', '2025-10-20 13:13:27');

SET FOREIGN_KEY_CHECKS = 1;
