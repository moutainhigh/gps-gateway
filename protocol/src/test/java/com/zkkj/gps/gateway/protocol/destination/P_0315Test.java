package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import org.junit.Test;

public class P_0315Test extends BaseTest {
	@Test
	public void decoderBody() throws Exception {
		byte[] bytes = this.getBytes(ProtocolConsts.P_0315);
		P_0315 p_0315 = new P_0315(bytes);
		System.out.println(p_0315);
	}

}