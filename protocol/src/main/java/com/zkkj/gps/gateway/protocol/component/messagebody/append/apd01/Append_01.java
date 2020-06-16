package com.zkkj.gps.gateway.protocol.component.messagebody.append.apd01;

import com.zkkj.gps.gateway.protocol.component.messagebody.append.BaseSiteAppendMessage;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/13 11:39
 * 01 里程
 */
public final class Append_01 extends BaseSiteAppendMessage {
	/**
	 * 里程数 4  1-10km
	 */
	private long mileage;

	public Append_01(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	@Override
	protected String subToString() {
		return " 里程数:" + mileage;
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		mileage = BitOperator.fourBytesToLong(Arrays.copyOfRange(bytes, 0, bytes.length));
	}

	public long getMileage() {
		return mileage;
	}

	public Append_01() {
	}

	public void setMileage(long mileage) {
		this.mileage = mileage;
	}
}