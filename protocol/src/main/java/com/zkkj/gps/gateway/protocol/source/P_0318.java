package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.component.common.SourceBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author chailixing
 * 2019/3/5 8:52
 * 读取终端区域及刷卡使用规则检验码及存储数量
 */
public final class P_0318 extends SourceBaseCompose {
	public P_0318(String terminalId, int serialNumber, int paramTotalCrc, int... address) throws Exception {
		super(ProtocolConsts.P_0318, terminalId, serialNumber);
		this.paramTotalCrc = paramTotalCrc;
		this.address = address;
	}

	/**
	 * 参数总数校验值 2
	 */
	private int paramTotalCrc;

	/**
	 * 校验卡号参数存储区域的起始地址及结束地址 [2] 4
	 */
	private int[] address = new int[0];

	@Override
	public String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int a : address) {
			stringBuilder.append(a).append(" ");
		}
		return "\n参数总数校验值:" + paramTotalCrc +
				"\n校验卡号参数存储区域的起始地址及结束地址:" + stringBuilder;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		if (address.length != 0) {
			List<byte[]> collect = IntStream.of(address)
					.boxed()
					.map(BitOperator::integerTo2Bytes)
					.collect(Collectors.toList());
			return BitOperator.concatAll(
					BitOperator.integerTo1Bytes(paramTotalCrc),
					BitOperator.concatAll(collect)
			);
		} else {
			throw new RuntimeException("参数列表为空!(0318)");
		}
	}

	public int getParamTotalCrc() {
		return paramTotalCrc;
	}

	public int[] getAddress() {
		return address;
	}
}