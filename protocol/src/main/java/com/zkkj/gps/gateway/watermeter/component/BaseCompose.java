package com.zkkj.gps.gateway.watermeter.component;

import com.zkkj.gps.gateway.watermeter.util.BCD8421Operator;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;
import com.zkkj.gps.gateway.watermeter.util.HexStringUtils;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/18 9:28
 * 公共组成
 */
public abstract class BaseCompose implements ProtocolSerializable {
	protected BaseCompose(int funcCode, byte[] bytes) throws Exception {
		this(funcCode);
		this.decoder(bytes);
	}

	protected BaseCompose(int funcCode) {
		this.func = funcCode;
		this.funcStr = HexStringUtils.toHexString(
				BitOperator.integerTo1Bytes(funcCode));
	}

	protected BaseCompose(int type, String address, int funcCode, int control) {
		this.type = type;
		this.func = funcCode;
		this.funcStr = HexStringUtils.toHexString(BitOperator.integerTo1Bytes(func));
		this.address = address;
		this.control = control;
	}

	/**
	 * 起始符 1
	 */
	private final int BEGIN = 0XC0;

	/**
	 * 类型码 1
	 */
	private int type;

	/**
	 * 地址域 6 bcd
	 */
	private String address;

	/**
	 * 功能码 1
	 */
	private int func;

	/**
	 * 功能码字符串
	 */
	private String funcStr;

	/**
	 * 控制码 1
	 */
	private int control;

	/**
	 * 长度域 2
	 */
	private int length;

	/**
	 * 校验码 1
	 */
	private int check;

	/**
	 * 结束符 1
	 */
	private final int END = 0XD0;

	@Override
	public String toString() {
		return "起始符:" + BEGIN +
				"\n类型码:" + type +
				"\n地址域:" + address +
				"\n功能码:" + func +
				"\n功能码Str:" + funcStr +
				"\n控制码:" + control +
				"\n长度域:" + length +
				subToString() +
				"\n校验码:" + check +
				"\n结束符:" + END;
	}

	@Override
	public byte[] encoder() throws Exception {
		// 计算数据域长度
		byte[] bytes = encoderBody();
		length = bytes.length;
		// 数据长度:0-500
		if (length > 500) {
			throw new RuntimeException("数据域长度异常!" + length);
		}
		// 协议字节数组
		byte[] protocolBody = BitOperator.concatAll(
				BitOperator.integerTo1Bytes(type),
				BCD8421Operator.string2Bcd(address),
				BitOperator.integerTo1Bytes(func),
				BitOperator.integerTo1Bytes(control),
				BitOperator.integerTo2Bytes(length)
		);
		if (length > 0) {
			protocolBody = BitOperator.concatAll(protocolBody, bytes);
		}
		// 计算校验码
		check = BitOperator.getCrc(protocolBody);
		// 下发
		return BitOperator.concatAll(
				BitOperator.integerTo1Bytes(BEGIN),
				protocolBody,
				BitOperator.integerTo1Bytes(check),
				BitOperator.integerTo1Bytes(END)
		);
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		if (bytes != null && bytes.length > 0) {
			// 去掉帧头帧尾
			byte[] realBytes = Arrays.copyOfRange(bytes, 1, bytes.length - 1);
			// 检测校验码
			boolean result = BitOperator.checkCrc(realBytes, funcStr);
			if (result) {
				type = BitOperator.oneByteToInteger(realBytes[0]);
				address = BCD8421Operator.bcd2String(Arrays.copyOfRange(realBytes, 1, 7));
				// func在初始化时确定
				control = BitOperator.oneByteToInteger(realBytes[8]);
				length = BitOperator.twoBytesToInteger(Arrays.copyOfRange(realBytes, 9, 11));
				int checkIndex = realBytes.length - 1;
				if (length != 0) {
					decoderBody(Arrays.copyOfRange(realBytes, 11, checkIndex));
				}
				check = BitOperator.oneByteToInteger(realBytes[checkIndex]);
			}
		}
	}

	protected abstract String subToString();

	protected abstract byte[] encoderBody() throws Exception;

	protected abstract void decoderBody(byte[] bytes) throws Exception;

	public int getBegin() {
		return BEGIN;
	}

	public int getType() {
		return type;
	}

	public String getAddress() {
		return address;
	}

	public int getFunc() {
		return func;
	}

	public int getControl() {
		return control;
	}

	public int getLength() {
		return length;
	}

	public int getCheck() {
		return check;
	}

	public int getEnd() {
		return END;
	}

	public int getBEGIN() {
		return BEGIN;
	}

	public String getFuncStr() {
		return funcStr;
	}

	public int getEND() {
		return END;
	}
}