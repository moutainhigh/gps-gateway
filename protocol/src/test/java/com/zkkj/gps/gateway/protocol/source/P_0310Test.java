package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.BaseParameter;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.Param4Int;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.Param4Str;
import com.zkkj.gps.gateway.protocol.util.ClientUtils;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class P_0310Test extends BaseTest {
	@Test
	public void encoderBody() throws Exception {
		List<BaseParameter> list = new ArrayList<>();
		list.add(new Param4Str(0X01, "aaa"));
		list.add(new Param4Int(0X23, 55));
		P_0310 p_0310 = new P_0310("1", 2, list);
		System.out.println(HexStringUtils.toHexString(p_0310.encoder()));
	}
}