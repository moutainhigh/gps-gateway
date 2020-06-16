package com.zkkj.gps.gateway.protocol.component.messagebody.parameter;

/**
 * @author chailixing
 * 2019/4/16 9:12
 * 参数类型枚举
 */
public enum ParamTypeEnum {
	/**
	 * 字符串: 转换为hax字符串
	 */
	STRING,

	/**
	 * byte,word: 转换为十进制
	 */
	INTEGER,

	/**
	 * byte[]: 转换为十进制数组
	 */
	INTEGER_ARRAY,

	/**
	 * bcd: 转换为bcd字符串
	 */
	BCD8421
}
