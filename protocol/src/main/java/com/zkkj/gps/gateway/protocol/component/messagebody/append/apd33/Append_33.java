package com.zkkj.gps.gateway.protocol.component.messagebody.append.apd33;


import com.zkkj.gps.gateway.protocol.component.messagebody.append.BaseSiteAppendMessage;
import com.zkkj.gps.gateway.protocol.util.BCD8421Operator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/13 11:54
 * 33 智能煤炭终端状态扩展信息
 */
public final class Append_33 extends BaseSiteAppendMessage {
	public Append_33(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	/**
	 * 开始符 1
	 */
	private String beginSymbol;

	/**
	 * 命令 3
	 * M00 主动上报
	 */
	private String command;

	/**
	 * 分隔符 1 共有两为分隔符,数据长度前后
	 */
	private String separator;

	/**
	 * 数据长度 3
	 */
	private String dataLength;

	/* ----终端状态begin---- */
	/**
	 * 保留字 1 共有两位保留字
	 */
	private String reservedWord;

	/**
	 * 电量值 3 400 (毫伏,相当于4.00V)
	 */
	private String power;

	/**
	 * 防拆开关 1 0:正常 1:被拆触发
	 */
	private String antiDismantle;

	/**
	 * 上报原因 1
	 * 0:定时 1:锁开/关状态变更 2:被拆状态变更
	 * 3:按键申请 4:RFID施封 5:RFID解封
	 * 6:SMS施封 7:SMS解封 8:因低电量休眠下线
	 * 9:非注册卡施封 A :非注册卡解封 B:注册卡施封
	 * C:注册卡解封 D:应急密码解封 E:司机卡 F:平台施封
	 * G:平台解封 H:非注册上锁卡 I:非注册开锁卡
	 * J:注册上锁卡 K:注册开锁卡 L:施封下有子锁被解开（异常变化）
	 * M:有子锁被拆开（异常变化） N:有子锁通讯超时（异常变化）
	 * O:子锁正常状态变更（正常变化） P:区域外刷施封卡
	 * Q:区域外刷解封卡 R:区域内自动解封 S:区域外自动施封
	 */
	private String report;
	/* ----终端状态end---- */

	/**
	 * 操作id 可变长度 1-20
	 * 0: 按键施封操作ID
	 * 12345678: IC卡操作ID
	 * 008613798242980: SMS操作ID(手机号)
	 */
	private String operationId;

	/**
	 * 内分隔符 1
	 */
	private String innerSeparator;

	/**
	 * 子锁状态 12
	 * 0：无绑定子锁
	 * 1：已绑定子锁
	 * 2：子锁为关
	 * 3：子锁为开
	 * 4：子锁被破坏
	 * 5：子锁通讯超时
	 */
	private String subLocks;

	/**
	 * 用户业务id 可变长度(0-16)
	 */
	private String userBusinessId;

	/**
	 * 结束符 1
	 */
	private String endSymbol;

	@Override
	protected String subToString() {
		return " 开始符:" + beginSymbol +
				" 命令:" + command +
				" 分隔符:" + separator +
				" 数据长度:" + dataLength +
				" 保留字:" + reservedWord +
				" 电量值:" + power +
				" 防拆开关:" + antiDismantle +
				" 上报原因:" + report +
				" 操作id:" + operationId +
				" 内分隔符:" + innerSeparator +
				" 子锁:" + subLocks +
				" 用户业务id:" + userBusinessId +
				" 结束符:" + endSymbol;
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		beginSymbol = BCD8421Operator.bytes2AsciiString(bytes[0]);
		command = BCD8421Operator.bytes2AsciiString(
				Arrays.copyOfRange(bytes, 1, 4));
		separator = BCD8421Operator.bytes2AsciiString(bytes[4]);
		// 数据长度 2-3字节
		int index = 0;
		for (int i = 5; i < 8; ++i) {
			if (bytes[i] == 0x2c) {
				dataLength = BCD8421Operator.bytes2AsciiString(
						Arrays.copyOfRange(bytes, 5, i));
				index = i;
				break;
			}
		}
		reservedWord = BCD8421Operator.bytes2AsciiString(bytes[index + 1]);
		power = BCD8421Operator.bytes2AsciiString(
				Arrays.copyOfRange(bytes, index + 3, index + 6));
		if (power != null && !power.equals("")){
			try {
				power = ((int) ((Double.valueOf(power) * 100 )/ 420)) + "";
			} catch (Exception e){
				power = "0";
				//System.out.println("电量计算异常" + e.toString());
			}
		} else {
			power = "0";
		}
		antiDismantle = BCD8421Operator.bytes2AsciiString(bytes[index + 6]);
		report = BCD8421Operator.bytes2AsciiString(bytes[index + 7]);
		// 19-08-17代码添加
		operationId = "";
		innerSeparator = "";
		subLocks = "";
		userBusinessId = "";
		// 19-08-17注释，旧智运盒报文解析出错报错
		// 操作id 长度不明
		/*for (int i = index + 8; ; ++i) {
			if (bytes[i] == 0x26) {
				operationId = BCD8421Operator.bytes2AsciiString(
						Arrays.copyOfRange(bytes, index + 8, i));
				index = i;
				break;
			}
		}
		innerSeparator = BCD8421Operator.bytes2AsciiString(bytes[index]);
		subLocks = BCD8421Operator.bytes2AsciiString(
				Arrays.copyOfRange(bytes, index + 1, index + 13));
		//用户业务id 0-16字节 可变
		if (bytes[index + 14] == 0x23) {
			userBusinessId = "";
		} else {
			for (int i = index + 14; i < index + 31; ++i) {
				if (bytes[i] == 0x23) {
					userBusinessId = BCD8421Operator.bytes2AsciiString(
							Arrays.copyOfRange(bytes, index + 14, i));
					break;
				}
			}
		}*/
		endSymbol = BCD8421Operator.bytes2AsciiString(bytes[bytes.length - 1]);
	}

	public String getBeginSymbol() {
		return beginSymbol;
	}

	public String getCommand() {
		return command;
	}

	public String getSeparator() {
		return separator;
	}

	public String getDataLength() {
		return dataLength;
	}

	public String getReservedWord() {
		return reservedWord;
	}

	public String getPower() {
		return power;
	}

	public String getAntiDismantle() {
		return antiDismantle;
	}

	public String getReport() {
		return report;
	}

	public String getOperationId() {
		return operationId;
	}

	public String getInnerSeparator() {
		return innerSeparator;
	}

	public String getSubLocks() {
		return subLocks;
	}

	public String getUserBusinessId() {
		return userBusinessId;
	}

	public String getEndSymbol() {
		return endSymbol;
	}

	public Append_33() {
	}

	public void setBeginSymbol(String beginSymbol) {
		this.beginSymbol = beginSymbol;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}

	public void setReservedWord(String reservedWord) {
		this.reservedWord = reservedWord;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public void setAntiDismantle(String antiDismantle) {
		this.antiDismantle = antiDismantle;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public void setInnerSeparator(String innerSeparator) {
		this.innerSeparator = innerSeparator;
	}

	public void setSubLocks(String subLocks) {
		this.subLocks = subLocks;
	}

	public void setUserBusinessId(String userBusinessId) {
		this.userBusinessId = userBusinessId;
	}

	public void setEndSymbol(String endSymbol) {
		this.endSymbol = endSymbol;
	}


}