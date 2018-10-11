/*
Navicat MySQL Data Transfer

Source Server         : 172.28.5.92
Source Server Version : 50639
Source Host           : 172.28.5.92:3306
Source Database       : chengde_ticket

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-10-10 09:07:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for annual_ticket_order
-- ----------------------------
DROP TABLE IF EXISTS `annual_ticket_order`;
CREATE TABLE `annual_ticket_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` bigint(50) DEFAULT NULL COMMENT '订单号',
  `ep_order_no` varchar(50) DEFAULT NULL COMMENT '恩普票务订单号',
  `platform_order_no` varchar(50) DEFAULT NULL COMMENT '聚合支付平台订单号',
  `pay_no` varchar(100) DEFAULT NULL COMMENT '支付平台订单号',
  `pay_time` bigint(20) DEFAULT NULL COMMENT '支付时间',
  `pay_flag` int(1) DEFAULT '0' COMMENT '是否调用过聚合支付（如果调用过聚合支付，该值为1，再次支付时需要重新下单）',
  `amount` decimal(18,2) DEFAULT NULL COMMENT '订单总额  人民币，以元为单位 ',
  `add_time` bigint(20) DEFAULT NULL COMMENT '下单时间',
  `flag` int(4) DEFAULT '1' COMMENT '支付状态  1未支付  2已支付 3已失效 4已取消',
  `user_id` varchar(20) DEFAULT NULL COMMENT '用户id',
  `cn_name` varchar(20) DEFAULT NULL COMMENT '取票人',
  `id_card` varchar(100) DEFAULT NULL COMMENT '身份证号',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `card_name` varchar(50) DEFAULT NULL COMMENT '年卡名',
  `card_type` varchar(10) DEFAULT NULL COMMENT '卡类型',
  `valid_term` varchar(20) DEFAULT NULL COMMENT '有效期',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `delete_flag` int(4) DEFAULT '0' COMMENT '逻辑删除标识 0未删除 1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_no` bigint(50) DEFAULT NULL COMMENT '订单组号',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单号',
  `ep_order_no` varchar(50) DEFAULT NULL COMMENT '恩普票务订单号',
  `platform_order_no` varchar(50) DEFAULT NULL COMMENT '支付平台订单号',
  `pay_no` varchar(100) DEFAULT NULL COMMENT '支付号',
  `pay_flag` int(1) DEFAULT '0' COMMENT '是否调用过聚合支付 1是 0否',
  `amount` decimal(18,2) DEFAULT NULL COMMENT '订单总额  人民币，以元为单位 ',
  `add_time` bigint(20) DEFAULT NULL COMMENT '下单时间',
  `flag` int(4) DEFAULT '1' COMMENT '支付状态  1未支付  2已支付  3已失效 4已取消',
  `pay_time` bigint(20) DEFAULT NULL COMMENT '支付时间',
  `user_id` varchar(20) DEFAULT NULL COMMENT '用户id',
  `cn_name` varchar(20) DEFAULT NULL COMMENT '取票人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `ticket_id` int(11) DEFAULT NULL COMMENT '门票ID',
  `ticket_name` varchar(50) DEFAULT NULL COMMENT '票名',
  `man_num` int(11) DEFAULT NULL COMMENT '人数',
  `memo` varchar(1000) DEFAULT NULL COMMENT '备注',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `delete_flag` int(4) DEFAULT '0' COMMENT '逻辑删除标识 0未删除 1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_ticket
-- ----------------------------
DROP TABLE IF EXISTS `order_ticket`;
CREATE TABLE `order_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tid` varchar(50) DEFAULT NULL COMMENT '票号',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单号',
  `ep_order_no` varchar(50) DEFAULT NULL COMMENT '恩普票务订单号',
  `ticket_name` varchar(50) DEFAULT NULL COMMENT '票名',
  `ticket_type` int(4) DEFAULT NULL COMMENT '票类型  1全价票 2半价票',
  `summary` varchar(255) DEFAULT NULL COMMENT '简介',
  `single_price` decimal(10,2) DEFAULT NULL COMMENT '票面价格(单价)   人民币，以元为单位 ',
  `tprice` decimal(10,2) DEFAULT NULL COMMENT '销售价格  人民币，以元为单位 ',
  `man_num` int(11) DEFAULT NULL COMMENT '人数',
  `use_time` varchar(20) DEFAULT NULL COMMENT '游览日期',
  `qr` varchar(100) DEFAULT NULL COMMENT '二维码图片地址',
  `qr_stream` varchar(1000) DEFAULT NULL COMMENT '二维码数据流',
  `flag` tinyint(4) DEFAULT NULL COMMENT '票状态   1未支付 2已支付 3已退票 4已使用 5已取票不允许退 6已取票可退 7已改签 8已过期',
  `refund_time` bigint(20) DEFAULT NULL COMMENT '退票时间',
  `change_time` bigint(20) DEFAULT NULL COMMENT '改签时间',
  `if_taopiao` int(4) DEFAULT NULL,
  `exchange_code` varchar(50) DEFAULT NULL COMMENT '兑换码   空值表示为电子票；非空表示为兑换码票， 需要凭兑换码换票',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `delete_flag` int(4) DEFAULT '0' COMMENT '逻辑删除标识 0未删除 1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_record`;
CREATE TABLE `pay_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` bigint(50) DEFAULT NULL COMMENT '系统订单号',
  `out_trade_no` varchar(50) DEFAULT NULL COMMENT '外部交易订单号（恩普订单号）',
  `transaction_id` varchar(100) DEFAULT NULL COMMENT '微信交易号',
  `total_fee` decimal(18,2) DEFAULT NULL COMMENT '订单金额',
  `cash_fee` decimal(18,2) DEFAULT NULL COMMENT '现金支付金额',
  `pay_status` int(4) DEFAULT NULL COMMENT '支付状态 1待支付 2已支付 3支付异常',
  `add_time` bigint(20) DEFAULT NULL COMMENT '下单时间',
  `pay_time` bigint(20) DEFAULT NULL COMMENT '支付时间',
  `business_type` int(4) DEFAULT NULL COMMENT '业务类型 1门票购买 2年票充值',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注（支付异常原因）',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `delete_flag` int(4) DEFAULT '0' COMMENT '删除标志 0未删除 1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `pass_word` varchar(50) NOT NULL,
  `person_name` varchar(20) NOT NULL COMMENT '人名',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `delete_flag` int(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
