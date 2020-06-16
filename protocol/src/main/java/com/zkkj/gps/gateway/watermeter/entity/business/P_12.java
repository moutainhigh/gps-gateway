package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BCD8421Operator;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/18 12:08
 * 读取采集器时间
 */
public final class P_12 extends BaseCompose {
	public P_12(String address, String ipAddr) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_12, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
	}

	public P_12(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_12, bytes);
	}

	/**
	 * pc终端地址 6
	 */
	private String ipAddr;

	/**
	 * 标准时间 6
	 */
	private LocalDateTime dateTime;

	@Override
	protected String subToString() {
		return "\nPC终端地址: " + ipAddr +
				"\n标准时间: " + dateTime;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		return BitOperator.encoderIpAddr(ipAddr);
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		ipAddr = BitOperator.decoderIpAddr(Arrays.copyOfRange(bytes, 0, 6));
		String dateString = BCD8421Operator.bcd2String(Arrays.copyOfRange(bytes, 6, 12));
		dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyMMddHHmmss"));
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
}