package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.component.common.SourceBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author chailixing
 * 2019/1/4 14:00
 * 终端参数查询
 */
public final class P_0312 extends SourceBaseCompose {
	public P_0312(String terminalId, int serialNumber, int... params) throws Exception {
		super(ProtocolConsts.P_0312, terminalId, serialNumber);
		this.paramId = params;
		this.paramTotal = paramId.length;
	}

	/**
	 * 参数总数 1
	 */
	private int paramTotal;

	/**
	 * 参数id n字节
	 */
	private int[] paramId = new int[0];

	@Override
	public String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int b : paramId) {
			stringBuilder.append(b).append(" ");
		}
		return "\n参数总数:" + paramTotal +
				" 参数id:" + stringBuilder;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		if (paramId.length != 0) {
			List<Byte> collect = IntStream.of(paramId)
					.boxed()
					.map(BitOperator::integerTo1Byte)
					.collect(Collectors.toList());
			return BitOperator.concatAll(
					BitOperator.integerTo1Bytes(paramTotal),
					BitOperator.toArray(collect)
			);
		} else {
			throw new RuntimeException("参数id列表为空!(0312)");
		}
	}

	public int getParamTotal() {
		return paramTotal;
	}

	public int[] getParamId() {
		return paramId;
	}
}