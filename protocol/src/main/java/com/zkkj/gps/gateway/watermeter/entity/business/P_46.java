package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

/**
 * @author chailixing
 * 2019/4/18 12:09
 * 删除水表无线链路
 */
public final class P_46 extends BaseCompose {
	public P_46(String address, String ipAddr) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_46, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
	}

	public P_46(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_46, bytes);
	}

	/**
	 * pc终端地址 6
	 */
	private String ipAddr;

	@Override
	protected String subToString() {
		return "\npc终端地址:" + ipAddr;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		return BitOperator.encoderIpAddr(ipAddr);
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		ipAddr = BitOperator.decoderIpAddr(bytes);
	}

	public String getIpAddr() {
		return ipAddr;
	}
}