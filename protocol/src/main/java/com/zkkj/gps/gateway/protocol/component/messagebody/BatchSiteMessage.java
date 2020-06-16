package com.zkkj.gps.gateway.protocol.component.messagebody;

import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.SiteAppendMessage;
import com.zkkj.gps.gateway.protocol.component.messagebody.basic.SiteBasicMessage;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/14 16:29
 * 批量位置信息消息体
 */
public final class BatchSiteMessage implements ProtocolSerializable {
	public BatchSiteMessage(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	public BatchSiteMessage() {
	}

	/**
	 * 数据长度
	 */
	private int dataLength;

	/**
	 * 位置基本信息
	 */
	private SiteBasicMessage siteBasicMessage;

	/**
	 * 位置追加信息
	 */
	private SiteAppendMessage siteAppendMessage;

	@Override
	public String toString() {
		return "\n数据长度:" + dataLength +
				"\n位置基本信息:" + siteBasicMessage +
				"\n位置追加信息:" + siteAppendMessage;
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		dataLength = BitOperator.oneByteToInteger(bytes[0]);
		byte[] basic = Arrays.copyOfRange(bytes, 1, 29);
		siteBasicMessage = new SiteBasicMessage(basic);
		byte[] append = Arrays.copyOfRange(bytes, 29, bytes.length);
		siteAppendMessage = new SiteAppendMessage(append);
	}

	public int getDataLength() {
		return dataLength;
	}

	public SiteBasicMessage getSiteBasicMessage() {
		return siteBasicMessage;
	}

	public SiteAppendMessage getSiteAppendMessage() {
		return siteAppendMessage;
	}
}