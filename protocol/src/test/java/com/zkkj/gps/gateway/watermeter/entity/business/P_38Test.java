package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.util.HexStringUtils;
import org.junit.Test;

public class P_38Test {
	private byte[] b = {
			(byte) 0xC0,
			0x31,
			0x34, 0x01, 0x13, 0x01, 0x00, 0x01,
			0x38,
			0x00,
			0x00, 0x06,
			(byte) 0xC0, (byte) 0xA8, 0x03, 0x64, 0x58, 0x58,
			0x00, 0x55,
			0x38,
			(byte) 0xD0
	};

	@Test
	public void encoderBody() throws Exception {
		P_38 p_38 = new P_38("340113010001", "192.168.3.100:8888");
		System.out.println(HexStringUtils.toHexString(p_38.encoder()));
	}

	@Test
	public void decoderBody() throws Exception {
//		P_38 p_38 = new P_38(b);
//		System.out.println(p_38);
	}
}