package com.zkkj.gps.gateway.tcp.monitor.app.api;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 5:31
 */

import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.component.messagebody.ParamRules;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.BaseParameter;
import com.zkkj.gps.gateway.protocol.source.*;
import com.zkkj.gps.gateway.tcp.monitor.app.proforward.factory.ProBuildFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 终端通信，对外Api接口,模型组装并做指令下发
 */
@Component
public class ElecProtocolApi {

    @Autowired
    private ElecProtocolPort elecProtocolPort;

    /**
     * 终端参数设置
     * @param terminalId
     * @return
     */
    public boolean setTerminalArgs(String terminalId,List<BaseParameter> list) throws Exception{
        P_0310 p_0310 = (P_0310) ProBuildFactory.producePro0310(terminalId, list);
        return elecProtocolPort.sendCommandImpl(p_0310);
    }

    /**
     * 获取电子运单
     */
    public boolean readTerminalArgs(String terminalId) throws Exception{
        P_0310 p_0310 = (P_0310) ProBuildFactory.producePro0310(terminalId);
        return elecProtocolPort.sendCommandImpl(p_0310);
    }

    /**
     * 终端参数读取
     */
    public boolean readTerminalArgs(String terminalId,int[] paramId) throws Exception{
        P_0312 p_0312 = (P_0312) ProBuildFactory.producePro0312(terminalId,paramId);
        return elecProtocolPort.sendCommandImpl(p_0312);
    }

    /**
     * 读取蓝牙电子车牌
     */
    public boolean readPlateNum(String terminalId,int[]paramId) throws Exception{
        P_0312 p_0312 = (P_0312) ProBuildFactory.producePlateNumPro0312(terminalId,paramId);
        return elecProtocolPort.sendCommandImpl(p_0312);
    }

    /**
     * 批量设置区域及刷卡的使用规则
     * @param terminalId
     * @return
     */
    public void setAreaBatch(String terminalId, int ruleTotal,int currentRules,List<ParamRules> list) throws Exception{
        P_0314 p_0314 = (P_0314) ProBuildFactory.producePro0314(terminalId,ruleTotal,currentRules,list);
        elecProtocolPort.sendCommandImpl(p_0314);
    }

    /**
     * 批量读取区域及刷卡的使用规则
     * @param terminalId
     * @return
     */
    public void readAreaBatch(String terminalId,int[] addressNumber) throws Exception{
        P_0316 p_0316 = (P_0316) ProBuildFactory.producePro0316(terminalId,addressNumber);
        elecProtocolPort.sendCommandImpl(p_0316);
    }

    /**
     * 读取终端区域及刷卡使用规则检验码及存储数量
     * @param terminalId
     * @return
     */
    public void readAreaCrc(String terminalId,int paramTotalCrc,int[] addressNumber) throws Exception{
        P_0318 p_0318 = (P_0318) ProBuildFactory.producePro0318(terminalId,paramTotalCrc,addressNumber);
        elecProtocolPort.sendCommandImpl(p_0318);
    }

    /**
     * 下发业务扩展数据
     * @param terminalId
     * @return
     */
    public boolean setBusinessData(String terminalId,BusinessExtensionData businessData) throws Exception{
        P_0410 P_0410 = (P_0410) ProBuildFactory.producePro0410(terminalId,businessData);
        return elecProtocolPort.sendCommandImpl(P_0410);
    }

    /**
     * 平台统一应答
     * @param terminalId  终端号
     * @param messageID
     * @return
     */
    public void platformResponse(String terminalId,int messageID) throws Exception{
        P_8001 p_8001 = (P_8001) ProBuildFactory.producePro8001(terminalId,messageID);
        elecProtocolPort.sendCommandImpl(p_8001);
    }

}
