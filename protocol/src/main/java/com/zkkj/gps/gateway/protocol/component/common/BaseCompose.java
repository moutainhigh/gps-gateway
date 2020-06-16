package com.zkkj.gps.gateway.protocol.component.common;


import com.zkkj.gps.gateway.protocol.ProtocolSerializable;

/**
 * @author chailixing
 * 2018/12/26 9:31
 * 基础组成部分: 帧头 消息头 校验码 帧尾
 */
public abstract class BaseCompose implements ProtocolSerializable {
	public BaseCompose(byte[] bytes, int protocolId) throws Exception {
		this.messageHeader = new MessageHeader(protocolId);
		this.decoder(bytes);
	}

	public BaseCompose(int protocolId, String terminalId, int serialNumber) throws Exception {
		this.messageHeader = new MessageHeader(protocolId, terminalId, serialNumber);
	}

	public BaseCompose(int protocolId) throws Exception {
		this.messageHeader = new MessageHeader(protocolId);
	}

	/**
	 * 帧头 1(byte)
	 */
	protected final byte FRAME_HEADER = 0x7e;

	/**
	 * 消息头 12
	 */
	protected MessageHeader messageHeader;

	/**
	 * 帧尾 1
	 */
	protected final byte FRAME_FOOTER = 0x7e;

	/**
	 * 校验码 1
	 */
	protected int crc;

	@Override
	public String toString() {
		return "{帧头:" + FRAME_HEADER +
				"\n消息头:" + messageHeader +
				"\n消息体:" + subToString() +
				"\n校验码:" + crc +
				"\n帧尾:" + FRAME_FOOTER + "}";
	}

	protected abstract String subToString();

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
}
