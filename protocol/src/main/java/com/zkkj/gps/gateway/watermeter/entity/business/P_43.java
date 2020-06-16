package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BCD8421Operator;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/18 12:09
 * 写水表无线信道
 */
public final class P_43 extends BaseCompose {
	public P_43(String address, String ipAddr, String wirelessMeterAddress, int wirelessChannel, int wirelessChannelStartTime) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_43, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
		this.wirelessMeterAddress = wirelessMeterAddress;
		this.wirelessChannel = wirelessChannel;
		this.wirelessChannelStartTime = wirelessChannelStartTime;
	}

	public P_43(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_43, bytes);
	}

	/**
	 * pc终端地址 6
	 */
	private String ipAddr;

	/**
	 * 无线水表地址 4
	 */
	private String wirelessMeterAddress;

	/**
	 * 无线信道 2
	 */
	private int wirelessChannel;

	/**
	 * 无线信道生效时间(分钟) 1
	 */
	private int wirelessChannelStartTime;

	@Override
	protected String subToString() {
		return "\npc终端地址:" + ipAddr +
				"\n无线水表地址:" + wirelessMeterAddress +
				"\n无线信道:" + wirelessChannel +
				"\n无线信道生效时间:" + wirelessChannelStartTime;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		return BitOperator.concatAll(
				BitOperator.encoderIpAddr(ipAddr),
				BCD8421Operator.string2Bcd(wirelessMeterAddress),
				BitOperator.integerTo2LowBytes(wirelessChannel),
				BitOperator.integerTo1Bytes(wirelessChannelStartTime)
		);
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		ipAddr = BitOperator.decoderIpAddr(Arrays.copyOfRange(bytes, 0, 6));
		wirelessMeterAddress = BCD8421Operator.bcd2String(Arrays.copyOfRange(bytes, 6, 10));
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public String getWirelessMeterAddress() {
		return wirelessMeterAddress;
	}

	public int getWirelessChannel() {
		return wirelessChannel;
	}

	public int getWirelessChannelStartTime() {
		return wirelessChannelStartTime;
	}
}