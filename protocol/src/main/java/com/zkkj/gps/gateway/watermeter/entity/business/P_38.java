package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/18 12:09
 * 读采集器无线信道
 */
public final class P_38 extends BaseCompose {
	public P_38(String address, String ipAddr) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_38, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
	}

	public P_38(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_38, bytes);
	}

	/**
	 * pc终端地址 6
	 */
	private String ipAddr;

	/**
	 * 无线信道 2
	 */
	private int wirelessChannel;

	@Override
	protected String subToString() {
		return "\npc终端地址:" + ipAddr +
				"\n无线信道:" + wirelessChannel;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		return BitOperator.encoderIpAddr(ipAddr);
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		ipAddr = BitOperator.decoderIpAddr(Arrays.copyOfRange(bytes, 0, 6));
		wirelessChannel = BitOperator.twoLowBytesToInteger(Arrays.copyOfRange(bytes, 6, bytes.length));
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public int getWirelessChannel() {
		return wirelessChannel;
	}
}