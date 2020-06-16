package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.BaseTest;
import org.junit.Test;

public class P_0317Test extends BaseTest {
	@Test
	public void decoderBody() throws Exception {
		byte[] bytes = {
				0x7e,
				0x03, 0x17,
				0x00, 0X06,
				0x09, 0x50, 0x39, 0x62, 0x78, 0x20,
				0x56, 0x05,

				0x00, 0x4b,
				0x00, 0x02,

				0x00,0x01,
				0X00,0X00,0X00,0X00,
				0X00,0X00,0X00,0X00,
				0X00,0X00,0X00,0X00,
				0X00,0X00,
				0X00,0X00,

				0x00,0x02,
				0X00,0X00,0X00,0X00,
				0X00,0X00,0X00,0X00,
				0X00,0X00,0X00,0X00,
				0X00,0X00,
				0X00,0X00,

				0x38,
				0x7e
		};
//		P_0317 p_0317 = new P_0317(bytes);
//		System.out.println(p_0317);
	}
}