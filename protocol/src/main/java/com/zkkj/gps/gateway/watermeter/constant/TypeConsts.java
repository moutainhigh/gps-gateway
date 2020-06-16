package com.zkkj.gps.gateway.watermeter.constant;

/**
 * @author chailixing
 * 2019/4/18 10:46
 * 协议类型 下发指令用
 */
public interface TypeConsts {
	/**
	 * 采集器->通讯服务器（登录/心跳）
	 */
	int TYPE_21 = 0X21;

	/**
	 * 通讯服务器->采集器（登录/心跳）
	 */
	int TYPE_22 = 0X22;

	/**
	 * 通讯服务器->采集器（命令）
	 */
	int TYPE_31 = 0X31;

	/**
	 * 采集器->通讯服务器（命令）
	 */
	int TYPE_32 = 0X32;
}
