package com.zkkj.gps.gateway.watermeter.entity.init;

import com.zkkj.gps.gateway.watermeter.util.HexStringUtils;
import org.junit.Test;

public class P_01Test {

	private byte[] b = {
			(byte) 0xc0,
			0x21,
			0x01, 0x00, 0x20, 0x18, 0x11, 0x60,
			0x01,
			0x00,
			0x00, 0x01,
			0x1c,
			(byte) 0xe9,
			(byte) 0xd0
	};

	@Test
	public void encoderBody() throws Exception {
		P_01 p_01 = new P_01("010020181160", 0X00);
		System.out.println(HexStringUtils.toHexString(p_01.encoder()));
	}

	@Test
	public void decoderBody() throws Exception {
		P_01 p_01 = new P_01(b);
		System.out.println(p_01.toString());
	}
}