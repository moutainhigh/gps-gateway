package com.zkkj.gps.gateway.protocol.component.messagebody.append;

import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/13 11:18
 * 位置追加信息公共字段
 */
public abstract class BaseSiteAppendMessage implements ProtocolSerializable {
	/**
	 * 附加信息id 1(byte)
	 */
	private int apdMsgId;

	/**
	 * 附加信息长度 1
	 */
	private int apdMsgLength;

	@Override
	public String toString() {
		return "附加信息id:" + apdMsgId +
				"附加信息长度:" + apdMsgLength +
				subToString();
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		apdMsgId = BitOperator.oneByteToInteger(bytes[0]);
		apdMsgLength = BitOperator.oneByteToInteger(bytes[1]);
		byte[] data = Arrays.copyOfRange(bytes, 2, bytes.length);
		subDecoder(data);
	}

	protected abstract void subDecoder(byte[] bytes) throws Exception;

	protected abstract String subToString();

	public int getApdMsgId() {
		return apdMsgId;
	}

	public int getApdMsgLength() {
		return apdMsgLength;
	}
}