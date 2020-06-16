package com.zkkj.gps.gateway.protocol.component.messagebody.append.apd36;


import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.BaseSiteAppendMessage;

/**
 * @author chailixing
 * 2019/3/13 12:00
 * 36 存储区域的业务数据内容
 */
public final class Append_36 extends BaseSiteAppendMessage {
	/**
	 * 36:业务扩展数据内容 196
	 */
	private BusinessExtensionData businessExtensionData;

	public Append_36(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	@Override
	protected String subToString() {
		return " 业务扩展数据内容:" + businessExtensionData;
	}

	@Override
	protected void subDecoder(byte[] bytes) throws Exception {
		businessExtensionData = new BusinessExtensionData(bytes);
	}

	public BusinessExtensionData getBusinessExtensionData() {
		return businessExtensionData;
	}

	public Append_36() {
	}

	public void setBusinessExtensionData(BusinessExtensionData businessExtensionData) {
		this.businessExtensionData = businessExtensionData;
	}
}