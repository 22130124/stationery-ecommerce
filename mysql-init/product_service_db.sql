/*
 Navicat Premium Dump SQL

 Source Server         : MariaDB
 Source Server Type    : MariaDB
 Source Server Version : 100432 (10.4.32-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : product_service_db

 Target Server Type    : MariaDB
 Target Server Version : 100432 (10.4.32-MariaDB)
 File Encoding         : 65001

 Date: 15/11/2025 15:19:35
*/

CREATE DATABASE IF NOT EXISTS product_service_db;
USE product_service_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for product_images
-- ----------------------------
DROP TABLE IF EXISTS `product_images`;
CREATE TABLE `product_images`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NULL DEFAULT NULL,
  `variant_id` int(11) NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `default_status` tinyint(1) NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `product_images_ibfk_1`(`product_id`) USING BTREE,
  INDEX `product_images_ibfk_2`(`variant_id`) USING BTREE,
  CONSTRAINT `FKqnq71xsohugpqwf3c9gxmsuy` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKqnqjv00ocaxfmu2k6b99ycdad` FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 223 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_images
-- ----------------------------
INSERT INTO `product_images` VALUES (1, 1, 1, 'https://placehold.co/600x600/3498db/ffffff.png?text=SP1001+Xanh', 1, '2025-10-20 19:17:04', '2025-10-20 19:17:04');
INSERT INTO `product_images` VALUES (2, 1, 2, 'https://placehold.co/600x600/e74c3c/ffffff.png?text=SP1001+Do', 0, '2025-10-20 19:17:06', '2025-10-20 19:17:06');
INSERT INTO `product_images` VALUES (3, 1, 3, 'https://placehold.co/600x600/34495e/ffffff.png?text=SP1001+Den', 0, '2025-10-20 19:17:08', '2025-10-20 19:17:08');
INSERT INTO `product_images` VALUES (4, 2, 4, 'https://placehold.co/600x600/34495e/ffffff.png?text=SP1002+Den', 1, '2025-10-20 19:17:09', '2025-10-20 19:17:09');
INSERT INTO `product_images` VALUES (5, 2, 5, 'https://placehold.co/600x600/2c3e50/ffffff.png?text=SP1002+XanhDen', 0, '2025-10-20 19:17:12', '2025-10-20 19:17:12');
INSERT INTO `product_images` VALUES (6, 2, 6, 'https://placehold.co/600x600/3498db/ffffff.png?text=SP1002+XanhDuong', 0, '2025-10-20 19:17:14', '2025-10-20 19:17:14');
INSERT INTO `product_images` VALUES (7, 3, 7, 'https://placehold.co/600x600/3498db/ffffff.png?text=SP1003+Xanh', 1, '2025-10-20 19:17:16', '2025-10-20 19:17:16');
INSERT INTO `product_images` VALUES (8, 3, 8, 'https://placehold.co/600x600/000000/ffffff.png?text=SP1003+Den', 0, '2025-10-20 19:17:17', '2025-10-20 19:17:17');
INSERT INTO `product_images` VALUES (9, 4, 9, 'https://placehold.co/600x600/2980b9/ffffff.png?text=SP1004+Xanh', 1, '2025-10-20 19:17:19', '2025-10-20 19:17:19');
INSERT INTO `product_images` VALUES (10, 4, 10, 'https://placehold.co/600x600/2c3e50/ffffff.png?text=SP1004+Den', 0, '2025-10-20 19:17:20', '2025-10-20 19:17:20');
INSERT INTO `product_images` VALUES (11, 5, 11, 'https://placehold.co/600x600/3498db/ffffff.png?text=SP1005', 1, '2025-10-20 19:17:22', '2025-10-20 19:17:22');
INSERT INTO `product_images` VALUES (12, 6, 12, 'https://placehold.co/600x600/f1c40f/000000.png?text=SP1006', 1, '2025-10-20 19:17:23', '2025-10-20 19:17:23');
INSERT INTO `product_images` VALUES (13, 7, 14, 'https://placehold.co/600x600/2c3e50/ffffff.png?text=SP1007', 1, '2025-10-20 19:17:25', '2025-10-20 19:17:25');
INSERT INTO `product_images` VALUES (14, 8, 15, 'https://placehold.co/600x600/27ae60/ffffff.png?text=SP1008', 1, '2025-10-20 19:17:28', '2025-10-20 19:17:28');
INSERT INTO `product_images` VALUES (15, 9, 16, 'https://placehold.co/600x600/c0392b/ffffff.png?text=SP1009', 1, '2025-10-20 19:17:29', '2025-10-20 19:17:29');
INSERT INTO `product_images` VALUES (16, 10, 17, 'https://placehold.co/600x600/7f8c8d/ffffff.png?text=SP1010+Std', 1, '2025-10-20 19:17:31', '2025-10-20 19:17:31');
INSERT INTO `product_images` VALUES (17, 10, 18, 'https://placehold.co/600x600/1abc9c/ffffff.png?text=SP1010+Mint', 0, '2025-10-20 19:17:33', '2025-10-20 19:17:33');
INSERT INTO `product_images` VALUES (18, 10, 19, 'https://placehold.co/600x600/ff7979/ffffff.png?text=SP1010+Pink', 0, '2025-10-20 19:17:34', '2025-10-20 19:17:34');
INSERT INTO `product_images` VALUES (19, 11, 20, 'https://placehold.co/600x600/3498db/ffffff.png?text=SP1011+Xanh', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (20, 11, 21, 'https://placehold.co/600x600/e74c3c/ffffff.png?text=SP1011+Do', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (21, 11, 22, 'https://placehold.co/600x600/2c3e50/ffffff.png?text=SP1011+Den', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (22, 12, 23, 'https://placehold.co/600x600/2980b9/ffffff.png?text=SP1012', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (23, 13, 24, 'https://placehold.co/600x600/f1c40f/000000.png?text=SP1013+Vang', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (24, 13, 25, 'https://placehold.co/600x600/fd79a8/ffffff.png?text=SP1013+Hong', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (25, 13, 26, 'https://placehold.co/600x600/2ecc71/ffffff.png?text=SP1013+Xanh', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (26, 14, 27, 'https://placehold.co/600x600/f1c40f/000000.png?text=SP1014', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (27, 15, 28, 'https://placehold.co/600x600/b2bec3/ffffff.png?text=SP1015+Gray', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (28, 15, 29, 'https://placehold.co/600x600/ff7675/ffffff.png?text=SP1015+Pink', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (29, 15, 30, 'https://placehold.co/600x600/55efc4/ffffff.png?text=SP1015+BlueGreen', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (30, 16, 31, 'https://placehold.co/600x600/95a5a6/ffffff.png?text=SP1016+Lined', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (31, 16, 32, 'https://placehold.co/600x600/95a5a6/ffffff.png?text=SP1016+Grid', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (32, 16, 33, 'https://placehold.co/600x600/95a5a6/ffffff.png?text=SP1016+Dot', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (33, 17, 34, 'https://placehold.co/600x600/3498db/ffffff.png?text=SP1017+Blue', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (34, 17, 35, 'https://placehold.co/600x600/e91e63/ffffff.png?text=SP1017+Pink', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (35, 17, 36, 'https://placehold.co/600x600/2ecc71/ffffff.png?text=SP1017+Green', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (36, 18, 37, 'https://placehold.co/600x600/3d3d3d/ffffff.png?text=SP1018+Den', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (37, 18, 38, 'https://placehold.co/600x600/8d6e63/ffffff.png?text=SP1018+Nau', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (38, 19, 39, 'https://placehold.co/600x600/1abc9c/ffffff.png?text=SP1019', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (39, 20, 41, 'https://placehold.co/600x600/0984e3/ffffff.png?text=SP1020+Xanh', 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (40, 20, 42, 'https://placehold.co/600x600/f9ca24/ffffff.png?text=SP1020+Vang', 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_images` VALUES (41, 21, 43, 'https://placehold.co/600x600/f1c40f/000000.png?text=SP1021+Vang', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (42, 21, 44, 'https://placehold.co/600x600/fd79a8/ffffff.png?text=SP1021+Hong', 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (43, 21, 45, 'https://placehold.co/600x600/55efc4/ffffff.png?text=SP1021+Xanh', 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (44, 22, 46, 'https://placehold.co/600x600/a29bfe/ffffff.png?text=SP1022', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (45, 23, 47, 'https://placehold.co/600x600/ecf0f1/2c3e50.png?text=SP1023', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (46, 24, 48, 'https://placehold.co/600x600/e67e22/ffffff.png?text=SP1024+12+Mau', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (47, 24, 49, 'https://placehold.co/600x600/e67e22/ffffff.png?text=SP1024+24+Mau', 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (48, 25, 50, 'https://placehold.co/600x600/3498db/ffffff.png?text=SP1025+12+Mau', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (49, 25, 51, 'https://placehold.co/600x600/3498db/ffffff.png?text=SP1025+24+Mau', 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (50, 26, 52, 'https://placehold.co/600x600/ecf0f1/000000.png?text=SP1026', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (51, 27, 53, 'https://placehold.co/600x600/34495e/ffffff.png?text=SP1027', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (52, 28, 54, 'https://placehold.co/600x600/34495e/ffffff.png?text=SP1028+Navy', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (53, 28, 55, 'https://placehold.co/600x600/95a5a6/ffffff.png?text=SP1028+Gray', 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (54, 29, 56, 'https://placehold.co/600x600/00a8ff/ffffff.png?text=SP1029+Xanh', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (55, 29, 57, 'https://placehold.co/600x600/e84393/ffffff.png?text=SP1029+Hong', 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (56, 30, 58, 'https://placehold.co/600x600/dfe6e9/000000.png?text=SP1030', 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_images` VALUES (57, 31, 59, 'https://placehold.co/600x600/16a085/ffffff.png?text=SP1031', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (58, 32, 60, 'https://placehold.co/600x600/bdc3c7/000000.png?text=SP1032', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (59, 33, 61, 'https://placehold.co/600x600/95a5a6/ffffff.png?text=SP1033', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (60, 34, 62, 'https://placehold.co/600x600/ecf0f1/2c3e50.png?text=SP1034+Trang', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (61, 34, 63, 'https://placehold.co/600x600/ecf0f1/2c3e50.png?text=SP1034+Mau', 0, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (62, 35, 64, 'https://placehold.co/600x600/a5b1c2/ffffff.png?text=SP1035', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (63, 36, 65, 'https://placehold.co/600x600/f6e58d/ffffff.png?text=SP1036', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (64, 37, 66, 'https://placehold.co/600x600/ff7f50/ffffff.png?text=SP1037', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (65, 38, 67, 'https://placehold.co/600x600/8e44ad/ffffff.png?text=SP1038+3co', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (66, 38, 68, 'https://placehold.co/600x600/8e44ad/ffffff.png?text=SP1038+5co', 0, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (67, 39, 69, 'https://placehold.co/600x600/ecf0f1/2c3e50.png?text=SP1039', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (68, 40, 70, 'https://placehold.co/600x600/d35400/ffffff.png?text=SP1040', 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_images` VALUES (69, 41, 71, 'https://placehold.co/600x600/ffffff/000000.png?text=SP1041+DoubleA', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (70, 42, 73, 'https://placehold.co/600x600/ffffff/000000.png?text=SP1042+PaperOne', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (71, 43, 74, 'https://placehold.co/600x600/1dd1a1/ffffff.png?text=SP1043+Xanh', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (72, 43, 75, 'https://placehold.co/600x600/ff9ff3/ffffff.png?text=SP1043+Hong', 0, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (73, 44, 76, 'https://placehold.co/600x600/feca57/ffffff.png?text=SP1044', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (74, 45, 78, 'https://placehold.co/600x600/54a0ff/ffffff.png?text=SP1045', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (75, 46, 79, 'https://placehold.co/600x600/485460/ffffff.png?text=SP1046', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (76, 47, 80, 'https://placehold.co/600x600/dcdde1/000000.png?text=SP1047', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (77, 48, 82, 'https://placehold.co/600x600/f39c12/ffffff.png?text=SP1048', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (78, 49, 83, 'https://placehold.co/600x600/341f97/ffffff.png?text=SP1049', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (79, 50, 84, 'https://placehold.co/600x600/ced6e0/000000.png?text=SP1050', 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_images` VALUES (80, 51, 85, 'https://placehold.co/600x600/2f3542/ffffff.png?text=SP1051', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (81, 52, 88, 'https://placehold.co/600x600/d1d8e0/000000.png?text=SP1052', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (82, 53, 89, 'https://placehold.co/600x600/0652DD/ffffff.png?text=SP1053', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (83, 54, 90, 'https://placehold.co/600x600/EA2027/ffffff.png?text=SP1054', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (84, 55, 91, 'https://placehold.co/600x600/0652DD/ffffff.png?text=SP1055+Xanh', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (85, 55, 92, 'https://placehold.co/600x600/1e272e/ffffff.png?text=SP1055+Den', 0, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (86, 56, 93, 'https://placehold.co/600x600/f9ca24/000000.png?text=SP1056', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (87, 57, 94, 'https://placehold.co/600x600/ff6b81/ffffff.png?text=SP1057', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (88, 58, 95, 'https://placehold.co/600x600/5352ed/ffffff.png?text=SP1058', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (89, 59, 96, 'https://placehold.co/600x600/00d2d3/ffffff.png?text=SP1059', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (90, 60, 97, 'https://placehold.co/600x600/ff4757/ffffff.png?text=SP1060', 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_images` VALUES (221, 100, 154, 'https://res.cloudinary.com/dmjlttgiu/image/upload/v1762965668/ecommerce/products/uwrch4fu5iyhnm93mj7s.jpg', 1, '2025-11-12 23:59:00', '2025-11-12 23:59:00');
INSERT INTO `product_images` VALUES (222, 100, 155, 'https://res.cloudinary.com/dmjlttgiu/image/upload/v1762965684/ecommerce/products/dy27ocvrqmugzz8dtbig.jpg', 1, '2025-11-12 23:59:00', '2025-11-12 23:59:00');

-- ----------------------------
-- Table structure for product_variants
-- ----------------------------
DROP TABLE IF EXISTS `product_variants`;
CREATE TABLE `product_variants`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `base_price` double NOT NULL,
  `discount_price` double NULL DEFAULT NULL,
  `active_status` tinyint(1) NOT NULL DEFAULT 1,
  `default_status` tinyint(1) NOT NULL,
  `created_at` datetime NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKchwb8x66br7hflxsaj4fim6e7`(`product_id`, `name`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  CONSTRAINT `FKosqitn4s405cynmhb87lkvuau` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_variants
-- ----------------------------
INSERT INTO `product_variants` VALUES (1, 1, 'Mực Xanh', 5000, 4500, 1, 1, '2025-10-20 19:14:35', '2025-10-20 19:14:35');
INSERT INTO `product_variants` VALUES (2, 1, 'Mực Đỏ', 5000, NULL, 1, 0, '2025-10-20 19:15:21', '2025-10-20 19:15:21');
INSERT INTO `product_variants` VALUES (3, 1, 'Mực Đen', 5000, NULL, 1, 0, '2025-10-20 19:15:29', '2025-10-20 19:15:29');
INSERT INTO `product_variants` VALUES (4, 2, 'Mực Đen', 25000, 22000, 1, 1, '2025-10-20 19:15:33', '2025-10-20 19:15:33');
INSERT INTO `product_variants` VALUES (5, 2, 'Mực Xanh Đen', 25000, NULL, 1, 0, '2025-10-20 19:15:38', '2025-10-20 19:15:38');
INSERT INTO `product_variants` VALUES (6, 2, 'Mực Xanh Dương', 25000, NULL, 1, 0, '2025-10-20 19:15:46', '2025-10-20 19:15:46');
INSERT INTO `product_variants` VALUES (7, 3, '0.7mm - Mực Xanh', 14000, NULL, 1, 1, '2025-10-20 19:15:46', '2025-10-20 19:15:46');
INSERT INTO `product_variants` VALUES (8, 3, '0.7mm - Mực Đen', 14000, NULL, 1, 0, '2025-10-20 19:15:50', '2025-10-20 19:15:50');
INSERT INTO `product_variants` VALUES (9, 4, 'Xanh', 6000, NULL, 1, 1, '2025-10-20 19:15:52', '2025-10-20 19:15:52');
INSERT INTO `product_variants` VALUES (10, 4, 'Đen', 6000, NULL, 1, 0, '2025-10-20 19:15:54', '2025-10-20 19:15:54');
INSERT INTO `product_variants` VALUES (11, 5, 'Xanh', 3000, NULL, 1, 1, '2025-10-20 19:15:56', '2025-10-20 19:15:56');
INSERT INTO `product_variants` VALUES (12, 6, '1 Cây', 8000, NULL, 1, 1, '2025-10-20 19:15:57', '2025-10-20 19:15:57');
INSERT INTO `product_variants` VALUES (13, 6, 'Hộp 12 Cây', 90000, 85000, 1, 0, '2025-10-20 19:15:58', '2025-10-20 19:15:58');
INSERT INTO `product_variants` VALUES (14, 7, 'Ngòi 0.5mm', 85000, NULL, 1, 1, '2025-10-20 19:16:00', '2025-10-20 19:16:00');
INSERT INTO `product_variants` VALUES (15, 8, 'Hộp 12 Cây', 72000, NULL, 1, 1, '2025-10-20 19:16:01', '2025-10-20 19:16:01');
INSERT INTO `product_variants` VALUES (16, 9, '1 Hộp', 20000, NULL, 1, 1, '2025-10-20 19:16:03', '2025-10-20 19:16:03');
INSERT INTO `product_variants` VALUES (17, 10, 'Standard', 135000, NULL, 1, 1, '2025-10-20 19:16:05', '2025-10-20 19:16:05');
INSERT INTO `product_variants` VALUES (18, 10, 'Xanh Bạc Hà', 135000, NULL, 1, 0, '2025-10-20 19:16:07', '2025-10-20 19:16:07');
INSERT INTO `product_variants` VALUES (19, 10, 'Hồng Anh Đào', 135000, NULL, 1, 0, '2025-10-20 19:16:08', '2025-10-20 19:16:08');
INSERT INTO `product_variants` VALUES (20, 11, 'Màu Xanh', 9000, NULL, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (21, 11, 'Màu Đỏ', 9000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (22, 11, 'Màu Đen', 9000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (23, 12, 'Xanh', 7000, NULL, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (24, 13, 'Vàng', 18000, NULL, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (25, 13, 'Hồng', 18000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (26, 13, 'Xanh lá', 18000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (27, 14, 'Vàng', 8000, NULL, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (28, 15, 'Mild Gray', 30000, 28000, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (29, 15, 'Mild Pink', 30000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (30, 15, 'Mild Blue Green', 30000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (31, 16, 'Ruột Kẻ ngang (Lined)', 45000, 42000, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (32, 16, 'Ruột Ô ly (Grid)', 45000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (33, 16, 'Ruột Chấm (Dot)', 45000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (34, 17, 'Bìa Xanh Dương - 80 trang', 38000, NULL, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (35, 17, 'Bìa Hồng - 80 trang', 38000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (36, 17, 'Bìa Xanh Lá - 80 trang', 38000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (37, 18, 'Bìa Đen - 240 trang', 95000, NULL, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (38, 18, 'Bìa Nâu - 240 trang', 95000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (39, 19, '1 Cuốn', 8000, NULL, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (40, 19, 'Lốc 10 Cuốn', 75000, 72000, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (41, 20, 'Bìa Xanh - 30 tờ', 18000, NULL, 1, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (42, 20, 'Bìa Vàng - 30 tờ', 18000, NULL, 1, 0, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `product_variants` VALUES (43, 21, 'Vàng Neon', 15000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (44, 21, 'Hồng Neon', 15000, NULL, 1, 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (45, 21, 'Xanh Neon', 15000, NULL, 1, 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (46, 22, '4 Màu', 12000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (47, 23, 'Tệp 20 tờ', 15000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (48, 24, 'Hộp 12 Màu', 28000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (49, 24, 'Hộp 24 Màu', 52000, NULL, 1, 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (50, 25, 'Bộ 12 Màu', 120000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (51, 25, 'Bộ 24 Màu', 220000, NULL, 1, 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (52, 26, 'Màu Trắng', 12000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (53, 27, '1 Cục', 25000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (54, 28, 'Màu Xanh Navy', 180000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (55, 28, 'Màu Xám', 180000, NULL, 1, 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (56, 29, 'Màu Xanh', 45000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (57, 29, 'Màu Hồng', 45000, NULL, 1, 0, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (58, 30, 'Bộ 4 Món', 15000, NULL, 1, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `product_variants` VALUES (59, 31, 'Size A4', 25000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (60, 32, 'Bộ Compa', 150000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (61, 33, '1 Cái', 18000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (62, 34, 'Hộp 10 viên - Phấn trắng', 10000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (63, 34, 'Hộp 10 viên - Phấn màu', 12000, NULL, 1, 0, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (64, 35, 'Tệp 18 nhãn', 5000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (65, 36, '1 Cuốn', 15000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (66, 37, '1 Tệp', 75000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (67, 38, 'Bộ 3 Cọ Cơ Bản', 80000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (68, 38, 'Bộ 5 Cọ Đa Năng', 125000, NULL, 1, 0, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (69, 39, '1 Cái', 12000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (70, 40, '1 Cái', 95000, NULL, 1, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `product_variants` VALUES (71, 41, '1 Ram 500 tờ', 65000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (72, 41, 'Thùng 5 Ram', 320000, NULL, 1, 0, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (73, 42, '1 Ram 500 tờ', 62000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (74, 43, 'Màu Xanh', 25000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (75, 43, 'Màu Hồng', 25000, NULL, 1, 0, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (76, 44, 'Thỏi 8g', 8000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (77, 44, 'Thỏi 21g', 15000, NULL, 1, 0, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (78, 45, '1 Cây', 20000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (79, 46, '1 Máy', 650000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (80, 47, 'Xấp 10 cái', 15000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (81, 47, 'Xấp 100 cái', 120000, NULL, 1, 0, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (82, 48, '1 Cây', 10000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (83, 49, 'Màu Ngẫu nhiên', 35000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (84, 50, '1 Hộp nhỏ', 5000, NULL, 1, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `product_variants` VALUES (85, 51, 'Size 19mm - Hộp 12 cái', 12000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (86, 51, 'Size 25mm - Hộp 12 cái', 18000, NULL, 1, 0, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (87, 51, 'Size 32mm - Hộp 12 cái', 25000, NULL, 1, 0, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (88, 52, '1 Cuộn', 15000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (89, 53, 'Màu Xanh Dương', 65000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (90, 54, '1 Cây', 45000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (91, 55, 'Màu Xanh', 80000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (92, 55, 'Màu Đen', 80000, NULL, 1, 0, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (93, 56, 'Xấp 100 tờ', 85000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (94, 57, 'Hộp 12 màu', 45000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (95, 58, '1 Cuốn 52 tuần', 55000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (96, 59, 'Màu Ngẫu nhiên', 8000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (97, 60, 'Bộ 12 màu', 38000, NULL, 1, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `product_variants` VALUES (154, 100, 'Màu đen', 3, NULL, 1, 1, '2025-11-12 23:41:37', '2025-11-12 23:59:00');
INSERT INTO `product_variants` VALUES (155, 100, 'Màu đỏ', 3, NULL, 1, 0, '2025-11-12 23:41:37', '2025-11-12 23:59:00');

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `category_id` int(11) NULL DEFAULT NULL,
  `supplier_id` int(255) NULL DEFAULT NULL,
  `brand_id` int(255) NULL DEFAULT NULL,
  `origin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `slug` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `rating` double NULL DEFAULT NULL,
  `active_status` tinyint(1) NULL DEFAULT 1,
  `created_at` datetime NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE,
  UNIQUE INDEX `slug`(`slug`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (1, 'SP1001', 'Bút bi Thiên Long TL-027', '**Bút bi Thiên Long TL-027** là dòng sản phẩm kinh điển, phù hợp với mọi đối tượng từ học sinh, sinh viên đến nhân viên văn phòng.\n\n#### Đặc điểm nổi bật:\n- Đầu bi *0.5mm* siêu bền, sản xuất tại Thụy Sĩ.\n- Mực ra đều, viết êm tay, không gây tắc mực.\n- Thi', 2, 1, 8, 'Việt Nam', 'but-bi-thien-long-tl-027', 4.8, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (2, 'SP1002', 'Bút bi gel Zebra Sarasa Clip 0.5mm', '**Zebra Sarasa Clip** là dòng bút gel cao cấp từ Nhật Bản, nổi tiếng với công nghệ mực Sarasa độc quyền.\n\n#### Đặc điểm nổi bật:\n- Mực gel *khô nhanh*, chống lem, phù hợp cho người viết tay trái.\n- Màu mực rực rỡ, sắc nét.\n- Kẹp bút gài tiện lợi.', 2, 9, 23, 'Nhật Bản', 'but-bi-gel-zebra-sarasa-clip-05mm', 4.9, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (3, 'SP1003', 'Bút bi Pilot Super Grip', '**Pilot Super Grip** - dòng bút bi bấm kinh điển đến từ Nhật Bản, mang lại trải nghiệm viết mượt mà và thoải mái.\n\n- Đệm cao su mềm mại, chống trơn trượt.\n- Mực chất lượng cao, đều và đậm.', 2, 10, 24, 'Nhật Bản', 'but-bi-pilot-super-grip', 4.8, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (4, 'SP1004', 'Bút bi Deli S656', 'Bút bi bấm **Deli S656** với thiết kế hiện đại, phù hợp cho môi trường văn phòng chuyên nghiệp.\n\n- Ngòi 0.7mm cho nét chữ rõ ràng.\n- Mực trơn tru, không vón cục.\n- Thân bút bằng nhựa ABS bền.', 2, 1, 9, 'Trung Quốc', 'but-bi-deli-s656', 4.5, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (5, 'SP1005', 'Bút bi Bến Nghé B01', 'Bút bi **Bến Nghé B01** là sản phẩm quen thuộc, chất lượng ổn định và giá cả phải chăng.\n\n- Đầu bi 0.8mm.\n- Thân bút trong suốt, dễ dàng theo dõi lượng mực.', 2, 6, 18, 'Việt Nam', 'but-bi-ben-nghe-b01', 4.4, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (6, 'SP1006', 'Bút chì gỗ Staedtler Noris 120-2B', 'Dòng bút chì chất lượng cao từ Đức, **Staedtler Noris** là sự lựa chọn hàng đầu cho việc viết, vẽ và phác thảo.\n\n- Ruột chì *2mm* chống gãy vượt trội.\n- Dễ dàng tẩy sạch và chuốt gọt.', 3, 5, 16, 'Đức', 'but-chi-go-staedtler-noris-120-2b', 4.7, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (7, 'SP1007', 'Bút chì kim Pentel A205', '**Pentel A205** là dòng bút chì kim chuyên nghiệp và bền bỉ.\n\n- Thiết kế tinh tế, trọng lượng nhẹ.\n- Đầu bút bằng kim loại giúp bảo vệ ngòi chì.', 3, 4, 14, 'Nhật Bản', 'but-chi-kim-pentel-a205', 4.9, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (8, 'SP1008', 'Bút chì gỗ Faber-Castell 2B', '**Faber-Castell** - Thương hiệu bút chì lâu đời và uy tín của Đức.\n\n- Gỗ mềm, dễ chuốt.\n- Ruột chì 2B đậm vừa phải, ít bụi, phù hợp cho cả viết và vẽ.', 3, 5, 17, 'Đức', 'but-chi-go-faber-castell-2b', 4.8, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (9, 'SP1009', 'Ruột chì kim Pentel 0.5mm 2B', 'Ruột chì **Pentel Hi-Polymer Super** cao cấp, siêu dẻo và bền, hạn chế tối đa gãy ngòi.\n\n- **Độ cứng:** 2B\n- **Cỡ ngòi:** 0.5mm\n- Một hộp gồm 40 ruột chì.', 3, 4, 14, 'Nhật Bản', 'ruot-chi-kim-pentel-0-5mm-2b', 4.9, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (10, 'SP1010', 'Bút chì bấm Tombow Monograph 0.5mm', 'Bút chì bấm cơ học **Tombow Monograph** với nhiều tính năng độc đáo.\n\n- **Cơ chế lắc:** Lắc bút để ruột chì tự động ra.\n- Gôm xoay dài và dễ dàng thay thế.\n- Thiết kế thời trang, nhiều màu sắc.', 3, 9, 22, 'Nhật Bản', 'but-chi-bam-tombow-monograph-0-5mm', 5, 1, '2025-10-20 18:29:34', '2025-10-20 18:29:34');
INSERT INTO `products` VALUES (11, 'SP1011', 'Bút lông dầu Thiên Long PM-09', 'Bút lông dầu **Thiên Long PM-09** là loại bút ghi được trên nhiều bề mặt khác nhau như giấy, gỗ, da, nhựa, thủy tinh, kim loại, gốm, sứ...\n\n#### Đặc điểm nổi bật:\n- Mực đậm, nhanh khô và không lem.\n- Ngòi bút 2.5mm cho nét viết rõ ràng.\n- Không chứa Xylen', 4, 1, 8, 'Việt Nam', 'but-long-dau-thien-long-pm-09', 4.7, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (12, 'SP1012', 'Bút lông viết bảng Deli', 'Bút lông viết bảng trắng **Deli**, dễ dàng lau sạch và không để lại vết mực.\n\n- Mực tươi sáng, không độc hại.\n- Thiết kế đầu bút êm, viết trơn tru.', 4, 1, 9, 'Trung Quốc', 'but-long-viet-bang-deli', 4.6, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (13, 'SP1013', 'Bút dạ quang Staedtler Textsurfer Classic', 'Bút dạ quang cao cấp **Staedtler Textsurfer** từ Đức.\n\n- Công nghệ INK·JET SAFE, không làm lem chữ in phun.\n- Mực pigment gốc nước, màu sắc rực rỡ.\n- Có thể mở nắp trong nhiều giờ mà không bị khô mực.', 5, 5, 16, 'Đức', 'but-da-quang-staedtler-textsurfer-classic', 4.9, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (14, 'SP1014', 'Bút dạ quang Thiên Long HL-02', 'Bút dạ quang **Thiên Long HL-02** là lựa chọn phổ biến và tiết kiệm.\n\n- Mực tươi sáng, làm nổi bật thông tin hiệu quả.\n- Đầu bút vát, dễ dàng tạo nét to nhỏ.', 5, 1, 8, 'Việt Nam', 'but-da-quang-thien-long-hl-02', 4.6, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (15, 'SP1015', 'Bút dạ quang Zebra Mildliner', 'Dòng bút highlight **Zebra Mildliner** nổi tiếng với tông màu pastel nhẹ nhàng, không gây chói mắt.\n\n- Thiết kế 2 đầu: 1 đầu vát và 1 đầu tròn.\n- Phù hợp để đánh dấu, trang trí sổ và bullet journal.', 5, 9, 23, 'Nhật Bản', 'but-da-quang-zebra-mildliner', 5, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (16, 'SP1016', 'Sổ lò xo KLONG A5 MS 956', '**Sổ lò xo KLONG MS 956** là người bạn đồng hành lý tưởng cho việc ghi chép.\n\n- Giấy định lượng *70 gsm*, trắng kem tự nhiên.\n- Gáy lò xo kép chắc chắn.\n- Kích thước: A5 - 200 trang.', 7, 3, 12, 'Việt Nam', 'so-lo-xo-klong-a5-ms-956', 4.9, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (17, 'SP1017', 'Sổ ghi chép Kokuyo Campus A5', 'Dòng sổ ghi chép thông minh và chất lượng cao từ **Kokuyo**, thương hiệu hàng đầu Nhật Bản.\n\n- Giấy viết mượt mà, phù hợp với nhiều loại bút.\n- Gáy sổ được may chắc chắn, có thể mở phẳng 180 độ.', 7, 10, 25, 'Nhật Bản', 'so-ghi-chep-kokuyo-campus-a5', 5, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (18, 'SP1018', 'Sổ bìa da Hồng Hà A5', 'Sổ bìa da **Hồng Hà** mang lại vẻ ngoài sang trọng và chuyên nghiệp, phù hợp cho các cuộc họp và ghi chép công việc.\n\n- Bìa da PU cao cấp.\n- Giấy kẻ ngang, định lượng 80 gsm.', 7, 2, 10, 'Việt Nam', 'so-bia-da-hong-ha-a5', 4.8, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (19, 'SP1019', 'Tập học sinh 96 trang 4 ô ly Hồng Hà', 'Sản phẩm vở kẻ ngang của **Hồng Hà** đã gắn liền với nhiều thế hệ học sinh Việt Nam.\n\n- Giấy trắng tự nhiên, chống lóa, bảo vệ mắt.\n- Dòng kẻ 4 ô ly rõ nét, phù hợp cho học sinh tiểu học.\n- Bìa vở thiết kế với hình ảnh ngộ nghĩnh.', 8, 2, 10, 'Việt Nam', 'tap-hoc-sinh-96-trang-hong-ha', 4.6, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (20, 'SP1020', 'Vở Campus B5 Grid Kokuyo', 'Vở **Campus** của Kokuyo với chất lượng giấy hàng đầu Nhật Bản, mang lại trải nghiệm viết tuyệt vời.\n\n- Giấy kẻ ô vuông (Grid) 5mm, thuận tiện cho việc vẽ biểu đồ, kẻ bảng.\n- Gáy may chắc chắn, có thể mở phẳng.', 8, 10, 25, 'Nhật Bản', 'vo-campus-b5-grid-kokuyo', 5, 1, '2025-10-20 20:15:56', '2025-10-20 20:15:56');
INSERT INTO `products` VALUES (21, 'SP1021', 'Giấy ghi chú Post-it 3M 654', 'Giấy ghi chú **Post-it** chính hãng từ 3M, giúp bạn ghi lại những ý tưởng và nhắc nhở quan trọng.\n\n#### Đặc điểm nổi bật:\n- **Kích thước:** 3x3 inch (76mm x 76mm).\n- **Số tờ:** 100 tờ/xấp.\n- Keo dính chắc chắn, có thể dán và gỡ ra nhiều lần mà không để lạ', 9, 4, 15, 'Mỹ', 'giay-ghi-chu-post-it-3m-654', 4.9, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (22, 'SP1022', 'Giấy ghi chú Deli A001', 'Giấy note **Deli A001** với nhiều màu sắc bắt mắt, giúp bạn phân loại công việc một cách dễ dàng.\n\n- **Kích thước:** 76mm x 76mm.\n- Chất lượng giấy tốt, viết êm.', 9, 1, 9, 'Trung Quốc', 'giay-ghi-chu-deli-a001', 4.5, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (23, 'SP1023', 'Giấy kiểm tra Campus A4', 'Giấy kiểm tra **Campus** với chất lượng giấy cao cấp, giúp bài làm của bạn trông sạch sẽ và chuyên nghiệp hơn.\n\n- Định lượng giấy *80 gsm*, dày dặn, chống lem mực.\n- Dòng kẻ ngang rõ nét.', 11, 10, 25, 'Nhật Bản', 'giay-kiem-tra-campus-a4', 5, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (24, 'SP1024', 'Bút sáp dầu Colokit CR-C01', 'Bút sáp dầu **Colokit** cho màu sắc tươi sáng, mịn màng và độ che phủ tốt.\n\n- An toàn cho trẻ em, không chứa chất độc hại.\n- Thân bút được bọc giấy giúp giữ tay sạch sẽ.\n- Dễ dàng phối trộn màu sắc.', 14, 2, 11, 'Việt Nam', 'but-sap-dau-colokit-cr-c01', 4.7, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (25, 'SP1025', 'Màu nước Faber-Castell', 'Bộ màu nước dạng nén **Faber-Castell** chất lượng cao, màu sắc tươi sáng và có độ trong.\n\n- Hộp nhựa tiện lợi, kèm theo 1 cọ vẽ.\n- Màu dễ dàng hòa tan trong nước.', 14, 5, 17, 'Đức', 'mau-nuoc-faber-castell', 4.9, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (26, 'SP1026', 'Gôm tẩy Pentel ZEH10', 'Gôm tẩy siêu sạch **Pentel Hi-Polymer Ain**, tẩy sạch vết chì mà không làm rách giấy hay để lại nhiều vụn.\n\n- Chất liệu polymer cao cấp.\n- Kích thước nhỏ gọn, tiện lợi.\n- Vỏ bọc giấy giúp giữ gôm luôn sạch sẽ.', 19, 4, 14, 'Nhật Bản', 'gom-tay-pentel-zeh10', 4.8, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (27, 'SP1027', 'Gôm đen Tombow Mono Dust Catch', 'Gôm đen **Tombow Mono Dust Catch** với công nghệ đặc biệt giúp vụn gôm dính vào nhau, giữ cho bàn làm việc luôn sạch sẽ.\n\n- Tẩy hiệu quả trên nhiều loại giấy.\n- Màu đen giúp gôm ít bị bẩn.', 19, 9, 22, 'Nhật Bản', 'gom-den-tombow-mono-dust-catch', 5, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (28, 'SP1028', 'Hộp bút vải Kokuyo C2', 'Hộp bút **Kokuyo C2** có thiết kế độc đáo, có thể đứng vững trên bàn và mở rộng miệng để lấy đồ dùng dễ dàng.\n\n- Chất liệu vải bền, đường may chắc chắn.\n- Sức chứa lớn, có thể đựng nhiều bút và dụng cụ.', 20, 10, 25, 'Nhật Bản', 'hop-but-vai-kokuyo-c2', 4.9, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (29, 'SP1029', 'Gọt bút chì Deli 0635', 'Gọt bút chì quay tay **Deli 0635** giúp chuốt chì nhanh, nhọn và không bị gãy ruột.\n\n- Hộc chứa vụn chì lớn, dễ dàng tháo lắp để vệ sinh.\n- Lưỡi dao bằng thép không gỉ, sắc bén và bền bỉ.', 21, 1, 9, 'Trung Quốc', 'got-but-chi-deli-0635', 4.7, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (30, 'SP1030', 'Bộ eke thước thẳng đo độ Deli', 'Bộ 4 dụng cụ thước kẻ **Deli** bằng nhựa cứng trong suốt, cần thiết cho môn Toán và Hình học.\n\n#### Bộ sản phẩm bao gồm:\n- 1 thước thẳng 15cm\n- 1 eke 45 độ\n- 1 eke 60 độ\n- 1 thước đo góc 180 độ', 22, 1, 9, 'Trung Quốc', 'bo-eke-thuoc-thang-do-do-deli', 4.6, 1, '2025-10-20 20:18:47', '2025-10-20 20:18:47');
INSERT INTO `products` VALUES (31, 'SP1031', 'Bảng học sinh Thiên Long B-016', 'Bảng viết 2 mặt **Thiên Long B-016**, một mặt viết bút lông và một mặt viết phấn, rất tiện lợi cho học sinh.\n\n#### Đặc điểm nổi bật:\n- Bề mặt bảng mịn, viết êm, dễ dàng lau sạch.\n- Có đường kẻ ô ly mờ giúp bé luyện chữ.\n- Kích thước nhỏ gọn, phù hợp cho l', 23, 1, 8, 'Việt Nam', 'bang-hoc-sinh-thien-long-b-016', 4.8, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (32, 'SP1032', 'Compa Staedtler Mars Comfort 552', '**Staedtler Mars Comfort 552** là compa kỹ thuật chuyên nghiệp, chính xác và bền bỉ.\n\n- Có nút nhấn điều chỉnh nhanh, tay cầm chắc chắn.\n- Đầu kim an toàn, không gây nguy hiểm.\n- Bao gồm hộp đựng và ngòi chì thay thế.', 24, 5, 16, 'Đức', 'compa-staedtler-mars-comfort-552', 4.9, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (33, 'SP1033', 'Compa học sinh Deli E9602', 'Compa học sinh **Deli E9602** bằng kim loại, thiết kế đơn giản và dễ sử dụng, là dụng cụ không thể thiếu trong môn toán.\n\n- Kèm theo hộp ngòi chì dự phòng.\n- Vít điều chỉnh chắc chắn.', 24, 1, 9, 'Trung Quốc', 'compa-hoc-sinh-deli-e9602', 4.5, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (34, 'SP1034', 'Phấn không bụi Hồng Hà', 'Phấn viết bảng **Hồng Hà** được sản xuất từ nguyên liệu cao cấp, đảm bảo ít bụi, an toàn cho sức khỏe người sử dụng.\n\n- Nét viết rõ ràng, mịn và êm.\n- Dễ dàng xóa sạch bằng khăn lau ẩm.', 25, 2, 10, 'Việt Nam', 'phan-khong-bui-hong-ha', 4.7, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (35, 'SP1035', 'Nhãn vở KLONG MS 766', 'Nhãn vở **KLONG** được thiết kế sẵn với các ô thông tin cần thiết, giúp học sinh trình bày sách vở một cách khoa học và sạch đẹp.\n\n- Keo dán sẵn, độ bám dính tốt.\n- Giấy decal chất lượng, dễ dàng ghi thông tin.', 10, 3, 12, 'Việt Nam', 'nhan-vo-klong-ms-766', 4.8, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (36, 'SP1036', 'Tập vẽ A4 KLONG', 'Tập vẽ **KLONG** khổ A4 với chất lượng giấy tốt, phù hợp cho việc vẽ phác thảo bằng bút chì hoặc tô màu sáp.\n\n- **Định lượng:** 100 gsm.\n- **Số tờ:** 20 tờ.\n- Gáy dán chắc chắn, dễ dàng xé rời từng tờ.', 17, 3, 12, 'Việt Nam', 'tap-ve-a4-klong', 4.7, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (37, 'SP1037', 'Giấy vẽ màu nước Happy A5', 'Giấy vẽ màu nước **Happy** được làm từ 100% bột giấy cellulose, vân Cold Press, phù hợp cho người mới bắt đầu.\n\n- **Định lượng:** 300 gsm, giấy dày, không bị cong vênh khi vẽ.\n- **Kích thước:** A5.\n- **Số tờ:** 20 tờ.', 17, 3, 12, 'Việt Nam', 'giay-ve-mau-nuoc-happy-a5', 4.9, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (38, 'SP1038', 'Bộ cọ vẽ Staedtler', 'Bộ cọ vẽ chất lượng cao **Staedtler** với lông cọ mềm, ngậm nước tốt, phù hợp cho nhiều loại màu như màu nước, acrylic.\n\n- Thân cọ bằng gỗ, dễ cầm nắm.\n- Đầu cọ đa dạng: đầu tròn, đầu dẹt.', 13, 5, 16, 'Đức', 'bo-co-ve-staedtler', 4.8, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (39, 'SP1039', 'Khay pha màu Colokit', 'Khay pha màu (palette) **Colokit** bằng nhựa, nhẹ và dễ dàng vệ sinh sau khi sử dụng.\n\n- Thiết kế nhiều ô tròn nhỏ để chứa màu và 1 ô lớn để pha trộn.\n- Có lỗ xỏ ngón tay cái để cầm tiện lợi.', 15, 2, 11, 'Việt Nam', 'khay-pha-mau-colokit', 4.6, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (40, 'SP1040', 'Giá vẽ tranh bằng gỗ để bàn', 'Giá vẽ mini bằng gỗ thông, phù hợp để trưng bày tranh, ảnh hoặc vẽ các tác phẩm kích thước nhỏ.\n\n- **Chất liệu:** Gỗ thông tự nhiên.\n- **Kích thước:** Cao 40cm.\n- Có thể gấp gọn, dễ dàng di chuyển và cất giữ.', 16, 5, 17, 'Việt Nam', 'gia-ve-tranh-bang-go-de-ban', 4.7, 1, '2025-10-20 20:22:01', '2025-10-20 20:22:01');
INSERT INTO `products` VALUES (41, 'SP1041', 'Giấy in A4 Double A 70gsm', 'Giấy in **Double A** nổi tiếng với chất lượng vượt trội, là lựa chọn hàng đầu cho các nhu cầu in ấn văn phòng và cá nhân.\n\n#### Đặc điểm nổi bật:\n- **Định lượng:** 70 gsm.\n- **Đóng gói:** 500 tờ/ram.\n- Bề mặt giấy láng mịn, trắng sáng, không chứa axit.\n- ', 6, 7, 19, 'Thái Lan', 'giay-in-a4-double-a-70gsm', 4.9, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (42, 'SP1042', 'Giấy in A4 PaperOne 70gsm', 'Giấy in **PaperOne** với công nghệ ProDigi™ HD Print Technology, mang lại bản in sắc nét và màu sắc sống động hơn.\n\n- **Định lượng:** 70 gsm.\n- Độ trắng cao, giúp làm nổi bật văn bản và hình ảnh.\n- Thân thiện với môi trường.', 6, 7, 20, 'Indonesia', 'giay-in-a4-paperone-70gsm', 4.8, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (43, 'SP1043', 'Băng xóa Plus V', 'Băng xóa **Plus V** thiết kế thông minh với đầu xóa linh hoạt, giúp xóa nhẹ nhàng và chính xác.\n\n- Băng xóa chất lượng cao, độ che phủ tốt.\n- Ruột thay thế dễ dàng, tiết kiệm chi phí.', 18, 3, 13, 'Nhật Bản', 'bang-xoa-plus-v', 4.8, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (44, 'SP1044', 'Keo khô dạng thỏi Deli', 'Keo khô dạng thỏi **Deli** tiện lợi, sạch sẽ, phù hợp cho các công việc thủ công, dán giấy tờ.\n\n- Keo dán chắc, khô nhanh.\n- Không chứa hóa chất độc hại, an toàn cho trẻ em.', 18, 1, 9, 'Trung Quốc', 'keo-kho-dang-thoi-deli', 4.6, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (45, 'SP1045', 'Kéo văn phòng Thiên Long SC-05', 'Kéo văn phòng **Thiên Long SC-05** với lưỡi kéo bằng thép không gỉ sắc bén và tay cầm bằng nhựa thoải mái.\n\n- Cắt được nhiều loại vật liệu như giấy, vải, bìa cứng mỏng.\n- Kích thước vừa phải, dễ sử dụng.', 18, 1, 8, 'Việt Nam', 'keo-van-phong-thien-long-sc-05', 4.7, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (46, 'SP1046', 'Máy tính Casio FX-580VN X', 'Máy tính khoa học **Casio FX-580VN X** là công cụ hỗ trợ đắc lực cho học sinh cấp 2, 3 và sinh viên.\n\n- **Số tính năng:** 521 tính năng.\n- Màn hình LCD độ phân giải cao, hiển thị rõ nét.\n- Được phép mang vào phòng thi theo quy định của Bộ Giáo dục và Đào ', 18, 8, 21, 'Thái Lan', 'may-tinh-casio-fx-580vn-x', 5, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (47, 'SP1047', 'Bìa lá A4 Thiên Long', 'Bìa lá (Clear Holder) **Thiên Long** giúp bảo quản và lưu trữ tài liệu một cách gọn gàng, tránh bụi bẩn và ẩm ướt.\n\n- Nhựa PP trong suốt, độ bền cao.\n- Phù hợp với giấy tờ khổ A4.', 18, 1, 8, 'Việt Nam', 'bia-la-a4-thien-long', 4.6, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (48, 'SP1048', 'Dao rọc giấy Deli 2058', 'Dao rọc giấy **Deli 2058** với thiết kế nhỏ gọn, an toàn và lưỡi dao sắc bén.\n\n- Vỏ nhựa ABS, có khóa an toàn tự động.\n- Lưỡi dao bằng thép carbon cao, dễ dàng thay thế.', 18, 1, 9, 'Trung Quốc', 'dao-roc-giay-deli-2058', 4.7, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (49, 'SP1049', 'Bấm kim số 10 Plus', 'Bấm kim (dập ghim) **Plus** số 10 nhỏ gọn, trợ lực tốt, giúp bấm giấy tờ nhẹ nhàng và hiệu quả.\n\n- **Khả năng bấm:** Tối đa 15 tờ giấy 70gsm.\n- Có ngăn chứa kim dự phòng tiện lợi.', 18, 3, 13, 'Nhật Bản', 'bam-kim-so-10-plus', 4.8, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (50, 'SP1050', 'Kim bấm số 10 Plus', 'Kim bấm **Plus** số 10, làm từ thép mạ kẽm, cứng cáp và ít bị gỉ sét, đảm bảo bấm chắc chắn.\n\n- **Quy cách:** 1000 kim/hộp.\n- Tương thích với các loại bấm kim số 10.', 18, 3, 13, 'Nhật Bản', 'kim-bam-so-10-plus', 4.7, 1, '2025-10-20 20:23:39', '2025-10-20 20:23:39');
INSERT INTO `products` VALUES (51, 'SP1051', 'Kẹp bướm Deli màu đen', 'Kẹp bướm (kẹp càng cua) **Deli** dùng để kẹp, cố định tài liệu, giấy tờ một cách gọn gàng.\n\n- **Chất liệu:** Thép không gỉ, sơn tĩnh điện màu đen.\n- Lực kẹp mạnh, giữ tài liệu chắc chắn.', 18, 1, 9, 'Trung Quốc', 'kep-buom-deli-mau-den', 4.6, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (52, 'SP1052', 'Băng keo trong Scotch 3M', 'Băng keo trong suốt **Scotch** của 3M với độ dính cao, bền bỉ, không bị ố vàng theo thời gian.\n\n- **Kích thước:** 18mm x 30m.\n- Phù hợp cho nhiều mục đích sử dụng trong văn phòng và gia đình.', 18, 4, 15, 'Mỹ', 'bang-keo-trong-scotch-3m', 4.9, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (53, 'SP1053', 'Bìa còng A4 King Jim', 'Bìa còng **King Jim** - giải pháp lưu trữ tài liệu chuyên nghiệp từ Nhật Bản.\n\n- Còng kim loại chắc chắn, dễ dàng đóng mở.\n- Bìa giấy cứng bọc simili bền đẹp.\n- **Gáy dày:** 7cm, có thể lưu trữ hàng trăm tờ giấy.', 18, 10, 25, 'Nhật Bản', 'bia-cong-a4-king-jim', 4.9, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (54, 'SP1054', 'Bút máy học sinh Hồng Hà', 'Bút máy luyện chữ đẹp **Hồng Hà** với ngòi mài êm trơn, giúp học sinh viết dễ dàng mà không cần ấn mạnh.\n\n- **Ngòi:** Mài êm, tự tạo nét thanh đậm nhẹ.\n- Có ống mực piston đi kèm, có thể dùng mực lọ hoặc ống mực tiện lợi.', 1, 2, 10, 'Việt Nam', 'but-may-hoc-sinh-hong-ha', 4.7, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (55, 'SP1055', 'Mực bút máy Pilot 30ml', 'Lọ mực bút máy **Pilot** chất lượng cao từ Nhật Bản, cho màu sắc tươi sáng và dòng mực ổn định.\n\n- Dung tích 30ml.\n- Không gây cặn, an toàn cho bút máy.', 1, 10, 24, 'Nhật Bản', 'muc-but-may-pilot-30ml', 4.9, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (56, 'SP1056', 'Giấy decal A4 đế vàng', 'Giấy decal **đế vàng** khổ A4, dùng để in tem nhãn, mã vạch, hình ảnh và dán lên sản phẩm.\n\n- Bề mặt giấy trắng, bám mực tốt.\n- Lớp keo dính chắc chắn.\n- **Đóng gói:** Xấp 100 tờ.', 6, 7, 19, 'Việt Nam', 'giay-decal-a4-de-vang', 4.6, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (57, 'SP1057', 'Bút lông màu rửa được Colokit', 'Bộ bút lông màu **Colokit** với loại mực đặc biệt, có thể dễ dàng rửa sạch khi dính trên tay hoặc quần áo.\n\n- An toàn tuyệt đối cho trẻ nhỏ.\n- Màu sắc tươi sáng, nắp đậy thông hơi an toàn.', 4, 2, 11, 'Việt Nam', 'but-long-mau-rua-duoc-colokit', 4.8, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (58, 'SP1058', 'Sổ kế hoạch KLONG Weekly Planner', 'Sổ kế hoạch tuần **KLONG** giúp bạn quản lý thời gian và công việc một cách hiệu quả.\n\n- In sẵn layout kế hoạch tuần, mục tiêu, ghi chú.\n- Giấy chất lượng cao, không lem mực.\n- Kích thước B5 rộng rãi.', 7, 3, 12, 'Việt Nam', 'so-ke-hoach-klong-weekly-planner', 4.9, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (59, 'SP1059', 'Thước dẻo 20cm Deli', 'Thước kẻ dẻo **Deli** làm từ nhựa cao cấp, có thể uốn cong mà không bị gãy, rất an toàn cho học sinh.\n\n- Vạch chia cm rõ nét, không phai mờ.\n- Nhiều màu sắc trẻ trung.', 22, 1, 9, 'Trung Quốc', 'thuoc-deo-20cm-deli', 4.7, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (60, 'SP1060', 'Đất nặn an toàn Colokit', 'Bộ đất nặn **Colokit** với 12 màu sắc sinh động, giúp bé thỏa sức sáng tạo và phát triển tư duy.\n\n- **Chất liệu:** Bột mì, an toàn, không dính tay.\n- Kèm theo khuôn tạo hình ngộ nghĩnh.', 14, 2, 11, 'Việt Nam', 'dat-nan-an-toan-colokit', 4.8, 1, '2025-10-20 20:25:46', '2025-10-20 20:25:46');
INSERT INTO `products` VALUES (100, 'SP1061', 'Bút Bi Thiên Long TL-089', NULL, 2, 1, 8, 'Albania', 'but-bi-thien-long-tl-089', 0, 1, '2025-11-12 23:41:37', '2025-11-12 23:59:00');

SET FOREIGN_KEY_CHECKS = 1;
