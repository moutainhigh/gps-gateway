package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/5 8:50
 * 批量设置区域及刷卡的使用规则应答
 */
public final class P_0315 extends DestinationBaseCompose {
	public P_0315(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0315);
	}

	public P_0315() throws Exception {
		super(ProtocolConsts.P_0315);
	}

	/**
	 * 应答流水号 2
	 */
	private int responseSerialNumber;

	/**
	 * 结果 1
	 */
	private int result;

	@Override
	public String subToString() {
		return "\n应答流水号:" + responseSerialNumber +
				"\n结果:" + result;
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		responseSerialNumber = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 12, 14));
		result = BitOperator.oneByteToInteger(bytes[14]);
	}

	public int getResponseSerialNumber() {
		return responseSerialNumber;
	}

	public int getResult() {
		return result;
	}
}