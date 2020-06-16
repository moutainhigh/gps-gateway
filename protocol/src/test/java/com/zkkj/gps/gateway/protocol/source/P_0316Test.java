package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;
import org.junit.Test;

public class P_0316Test extends BaseTest {
	@Test
	public void encoderBody() throws Exception {
		P_0316 p_0316 = new P_0316("2", 2, 2, 2, 2, 2);
		System.out.println(HexStringUtils.toHexString(p_0316.encoder()));
	}
}