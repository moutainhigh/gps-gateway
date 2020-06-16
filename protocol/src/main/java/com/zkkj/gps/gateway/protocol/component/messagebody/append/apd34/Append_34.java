package com.zkkj.gps.gateway.protocol.component.messagebody.append.apd34;

import com.zkkj.gps.gateway.protocol.component.messagebody.append.BaseSiteAppendMessage;
import com.zkkj.gps.gateway.protocol.util.BCD8421Operator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/13 11:56
 * 34 子锁操作
 */
public final class Append_34 extends BaseSiteAppendMessage {
	/**
	 * 34:子锁信息 12
	 */
	private String subLocks;

	public Append_34(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	@Override
	protected String subToString() {
		return " 子锁信息:" + subLocks;
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		subLocks = BCD8421Operator.bcd2ToString(Arrays.copyOfRange(bytes, 0, bytes.length));
	}

	public String getSubLocks() {
		return subLocks;
	}

	public Append_34() {
	}

	public void setSubLocks(String subLocks) {
		this.subLocks = subLocks;
	}
}