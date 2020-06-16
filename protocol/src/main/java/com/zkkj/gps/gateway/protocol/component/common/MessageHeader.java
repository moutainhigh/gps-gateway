package com.zkkj.gps.gateway.protocol.component.common;

import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.util.BCD8421Operator;
import com.zkkj.gps.gateway.protocol.util.BitOperator;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;

import java.util.Arrays;

/**
 * @author chailixing
 * 2018/12/29 11:23
 * 消息头 12字节
 */
public final class MessageHeader implements ProtocolSerializable {

	public MessageHeader(int msgId) {
		messageId = msgId;
	}

	public MessageHeader(int messageId, String terminalId, int serialNumber) {
		this.messageId = messageId;
		this.terminalId = terminalId;
		this.serialNumber = serialNumber;
	}

	/**
	 * 消息id 2
	 */
	private int messageId;

	/**
	 * 消息id 2
	 */
	private String messageIdStr;

	/**
	 * 消息体属性 2
	 * [0-9] 消息体长度
	 * [14] 0:长连接
	 */
	private int messageAttribute;

	/**
	 * 终端序号 6 bcd码
	 * 1) 使用手机号
	 * 2) 使用终端内的IMEI
	 * 3) 使用终端贴牌号
	 */
	private String terminalId;

	/**
	 * 流水号 2
	 */
	private int serialNumber;

	@Override
	public String toString() {
		return "{消息id:" + messageId +
				" 消息idStr:" + messageIdStr +
				" 消息属性:" + messageAttribute +
				" 终端序号:" + terminalId +
				" 流水号:" + serialNumber + "}";
	}

	@Override
	public byte[] encoder(int messageBodyLength) throws Exception {
		messageAttribute = messageBodyLength;
		return BitOperator.concatAll(
				BitOperator.integerTo2Bytes(messageId),
				BitOperator.integerTo2Bytes(messageAttribute),
				BCD8421Operator.str2Bcd(terminalId),
				BitOperator.integerTo2Bytes(serialNumber)
		);
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		messageIdStr = HexStringUtils.toHexString(Arrays.copyOfRange(bytes, 0, 2));
		messageAttribute = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 2, 4));
		terminalId = BCD8421Operator.bcd2ToString(Arrays.copyOfRange(bytes, 4, 10));
		serialNumber = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 10, 12));
	}

	public int getMessageId() {
		return messageId;
	}

	public int getMessageAttribute() {
		return messageAttribute;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public String getMessageIdStr() {
		return messageIdStr;
	}

	public int getSerialNumber() {
		return serialNumber;
	}
}