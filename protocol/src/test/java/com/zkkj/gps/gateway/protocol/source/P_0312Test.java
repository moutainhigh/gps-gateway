package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.util.ClientUtils;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;
import org.junit.Test;

public class P_0312Test extends BaseTest {
	@Test
	public void encoderBody() throws Exception {
		P_0312 p_0312 = new P_0312("1", 2, 2,2,2,2,2);
		System.out.println(HexStringUtils.toHexString(p_0312.encoder()));
		p_0312.getMessageHeader().getMessageAttribute();

	}

}