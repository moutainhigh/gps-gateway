package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

/**
 * @author chailixing
 * 2019/4/18 12:09
 * 设置抄表终端 IP 端口
 */
public final class P_24 extends BaseCompose {
	public P_24(String address, String ipAddr, String ip, int port) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_24, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
		this.ip = ip;
		this.port = port;
	}

	public P_24(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_24, bytes);
	}

	/**
	 * pc终端地址 6
	 */
	private String ipAddr;

	/**
	 * 下发ip 4
	 */
	private String ip;

	/**
	 * 下发端口 2
	 */
	private int port;

	@Override
	protected String subToString() {
		return "\npc终端地址:" + ipAddr +
				"\n下发ip:" + ip +
				"\n下发端口:" + port;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		return BitOperator.concatAll(
				BitOperator.encoderIpAddr(ipAddr),
				BitOperator.processIp(ip),
				BitOperator.integerTo2Bytes(port)
		);
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		ipAddr = BitOperator.decoderIpAddr(bytes);
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
}