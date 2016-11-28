/*
 Navicat MySQL Data Transfer

 Source Server         : mysql
 Source Server Version : 50548
 Source Host           : localhost
 Source Database       : data_manager

 Target Server Version : 50548
 File Encoding         : utf-8

 Date: 11/28/2016 09:56:14 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `bucket_inst`
-- ----------------------------
DROP TABLE IF EXISTS `bucket_inst`;
CREATE TABLE `bucket_inst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bucket_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `bucket_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`,`bucket_id`),
  UNIQUE KEY `bucket_id` (`bucket_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `bucket_inst`
-- ----------------------------
BEGIN;
INSERT INTO `bucket_inst` VALUES ('1', '2', '111', 'buck2'), ('14', '1', '111', 'buck1'), ('18', '4444', '444', 'addTest'), ('23', '6666', '666', 'authorityTest'), ('24', '8888', '888', 'imageTest');
COMMIT;

-- ----------------------------
--  Table structure for `bucket_inst_authority`
-- ----------------------------
DROP TABLE IF EXISTS `bucket_inst_authority`;
CREATE TABLE `bucket_inst_authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) DEFAULT NULL,
  `inst_id` bigint(11) DEFAULT NULL,
  `authority` int(1) NOT NULL DEFAULT '2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `bucket_inst_authority`
-- ----------------------------
BEGIN;
INSERT INTO `bucket_inst_authority` VALUES ('1', '111', '1', '2'), ('2', '111', '2', '2'), ('6', '444', '4444', '2'), ('7', '555', '1', '1'), ('8', '666', '2', '1'), ('9', '888', '8888', '2');
COMMIT;

-- ----------------------------
--  Table structure for `data_inst`
-- ----------------------------
DROP TABLE IF EXISTS `data_inst`;
CREATE TABLE `data_inst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_inst_id` bigint(11) NOT NULL,
  `data_inst_name` varchar(20) DEFAULT NULL,
  `file_path` varchar(200) DEFAULT NULL,
  `user_id` bigint(11) NOT NULL,
  `bucket_id` bigint(11) NOT NULL,
  `bucket_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `data_inst`
-- ----------------------------
BEGIN;
INSERT INTO `data_inst` VALUES ('1', '1', 'data1', 'aaaaaa', '111', '1', 'buck1'), ('2', '2', 'data2', 'bbbbbb', '111', '1', 'buck1'), ('3', '3', 'data3', 'cccccc', '111', '2', 'buck2'), ('4', '4', 'data4', 'dddddd', '111', '2', 'buck2');
COMMIT;

-- ----------------------------
--  Table structure for `data_inst_authority`
-- ----------------------------
DROP TABLE IF EXISTS `data_inst_authority`;
CREATE TABLE `data_inst_authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) DEFAULT NULL,
  `inst_id` bigint(11) DEFAULT NULL,
  `authority` int(1) NOT NULL DEFAULT '2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `data_inst_authority`
-- ----------------------------
BEGIN;
INSERT INTO `data_inst_authority` VALUES ('1', '111', '1', '2'), ('2', '111', '2', '2'), ('3', '111', '3', '2'), ('4', '111', '4', '2'), ('39', '444', '44444', '2');
COMMIT;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) NOT NULL,
  `user_name` varchar(200) DEFAULT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', '111', 'twh', '111'), ('2', '444', 'ly', '123');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
