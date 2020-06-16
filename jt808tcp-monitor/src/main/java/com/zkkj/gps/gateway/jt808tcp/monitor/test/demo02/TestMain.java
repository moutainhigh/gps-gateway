package com.zkkj.gps.gateway.jt808tcp.monitor.test.demo02;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch.ElecDispatchInfo;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup.F_8182;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.HexStringUtils;

import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        byte[]bytes1 = {-13,63,-1,-1,-127,-125,-59,1,49,53,48,49,50,49,57,49,48,50,51,48,48,48,56,0,0,0,0,0,50,48,49,57,48,55,50,54,0,0,-51,-88,-48,-59,-42,-48,-48,-60,-78,-30,-54,-44,51,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-42,-48,-78,-30,-75,-25,-77,-89,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-42,-48,-78,-30,-77,-48,-44,-53,-55,-52,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-55,-62,68,57,57,57,57,56,0,0,0,0,0,0,0,0,0,0,0,-60,-83,-61,-70,0,0,0,0,0,0,-60,-83,-61,-70,0,0,0,0,0,0,48,46,48,0,0,48,46,48,0,0,48,46,48,0,0,48,46,70,70,70,70,70,70,48,70,70,70,70,70,70,48,70,70,70,70,70,70,-57,-12,79};
        byte[]bytes = {49,53,48,49,50,49,57,49,48,50,51,48,48,48,56,0,0,0,0,0,50,48,49,57,48,55,50,54,0,0,-51,-88,-48,-59,-42,-48,-48,-60,-78,-30,-54,-44,51,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-42,-48,-78,-30,-75,-25,-77,-89,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-42,-48,-78,-30,-77,-48,-44,-53,-55,-52,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-55,-62,68,57,57,57,57,56,0,0,0,0,0,0,0,0,0,0,0,-60,-83,-61,-70,0,0,0,0,0,0,-60,-83,-61,-70,0,0,0,0,0,0,48,46,48,0,0,48,46,48,0,0,48,46,48,0,0,48,46,70,70,70,70,70,70,48,70,70,70,70,70,70,48,70,70,70,70,70,70};
        System.out.println(bytes1.length);
        System.out.println(HexStringUtils.toHexString(bytes1));
        //313530313231393130323330303033000000000032303139303732360000CDA8D0C5D6D0D0C4B2E2CAD4330000000000000000000000000000000000D6D0B2E2B5E7B3A700000000000000000000000000000000000000000000D6D0B2E2B3D0D4CBC9CC0000000000000000000000000000000000000000C9C24439393939380000000000000000000000C4ADC3BA000000000000C4ADC3BA000000000000302E300000302E300000302E300000302E4646464646463046464646464630464646464646
        List<Byte> byteLists = Lists.newArrayList();
        for (byte b : bytes1) {
            byteLists.add(b);
        }
        ElecDispatchInfo elecDispatchInfo = new ElecDispatchInfo(byteLists, 0);
        System.out.println("解析的电子运单数据：【" + elecDispatchInfo.toString() + "】");

        /*List<Byte> byteList = ElecProtocolUtil.gbk2BytesWithFixLength("7", 1);//十六进制   倒数第20位为状态字段
        System.out.println("====");
        for (int i = 0; i < byteList.size(); i++) {
            System.out.print(byteList.get(i) + ",");
        }
        System.out.println("\n====");*/
        /**
         * disPatchNo=150121910230009, taskNumber=20190726, consignerName=通信中心测试3, receiverName=中测电厂, shipperName=中测承运商,
         * plateNumber=陕D99998, driverName=, goodsName=沫煤, kcal=, goodCategory=沫煤, sendTareWeight=0.0, sendGrossWeight=0.0, rcvTareWeight=0.0,
         * rcvGrossWeight=0.0, status=0, deductWeight=0, deductReason=000, lastChangeBy=中测运输
         */
        ElecDispatchInfo elecDispatchInfo1 = new ElecDispatchInfo();
        elecDispatchInfo1.setDisPatchNo("150121910230009");
        elecDispatchInfo1.setTaskNumber("20190726");
        elecDispatchInfo1.setConsignerName("通信中心测试3");
        elecDispatchInfo1.setReceiverName("中测电厂");
        elecDispatchInfo1.setShipperName("中测承运商");
        elecDispatchInfo1.setPlateNumber("陕D99998");
        elecDispatchInfo1.setDriverName("");
        elecDispatchInfo1.setGoodsName("沫煤");
        elecDispatchInfo1.setKcal("");
        elecDispatchInfo1.setGoodCategory("沫煤");
        elecDispatchInfo1.setSendTareWeight(0.0);
        elecDispatchInfo1.setSendGrossWeight(0.0);
        elecDispatchInfo1.setRcvTareWeight(0.0);
        elecDispatchInfo1.setRcvGrossWeight(0.0);
        elecDispatchInfo1.setStatus(Byte.parseByte("0"));
        elecDispatchInfo1.setDeductWeight(0);
        elecDispatchInfo1.setDeductReason("000");
        elecDispatchInfo1.setLastChangeBy("中测运输");
        F_8182 f_8182 = new F_8182(elecDispatchInfo1);
        byte[] f_8182SendBytes = f_8182.getSendBytes();
        byte [] bytes2 = {49,53,48,49,50,49,57,49,48,50,51,48,48,48,57,0,0,0,0,0,50,48,49,57,48,55,50,54,0,0,-51,-88,-48,-59,-42,-48,-48,-60,-78,-30,-54,-44,51,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-42,-48,-78,-30,-75,-25,-77,-89,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-42,-48,-78,-30,-77,-48,-44,-53,-55,-52,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-55,-62,68,57,57,57,57,56,0,0,0,0,0,0,0,0,0,0,0,-60,-83,-61,-70,0,0,0,0,0,0,-60,-83,-61,-70,0,0,0,0,0,0,48,46,48,0,0,48,46,48,0,0,48,46,48,0,0,48,46,48,0,0,0,48,46,48,0,0,48,48,48,-42,-48,-78,-30,-44,-53,-54,-28};
    }
}
