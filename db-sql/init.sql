/*
Navicat MySQL Data Transfer

Source Server         : 192.168.3.251
Source Server Version : 50726
Source Host           : 192.168.3.251:3306
Source Database       : zk_gpsgateway

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-06-04 15:16:50
*/

SET FOREIGN_KEY_CHECKS=0;

CREATE DATABASE IF NOT EXISTS zk_gpsgateway DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

-- ----------------------------
-- Table structure for dispatch
-- ----------------------------
DROP TABLE IF EXISTS `dispatch`;
CREATE TABLE `dispatch` (
  `id` char(36) NOT NULL COMMENT 'id',
  `appkey` varchar(40) DEFAULT NULL COMMENT '对应用户appkey',
  `identity` varchar(40) DEFAULT NULL COMMENT '公司唯一标识',
  `car_number` varchar(20) DEFAULT NULL COMMENT '车牌号',
  `terminal_id` varchar(40) DEFAULT NULL COMMENT '设备id',
  `corp_name` varchar(100) DEFAULT NULL COMMENT '所属公司名称',
  `receiver_corp_name` varchar(100) DEFAULT NULL COMMENT '收货方公司名称',
  `consigner_corp_name` varchar(100) DEFAULT NULL COMMENT '发货方公司名称',
  `shipper_corp_name` varchar(100) DEFAULT NULL COMMENT '承运商公司名称',
  `dispatch_no` varchar(50) DEFAULT NULL COMMENT '运单编号',
  `driver_name` varchar(20) DEFAULT NULL COMMENT '司机姓名',
  `driver_mobile` varchar(20) DEFAULT NULL COMMENT '司机电话',
  `remake` varchar(200) DEFAULT NULL COMMENT '详情说明',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_by_name` varchar(40) DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for terminal_alarm_config
-- ----------------------------
DROP TABLE IF EXISTS `terminal_alarm_config`;
CREATE TABLE `terminal_alarm_config` (
  `id` varchar(40) COLLATE utf8_bin NOT NULL,
  `custom_config_id` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '外部传来的报警配置id',
  `app_key` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '区分数据来源',
  `config_value` decimal(18,8) DEFAULT NULL COMMENT '报警配置值',
  `alarm_type` int(4) NOT NULL COMMENT '掉线:1;超速:2,停车超时:4,进入区域:8,离开区域:16,设备拆除:32,线路偏移::64,低电量报警:128,其他设备异常:2048',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `identity` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '公司唯一标识',
  `corp_name` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '公司名称',
  `terminal_id` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '终端编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `task_order` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `car_num` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`custom_config_id`,`app_key`,`terminal_id`),
  UNIQUE KEY `c_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='报警配置信息terminal_alarm_config';

-- ----------------------------
-- Table structure for terminal_alarm_info
-- ----------------------------
DROP TABLE IF EXISTS `terminal_alarm_info`;
CREATE TABLE `terminal_alarm_info` (
  `id` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '主键',
  `alarm_group_id` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '报警分组id',
  `res_type` int(4) DEFAULT NULL COMMENT '报警响应类型，开始报警，结束报警,0:开始,1:结束',
  `identity` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '报警所属公司ID',
  `corp_name` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '报警公司名称',
  `terminal_id` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '报警终端id',
  `car_num` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '车牌号',
  `alarm_time` datetime DEFAULT NULL COMMENT '考虑到实时性，检测到开始即产生报警，检测到结束再次产生一条报警信息',
  `longitude` decimal(30,15) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(30,15) DEFAULT NULL COMMENT '纬度',
  `alarm_info` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '报警内容信息',
  `alarm_type` int(4) DEFAULT NULL COMMENT '掉线:1;超速:2,停车超时:4,进入区域:8,离开区域:16,设备拆除:32,线路偏移::64,低电量报警:128,其他设备异常:2048',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `area_id` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '区域ID，0表示无区域',
  `alarm_value` decimal(18,8) DEFAULT NULL COMMENT '报警结束有效，报警值，如：超时为超时时间，掉线时间，最大超速速度，线路偏移最大距离（米）等',
  `config_value` decimal(18,8) DEFAULT NULL COMMENT '配置限定值',
  `alarm_create_time` datetime DEFAULT NULL COMMENT '报警创建时间',
  `app_key` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '数据来源',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `alarm_config_id` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '报警配置id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='终端产生的报警信息表';

-- ----------------------------
-- Table structure for terminal_area
-- ----------------------------
DROP TABLE IF EXISTS `terminal_area`;
CREATE TABLE `terminal_area` (
  `id` varchar(40) COLLATE utf8_bin NOT NULL,
  `custom_area_id` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '外部传来的区域id',
  `app_key` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '区分数据来源',
  `area_name` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '区域名称',
  `address` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '区域详细地址',
  `center_lng` decimal(30,15) DEFAULT NULL COMMENT '中心点，经度',
  `center_lat` decimal(30,15) DEFAULT NULL COMMENT '中心点，纬度',
  `radius` decimal(13,3) DEFAULT NULL COMMENT '半径',
  `area_points` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '区域点集合，纬度，经度；23.45,130.12；23.45,130.12',
  `graph_type` int(4) DEFAULT NULL COMMENT '1:圆,2:多边形,3:线',
  `alarm_config_id` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '报警配置id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='区域表';

-- ----------------------------
-- Table structure for terminal_route
-- ----------------------------
DROP TABLE IF EXISTS `terminal_route`;
CREATE TABLE `terminal_route` (
  `id` varchar(40) COLLATE utf8_bin NOT NULL,
  `custom_route_id` varchar(40) COLLATE utf8_bin NOT NULL,
  `route_name` varchar(60) COLLATE utf8_bin DEFAULT NULL,
  `width` decimal(18,8) DEFAULT NULL,
  `point_sequence` longtext COLLATE utf8_bin COMMENT '纬度(lat),经度(lng);34.200753,108.920162;34.201081,108.921837;',
  `alarm_config_id` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `app_key` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for vehicle_location_his
-- ----------------------------
DROP TABLE IF EXISTS `vehicle_location_his`;
CREATE TABLE `vehicle_location_his` (
  `terminal_id` varchar(23) NOT NULL COMMENT '设备ID',
  `gps_time` datetime NOT NULL COMMENT 'gps时间',
  `rcv_time` datetime DEFAULT NULL COMMENT '接收时间',
  `lon` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `lat` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `direction` smallint(6) DEFAULT NULL COMMENT '方向',
  `speed` tinyint(4) DEFAULT NULL COMMENT '速度（1/10 km/h）',
  `gsm_signal` tinyint(4) DEFAULT NULL COMMENT 'gsm信号',
  `gps_signal` tinyint(4) DEFAULT NULL COMMENT 'gps信号',
  `fuel` int(11) DEFAULT NULL COMMENT '油量',
  `mileage` int(11) unsigned DEFAULT NULL COMMENT '里程(1/10km)',
  `car_state` int(11) unsigned DEFAULT NULL COMMENT '车辆状态',
  `terminal_state` int(11) unsigned DEFAULT NULL COMMENT '终端状态',
  `alarm_state` int(11) unsigned DEFAULT NULL COMMENT '报警状态',
  `power` int(11) unsigned DEFAULT NULL COMMENT '电量',
  `pressure` decimal(8,4) DEFAULT NULL COMMENT '压力值',
  `site_append_message` varchar(4000) DEFAULT NULL COMMENT '位置附加信息列表（见表6),json',
  `extend1` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `extend2` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `extend3` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `extend4` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `extend5` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `load` decimal(5,2) DEFAULT NULL COMMENT '载重百分比',
  PRIMARY KEY (`terminal_id`,`gps_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='历史轨迹信息表';

-- ----------------------------
-- Table structure for vehicle_location_last
-- ----------------------------
DROP TABLE IF EXISTS `vehicle_location_last`;
CREATE TABLE `vehicle_location_last` (
  `terminal_id` varchar(23) NOT NULL COMMENT '设备ID',
  `gps_time` datetime NOT NULL COMMENT 'gps时间',
  `rcv_time` datetime NOT NULL COMMENT '接收时间',
  `lon` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `lat` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `direction` smallint(6) DEFAULT NULL COMMENT '方向',
  `speed` tinyint(4) DEFAULT NULL COMMENT '速度（1/10 km/h）',
  `gsm_signal` tinyint(4) DEFAULT NULL COMMENT 'gsm信号',
  `gps_signal` tinyint(4) DEFAULT NULL COMMENT 'gps信号',
  `fuel` int(11) DEFAULT NULL COMMENT '油量',
  `mileage` int(11) unsigned DEFAULT NULL COMMENT '里程(1/10km)',
  `car_state` int(11) unsigned DEFAULT NULL COMMENT '车辆状态',
  `terminal_state` int(11) unsigned DEFAULT NULL COMMENT '终端状态',
  `alarm_state` int(11) unsigned DEFAULT NULL COMMENT '报警状态',
  `power` int(11) unsigned DEFAULT NULL COMMENT '电量',
  `pressure` decimal(8,4) DEFAULT NULL COMMENT '压力值',
  `extend1` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `extend2` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `extend3` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `extend4` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `extend5` varchar(500) DEFAULT NULL COMMENT '扩展字段在非必要情况下不使用，初期尽量以加字段方式',
  `site_append_message` varchar(4000) DEFAULT NULL COMMENT '位置附加信息列表（见表6),json',
  `load` decimal(5,2) DEFAULT NULL COMMENT '载重百分比',
  PRIMARY KEY (`terminal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='最新经纬度信息表';
