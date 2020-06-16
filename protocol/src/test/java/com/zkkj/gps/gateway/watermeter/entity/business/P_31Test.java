package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.util.HexStringUtils;
import org.junit.Test;

public class P_31Test {
	private byte[] b = {
			(byte) 0xC0,
			0x31,
			0x34, 0x01, 0x13, 0x01, 0x00, 0x01,
			0x31,
			0x00,
			0x00, 0x0A,
			(byte) 0xC0, (byte) 0xA8, 0x03, 0x64, 0x58, 0x58,
			0x00, 0x00, 0x22, (byte) 0xA4,
			(byte) 0xC0, (byte) 0xA8, 0x03, 0x64, 0x58, 0x58,
			(byte) 0xC0, (byte) 0xA8, 0x03, 0x64, 0x58, 0x58,
			(byte) 0xC0, (byte) 0xA8, 0x03, 0x64, 0x58, 0x58,
			(byte) 0xFB,
			(byte) 0xD0
	};

	@Test
	public void encoderBody() throws Exception {
		P_31 p_31 = new P_31("010020181160", "192.168.3.100:15151", "0000729");
		System.out.println(HexStringUtils.toHexString(p_31.encoder()));
	}

	@Test
	public void decoderBody() throws Exception {
//		P_31 p_31 = new P_31(b);
//		System.out.println(p_31);
		int i = 0X2D9;
	}

}