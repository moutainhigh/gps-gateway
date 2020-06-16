package com.zkkj.gps.gateway.protocol.component.common;

import com.zkkj.gps.gateway.protocol.util.BitOperator;
import com.zkkj.gps.gateway.protocol.util.EscapeUtils;

/**
 * 序列化协议基类
 */
public abstract class SourceBaseCompose extends BaseCompose {
	public SourceBaseCompose(int protocolId, String terminalId, int serialNumber) throws Exception {
		super(protocolId, terminalId, serialNumber);
	}

	@Override
	public byte[] encoder() throws Exception {
		byte[] messageBodyByte = encoderBody();
		byte[] protocolByte = BitOperator.concatAll(messageHeader.encoder(messageBodyByte.length), messageBodyByte);
		crc = BitOperator.getCrc(protocolByte);
		byte[] bytes2 = BitOperator.concatAll(protocolByte, BitOperator.integerTo1Bytes(crc));
		return BitOperator.concatAll(
				BitOperator.integerTo1Bytes(FRAME_HEADER),
				EscapeUtils.doEscape4Send(bytes2, 0, bytes2.length),
				BitOperator.integerTo1Bytes(FRAME_FOOTER)
		);
	}

	/**
	 * 输出为协议
	 */
	protected abstract byte[] encoderBody() throws Exception;
}
