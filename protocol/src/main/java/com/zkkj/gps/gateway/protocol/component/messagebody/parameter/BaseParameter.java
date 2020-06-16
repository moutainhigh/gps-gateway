package com.zkkj.gps.gateway.protocol.component.messagebody.parameter;

import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/15 16:13
 * 参数项抽象
 */
public abstract class BaseParameter implements ProtocolSerializable {
	BaseParameter(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	BaseParameter(int paramId, int paramLength) throws Exception {
		this.paramId = paramId;
		this.paramLength = paramLength;
	}

	BaseParameter() {
	}

	/**
	 * 参数id 1
	 */
	private int paramId;

	/**
	 * 参数长度 1
	 */
	int paramLength;

	@Override
	public String toString() {
		return "{参数id:" + paramId +
				" 参数长度:" + paramLength +
				subToString();
	}

	@Override
	public byte[] encoder() {
		return BitOperator.concatAll(
				BitOperator.integerTo1Bytes(paramId),
				BitOperator.integerTo1Bytes(paramLength),
				subEncoder()
		);
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		paramId = BitOperator.oneByteToInteger(bytes[0]);
		paramLength = BitOperator.oneByteToInteger(bytes[1]);
		byte[] data = Arrays.copyOfRange(bytes, 2, bytes.length);
		subDecoder(data);
	}

	protected abstract void subDecoder(byte[] data) throws Exception;

	protected abstract byte[] subEncoder();

	protected abstract String subToString();

	public abstract Object getParamValue();

	public int getParamId() {
		return paramId;
	}

	public int getParamLength() {
		return paramLength;
	}

	public void setParamLength(int paramLength) {
		this.paramLength = paramLength;
	}
}
