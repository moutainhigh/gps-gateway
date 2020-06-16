package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

/**
 * @author chailixing
 * 2019/4/18 12:09
 * 写采集器无线信道
 */
public final class P_48 extends BaseCompose {
	public P_48(String address, String ipAddr, int wirelessChannel) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_48, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
		this.wirelessChannel = wirelessChannel;
	}

	public P_48(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_48, bytes);
	}

	/**
	 * pc终端地址 6
	 */
	private String ipAddr;

	/**
	 * 无线信道(0-370) 2
	 */
	private int wirelessChannel;

	@Override
	protected String subToString() {
		return "\npc终端地址:" + ipAddr +
				"\n无线信道:" + wirelessChannel;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		return BitOperator.concatAll(
				BitOperator.encoderIpAddr(ipAddr),
				BitOperator.integerTo2LowBytes(wirelessChannel)
		);
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		ipAddr = BitOperator.decoderIpAddr(bytes);
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public int getWirelessChannel() {
		return wirelessChannel;
	}
}