package com.zkkj.gps.gateway.tcp.monitor.app.proforward.factory;

import com.zkkj.gps.gateway.protocol.component.common.BaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.component.messagebody.ParamRules;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.BaseParameter;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.Param4Int;
import com.zkkj.gps.gateway.tcp.monitor.app.proforward.build.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 协议组装并做协议下发
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-21 下午 5:21
 */
public class ProBuildFactory {
    /**
     * 组装终端参数设置协议模型
     * @param terminalId
     * @param list
     * @return
     * @throws Exception
     */
    public static BaseCompose producePro0310(String terminalId, List<BaseParameter> list) throws Exception{
        P_0310Build p_0310Set = new P_0310Build(terminalId,list);
        BaseCompose compose = p_0310Set.getP_0310();
        return compose;
    }

    /**
     * 组装获取电子运单前终端参数设置协议模型
     * @param terminalId
     * @return
     * @throws Exception
     */
    public static BaseCompose producePro0310(String terminalId) throws Exception{
        List<BaseParameter> list = new ArrayList<>();
        Param4Int param4Int = new Param4Int(0x23, 6);
        list.add(param4Int);
        P_0310Build p_0310Set = new P_0310Build(terminalId,list);
        BaseCompose compose = p_0310Set.getP_0310();
        return compose;
    }

    /**
     * 组装获取电子车牌前终端参数设置协议模型
     * @param terminalId
     * @param paramId
     * @return
     * @throws Exception
     */
    public static BaseCompose producePlateNumPro0312(String terminalId,int[] paramId) throws Exception{
        P_0312Build p_0312Set = new P_0312Build(terminalId,paramId);
        BaseCompose compose = p_0312Set.getP_0312();
        return compose;
    }

    /**
     * 组装终端参数读取协议模型
     * @param terminalId
     * @param paramId
     * @return
     * @throws Exception
     */
    public static BaseCompose producePro0312(String terminalId,int[] paramId) throws Exception{
        P_0312Build p_0312Set = new P_0312Build(terminalId,paramId);
        BaseCompose compose = p_0312Set.getP_0312();
        return compose;
    }

    /**
     * 组装获取电子运单协议模型
     * @param terminalId
     * @return
     * @throws Exception
     */
    public static BaseCompose producePro0312(String terminalId) throws Exception{
        int paramId[] = {0x23};
        P_0312Build p_0312Set = new P_0312Build(terminalId,paramId);
        BaseCompose compose = p_0312Set.getP_0312();
        return compose;
    }

    /**
     * 批量设置区域及刷卡的使用规则协议构建器
     * @param terminalId
     * @param ruleTotal
     * @param currentRules
     * @param list
     * @return
     * @throws Exception
     */
    public static BaseCompose producePro0314 (String terminalId, int ruleTotal,int currentRules,List<ParamRules> list) throws Exception{
        P_0314Build p_0314Set = new P_0314Build(terminalId,ruleTotal,currentRules,list);
        BaseCompose compose = p_0314Set.getP_0314();
        return compose;
    }

    /**
     * 批量读取区域及刷卡的使用规则协议构建器
     * @param terminalId
     * @param addressNumber
     * @return
     * @throws Exception
     */
    public static BaseCompose producePro0316(String terminalId,int[] addressNumber) throws Exception{
        P_0316Build p_0316Set = new P_0316Build(terminalId, addressNumber);
        BaseCompose compose = p_0316Set.getP_0316();
        return compose;
    }

    /**
     * 读取终端区域及刷卡使用规则检验码及存储数量协议构建器
     * @param terminalId
     * @param paramTotalCrc
     * @param addressNumber
     * @return
     * @throws Exception
     */
    public static BaseCompose producePro0318(String terminalId,int paramTotalCrc,int[] addressNumber) throws Exception{
        P_0318Build p_0318Set = new P_0318Build(terminalId,paramTotalCrc, addressNumber);
        BaseCompose compose = p_0318Set.getP_0318();
        return compose;
    }

    /**
     * 组装下发电子运单协议模型
     * @param terminalId
     * @param businessData
     * @return
     * @throws Exception
     */
    public static BaseCompose producePro0410(String terminalId, BusinessExtensionData businessData) throws Exception{
        P_0410Build p_0410Set = new P_0410Build(terminalId,businessData);
        BaseCompose compose = p_0410Set.getP_0410();
        return compose;
    }

    /**
     * 组装平台统一回复协议模型
     * @param terminalId
     * @param msgID
     * @return
     */
    public static BaseCompose producePro8001(String terminalId, int msgID) throws Exception{
        P_8001Build p_8001Set = new P_8001Build(terminalId,msgID);
        BaseCompose compose = p_8001Set.getP_8001();
        return compose;
    }


}
