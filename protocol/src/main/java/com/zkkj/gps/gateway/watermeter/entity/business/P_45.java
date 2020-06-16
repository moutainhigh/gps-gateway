package com.zkkj.gps.gateway.watermeter.entity.business;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.component.WirelessMeterLink;
import com.zkkj.gps.gateway.watermeter.constant.ControlCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chailixing
 * 2019/4/18 12:09
 * 写水表无线链路
 */
public final class P_45 extends BaseCompose {
	public P_45(String address, String ipAddr, int pageNum, List<WirelessMeterLink> wirelessMeterLinks) {
		super(TypeConsts.TYPE_31, address, FuncCodeConsts.P_45, ControlCodeConsts.SUCCESS);
		this.ipAddr = ipAddr;
		this.pageNum = pageNum;
		this.wirelessMeterLinks = wirelessMeterLinks;
	}

	public P_45(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_45, bytes);
	}

	/**
	 * pc终端地址 6
	 */
	private String ipAddr;

	/**
	 * 页号 1
	 */
	private int pageNum;

	/**
	 * 水表无线链路 8:如果不超过50,页号为1,超过50,页号依次递增
	 */
	private List<WirelessMeterLink> wirelessMeterLinks;

	@Override
	protected String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		wirelessMeterLinks.forEach(x -> stringBuilder.append(x.toString()));
		return "\npc终端地址:" + ipAddr +
				"\n页号:" + pageNum +
				"\n水表无线链路:" + stringBuilder.toString();
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		if (wirelessMeterLinks != null && wirelessMeterLinks.size() > 0) {
			List<byte[]> list = new ArrayList<>();
			for (WirelessMeterLink w : wirelessMeterLinks) {
				list.add(w.encoder());
			}
			return BitOperator.concatAll(
					BitOperator.encoderIpAddr(ipAddr),
					BitOperator.integerTo1Bytes(pageNum),
					BitOperator.concatAll(list)
			);
		} else {
			throw new RuntimeException("写水表无线链路下发为空!(P_45)");
		}
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		wirelessMeterLinks = new ArrayList<>();
		ipAddr = BitOperator.decoderIpAddr(Arrays.copyOfRange(bytes, 0, 6));
		pageNum = BitOperator.oneByteToInteger(bytes[6]);
	}
}