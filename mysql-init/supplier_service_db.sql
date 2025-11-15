/*
 Navicat Premium Dump SQL

 Source Server         : MariaDB
 Source Server Type    : MariaDB
 Source Server Version : 100432 (10.4.32-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : supplier_service_db

 Target Server Type    : MariaDB
 Target Server Version : 100432 (10.4.32-MariaDB)
 File Encoding         : 65001

 Date: 15/11/2025 15:19:52
*/

CREATE DATABASE IF NOT EXISTS supplier_service_db;
USE supplier_service_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for suppliers
-- ----------------------------
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE `suppliers`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `active_status` tinyint(1) NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of suppliers
-- ----------------------------
INSERT INTO `suppliers` VALUES (1, 'Công ty TNHH An Lộc Phát', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (2, 'Nhà phân phối Sách và TBGD Minh Tâm', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (3, 'Tổng kho Văn phòng phẩm Á Châu', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (4, 'Công ty CP Hợp tác Quốc tế Sao Việt', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (5, 'Cửa hàng Dụng cụ Mỹ thuật ArtHub', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (6, 'Công ty TNHH Thương mại Dịch vụ FlexOffice', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (7, 'Nhà cung cấp Giấy in Thành Đạt', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (8, 'Công ty TNHH Smart Office Solutions', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (9, 'Đại lý VPP Hoàng Hà', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');
INSERT INTO `suppliers` VALUES (10, 'Công ty Nhập khẩu Thiết bị Nhật Bản J-Tech', 1, '2025-10-20 13:44:28', '2025-10-20 13:44:28');

SET FOREIGN_KEY_CHECKS = 1;
