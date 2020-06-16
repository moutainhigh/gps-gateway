package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.WirelessMeterLink;
import com.zkkj.gps.gateway.watermeter.util.HexStringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class P_45Test {
	private byte[] b = {
			(byte) 0xC0,
			0x32,
			0x01,0x00,0x20,0x18,0x11,0x60,
			0x42,
			0x00,
			0x00,0x0C,
			0x71, (byte) 0x8D,0x1C, (byte) 0x92,0x14,0x03,
			0x00,0x00,0x07,0x29,
			0x00,0x00,
			0x1D,
			(byte) 0xD0
	};

	@Test
	public void encoderBody() throws Exception {
		List<WirelessMeterLink> links = new ArrayList<>();
		links.add(new WirelessMeterLink("0000729", "0000730"));
		P_45 p_45 = new P_45("340113010001", "192.168.3.100:8888", 1, links);
		System.out.println(HexStringUtils.toHexString(p_45.encoder()));
	}

	@Test
	public void decoderBody() throws Exception {
		P_45 p_45 = new P_45(b);
		System.out.println(p_45);
	}

}