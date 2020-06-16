package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import org.junit.Test;

public class P_0411Test extends BaseTest {
	@Test
	public void decoderBody() throws Exception {
		byte[] bytes = this.getBytes(ProtocolConsts.P_0411);
		P_0411 p_0411 = new P_0411(bytes);
		System.out.println(p_0411);
	}
}