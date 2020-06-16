package com.zkkj.gps.gateway.protocol.component.messagebody.append.apd02;

import com.zkkj.gps.gateway.protocol.component.messagebody.append.BaseSiteAppendMessage;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/13 11:52
 * 02 油量
 */
public final class Append_02 extends BaseSiteAppendMessage {
	/**
	 * 油量 2 1-10L
	 */
	private int oilMass;

	public Append_02(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	@Override
	protected String subToString() {
		return " 油量:" + oilMass;
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		oilMass = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 0, bytes.length));
	}

	public int getOilMass() {
		return oilMass;
	}

	public Append_02(int oilMass) {
		this.oilMass = oilMass;
	}

	public Append_02() {
	}
}