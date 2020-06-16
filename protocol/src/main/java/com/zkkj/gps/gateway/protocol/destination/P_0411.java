package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/1/4 15:31
 * 下发业务扩展数据应答
 */
public final class P_0411 extends DestinationBaseCompose {
	public P_0411(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0411);
	}

	public P_0411() throws Exception {
		super(ProtocolConsts.P_0411);
	}

	/**
	 * 应答流水号 2
	 */
	private int responseSerialNumber;

	/**
	 * 结果值 1
	 */
	private int result;

	@Override
	public String subToString() {
		return "\n应答流水号:" + responseSerialNumber +
				"\n结果值:" + result;
	}

	@Override
	public void decoderBody(byte[] bytesBody) throws Exception {
		responseSerialNumber = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytesBody, 12, 14));
		result = BitOperator.oneByteToInteger(bytesBody[14]);
	}

	public int getResponseSerialNumber() {
		return responseSerialNumber;
	}

	public int getResult() {
		return result;
	}
}
