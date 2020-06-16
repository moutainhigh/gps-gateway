package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.component.messagebody.ParamRules;
import com.zkkj.gps.gateway.protocol.util.ClientUtils;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class P_0314Test extends BaseTest {
	@Test
	public void encoderBody() throws Exception {
		List<ParamRules> list = new ArrayList<>();
		ParamRules paramRules = new ParamRules();
		paramRules.setAddressId(30);
		paramRules.setIcCard("12345678");
		paramRules.setLatitude(32.555555F);
		paramRules.setLongitude(32.555555F);
		paramRules.setRadius(20);
		paramRules.setSubLockControl(5);
		list.add(paramRules);
		P_0314 p_0314 = new P_0314("11", 2, 2, 2, list);
		System.out.println(HexStringUtils.toHexString(p_0314.encoder()));
	}
}