package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.SiteAppendMessage;
import com.zkkj.gps.gateway.protocol.component.messagebody.basic.SiteBasicMessage;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;

import java.util.Arrays;

/**
 * @author chailixing
 * 2018/12/26 9:33
 * 实时位置状态信息汇报
 */
public final class P_0200 extends DestinationBaseCompose {
	public P_0200(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0200);
	}

	public P_0200() throws Exception {
		super(ProtocolConsts.P_0200);
	}

	/**
	 * 位置基本信息
	 */
	private SiteBasicMessage siteBasicMessage;

	/**
	 * 位置追加信息
	 */
	private SiteAppendMessage siteAppendMessage;

	@Override
	public String subToString() {
		return "\n位置基本信息:" + siteBasicMessage +
				"\n位置追加信息:" + siteAppendMessage;
	}

	@Override
	protected void decoderBody(byte[] bytesBody) throws Exception {
		byte[] basic = Arrays.copyOfRange(bytesBody, 12, 40);
		siteBasicMessage = new SiteBasicMessage(basic);
		byte[] append = Arrays.copyOfRange(bytesBody, 40, bytesBody.length - 1);
		siteAppendMessage = new SiteAppendMessage(append);
	}

	public SiteBasicMessage getSiteBasicMessage() {
		return siteBasicMessage;
	}

	public SiteAppendMessage getSiteAppendMessage() {
		return siteAppendMessage;
	}
}