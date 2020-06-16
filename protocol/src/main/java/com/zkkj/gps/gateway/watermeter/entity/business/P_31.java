package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.component.WirelessMeterData;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BCD8421Operator;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/18 12:09
 * 读水表数据
 */
public final class P_31 extends BaseCompose {
	public P_31(String address, String ipAddr, String wirelessMeterAddress) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_31, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
		this.wirelessMeterAddress = wirelessMeterAddress;
	}

	public P_31(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_31, bytes);
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
	 * 无线水表数据 18
	 */
	private WirelessMeterData wirelessMeterData;

	@Override
	protected String subToString() {
		return "\npc终端地址:" + ipAddr +
				"\n无线水表地址:" + wirelessMeterAddress +
				"\n无线水表数据:" + wirelessMeterData;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		return BitOperator.concatAll(
				BitOperator.encoderIpAddr(ipAddr),
				BCD8421Operator.string2Bcd(wirelessMeterAddress)
		);
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		ipAddr = BitOperator.decoderIpAddr(Arrays.copyOfRange(bytes, 0, 6));
		wirelessMeterAddress = BCD8421Operator.bcd2String(Arrays.copyOfRange(bytes, 6, 10));
		byte[] nextData = Arrays.copyOfRange(bytes, 10, bytes.length);
		wirelessMeterData = new WirelessMeterData(nextData);
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public String getWirelessMeterAddress() {
		return wirelessMeterAddress;
	}

	public WirelessMeterData getWirelessMeterData() {
		return wirelessMeterData;
	}
}