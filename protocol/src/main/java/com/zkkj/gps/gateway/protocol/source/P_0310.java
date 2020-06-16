package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.component.common.SourceBaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.BaseParameter;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.ParamIdConsts;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chailixing
 * 2019/1/4 13:24
 * 终端参数设置
 */
public final class P_0310 extends SourceBaseCompose {
	public P_0310(String terminalId, int serialNumber, List<BaseParameter> list) throws Exception {
		super(ProtocolConsts.P_0310, terminalId, serialNumber);
		this.parameters = list;
		this.paramTotal = parameters.size();
		this.setParamLength(parameters);
	}

	private static final Map<Integer, Integer> paramLengthMap = new HashMap<>();

	// 存放构造器中无法确认的paramLength
	// paramId -> paramLength
	static {
		paramLengthMap.put(ParamIdConsts.PARAM_01, 6);
		paramLengthMap.put(ParamIdConsts.PARAM_02, 2);
		paramLengthMap.put(ParamIdConsts.PARAM_06, 2);
		paramLengthMap.put(ParamIdConsts.PARAM_07, 1);
		paramLengthMap.put(ParamIdConsts.PARAM_08, 2);
		paramLengthMap.put(ParamIdConsts.PARAM_0F, 1);
		paramLengthMap.put(ParamIdConsts.PARAM_10, 1);
		paramLengthMap.put(ParamIdConsts.PARAM_11, 2);
		paramLengthMap.put(ParamIdConsts.PARAM_14, 2);
		paramLengthMap.put(ParamIdConsts.PARAM_16, 1);
		paramLengthMap.put(ParamIdConsts.PARAM_23, 1);
		paramLengthMap.put(ParamIdConsts.PARAM_24, 1);
		paramLengthMap.put(ParamIdConsts.PARAM_25, 2);
		paramLengthMap.put(ParamIdConsts.PARAM_27, 2);

		paramLengthMap.put(ParamIdConsts.PARAM_28, 9);
		paramLengthMap.put(ParamIdConsts.PARAM_29, 9);
		paramLengthMap.put(ParamIdConsts.PARAM_2A, 9);
		paramLengthMap.put(ParamIdConsts.PARAM_2B, 9);
		paramLengthMap.put(ParamIdConsts.PARAM_35, 9);
		paramLengthMap.put(ParamIdConsts.PARAM_36, 8);
	}

	private void setParamLength(List<BaseParameter> list) {
		list.stream()
				.filter(x -> paramLengthMap.containsKey(x.getParamId()))
				.forEach(x -> x.setParamLength(paramLengthMap.get(x.getParamId())));
	}

	/**
	 * 参数总数 1
	 */
	private int paramTotal;

	/**
	 * 参数项列表
	 */
	private List<BaseParameter> parameters = new ArrayList<>();

	@Override
	public String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		parameters.forEach(x -> stringBuilder.append(x.toString()).append(" "));
		return "\n参数总数:" + paramTotal +
				"\n参数项:" + stringBuilder;
	}

	@Override
	protected byte[] encoderBody() throws Exception {
		if (parameters.size() > 0) {
			List<byte[]> collect = parameters.stream().map(BaseParameter::encoder).collect(Collectors.toList());
			return BitOperator.concatAll(
					BitOperator.integerTo1Bytes(paramTotal),
					BitOperator.concatAll(collect)
			);
		} else {
			throw new RuntimeException("参数列表为空!(0310)");
		}
	}

	public int getParamTotal() {
		return paramTotal;
	}

	public List<BaseParameter> getParameters() {
		return parameters;
	}
}