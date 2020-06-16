package com.zkkj.gps.gateway.tcp.monitor.app.api;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.component.common.MessageHeader;
import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.component.messagebody.ParamRules;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.BaseParameter;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.BusinessBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.SyncBean;

import java.util.List;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 5:30
 */

/**
 * 外部调用接口
 */
public interface IViewAction {

    /**
     * 终端参数设置
     * @param terminalId
     */
    void setTerminalArgs(String terminalId, List<BaseParameter> list) throws  Exception ;

    /**
     * 终端参数设置
     * @param terminalId
     */
    SyncBean setTerminalArgs(String terminalId, List<BaseParameter> list, SyncBean syncBean) throws  Exception ;

    /**
     * 获取终端参数
     * @param terminalId
     * @return
     */
    void readTerminalArgs(String terminalId, int[] paramId) throws Exception ;

    /**
     * 获取终端参数
     * @param terminalId
     * @param paramId
     * @param syncBean
     * @throws Exception
     */
    SyncBean readTerminalArgs(String terminalId, int[] paramId, SyncBean syncBean) throws Exception ;

    /**
     * 读取电子运单
     * @param terminalId
     * @return
     */
    void readBusiness(String terminalId) throws Exception ;

    /**
     * 同步读取电子运单
     * @param terminalId
     * @param syncBean
     * @return
     * @throws Exception
     */
    SyncBean<BusinessBean> readBusiness(String terminalId, SyncBean syncBean) throws Exception ;

    /**
     * 读取电子车牌
     */
    SyncBean<String> readPlateNum(String terminalId, int[] paramId, SyncBean syncBean) throws Exception ;

    /**
     * 批量设置区域及刷卡的使用规则
     * @param terminalId
     */
    void setAreaBatch(String terminalId, int ruleTotal, int currentRules, List<ParamRules> list) throws Exception ;

    /**
     * 批量读取区域及刷卡的使用规则
     * @param terminalId
     */
    void readAreaBatch(String terminalId, int[] addressNumber) throws Exception ;

    /**
     * 读取终端区域及刷卡使用规则检验码及存储数量
     * @param terminalId
     */
    void readAreaCrc(String terminalId, int paramTotalCrc, int[] addressNumber) throws Exception ;

    /**
     * 下发业务扩展数据
     * @param terminalId
     */
    void setBusinessData(String terminalId, BusinessExtensionData businessData) throws Exception ;

    /**
     * 下发业务扩展数据
     * @param terminalId
     * @param businessData
     * @param syncBean
     * @throws Exception
     */
    SyncBean<String> setBusinessData(String terminalId, BusinessBean businessData, SyncBean syncBean) throws Exception ;

    /**
     * 平台统一响应8001
     * @param terminalId
     * @param messageID
     */
    void platformResponse(String terminalId, int messageID) throws Exception ;

    /**
     * 终端响应回调
     * @param compose
     * @param msgHeader
     */
    void responseCallBack(DestinationBaseCompose compose, MessageHeader msgHeader);

    /**
     * 蓝牙电子锁解封施封
     * @param terminalId
     * @param list
     * @param flag
     * @param syncBean
     * @return
     * @throws Exception
     */
    SyncBean<String> openOrCloseLock(String terminalId, List<BaseParameter> list, int flag, SyncBean syncBean) throws Exception;

    /**
     * 蓝牙设备下发电子车牌
     * @param terminalId
     * @param list
     * @param syncBean
     * @return
     * @throws Exception
     */
    SyncBean<String> issuePlateNum(String terminalId, List<BaseParameter> list, SyncBean syncBean) throws Exception;
}
