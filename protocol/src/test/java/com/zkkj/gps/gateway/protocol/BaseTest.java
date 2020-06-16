package com.zkkj.gps.gateway.protocol;

import com.zkkj.gps.gateway.protocol.util.BitOperator;
import com.zkkj.gps.gateway.protocol.util.ClientUtils;

import java.io.InputStream;

public class BaseTest {
	protected byte[] getBytes(int protocolId) {
		InputStream inputStream = this.getClass().getResourceAsStream("/tcp-destination-capture-file-20181026154431.cap");
		byte[] buffer = ClientUtils.getBytes(inputStream);
		return ClientUtils.getProtocol(buffer, BitOperator.integerTo2Bytes(protocolId));
	}
}