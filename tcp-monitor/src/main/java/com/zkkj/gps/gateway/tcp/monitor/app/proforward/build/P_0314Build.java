package com.zkkj.gps.gateway.tcp.monitor.app.proforward.build;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 4:42
 */

import com.zkkj.gps.gateway.protocol.component.messagebody.ParamRules;
import com.zkkj.gps.gateway.protocol.source.P_0314;

import java.util.List;

/**
 * 批量设置区域及刷卡的使用规则协议构建器
 */
public class P_0314Build extends ProBuildBase {

    /**
     * 规则总数 2
     */
    private int ruleTotal;

    /**
     * 当前规则数量值 2
     */
    private int currentRules;
    /**
     * 参数列表
     */
    private List<ParamRules> paramRulesList;
    /**
     * 获取协议参数
     * @param terminalId
     */
    public P_0314Build(String terminalId,int ruleTotal,int currentRules,List<ParamRules> list) {
        super(terminalId);
        this.ruleTotal = ruleTotal;
        this.currentRules = currentRules;
        this.paramRulesList = list;
    }

    /**
     * 组装P_0314对象
     * @return
     */
    public P_0314 getP_0314() throws Exception{
        //int ruleTotal = 2;
        //int currentRules = 2;
        /*List<ParamRules> list = new ArrayList<>();
        ParamRules paramRules = new ParamRules();
        paramRules.setAddressId(0);
        paramRules.setIcCard("12345678");
        paramRules.setLatitude(34.198904f);
        paramRules.setLongitude(108.892577f);
        paramRules.setRadius(100);
        paramRules.setSubLockControl(0);
        ParamRules paramRules1 = new ParamRules();
        paramRules1.setAddressId(0);
        paramRules1.setIcCard("12345678");
        paramRules1.setLatitude(34.198904f);
        paramRules1.setLongitude(108.892577f);
        paramRules1.setRadius(100);
        paramRules1.setSubLockControl(1);
        list.add(paramRules);
        list.add(paramRules1);*/
        P_0314 p_0314 = new P_0314(getTerminalId(), getReqSerialnumber(), ruleTotal, currentRules, paramRulesList);
        return p_0314;
    }

}
