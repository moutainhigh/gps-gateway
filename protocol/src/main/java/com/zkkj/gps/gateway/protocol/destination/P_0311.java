package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/1/4 13:24
 * 终端参数设置应答
 */
public final class P_0311 extends DestinationBaseCompose {
	public P_0311(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0311);
	}

	public P_0311() throws Exception {
		super(ProtocolConsts.P_0311);
	}

	/**
	 * 应答流水号 2
	 */
	private int responseSerialNumber;

	/**
	 * 参数总数 1
	 */
	private int paramTotal;

	/**
	 * 参数id 1
	 */
	private int paramId;

	/**
	 * 结果值 1
	 */
	private int result;

	@Override
	public String subToString() {
		return " 应答流水号:" + responseSerialNumber +
				" 参数总数:" + paramTotal +
				" 参数id:" + paramId +
				" 结果值:" + result;
	}

	@Override
	public void decoderBody(byte[] bytesBody) throws Exception {
		responseSerialNumber = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytesBody, 12, 14));
		paramTotal = BitOperator.oneByteToInteger(bytesBody[14]);
		paramId = BitOperator.oneByteToInteger(bytesBody[15]);
		result = BitOperator.oneByteToInteger(bytesBody[16]);
	}

	public int getResponseSerialNumber() {
		return responseSerialNumber;
	}

	public int getParamTotal() {
		return paramTotal;
	}

	public int getParamId() {
		return paramId;
	}

	public int getResult() {
		return result;
	}
}
