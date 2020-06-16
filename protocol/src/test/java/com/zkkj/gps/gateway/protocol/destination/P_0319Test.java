package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import org.junit.Test;

public class P_0319Test extends BaseTest {
	@Test
	public void decoderBody() throws Exception {
		byte[] bytes = this.getBytes(ProtocolConsts.P_0319);
		P_0319 p_0319 = new P_0319(bytes);
		System.out.println(p_0319);
	}
}