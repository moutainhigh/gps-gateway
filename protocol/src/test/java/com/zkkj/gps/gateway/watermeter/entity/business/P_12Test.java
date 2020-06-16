package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.util.BitOperator;
import com.zkkj.gps.gateway.watermeter.util.HexStringUtils;
import org.junit.Test;

public class P_12Test {

	private byte[] b = {
			(byte) 0xC0,
			0x31,
			0x01, 0x00, 0x20, 0x18, 0x11, 0x60,
			0x12,
			0x00,
			0x00, 0x0c,
			0x71, (byte) 0x8D, 0x1C, (byte) 0x92, 0x14, 0x03,
			0X19, 0X05, 0X10, 0X11, 0X20, 0X55,
			(byte) 0x70,
			(byte) 0xD0
	};

	private byte[] a = {0x31,
			0x01, 0x00, 0x20, 0x18, 0x11, 0x60,
			0x12,
			0x00,
			0x00, 0x0c,
			0x71, (byte) 0x8D, 0x1C, (byte) 0x92, 0x14, 0x03,
			0X19, 0X05, 0X10, 0X11, 0X20, 0X55,};

	@Test
	public void encoderBody() throws Exception {
		P_12 p_12 = new P_12("010020181160", "113.141.28.146:2003");
		System.out.println(HexStringUtils.toHexString(p_12.encoder()));
	}

	@Test
	public void decoderBody() throws Exception {
//		byte crc = BitOperator.getCrc(a);
//		System.out.println(crc);
		P_12 p_12 = new P_12(b);
		System.out.println(p_12.toString());
	}
}