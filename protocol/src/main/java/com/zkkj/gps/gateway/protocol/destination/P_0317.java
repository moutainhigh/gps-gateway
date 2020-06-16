package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.ParamRules;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chailixing
 * 2019/3/5 8:50
 * 批量读取区域及刷卡的使用规则应答
 */
public final class P_0317 extends DestinationBaseCompose {
	public P_0317(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0317);
	}

	public P_0317() throws Exception {
		super(ProtocolConsts.P_0317);
	}

	/**
	 * 应答流水号 2
	 */
	private int responseSerialNumber;

	/**
	 * 规则总数 2
	 */
	private int ruleTotal;

	/**
	 * 多项规则参数
	 */
	private List<ParamRules> paramRules;

	@Override
	public String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (ParamRules p : paramRules) {
			stringBuilder.append(p.toString()).append(" ");
		}
		return "\n应答流水号:" + responseSerialNumber +
				"\n规则总数:" + ruleTotal +
				"\n多项规则参数:" + stringBuilder;
	}

	@Override
	protected void decoderBody(byte[] bytes) throws Exception {
		paramRules = new ArrayList<>();
		responseSerialNumber = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 12, 14));
		ruleTotal = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 14, 16));
		// 截取多项规则参数
		byte[] paramRuleBytes = Arrays.copyOfRange(bytes, 16, bytes.length - 1);
		// 18字节为一条规则
		if ((paramRuleBytes.length % 18) == 0) {
			cutData(paramRuleBytes);
		} else {
			throw new RuntimeException("多项规则参数长度异常!(0317)");
		}
	}

	private void cutData(byte[] bytes) throws Exception {
		byte[] ParamRules = Arrays.copyOfRange(bytes, 0, 18);
		paramRules.add(new ParamRules(ParamRules));
		byte[] next = Arrays.copyOfRange(bytes, 18, bytes.length);
		if (next.length > 0) {
			cutData(next);
		}
	}

	public int getResponseSerialNumber() {
		return responseSerialNumber;
	}

	public int getRuleTotal() {
		return ruleTotal;
	}

	public List<ParamRules> getParamRules() {
		return paramRules;
	}
}