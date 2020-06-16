package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.component.common.SourceBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author chailixing
 * 2019/3/5 8:50
 * 批量读取区域及刷卡的使用规则
 */
public final class P_0316 extends SourceBaseCompose {
	public P_0316(String terminalId, int serialNumber, int... addressNumber) throws Exception {
		super(ProtocolConsts.P_0316, terminalId, serialNumber);
		this.addressNumber = addressNumber;
	}

	/**
	 * 存储地址序号
	 */
	private int[] addressNumber = new int[0];

	@Override
	public String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int b : addressNumber) {
			stringBuilder.append(b).append(" ");
		}
		return "\n存储地址序号:" + stringBuilder;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		if (addressNumber.length != 0) {
			List<byte[]> collect = IntStream.of(addressNumber)
					.boxed()
					.map(BitOperator::integerTo2Bytes)
					.collect(Collectors.toList());
			return BitOperator.concatAll(
					BitOperator.concatAll(collect)
			);
		} else {
			throw new RuntimeException("参数列表为空!(0316)");
		}
	}

	public int[] getAddressNumber() {
		return addressNumber;
	}
}
