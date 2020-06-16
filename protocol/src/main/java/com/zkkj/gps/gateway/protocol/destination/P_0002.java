package com.zkkj.gps.gateway.protocol.destination;


import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;

/**
 * @author chailixing
 * 2019/3/4 17:40
 * 终端心跳数据
 */
public final class P_0002 extends DestinationBaseCompose {
	public P_0002(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0002);
	}

	public P_0002() throws Exception {
		super(ProtocolConsts.P_0002);
	}

	// 消息体为空

	@Override
	public String subToString() {
		return "消息体为空!";
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		// 消息体为空
	}
}