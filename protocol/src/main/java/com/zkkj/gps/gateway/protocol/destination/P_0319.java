package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/5 8:50
 * 读取终端区域及刷卡使用规则检验码及存储数量应答
 */
public final class P_0319 extends DestinationBaseCompose {
	public P_0319(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0319);
	}

	public P_0319() throws Exception {
		super(ProtocolConsts.P_0319);
	}

	/**
	 * 应答流水号 2
	 */
	private int responseSerialNumber;

	/**
	 * 区域参数校验 2
	 */
	private int areaParamCrc;

	/**
	 * IC卡数量值 2
	 */
	private int cardCount;

	@Override
	public String subToString() {
		return "\n应答流水号:" + responseSerialNumber +
				"\n区域参数校验:" + areaParamCrc +
				"\nIC卡数量值:" + cardCount;
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		responseSerialNumber = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 12, 14));
		areaParamCrc = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 14, 16));
		cardCount = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 16, 18));
	}

	public int getResponseSerialNumber() {
		return responseSerialNumber;
	}

	public int getAreaParamCrc() {
		return areaParamCrc;
	}

	public int getCardCount() {
		return cardCount;
	}
}