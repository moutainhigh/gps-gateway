package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.BaseTest;
import org.junit.Test;

public class P_0313Test extends BaseTest {
	@Test
	public void decoderBody() throws Exception {
		byte[] buf = {
				0X7e,
				0x03, 0x13,
				0x01, 0x03,
				0x09, 0x50, 0x39, 0x62, 0x78, 0x20,
				0x54, 0x72,
				0x00, 0x02,
				0x03,
				0x33, 0x00,
				0X07, 0X01, 0x03,
				0x37, 0x01, 0x1b,
				0x30,
				0x7e
		};
//		P_0313 p_0313 = new P_0313(buf);
//		System.out.println(p_0313);
	}
}