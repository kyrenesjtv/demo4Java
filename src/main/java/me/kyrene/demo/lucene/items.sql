/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : mydb

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-02-16 15:02:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for items
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
  `id` bigint(10) NOT NULL,
  `title` varchar(255) NOT NULL,
  `price` bigint(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of items
-- ----------------------------
INSERT INTO `items` VALUES ('1', '华为 荣耀 畅玩6X 4GB 32GB 全网通4G手机 高配版 铂光金', '139900');
INSERT INTO `items` VALUES ('2', '一加手机3T (A3010) 6GB+64GB 枪灰版 全网通 双卡双待 移动联通电信4G手机', '269900');
INSERT INTO `items` VALUES ('3', 'OPPO R9s 全网通4G+64G 双卡双待手机 金色', '279900');
INSERT INTO `items` VALUES ('4', '华为 荣耀 畅玩5C 全网通 高配版 3GB+32GB 落日金 移动联通电信4G手机 双卡双待', '119900');
INSERT INTO `items` VALUES ('5', 'VOTO Xplay5 移动4G 双卡双待 智能手机 金色', '79900');
INSERT INTO `items` VALUES ('6', '华为 荣耀 V8 全网通 高配版 4GB+64GB 典雅灰 移动联通电信4G手机 双卡双待双通', '259900');
INSERT INTO `items` VALUES ('7', 'vivo X9 全网通 4GB+64GB 移动联通电信4G手机 双卡双待 玫瑰金', '279800');
INSERT INTO `items` VALUES ('8', 'Apple iPhone 7 Plus (A1661) 32G 黑色 移动联通电信4G手机', '639900');
INSERT INTO `items` VALUES ('9', '小米5s 全网通 高配版 3GB内存 64GB ROM 哑光金 移动联通电信4G手机', '199900');
INSERT INTO `items` VALUES ('10', 'Apple iPhone 7 (A1660) 128G 亮黑色 移动联通电信4G手机', '589900');
INSERT INTO `items` VALUES ('11', '华为 荣耀 NOTE 8 4GB+64GB 全网通4G手机 冰河银, 2K大屏，长续航，4500毫安电池', '249900');
INSERT INTO `items` VALUES ('12', 'OPPO R9s Plus 6GB+64GB内存版 全网通4G手机 双卡双待 玫瑰金, 6英寸大屏，1300万像素，光学防抖', '349900');
INSERT INTO `items` VALUES ('13', '华为 Mate 9 4GB+64GB版 香槟金 移动联通电信4G手机 双卡双待，麒麟960芯片！第二代徕卡双摄像头', '389900');
INSERT INTO `items` VALUES ('14', 'Apple iPhone 6s (A1700) 32G 深空灰色 移动联通电信4G手机', '429900');
