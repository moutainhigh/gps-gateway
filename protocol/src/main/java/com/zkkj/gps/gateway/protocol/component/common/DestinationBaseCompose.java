package com.zkkj.gps.gateway.protocol.component.common;

import com.zkkj.gps.gateway.protocol.util.BitOperator;
import com.zkkj.gps.gateway.protocol.util.ClientUtils;
import com.zkkj.gps.gateway.protocol.util.EscapeUtils;

import java.util.Arrays;

/**
 * 接收消息协议基类
 */
public abstract class DestinationBaseCompose extends BaseCompose {

	public DestinationBaseCompose(byte[] bytes, int protocolId) throws Exception {
		super(bytes, protocolId);
	}

	public DestinationBaseCompose(int protocolId) throws Exception {
		super(protocolId);
	}

	/**
	 * 通过一段包含协议的字节数组,解析为对应对象
	 */
	public void decoderBuffer(byte[] bytes) throws Exception {
		byte[] msgIdByte = BitOperator.integerTo2Bytes(messageHeader.getMessageId());
		byte[] protocolBytes = ClientUtils.getProtocol(bytes, msgIdByte);
		this.decoder(protocolBytes);
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		if (bytes != null && bytes.length > 0) {
			byte[] bytes1 = Arrays.copyOfRange(bytes, 1, bytes.length - 1);
			byte[] bytesBody = EscapeUtils.doEscape4Receive(bytes1, 0, bytes1.length);
			BitOperator.checkCrc(bytesBody, this.messageHeader.getMessageId());
			messageHeader.decoder(Arrays.copyOfRange(bytesBody, 0, 12));
			this.decoderBody(bytesBody);
			crc = BitOperator.oneByteToInteger(bytesBody[bytesBody.length - 1]);
		}
	}

	/**
	 * 解析为对象
	 */
	protected abstract void decoderBody(byte[] bytesBody) throws Exception;
}
