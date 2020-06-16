package com.zkkj.gps.gateway.protocol.destination;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.*;
import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.*;

/**
 * @author chailixing
 * 2019/1/15 11:12
 * 终端参数查询应答
 */
public final class P_0313 extends DestinationBaseCompose {
	public P_0313(byte[] bytes) throws Exception {
		super(bytes, ProtocolConsts.P_0313);
	}

	public P_0313() throws Exception {
		super(ProtocolConsts.P_0313);
	}

	/**
	 * 应答流水号 2
	 */
	private int responseSerialNumber;

	/**
	 * 参数总数 1
	 */
	private int paramTotal;

	/**
	 * 参数项列表 n字节
	 */
	private List<BaseParameter> parameters;

	@Override
	public String subToString() {
		StringBuilder stringBuilder = new StringBuilder();
		parameters.forEach(x -> stringBuilder.append("\n").append(x.toString()));
		return "\n应答流水号:" + responseSerialNumber +
				"\n总数(忽略 id33, id37):" + paramTotal +
				"\n参数项列表:" + stringBuilder;
	}

	@Override
	public void decoderBody(byte[] bytesBody) throws Exception {
		parameters = new ArrayList<>();
		responseSerialNumber = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytesBody, 12, 14));
		paramTotal = BitOperator.oneByteToInteger(bytesBody[14]);
		// 截取参数列表(变长)
		int crcIndex = bytesBody.length - 1;
		// 处理参数项列表
		byte[] paramList = Arrays.copyOfRange(bytesBody, 15, crcIndex);
		cutParam(paramList);
	}

	private void cutParam(byte[] paramList) throws Exception {
		if (paramList.length > 0) {
			if (paramList[0] != 0) {
				int paramLength = BitOperator.oneByteToInteger(paramList[1]);
				int paramTotalLength = paramLength + 2;
				byte[] paramBytes = Arrays.copyOfRange(paramList, 0, paramTotalLength);
				int paramId = BitOperator.oneByteToInteger(paramList[0]);
				if (paramIdMap.containsKey(paramId)) {
					BaseParameter paramBean = getParamBean(paramId);
					paramBean.decoder(paramBytes);
					parameters.add(paramBean);
				}
				byte[] bytes = Arrays.copyOfRange(paramList, paramLength + 2, paramList.length);
				cutParam(bytes);
			} else {
				byte[] emptyData = Arrays.copyOfRange(paramList, 0, 2);
				parameters.add(new Param4Str(emptyData));
				byte[] nextData = Arrays.copyOfRange(paramList, 2, paramList.length);
				cutParam(nextData);
			}
		}
	}

	private BaseParameter getParamBean(int paramId) throws Exception {
		ParamTypeEnum paramEnum = paramIdMap.get(paramId);
		Optional<ParamTypeEnum> anEnum = Optional.of(paramEnum);
		return ParamFactory.getInstance().getParamBean(anEnum.get());
	}

	// 参数id与参数类型对应
	private final static Map<Integer, ParamTypeEnum> paramIdMap = new HashMap<>();

	static {
		paramIdMap.put(ParamIdConsts.PARAM_01, ParamTypeEnum.BCD8421);
		paramIdMap.put(ParamIdConsts.PARAM_02, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_03, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_04, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_05, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_06, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_07, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_08, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_09, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_0A, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_0B, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_0C, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_0D, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_0E, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_0F, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_10, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_11, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_12, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_13, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_14, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_15, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_16, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_17, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_18, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_19, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_1A, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_1B, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_1C, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_1D, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_1E, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_1F, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_20, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_21, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_22, ParamTypeEnum.STRING);
		paramIdMap.put(ParamIdConsts.PARAM_23, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_24, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_25, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_26, ParamTypeEnum.INTEGER_ARRAY);
		paramIdMap.put(ParamIdConsts.PARAM_27, ParamTypeEnum.INTEGER);
		paramIdMap.put(ParamIdConsts.PARAM_28, ParamTypeEnum.INTEGER_ARRAY);
		paramIdMap.put(ParamIdConsts.PARAM_29, ParamTypeEnum.INTEGER_ARRAY);
		paramIdMap.put(ParamIdConsts.PARAM_2A, ParamTypeEnum.INTEGER_ARRAY);
		paramIdMap.put(ParamIdConsts.PARAM_2B, ParamTypeEnum.INTEGER_ARRAY);
		paramIdMap.put(ParamIdConsts.PARAM_35, ParamTypeEnum.INTEGER_ARRAY);
		paramIdMap.put(ParamIdConsts.PARAM_36, ParamTypeEnum.INTEGER_ARRAY);
	}

	public int getResponseSerialNumber() {
		return responseSerialNumber;
	}

	public int getParamTotal() {
		return paramTotal;
	}

	public List<BaseParameter> getParameters() {
		return parameters;
	}
}
