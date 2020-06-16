package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import org.junit.Test;

public class P_0002Test extends BaseTest {
	@Test
	public void decoderBody() throws Exception {
		byte[] buf = this.getBytes(ProtocolConsts.P_0002);
		P_0002 p_0002 = new P_0002(buf);
		System.out.println(p_0002);
	}
}