package com.zkkj.gps.gateway.protocol.source;

import com.zkkj.gps.gateway.protocol.BaseTest;
import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;
import org.junit.Test;

public class P_0410Test extends BaseTest {
	@Test
	public void encoderBody() throws Exception {
		BusinessExtensionData business = new BusinessExtensionData();
		business.setDisPatchNo("N123456");
		business.setTaskNumber("T2222222");
		business.setConsignerName("西川矿");
		business.setReceiverName("铜川华能");
		business.setShipperName("三原盛天");
		business.setPlateNumber("陕M12345");
		business.setDriverName("张三");
		business.setGoodCategory("水果");
		business.setGoodsName("苹果");
		business.setSendTareWeight("12.23");
		business.setSendGrossWeight("12.12");
		business.setRcvTareWeight("12.02");
		business.setRcvGrossWeight("12.01");
		business.setStatus("3");
		business.setLastChangeBy("张三");
		business.setDeductReason("111");
		business.setDeductWeight("0.2");
		P_0410 p_0410 = new P_0410("2", 2, 0, business);
		System.out.println(HexStringUtils.toHexString(p_0410.encoder()));
		// 错误的示范:
		// 派单编号 0x4e, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36,
		// 任务编号 0x54, 0x32, 0x32, 0x32, 0x32, 0x32, 0x32, 0x32,
		// 发货单位 CE F7 B4 A8 BF F3
		// 收货单位 CD AD B4 A8 BB AA C4 DC
		// 运输单位 C8 FD D4 AD CA A2 CC EC
		// 车牌 C9 C2 4D 31 32 33 34 35
		// 司机 D5 C5 C8 FD
		// 获取 CB AE B9 FB
		// 种类 C6 BB B9 FB
		// 发货皮重 0x31, 0x32, 0x2e, 0x32, 0x33,
		// 发货毛重 0x31, 0x32, 0x2e, 0x31, 0x32,
		// 收货皮重 0x31, 0x32, 0x2e, 0x30, 0x32,
		// 收货毛重 0x31, 0x32, 0x2e, 0x30, 0x31,
		// 任务状态字 0x33,
		// 发货数据来源 0x31,
		// 收货数据来源 0x33,

		// 正确的示范:
		// 长度 C5
		// 存储区域 00
		// 派单编号 00 00 00 00 00 00 00 00 00 00 00 00 00 4E 31 32 33 34 35 36
		// 任务编号 00 00 54 32 32 32 32 32 32 32
		// 发货单位 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 CE F7 B4 A8 BF F3
		// 收货单位 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 CD AD B4 A8 BB AA C4 DC
		// 运输单位 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C8 FD D4 AD CA A2 CC EC
		// 车牌 00 C9 C2 4D 31 32 33 34 35
		// 司机 00 00 00 00 00 00 D5 C5 C8 FD
		// 运输货物 00 00 00 00 00 00 CB AE B9 FB
		// 品类 00 00 00 00 00 00 C6 BB B9 FB
		// 发货皮重 31 32 2E 32 33
		// 发货毛重 31 32 2E 31 32
		// 收货皮重 31 32 2E 30 32
		// 收货毛重 31 32 2E 30 31
		// 任务状态字 33
		// 发货数据来源 00 00 00 00 00 00 00 31
		// 收货数据来源 00 00 00 00 00 00 00 33
	}
}