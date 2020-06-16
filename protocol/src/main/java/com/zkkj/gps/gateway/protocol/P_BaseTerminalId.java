package com.zkkj.gps.gateway.protocol;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;

public class P_BaseTerminalId extends DestinationBaseCompose {

	public P_BaseTerminalId(byte[] bytes) throws Exception {
		super(bytes, 0x0000);
	}

	@Override
	protected void decoderBody(byte[] bytesBody) throws Exception {
		this.protocolBodyStr = HexStringUtils.toHexString(bytesBody);
	}

	public String getProtocolBodyStr() {
		return protocolBodyStr;
	}

	private String protocolBodyStr;

	@Override
	public String subToString() {
		return "{帧头:" + FRAME_HEADER +
				" 消息头:" + messageHeader.toString() +
//                " 消息体:" + protocolBodyStr +
				" 帧尾:" + FRAME_FOOTER +
				" 校验码:" + crc + "}";
	}
}
