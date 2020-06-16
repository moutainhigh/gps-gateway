package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.component.common.SourceBaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

/**
 * @author chailixing
 * 2019/1/4 14:57
 * 下发业务扩展数据
 */
public final class P_0410 extends SourceBaseCompose {
	public P_0410(String terminalId, int serialNumber, int storageRegion, BusinessExtensionData businessExtensionData) throws Exception {
		super(ProtocolConsts.P_0410, terminalId, serialNumber);
		this.storageRegion = storageRegion;
		this.businessExtensionData = businessExtensionData;
	}

	/**
	 * 业务数据长度 2 (暂固定196+1 即0X00C5)
	 */
	private int businessDataLength;

	/**
	 * 存储区域 1  0X00：A区  0X01：B区
	 */
	private int storageRegion;

	/**
	 * 业务扩展数据 196
	 */
	private BusinessExtensionData businessExtensionData = new BusinessExtensionData();

	@Override
	public String subToString() {
		return "\n业务数据长度:" + businessDataLength +
				"\n存储区域:" + storageRegion +
				"\n业务扩展数据:" + businessExtensionData;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		byte[] encoder = businessExtensionData.encoder();
		// 业务数据长度 = 业务扩展数据 + 存储区域
		businessDataLength = encoder.length + 1;
		return BitOperator.concatAll(
				BitOperator.integerTo2Bytes(businessDataLength),
				BitOperator.integerTo1Bytes(storageRegion),
				encoder
		);
	}

	public int getBusinessDataLength() {
		return businessDataLength;
	}

	public int getStorageRegion() {
		return storageRegion;
	}

	public BusinessExtensionData getBusinessExtensionData() {
		return businessExtensionData;
	}
}