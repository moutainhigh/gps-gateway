package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.component.common.SourceBaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.ParamRules;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chailixing
 * 2019/3/5 8:50
 * 批量设置区域及刷卡的使用规则
 */
public final class P_0314 extends SourceBaseCompose {
	public P_0314(String terminalId, int serialNumber, int ruleTotal, int currentRules, List<ParamRules> list) throws Exception {
		super(ProtocolConsts.P_0314, terminalId, serialNumber);
		this.ruleTotal = ruleTotal;
		this.currentRules = currentRules;
		this.paramRules = list;
	}

	/**
	 * 规则总数 2
	 */
	private int ruleTotal;

	/**
	 * 当前规则数量值 2
	 */
	private int currentRules;

	/**
	 * 多项规则参数
	 */
	private List<ParamRules> paramRules = new ArrayList<>();

	@Override
	public String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		paramRules.forEach(x -> stringBuilder.append(x.toString()).append(" "));
		return "\n规则总数:" + ruleTotal +
				"\n当前规则数量:" + currentRules +
				"\n多项规则参数:" + stringBuilder;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		if (paramRules.size() > 0) {
			currentRules = paramRules.size();
			List<byte[]> collect = paramRules.stream()
					.map(ParamRules::encoder)
					.collect(Collectors.toList());
			return BitOperator.concatAll(
					BitOperator.integerTo2Bytes(ruleTotal),
					BitOperator.integerTo2Bytes(currentRules),
					BitOperator.concatAll(collect)
			);
		} else {
			throw new RuntimeException("参数列表为空!(0314)");
		}
	}

	public int getRuleTotal() {
		return ruleTotal;
	}

	public int getCurrentRules() {
		return currentRules;
	}

	public List<ParamRules> getParamRules() {
		return paramRules;
	}
}