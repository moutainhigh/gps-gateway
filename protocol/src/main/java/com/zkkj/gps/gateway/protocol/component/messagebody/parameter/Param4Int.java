package com.zkkj.gps.gateway.protocol.component.messagebody.parameter;

import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/15 17:13
 * 参数类型为int
 */
public final class Param4Int extends BaseParameter {
	public Param4Int(byte[] bytes) throws Exception {
		super(bytes);
	}

	public Param4Int(int id, int paramValue) throws Exception {
		super(id, 0);
		this.paramValue = paramValue;
	}

	public Param4Int() {
	}

	/**
	 * 参数值
	 */
	private int paramValue;

	@Override
	protected String subToString() {
		return " 参数值:" + paramValue + "}";
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		if (paramLength == 1) {
			paramValue = BitOperator.oneByteToInteger(bytes[0]);
		} else if (paramLength > 1) {
			paramValue = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 0, bytes.length));
		} else {
			paramValue = 0;
		}
	}

	@Override
	protected byte[] subEncoder() {
		if (paramLength == 1) {
			return BitOperator.integerTo1Bytes(paramValue);
		} else {
			return BitOperator.integerTo2Bytes(paramValue);
		}
	}

	@Override
	public Integer getParamValue() {
		return paramValue;
	}
}
