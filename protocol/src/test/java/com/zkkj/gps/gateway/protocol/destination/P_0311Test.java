package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import org.junit.Test;

public class P_0311Test extends BaseTest {
	@Test
	public void decoderBody() throws Exception {
		byte[] buf = this.getBytes(ProtocolConsts.P_0311);
		P_0311 p_0311 = new P_0311(buf);
		System.out.println(p_0311);
	}
}