package com.zkkj.gps.gateway.tcp.monitor.ApiTest;

import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.dto.base.ResultVo;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.BusinessBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.TerminalParams;
import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-10 下午 1:42
 */
@Slf4j
public class TerminalApiTest extends BaseTests{

    /**
     * 测试
     */
    @Test
    public void testHello(){
        String arg = "你好！";
        Map<String, String> params = new HashMap<>();
        params.put("arg", arg);
        ResultVo<String> resultVo = postTest("gps/hello", params);
        Assert.assertTrue(resultVo.isSuccess());
        Assert.assertEquals(resultVo.getData(),"Hello World!" + arg);
    }

    /**
     * 设置终端参数测试
     */
    @Test
    public void setTerminalArgs() {
        String terminalID = "095039621773";
        Map<String, String> address = new HashMap<>();
        address.put("terminalId", terminalID);
        TerminalParams terminalParams = new TerminalParams();
        terminalParams.setTerminalId(terminalID);
        terminalParams.setMcuTime(0);
        terminalParams.setMainIp("39.105.207.6");
        terminalParams.setBackupIp("192.168.3.86");
        terminalParams.setPort("1001");
        terminalParams.setReportTime(30);
        terminalParams.setLocationMode(3);
        terminalParams.setPalmusTime(195);
        terminalParams.setApnName("CMMTM");
        terminalParams.setApnUserName("guse");
        terminalParams.setApnPassword("guse");
        terminalParams.setApnName2("CMMTM");
        terminalParams.setApnUserName2("guse");
        terminalParams.setApnPassword2("guse");
        terminalParams.setSimMode(2);
        terminalParams.setNetMode(0);
        terminalParams.setWarnVoltage(370);
        terminalParams.setIcPassword("665512668834");
        terminalParams.setSmsPassword("66556688");
        terminalParams.setShakeTime(180);
        terminalParams.setCurrentTime("20190515165804");
        terminalParams.setSelfLock(2);
        terminalParams.setTerminalAction(0);
        terminalParams.setIsUnlocking(0);
        terminalParams.setEmergencyUnlock("12341234");
        terminalParams.setTempIcCardId1("");
        terminalParams.setTempIcCardId2("");
        terminalParams.setTempIcCardId3("");
        terminalParams.setTempIcCardId4("44556677");
        terminalParams.setBluetoothName("陕KD7765");
        terminalParams.setBluetoothPassword("00000000");
        ResultVo<String> resultVo = postTestBody("gps/setTerminalArgs", address, terminalParams);
        LoggerUtils.info(log, terminalID, resultVo.toString());
    }

    /**
     * 读取终端参数测试
     */
    @Test
    public void readTerminalArgs() {
        String terminalID = "095039621773";
        Map<String, String> params = new HashMap<>();
        params.put("terminalId", terminalID);
        ResultVo<TerminalParams> resultVo = postTest("gps/readTerminalArgs", params);
        LoggerUtils.info(log,terminalID,resultVo.toString());
    }

    /**
     * 读电子运单测试
     */
    @Test
    public void readBusiness() {
        String terminalID = "095039621773";
        Map<String, Object> params = new HashMap<>();
        params.put("terminalType", 1);
        params.put("terminalId", terminalID);
        ResultVo<BusinessBean> resultVo = postTest("gps/readBusiness", params);
        LoggerUtils.info(log,terminalID,resultVo.toString());
    }

    /**
     * 下发电子运单测试
     */
    @Test
    public void setBusinessData() {
        String terminalID = "095039621773";
        Map<String, Object> address = new HashMap<>();
        address.put("terminalType", 1);
        address.put("terminalId", terminalID);
        BusinessExtensionData businessData = new BusinessExtensionData();
        businessData.setDisPatchNo("N44444");
        businessData.setTaskNumber("123456");
        businessData.setConsignerName("柴家沟煤矿");
        businessData.setReceiverName("铜川照金煤电");
        businessData.setShipperName("藤原拓海");
        businessData.setPlateNumber("陕M00000");
        businessData.setDriverName("罗曼-格罗斯让");
        businessData.setGoodsName("香蕉");
        businessData.setGoodCategory("水果");
        businessData.setSendTareWeight("550");
        businessData.setSendGrossWeight("100");
        businessData.setRcvTareWeight("540");
        businessData.setRcvGrossWeight("440");
        businessData.setStatus("1");
        businessData.setLastChangeBy("王五");
        businessData.setDeductWeight("111");
        businessData.setDeductReason("0.3");
        businessData.setKcal("444");
        ResultVo<String> resultVo = postTestBody("gps/setBusiness", address,businessData);
        LoggerUtils.info(log,terminalID,resultVo.toString());
    }

}
