package com.zkkj.gps.gateway.watermeter.component;

import com.zkkj.gps.gateway.watermeter.util.BCD8421Operator;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

/**
 * @author chailixing
 * 2019/4/26 10:43
 * 无线水表链路组件
 */
public final class WirelessMeterLink implements ProtocolSerializable {

	public WirelessMeterLink(String meterAddress, String relayLink) throws Exception {
		this.meterAddress = meterAddress;
		this.relayLink = relayLink;
	}

	/**
	 * 中继链路 4
	 */
	private String relayLink;

	/**
	 * 水表地址 4
	 */
	private String meterAddress;

	@Override
	public byte[] encoder() throws Exception {
		return BitOperator.concatAll(
				BCD8421Operator.string2Bcd(meterAddress),
				BCD8421Operator.string2Bcd(relayLink)
		);
	}

	public String getRelayLink() {
		return relayLink;
	}

	public String getMeterAddress() {
		return meterAddress;
	}
}