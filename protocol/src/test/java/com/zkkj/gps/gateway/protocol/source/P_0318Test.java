package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.util.ClientUtils;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;
import org.junit.Test;

public class P_0318Test extends BaseTest {
	@Test
	public void encoderBody() throws Exception {
		P_0318 p_0318 = new P_0318("2", 1, 2, 2, 2, 2, 2, 2, 2);
		System.out.println(HexStringUtils.toHexString(p_0318.encoder()));
	}

}