package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.util.ClientUtils;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;
import org.junit.Test;

public class P_8001Test extends BaseTest {
	@Test
	public void encoderBody() throws Exception {
		P_8001 p_8001 = new P_8001("2", 2, 2, 2, 1);
		System.out.println(HexStringUtils.toHexString(p_8001.encoder()));
	}
}