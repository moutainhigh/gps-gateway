package com.zkkj.gps.gateway.protocol.component.messagebody.parameter;

/**
 * @author chailixing
 * 2019/4/16 10:18
 * 参数工厂
 */
public final class ParamFactory {

	private ParamFactory() {
	}

	private static class Factory {
		private static ParamFactory paramFactory = new ParamFactory();
	}

	public static ParamFactory getInstance() {
		return Factory.paramFactory;
	}

	public BaseParameter getParamBean(ParamTypeEnum e) throws Exception {
		if (e.equals(ParamTypeEnum.STRING)) {
			return getParam4Str();
		} else if (e.equals(ParamTypeEnum.INTEGER)) {
			return getParam4Int();
		} else if (e.equals(ParamTypeEnum.INTEGER_ARRAY)) {
			return getParam4IntArr();
		} else if (e.equals(ParamTypeEnum.BCD8421)) {
			return getParamBcd();
		} else {
			throw new RuntimeException("参数类型错误!");
		}
	}

	private Param4Bcd getParamBcd() throws Exception {
		return new Param4Bcd();
	}

	private Param4Int getParam4Int() throws Exception {
		return new Param4Int();
	}

	private Param4Str getParam4Str() throws Exception {
		return new Param4Str();
	}

	private Param4IntArr getParam4IntArr() throws Exception {
		return new Param4IntArr();
	}
}