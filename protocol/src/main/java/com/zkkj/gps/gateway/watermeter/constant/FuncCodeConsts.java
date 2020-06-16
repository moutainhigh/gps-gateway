package com.zkkj.gps.gateway.watermeter.constant;

/**
 * @author chailixing
 * 2019/4/18 11:05
 * 功能码
 */
public interface FuncCodeConsts {
	/**
	 * 终端初始化（登录/心跳）
	 */
	int P_01 = 0X01;

	/**
	 * 读取采集器时间
	 */
	int P_12 = 0X12;

	/**
	 * 设置采集器时间
	 */
	int P_22 = 0X22;

	/**
	 * 设置抄表终端 IP 端口
	 */
	int P_24 = 0X24;

	/**
	 * 读水表数据
	 */
	int P_31 = 0X31;

	/**
	 * 读采集器无线信道
	 */
	int P_38 = 0X38;

	/**
	 * 写水表阀门控制
	 */
	int P_42 = 0X42;

	/**
	 * 写水表无线信道
	 */
	int P_43 = 0X43;

	/**
	 * 写水表无线链路
	 */
	int P_45 = 0X45;

	/**
	 * 删除水表无线链路
	 */
	int P_46 = 0X46;

	/**
	 * 写采集器无线信道
	 */
	int P_48 = 0X48;
}