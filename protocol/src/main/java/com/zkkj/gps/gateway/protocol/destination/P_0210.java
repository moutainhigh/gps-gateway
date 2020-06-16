package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.BatchSiteMessage;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chailixing
 * 2019/3/4 15:23
 * 批量位置状态信息补报
 */
public final class P_0210 extends DestinationBaseCompose {
	public P_0210(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0210);
	}

	public P_0210() throws Exception {
		super(ProtocolConsts.P_0210);
	}

	/**
	 * 批量信息集合
	 */
	private List<BatchSiteMessage> siteMessageList;

	@Override
	public String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		siteMessageList.forEach(x -> stringBuilder.append(x.toString()).append(" "));
		return stringBuilder.toString();
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		siteMessageList = new ArrayList<>();
		byte[] data = Arrays.copyOfRange(bytes, 12, bytes.length - 1);
		cutData(data);
	}

	private void cutData(byte[] bytes) throws Exception {
		if (bytes.length > 0) {
			int length = BitOperator.oneByteToInteger(bytes[0]);
			byte[] oneData = Arrays.copyOfRange(bytes, 0, length + 1);
			boolean flag = siteMessageList.add(new BatchSiteMessage(oneData));
			byte[] bytes1 = Arrays.copyOfRange(bytes, length + 1, bytes.length);
			cutData(bytes1);
		}
	}

	public List<BatchSiteMessage> getSiteMessageList() {
		return siteMessageList;
	}
}