package com.zkkj.gps.gateway.protocol.constant;

public class ProtocolConsts {
	/*终端上传协议*/
	public final static int P_0200 = 0X0200;
	public final static int P_0311 = 0x0311;
	public final static int P_0313 = 0x0313;
	public final static int P_0411 = 0x0411;
	public final static int P_0310 = 0x0310;
	public final static int P_0312 = 0x0312;
	public final static int P_0410 = 0x0410;
	public final static int P_8001 = 0x8001;
	public final static int P_0210 = 0x0210;
	public final static int P_0002 = 0x0002;
	public final static int P_0314 = 0x0314;
	public final static int P_0315 = 0x0315;
	public final static int P_0316 = 0x0316;
	public final static int P_0317 = 0x0317;
	public final static int P_0318 = 0x0318;
	public final static int P_0319 = 0x0319;

	public final static int[] getProtocolConsts() {
		int[] protocolConsts = new int[]{
				ProtocolConsts.P_0200,
				ProtocolConsts.P_0310,
				ProtocolConsts.P_0311,
				ProtocolConsts.P_0312,
				ProtocolConsts.P_0313,
				ProtocolConsts.P_0410,
				ProtocolConsts.P_0411,
				ProtocolConsts.P_8001,
				ProtocolConsts.P_0210,
				ProtocolConsts.P_0002,
				ProtocolConsts.P_0314,
				ProtocolConsts.P_0315,
				ProtocolConsts.P_0316,
				ProtocolConsts.P_0317,
				ProtocolConsts.P_0318,
				ProtocolConsts.P_0319,
		};
		return protocolConsts;
	}
}
