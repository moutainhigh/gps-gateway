package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.component.common.SourceBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

/**
 * @author chailixing
 * 2019/1/2 17:03
 * 平台通用应答
 */
public final class P_8001 extends SourceBaseCompose {
	public P_8001(String terminalId, int serialNumber, int responseSerialNumber, int responseId, int result) throws Exception {
		super(ProtocolConsts.P_8001, terminalId, serialNumber);
		this.responseId = responseId;
		this.responseSerialNumber = responseSerialNumber;
		this.result = result;
	}


	/**
	 * 应答流水号 2
	 */
	private int responseSerialNumber;

	/**
	 * 应答id 2
	 */
	private int responseId;

	/**
	 * 结果 1
	 */
	private int result;

	@Override
	public String subToString() {
		return "\n消息体:{应答流水号:" + responseSerialNumber +
				" 应答id:" + responseId +
				" 结果:" + result + "}";
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		return BitOperator.concatAll(
				BitOperator.integerTo2Bytes(responseSerialNumber),
				BitOperator.integerTo2Bytes(responseId),
				BitOperator.integerTo1Bytes(result)
		);
	}

	public int getResponseSerialNumber() {
		return responseSerialNumber;
	}

	public int getResponseId() {
		return responseId;
	}

	public int getResult() {
		return result;
	}
}
