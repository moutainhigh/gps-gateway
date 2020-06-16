package com.zkkj.gps.gateway.tcp.monitor.app.api;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.component.common.MessageHeader;
import com.zkkj.gps.gateway.protocol.component.messagebody.BatchSiteMessage;
import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.component.messagebody.ParamRules;
import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.BaseParameter;
import com.zkkj.gps.gateway.protocol.destination.*;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.BusinessBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.SyncBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.TerminalParams;
import com.zkkj.gps.gateway.tcp.monitor.app.proforward.response.ResultHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zkkj.gps.gateway.tcp.monitor.utils.LicencePlateUtils.licenceDecode;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 5:33
 */
@Component
public class ViewActionImpl implements IViewAction {

    @Autowired
    private ElecProtocolApi elecProtocolApi;

    @Autowired
    private ResultHandler resultHandler;

    /**
     * key：terminalid +协议ID  value：保存每个会话生成的锁对象
     * 设置终端参数 终端ID + 0311
     * 获取终端参数 终端ID + 0312
     * 下发电子运单 终端ID + 0410
     * 获取电子运单 终端ID + 0310
     */
    private static Map<String,List<SyncBean>> syncLockMap=new HashMap<>();

    /**
     * 设置终端参数
     * @param terminalId
     */
    @Override
    public void setTerminalArgs(String terminalId,List<BaseParameter> list) throws Exception {
        elecProtocolApi.setTerminalArgs(terminalId,list);
    }

    /**
     * 设置终端参数
     * @param terminalId
     * @param list
     * @param syncBean
     * @return
     * @throws Exception
     */
    @Override
    public SyncBean<String> setTerminalArgs(String terminalId, List<BaseParameter> list, SyncBean syncBean) throws Exception {
        SyncBean<String> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId) || CollectionUtils.isEmpty(list)) {
            result.resultFail("参数异常，请检查！");
            return result;
        }
        if (!elecProtocolApi.setTerminalArgs(terminalId,list)){
            result.resultFail("设备未上线或通道已关闭");
            return result;
        }
        String key = terminalId + "0311";
        setSyncLockMap(key,syncBean);
        synchronized (syncBean){
            //等待20s
            syncBean.wait(20 * 1000);
        }
        //syncLockMap.get(key).remove(syncBean);
        syncLockMap.remove(key);
        P_0311 data = (P_0311) syncBean.getData();
        if (data != null){
            if (data.getResult() == 0){//成功
                result.resultSuccess("success","终端参数设置成功");
            } else if (data.getResult() == 1){//失败
                result.resultFail("终端参数设置失败");
            }
        } else {
            result.resultFail("终端参数设置失败");
        }
        return result;
    }

    /**
     * 获取终端参数
     * @param terminalId
     * @param paramId
     * @param syncBean
     * @throws Exception
     */
    @Override
    public SyncBean<TerminalParams> readTerminalArgs(String terminalId, int[] paramId, SyncBean syncBean) throws Exception {
        SyncBean<TerminalParams> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId) || paramId == null || paramId.length == 0) {
            result.resultFail("参数异常，请检查！");
            return result;
        }
        if (!elecProtocolApi.readTerminalArgs(terminalId,paramId)){
            result.resultFail("设备未上线或通道已关闭");
            return result;
        }
        String key = terminalId + "0312";
        setSyncLockMap(key,syncBean);
        synchronized (syncBean){
            //等待20s
            syncBean.wait(20 * 1000);
        }
        //syncLockMap.get(key).remove(syncBean);
        syncLockMap.remove(key);
        P_0313 p_0313 = (P_0313) syncBean.getData();
        if (p_0313 != null && !CollectionUtils.isEmpty(p_0313.getParameters())) {
            TerminalParams terminalParams = new TerminalParams();
            for (BaseParameter param : p_0313.getParameters()) {
                if (ObjectUtils.isEmpty(param)) continue;
                Object value = param.getParamValue();
                switch (param.getParamId()) {
                    case 1:
                        try {
                            terminalParams.setTerminalId((String) value);
                        } catch (Exception e) {
                            terminalParams.setTerminalId("");
                        }
                        break;
                    case 2:
                        try {
                            terminalParams.setMcuTime((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setMcuTime(-10);
                        }
                        break;
                    case 3:
                        try {
                            terminalParams.setMainIp((String) value);
                        } catch (Exception e) {
                            terminalParams.setMainIp("");
                        }
                        break;
                    case 4:
                        try {
                            terminalParams.setBackupIp((String) value);
                        } catch (Exception e) {
                            terminalParams.setBackupIp("");
                        }
                        break;
                    case 5:
                        try {
                            terminalParams.setPort((String) value);
                        } catch (Exception e) {
                            terminalParams.setPort("");
                        }
                        break;
                    case 6:
                        try {
                            terminalParams.setReportTime((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setReportTime(-10);
                        }
                        break;
                    case 7:
                        try {
                            terminalParams.setLocationMode((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setLocationMode(-10);
                        }
                        break;
                    case 8:
                        try {
                            terminalParams.setPalmusTime((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setPalmusTime(-10);
                        }
                        break;
                    case 9:
                        try {
                            terminalParams.setApnName((String) value);
                        } catch (Exception e) {
                            terminalParams.setApnName("");
                        }
                        break;
                    case 10:
                        try {
                            terminalParams.setApnUserName((String) value);
                        } catch (Exception e) {
                            terminalParams.setApnUserName("");
                        }
                        break;
                    case 11:
                        try {
                            terminalParams.setApnPassword((String) value);
                        } catch (Exception e) {
                            terminalParams.setApnPassword("");
                        }
                        break;
                    case 12:
                        try {
                            terminalParams.setApnName2((String) value);
                        } catch (Exception e) {
                            terminalParams.setApnName2("");
                        }
                        break;
                    case 13:
                        try {
                            terminalParams.setApnUserName2((String) value);
                        } catch (Exception e) {
                            terminalParams.setApnUserName2("");
                        }
                        break;
                    case 14:
                        try {
                            terminalParams.setApnPassword2((String) value);
                        } catch (Exception e) {
                            terminalParams.setApnPassword2("");
                        }
                        break;
                    case 15:
                        try {
                            terminalParams.setSimMode((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setSimMode(-10);
                        }
                        break;
                    case 16:
                        try {
                            terminalParams.setNetMode((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setNetMode(-10);
                        }
                        break;
                    case 17:
                        try {
                            terminalParams.setWarnVoltage((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setWarnVoltage(-10);
                        }
                        break;
                    case 18:
                        try {
                            terminalParams.setIcPassword((String) value);
                        } catch (Exception e) {
                            terminalParams.setIcPassword("");
                        }
                        break;
                    case 19:
                        try {
                            terminalParams.setSmsPassword((String) value);
                        } catch (Exception e) {
                            terminalParams.setSmsPassword("");
                        }
                        break;
                    case 20:
                        try {
                            terminalParams.setShakeTime((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setShakeTime(-10);
                        }
                        break;
                    case 21:
                        try {
                            terminalParams.setCurrentTime((String) value);
                        } catch (Exception e) {
                            terminalParams.setCurrentTime("");
                        }
                        break;
                    case 22:
                        try {
                            terminalParams.setSelfLock((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setSelfLock(-10);
                        }
                        break;
                    case 35:
                        try {
                            terminalParams.setTerminalAction((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setTerminalAction(-10);
                        }
                        break;
                    /*case 36:
                        try {
                            terminalParams.setReserve1((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setReserve1(-10);
                        }
                        break;*/
                    case 37:
                        try {
                            terminalParams.setIsUnlocking((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setIsUnlocking(-10);
                        }
                        break;
                    case 38:
                        try {
                            terminalParams.setEmergencyUnlock((String) value);
                        } catch (Exception e) {
                            terminalParams.setEmergencyUnlock("");
                        }
                        break;
                    /*case 39:
                        try {
                            terminalParams.setReserve2((Integer) value);
                        } catch (Exception e) {
                            terminalParams.setReserve2(-10);
                        }
                        break;*/
                    case 40:
                        try {
                            //((String) value).replaceAll("\\u0000","")
                            terminalParams.setTempIcCardId1(((String) value).replaceAll("\\u0000",""));
                        } catch (Exception e) {
                            terminalParams.setTempIcCardId1("");
                        }
                        break;
                    case 41:
                        try {
                            terminalParams.setTempIcCardId2(((String) value).replaceAll("\\u0000",""));
                        } catch (Exception e) {
                            terminalParams.setTempIcCardId2("");
                        }
                        break;
                    case 42:
                        try {
                            terminalParams.setTempIcCardId3(((String) value).replaceAll("\\u0000",""));
                        } catch (Exception e) {
                            terminalParams.setTempIcCardId3("");
                        }
                        break;
                    case 43:
                        try {
                            terminalParams.setTempIcCardId4(((String) value).replaceAll("\\u0000",""));
                        } catch (Exception e) {
                            terminalParams.setTempIcCardId4("");
                        }
                        break;
                    case 53:
                        try {
                            String licenceDecode = licenceDecode((String) value);
                            terminalParams.setBluetoothName(licenceDecode);
                        } catch (Exception e) {
                            terminalParams.setBluetoothName("");
                        }
                        break;
                    case 54:
                        try {
                            terminalParams.setBluetoothPassword((String) value);
                        } catch (Exception e) {
                            terminalParams.setBluetoothPassword("");
                        }
                        break;
                }
            }
            result.resultSuccess(terminalParams, "获取终端参数成功");
        } else {
            result.resultFail("获取终端参数失败");
        }
        return result;
    }

    /**
     * 下发电子运单数据
     * @param terminalId
     * @param businessData
     * @param syncBean
     * @throws Exception
     */
    @Override
    public SyncBean<String> setBusinessData(String terminalId, BusinessBean businessData, SyncBean syncBean) throws Exception {
        SyncBean<String> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId) || ObjectUtils.isEmpty(businessData)) {
            result.resultFail("参数异常，请检查！");
            return result;
        }
        BusinessExtensionData businessExtensionData = new BusinessExtensionData();
        BeanUtils.copyProperties(businessData,businessExtensionData);
        if (!elecProtocolApi.setBusinessData(terminalId,businessExtensionData)){
            result.resultFail("设备未上线或通道已关闭");
            return result;
        }
        String key = terminalId + "0410";
        setSyncLockMap(key,syncBean);
        synchronized (syncBean){
            //等待20s
            syncBean.wait(20 * 1000);
        }
        //syncLockMap.get(key).remove(syncBean);
        syncLockMap.remove(key);
        P_0411 p0411 = (P_0411) syncBean.getData();
        if (p0411 != null){
            if (p0411.getResult() == 0) {//成功
                result.resultSuccess("seccess","电子运单数据下发成功");
            } else if (p0411.getResult() == 1){//失败
                result.resultFail("电子运单数据下发失败");
            }
        } else {
            result.resultFail("电子运单数据下发失败");
        }
        return result;
    }

    /**
     * 获取电子运单
     * @param terminalId
     * @throws Exception
     */
    @Override
    public SyncBean<BusinessBean> readBusiness(String terminalId, SyncBean syncBean) throws Exception {
        SyncBean<BusinessBean> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId)) {
            result.resultFail("设备终端号不能为空！");
            return result;
        }
        if (!elecProtocolApi.readTerminalArgs(terminalId)){
            result.resultFail("设备未上线或通道已关闭！");
            return result;
        }
        String key = terminalId + "0310";
        setSyncLockMap(key,syncBean);
        synchronized (syncBean){
            //等待20s
            syncBean.wait(20 * 1000);
        }
        //syncLockMap.get(key).remove(syncBean);
        syncLockMap.remove(key);
        BusinessExtensionData business = (BusinessExtensionData) syncBean.getData();
        if (business != null){
            BusinessBean businessBean = new BusinessBean();
            BeanUtils.copyProperties(business,businessBean);
            result.resultSuccess(businessBean,"电子运单数据获取成功");
        } else {
            result.resultFail("获取电子运单数据失败");
        }
        return result;
    }

    /**
     * 读取电子车牌
     * @param terminalId
     * @return
     * @throws Exception
     */
    @Override
    public SyncBean<String> readPlateNum(String terminalId,int[] paramId,SyncBean syncBean) throws Exception {
        SyncBean<String> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId) || paramId == null || paramId.length == 0) {
            result.resultFail("参数异常，请检查！");
            return result;
        }
        if (!elecProtocolApi.readPlateNum(terminalId, paramId)){
            result.resultFail("设备未上线或通道已关闭");
            return result;
        }
        String key = terminalId + "0312";
        setSyncLockMap(key,syncBean);
        synchronized (syncBean){
            //等待20s
            syncBean.wait(20 * 1000);
        }
        //syncLockMap.get(key).remove(syncBean);
        syncLockMap.remove(key);
        P_0313 p_0313 = (P_0313) syncBean.getData();
        if (!ObjectUtils.isEmpty(p_0313)){
            if (!CollectionUtils.isEmpty(p_0313.getParameters())) {//成功
                BaseParameter parameters = p_0313.getParameters().get(0);
                if (!ObjectUtils.isEmpty(parameters)){
                    //todo 新能源车牌需要解码操作
                    String licenceDecode = licenceDecode((String) parameters.getParamValue());
                    result.resultSuccess(licenceDecode,"电子车牌读取成功");
                    return result;
                }
            }
        }
        result.resultFail("电子车牌读取失败");
        return result;
    }

    /**
     * 获取终端参数
     * @param terminalId
     */
    @Override
    public void readTerminalArgs(String terminalId,int[] paramId) throws Exception{
        elecProtocolApi.readTerminalArgs(terminalId,paramId);
    }

    /**
     * 获取电子运单
     * @param terminalId
     * @throws Exception
     */
    @Override
    public void readBusiness(String terminalId) throws Exception {
        if (!StringUtils.isEmpty(terminalId)) return;
        elecProtocolApi.readTerminalArgs(terminalId);
    }

    /**
     * 批量设置区域及刷卡的使用规则
     * @param terminalId
     */
    @Override
    public void setAreaBatch(String terminalId,int ruleTotal,int currentRules, List<ParamRules> list) throws Exception{
        elecProtocolApi.setAreaBatch(terminalId ,ruleTotal, currentRules,list);
    }

    /**
     * 批量读取区域及刷卡的使用规则
     * @param terminalId
     */
    @Override
    public void readAreaBatch(String terminalId,int[] addressNumber) throws Exception{
        elecProtocolApi.readAreaBatch(terminalId,addressNumber);
    }

    /**
     * 读取终端区域及刷卡使用规则检验码及存储数量
     * @param terminalId
     */
    @Override
    public void readAreaCrc(String terminalId,int paramTotalCrc,int[] addressNumber) throws Exception {
        elecProtocolApi.readAreaCrc(terminalId,paramTotalCrc,addressNumber);
    }

    /**
     * 下发电子运单数据
     * @param terminalId
     */
    @Override
    public void setBusinessData(String terminalId,BusinessExtensionData businessData) throws Exception {
        elecProtocolApi.setBusinessData(terminalId,businessData);
    }

    /**
     * 蓝牙电子锁解封施封
     * @param terminalId
     * @param list
     * @param syncBean
     * @return
     * @throws Exception
     */
    @Override
    public SyncBean<String> openOrCloseLock(String terminalId, List<BaseParameter> list, int flag,SyncBean syncBean) throws Exception {
        SyncBean<String> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId) || CollectionUtils.isEmpty(list)) {
            result.resultFail("参数异常，请检查！");
            return result;
        }
        if (!elecProtocolApi.setTerminalArgs(terminalId,list)){
            result.resultFail("设备未上线或通道已关闭");
            return result;
        }
        String key = terminalId + "0311";
        setSyncLockMap(key,syncBean);
        synchronized (syncBean){
            //等待20s
            syncBean.wait(20 * 1000);
        }
        //syncLockMap.get(key).remove(syncBean);
        syncLockMap.remove(key);
        P_0311 data = (P_0311) syncBean.getData();
        String flagDesc = "操作";
        switch (flag){
            case 0:
                flagDesc = "解封";
                break;
            case 1:
                flagDesc = "施封";
                break;
        }
        if (data != null){
            if (data.getResult() == 0){//成功
                result.resultSuccess("success", "电子锁" + flagDesc + "成功");
            } else if (data.getResult() == 1){//失败
                result.resultFail("电子锁" + flagDesc + "失败");
            }
        } else {
            result.resultFail("电子锁" + flagDesc + "失败");
        }
        return result;
    }

    /**
     * 蓝牙设备下发电子车牌
     * @param terminalId
     * @param list
     * @param syncBean
     * @return
     * @throws Exception
     */
    @Override
    public SyncBean<String> issuePlateNum(String terminalId, List<BaseParameter> list, SyncBean syncBean) throws Exception {
        SyncBean<String> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId) || CollectionUtils.isEmpty(list)) {
            result.resultFail("参数异常，请检查！");
            return result;
        }
        if (!elecProtocolApi.setTerminalArgs(terminalId,list)){
            result.resultFail("设备未上线或通道已关闭");
            return result;
        }
        String key = terminalId + "0311";
        setSyncLockMap(key,syncBean);
        synchronized (syncBean){
            //等待20s
            syncBean.wait(20 * 1000);
        }
        //syncLockMap.get(key).remove(syncBean);
        syncLockMap.remove(key);
        P_0311 data = (P_0311) syncBean.getData();
        if (data != null){
            if (data.getResult() == 0){//成功
                result.resultSuccess("success", "电子车牌下发成功");
            } else if (data.getResult() == 1){//失败
                result.resultFail("电子车牌下发失败");
            }
        } else {
            result.resultFail("电子车牌下发失败");
        }
        return result;
    }

    /**
     * 平台统一响应8001
     * @param terminalId
     * @param messageID
     */
    @Override
    public void platformResponse(String terminalId, int messageID)throws Exception {
        elecProtocolApi.platformResponse(terminalId,messageID);
    }

    /**
     * 终端响应回调
     * @param compose
     * @param msgHeader
     */
    @Override
    public void responseCallBack(DestinationBaseCompose compose, MessageHeader msgHeader) {
        try {
            if (msgHeader == null || compose == null) return;
            String terminalId = msgHeader.getTerminalId();
            String msgIdStr = msgHeader.getMessageIdStr();
            StringBuffer key = new StringBuffer();
            key.append(terminalId);
            switch (msgIdStr){
                case "0200"://实时位置状态信息汇报
                    P_0200 p0200 = (P_0200) compose;
                    if (p0200 != null){
                        if (p0200.getSiteAppendMessage() != null){
                            if (p0200.getSiteAppendMessage().getAppend_36() != null &&
                                    p0200.getSiteAppendMessage().getAppend_36().getBusinessExtensionData() != null){
                                //获取电子运单的key:0310
                                key.append("0310");
                                lockNotify(key.toString(),p0200.getSiteAppendMessage().getAppend_36().getBusinessExtensionData(),"获取电子运单");
                            }
                        }
                    }
                    resultHandler.positionChange(compose,terminalId,msgIdStr);
                    break;
                case "0210"://实时位置状态信息汇报补传（可能会携带电子运单）
                    P_0210 p0210 = (P_0210) compose;
                    if (!ObjectUtils.isEmpty(p0210)){
                        if (!CollectionUtils.isEmpty(p0210.getSiteMessageList())){
                            for (BatchSiteMessage batchSiteMessage : p0210.getSiteMessageList()) {
                                if (!ObjectUtils.isEmpty(batchSiteMessage) &&
                                        !ObjectUtils.isEmpty(batchSiteMessage.getSiteAppendMessage()) &&
                                        !ObjectUtils.isEmpty(batchSiteMessage.getSiteAppendMessage().getAppend_36()) &&
                                        !ObjectUtils.isEmpty(batchSiteMessage.getSiteAppendMessage().getAppend_36().getBusinessExtensionData())) {
                                    //定位信息补传时携带的电子运单的key:0310
                                    key.append("0310");
                                    lockNotify(key.toString(), batchSiteMessage.getSiteAppendMessage().getAppend_36().getBusinessExtensionData(), "定位信息补传返回电子运单");
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                    resultHandler.positionChange(compose,terminalId,msgIdStr);
                    break;
                case "0311"://终端参数设置应答
                    key.append("0311");
                    P_0311 p0311 = (P_0311) compose;
                    lockNotify(key.toString(),p0311,"终端参数设置");
                    break;
                case "0313"://终端参数查询应答
                    key.append("0312");
                    P_0313 p0313 = (P_0313) compose;
                    lockNotify(key.toString(),p0313,"终端参数查询");
                    break;
                case "0315"://批量设置区域及刷卡的使用规则应答
                    break;
                case "0317"://批量读取区域及刷卡的使用规则应答
                    break;
                case "0319"://读取终端区域及刷卡使用规则检验码及存储数量应答
                    break;
                case "0411"://下发业务扩展数据应答
                    key.append("0410");
                    P_0411 p0411 = (P_0411) compose;
                    lockNotify(key.toString(),p0411,"下发电子运单");
                    break;
            }
        } catch (Exception e){
            //LoggerUtils.error(log,"终端响应数据解析异常",  e.toString());
        }
    }

    /**
     * 向同步锁集合设置同步对象
     * @param key
     * @param syncBean
     */
    private void setSyncLockMap (String key,SyncBean syncBean){
        if (syncLockMap.containsKey(key)){
            syncLockMap.get(key).add(syncBean);
        } else {
            List<SyncBean> syncBeans = new ArrayList<>();
            syncBeans.add(syncBean);
            syncLockMap.put(key,syncBeans);
        }
    }

    /**
     * 唤醒同步对象锁
     * @param key
     * @param compose
     * @param msg
     */
    private void lockNotify(String key,Object compose,String msg){
        if (CollectionUtils.isEmpty(syncLockMap)) return;
        if (syncLockMap.containsKey(key.toString())) {
            List<SyncBean> syncBeans = syncLockMap.get(key.toString());
            if (!CollectionUtils.isEmpty(syncBeans)) {
                for (SyncBean syncBean : syncBeans) {
                    if (syncBean == null) continue;
                    synchronized (syncBean) {
                        syncBean.resultSuccess(compose,msg);
                        syncBean.notifyAll();
                    }
                }
            }
        }
    }


}
