package com.zkkj.gps.gateway.protocol.component.messagebody.parameter;

import com.zkkj.gps.gateway.protocol.util.HexStringUtils;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/15 17:17
 */
public final class Param4Str extends BaseParameter {
	public Param4Str(byte[] bytes) throws Exception {
		super(bytes);
	}

	public Param4Str(int id, String paramValue) throws Exception {
		super(id, paramValue.length());
		this.paramValue = paramValue;
	}

	public Param4Str() {
	}

	/**
	 * 参数值
	 */
	private String paramValue = "";

	@Override
	protected String subToString() {
		return " 参数值:" + paramValue + "}";
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		String charset = "GBK";
		paramValue = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 0, bytes.length), charset);
		//paramValue = HexStringUtils.toHexString(Arrays.copyOfRange(bytes, 0, bytes.length));
	}

	@Override
	protected byte[] subEncoder() {
		return paramValue.getBytes();
	}

	@Override
	public String getParamValue() {
		return paramValue;
	}
}
