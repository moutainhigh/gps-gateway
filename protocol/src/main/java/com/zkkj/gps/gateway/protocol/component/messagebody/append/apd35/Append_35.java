package com.zkkj.gps.gateway.protocol.component.messagebody.append.apd35;

import com.zkkj.gps.gateway.protocol.component.messagebody.append.BaseSiteAppendMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chailixing
 * 2019/3/13 11:59
 * 35 基站信息
 */
public final class Append_35 extends BaseSiteAppendMessage {
	/**
	 * 35:基站信息集合 最多7组
	 */
	private List<Info4BaseStation> info4BaseStationList;

	public Append_35(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	@Override
	protected String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		info4BaseStationList.forEach(
				s -> stringBuilder.append(s.toString()).append(" "));
		return stringBuilder.toString();
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		info4BaseStationList = new ArrayList<>();
		// 截取国家码
		byte[] countryCode = Arrays.copyOfRange(bytes, 0, 2);
		// 截取基站信息(多组)
		byte[] baseInfo = Arrays.copyOfRange(bytes, 2, bytes.length);
		cutData(baseInfo, countryCode);
	}

	private void cutData(byte[] bytes, byte[] countryCode) throws Exception {
		Info4BaseStation info4BaseStation = new Info4BaseStation(countryCode);
		info4BaseStation.decoder(Arrays.copyOfRange(bytes, 0, 7));
		info4BaseStationList.add(info4BaseStation);
		if (bytes.length > 7) {
			byte[] oneData = Arrays.copyOfRange(bytes, 7, bytes.length);
			cutData(oneData, countryCode);
		}
	}

	public List<Info4BaseStation> getInfo4BaseStationList() {
		return info4BaseStationList;
	}

	public Append_35() {
	}

	public void setInfo4BaseStationList(List<Info4BaseStation> info4BaseStationList) {
		this.info4BaseStationList = info4BaseStationList;
	}
}