/*
 Navicat MySQL Data Transfer

 Source Server         : mysql
 Source Server Version : 50548
 Source Host           : localhost
 Source Database       : data_manager

 Target Server Version : 50548
 File Encoding         : utf-8

 Date: 11/28/2016 10:07:08 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `bucket_inst`
-- ----------------------------
DROP TABLE IF EXISTS `bucket_inst`;
CREATE TABLE `bucket_inst` (
  `id`          INT(11)    NOT NULL AUTO_INCREMENT
  COMMENT 'id，自动增长不需要理会',
  `bucket_id`   BIGINT(11) NOT NULL
  COMMENT 'bucket编号，此表中唯一主键',
  `user_id`     BIGINT(11) NOT NULL
  COMMENT '这个bucket的创建者id',
  `bucket_name` VARCHAR(200)        DEFAULT NULL
  COMMENT 'bucket的名称，可以为空',
  PRIMARY KEY (`id`, `bucket_id`),
  UNIQUE KEY `bucket_id` (`bucket_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 25
  DEFAULT CHARSET = utf8;

-- ----------------------------
--  Records of `bucket_inst`
-- ----------------------------
BEGIN;
INSERT INTO `bucket_inst`
VALUES ('1', '2', '111', 'buck2'), ('14', '1', '111', 'buck1'), ('18', '4444', '444', 'addTest'),
  ('23', '6666', '666', 'authorityTest'), ('24', '8888', '888', 'imageTest');
COMMIT;

-- ----------------------------
--  Table structure for `bucket_inst_authority`
-- ----------------------------
DROP TABLE IF EXISTS `bucket_inst_authority`;
CREATE TABLE `bucket_inst_authority` (
  `id`        INT(11) NOT NULL AUTO_INCREMENT
  COMMENT '自增长id',
  `user_id`   BIGINT(11)       DEFAULT NULL
  COMMENT '具有此权限的用户id',
  `inst_id`   BIGINT(11)       DEFAULT NULL
  COMMENT '对应bucket编号',
  `authority` INT(1)  NOT NULL DEFAULT '2'
  COMMENT '用户对此bucket的权限，1为只读，2为读写',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8
  COMMENT = 'bucket权限表，每条记录表示一个用户对某个bucket的权限，bucket的创建者也会记录在表中，权限为2（读写）';

-- ----------------------------
--  Records of `bucket_inst_authority`
-- ----------------------------
BEGIN;
INSERT INTO `bucket_inst_authority`
VALUES ('1', '111', '1', '2'), ('2', '111', '2', '2'), ('6', '444', '4444', '2'), ('7', '555', '1', '1'),
  ('8', '666', '2', '1'), ('9', '888', '8888', '2');
COMMIT;

-- ----------------------------
--  Table structure for `data_inst`
-- ----------------------------
DROP TABLE IF EXISTS `data_inst`;
CREATE TABLE `data_inst` (
  `id`             INT(11)    NOT NULL AUTO_INCREMENT,
  `data_inst_id`   BIGINT(11) NOT NULL
  COMMENT '唯一，dataInst编号',
  `data_inst_name` VARCHAR(20)         DEFAULT NULL
  COMMENT 'dataInst名称，可以为空',
  `file_path`      VARCHAR(200)        DEFAULT NULL
  COMMENT '文件存储路径',
  `user_id`        BIGINT(11) NOT NULL
  COMMENT 'dataInst的创建者',
  `bucket_id`      BIGINT(11) NOT NULL
  COMMENT 'dataInst所属bucket的编号',
  `bucket_name`    VARCHAR(20)         DEFAULT NULL
  COMMENT '所属bucket的名称，可以为空',
  PRIMARY KEY (`id`, `data_inst_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 76
  DEFAULT CHARSET = utf8
  COMMENT = 'dataInst表，每天记录表示一个dataInst，通过dataInst的文件路径，可以查询到存储的文件';

-- ----------------------------
--  Records of `data_inst`
-- ----------------------------
BEGIN;
INSERT INTO `data_inst`
VALUES ('1', '1', 'data1', 'aaaaaa', '111', '1', 'buck1'), ('2', '2', 'data2', 'bbbbbb', '111', '1', 'buck1'),
  ('3', '3', 'data3', 'cccccc', '111', '2', 'buck2'), ('4', '4', 'data4', 'dddddd', '111', '2', 'buck2');
COMMIT;

-- ----------------------------
--  Table structure for `data_inst_authority`
-- ----------------------------
DROP TABLE IF EXISTS `data_inst_authority`;
CREATE TABLE `data_inst_authority` (
  `id`        INT(11) NOT NULL AUTO_INCREMENT,
  `user_id`   BIGINT(11)       DEFAULT NULL,
  `inst_id`   BIGINT(11)       DEFAULT NULL,
  `authority` INT(1)  NOT NULL DEFAULT '2',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 40
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT
  COMMENT = '此表目前未使用，留给后续开发';

-- ----------------------------
--  Records of `data_inst_authority`
-- ----------------------------
BEGIN;
INSERT INTO `data_inst_authority`
VALUES ('1', '111', '1', '2'), ('2', '111', '2', '2'), ('3', '111', '3', '2'), ('4', '111', '4', '2'),
  ('39', '444', '44444', '2');
COMMIT;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`        INT(11)     NOT NULL AUTO_INCREMENT,
  `user_id`   BIGINT(11)  NOT NULL,
  `user_name` VARCHAR(200)         DEFAULT NULL,
  `password`  VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`, `user_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8
  COMMENT = '此表目前未使用，留给后续开发';

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', '111', 'twh', '111'), ('2', '444', 'ly', '123');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
