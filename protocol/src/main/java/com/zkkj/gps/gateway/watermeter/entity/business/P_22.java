package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BCD8421Operator;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author chailixing
 * 2019/4/18 12:08
 * 设置采集器时间
 */
public final class P_22 extends BaseCompose {
	public P_22(String address, String ipAddr, LocalDateTime dateTime) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_22, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
		this.dateTime = dateTime;
	}

	public P_22(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_22, bytes);
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
		String dataTimeStr = dateTime.format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
		return BitOperator.concatAll(
				BitOperator.encoderIpAddr(ipAddr),
				BCD8421Operator.string2Bcd(dataTimeStr)
		);
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		ipAddr = BitOperator.decoderIpAddr(bytes);
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
}