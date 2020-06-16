package com.zkkj.gps.gateway.protocol.component.messagebody.parameter;

import com.zkkj.gps.gateway.protocol.util.BCD8421Operator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/15 17:17
 * 参数类型为bcd
 */
public final class Param4Bcd extends BaseParameter {
	public Param4Bcd(byte[] bytes) throws Exception {
		super(bytes);
	}

	public Param4Bcd(int id, String value) throws Exception {
		super(id, 0);
		this.paramValue = value;
	}

	public Param4Bcd() {
	}

	/**
	 * 参数值
	 */
	private String paramValue = "";

	@Override
	protected String subToString() {
		return " 参数值: " + paramValue + "}";
	}

	@Override
	protected byte[] subEncoder() {
		return BCD8421Operator.str2Bcd(paramValue);
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		paramValue = BCD8421Operator.bcd2ToString(Arrays.copyOfRange(bytes, 0, bytes.length));
	}

	@Override
	public String getParamValue() {
		return paramValue;
	}
}
