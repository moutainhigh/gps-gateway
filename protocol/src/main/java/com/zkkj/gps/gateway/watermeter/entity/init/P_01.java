package com.zkkj.gps.gateway.watermeter.entity.init;

import com.zkkj.gps.gateway.watermeter.component.BaseCompose;
import com.zkkj.gps.gateway.watermeter.constant.FuncCodeConsts;
import com.zkkj.gps.gateway.watermeter.constant.TypeConsts;
import com.zkkj.gps.gateway.watermeter.util.BitOperator;

/**
 * @author chailixing
 * 2019/4/18 10:25
 * 终端初始化
 */
public final class P_01 extends BaseCompose {
	public P_01(String address, int control) {
		super(TypeConsts.TYPE_22, address, FuncCodeConsts.P_01, control);
	}

	public P_01(byte[] bytes) throws Exception {
		super(FuncCodeConsts.P_01, bytes);
	}

	/**
	 * 上传: 信号强度 1
	 * 下发: 空
	 */
	private int signalIntensity;

	@Override
	protected String subToString() {
		return "\n信号强度:" + signalIntensity;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		// 初始化下发数据域为空
		return new byte[0];
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		signalIntensity = BitOperator.byteToInteger(bytes);
	}

	public int getData() {
		return signalIntensity;
	}
}