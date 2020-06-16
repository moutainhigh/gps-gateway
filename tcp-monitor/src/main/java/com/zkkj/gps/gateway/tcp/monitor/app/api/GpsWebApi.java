package com.zkkj.gps.gateway.tcp.monitor.app.api;

import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.*;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.BusinessBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.SyncBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.TerminalParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zkkj.gps.gateway.tcp.monitor.utils.LicencePlateUtils.licenceEncode;

/**
 * 车载终端操作Api
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-14 上午 10:01
 */
@Component
public class GpsWebApi {

    @Autowired
    private ViewActionImpl viewActionImpl;

    /**
     * 读取设备电子运单
     * @param terminalId
     * @return
     * @throws Exception
     */
    public SyncBean<BusinessBean> readBusiness(String terminalId) throws Exception {
        return viewActionImpl.readBusiness(terminalId, new SyncBean());
    }

    /**
     * 下发电子运单数据
     * @param terminalId
     * @param businessData
     * @return
     * @throws Exception
     */
    public SyncBean<String> setBusinessData(String terminalId, BusinessBean businessData) throws Exception {
        return viewActionImpl.setBusinessData(terminalId, businessData,new SyncBean());
    }

    /**
     * 获取终端参数
     * @param terminalId
     * @return
     * @throws Exception
     */
    public SyncBean<TerminalParams> readTerminalArgs(String terminalId) throws Exception {
        int parmas[] = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,
                        0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x10,
                        0x11,0x12,0x13,0x14,0x15,0x16,0x23,0X25,
                        0X26,0X28,0x29,0x2A,0x2B,0x35,0x36};
        return viewActionImpl.readTerminalArgs(terminalId,parmas,new SyncBean());
    }

    /**
     * 设置终端参数
     * @param terminalId
     * @param terminalParams
     * @return
     * @throws Exception
     */
    public SyncBean<String> setTerminalArgs(String terminalId, TerminalParams terminalParams) throws Exception {
        if (ObjectUtils.isEmpty(terminalParams) || StringUtils.isEmpty(terminalId)){
            SyncBean<String> result = new SyncBean<>();
            result.resultFail("参数异常，请检查！");
            return result;
        }
        List<BaseParameter> params = new ArrayList<>();
        Param4Bcd param4Bcd;
        Param4Int param4Int;
        Param4Str param4Str;
        Param4IntUtf8Arr param4IntUtf8Arr;
        param4Bcd = new Param4Bcd(0x01,terminalParams.getTerminalId());
        params.add(param4Bcd);
        param4Int = new Param4Int(0x02,terminalParams.getMcuTime());
        params.add(param4Int);
        param4Str = new Param4Str(0x03,terminalParams.getMainIp());
        params.add(param4Str);
        param4Str = new Param4Str(0x04,terminalParams.getBackupIp());
        params.add(param4Str);
        param4Str = new Param4Str(0x05,terminalParams.getPort());
        params.add(param4Str);
        param4Int = new Param4Int(0x06,terminalParams.getReportTime());
        params.add(param4Int);
        param4Int = new Param4Int(0x07,terminalParams.getLocationMode());
        params.add(param4Int);
        param4Int = new Param4Int(0x08,terminalParams.getPalmusTime());
        params.add(param4Int);
        param4Str = new Param4Str(0x09,terminalParams.getApnName());
        params.add(param4Str);
        param4Str = new Param4Str(0x0A,terminalParams.getApnUserName());
        params.add(param4Str);
        param4Str = new Param4Str(0x0B,terminalParams.getApnPassword());
        params.add(param4Str);
        param4Str = new Param4Str(0x0C,terminalParams.getApnName2());
        params.add(param4Str);
        param4Str = new Param4Str(0x0D,terminalParams.getApnUserName2());
        params.add(param4Str);
        param4Str = new Param4Str(0x0E,terminalParams.getApnPassword2());
        params.add(param4Str);
        param4Int = new Param4Int(0x0F,terminalParams.getSimMode());
        params.add(param4Int);
        param4Int = new Param4Int(0x10,terminalParams.getNetMode());
        params.add(param4Int);
        param4Int = new Param4Int(0x11,terminalParams.getWarnVoltage());
        params.add(param4Int);
        param4Str = new Param4Str(0x12,terminalParams.getIcPassword());
        params.add(param4Str);
        param4Str = new Param4Str(0x13,terminalParams.getSmsPassword());
        params.add(param4Str);
        param4Int = new Param4Int(0x14,terminalParams.getShakeTime());
        params.add(param4Int);
        param4Str = new Param4Str(0x15,terminalParams.getCurrentTime());
        params.add(param4Str);
        param4Int = new Param4Int(0x16,terminalParams.getSelfLock());
        params.add(param4Int);
        param4Int = new Param4Int(0x23,terminalParams.getTerminalAction());
        params.add(param4Int);
        param4Int = new Param4Int(0x25,terminalParams.getIsUnlocking());
        params.add(param4Int);
        param4IntUtf8Arr = new Param4IntUtf8Arr(0x26,terminalParams.getEmergencyUnlock(),8);
        params.add(param4IntUtf8Arr);
        param4IntUtf8Arr = new Param4IntUtf8Arr(0x28,terminalParams.getTempIcCardId1(),9);
        params.add(param4IntUtf8Arr);
        param4IntUtf8Arr = new Param4IntUtf8Arr(0x29,terminalParams.getTempIcCardId2(),9);
        params.add(param4IntUtf8Arr);
        param4IntUtf8Arr = new Param4IntUtf8Arr(0x2A,terminalParams.getTempIcCardId3(),9);
        params.add(param4IntUtf8Arr);
        param4IntUtf8Arr = new Param4IntUtf8Arr(0x2B,terminalParams.getTempIcCardId4(),9);
        params.add(param4IntUtf8Arr);

        // todo 新能源车牌与传统车牌互相兼容问题
        String licenceEncodeStr = licenceEncode(terminalParams.getBluetoothName());
        if (StringUtils.isEmpty(licenceEncodeStr)) {
            SyncBean<String> result = new SyncBean<>();
            result.resultFail("蓝牙名称有误，请检查！");
            return result;
        }

        param4IntUtf8Arr = new Param4IntUtf8Arr(0x35,licenceEncodeStr,9);
        params.add(param4IntUtf8Arr);
        param4IntUtf8Arr = new Param4IntUtf8Arr(0x36,terminalParams.getBluetoothPassword(),8);
        params.add(param4IntUtf8Arr);
        return viewActionImpl.setTerminalArgs(terminalId, params,new SyncBean());
    }

    /**
     * 蓝牙电子锁解封施封
     * @param terminalId
     * @param flag  0：解封 1：施封
     * @return
     * @throws Exception
     */
    public SyncBean<String> openOrCloseLock(String terminalId,int flag) throws Exception {
        SyncBean<String> result = new SyncBean<>();
        result.resultFail("参数异常，请检查！");

        List<BaseParameter> params = new ArrayList<>();
        Param4Int param4Int = null;
        switch (flag){
            case 0://解封
                param4Int = new Param4Int(0x24,00);
                break;
            case 1://施封
                param4Int = new Param4Int(0x24,01);
                break;
        }
        if (ObjectUtils.isEmpty(param4Int) || StringUtils.isEmpty(terminalId)){
            return result;
        }
        params.add(param4Int);
        return viewActionImpl.openOrCloseLock(terminalId, params, flag, new SyncBean());
    }

    /**
     * 蓝牙设备下发电子车牌
     * @param terminalId
     * @param plateNum
     * @return
     * @throws Exception
     */
    public SyncBean<String> issuePlateNum(String terminalId,String plateNum) throws Exception {
        SyncBean<String> result = new SyncBean<>();
        result.resultFail("参数异常，请检查！");

        List<BaseParameter> params = new ArrayList<>();

        // todo 新能源车牌与传统车牌互相兼容问题
        String licenceEncodeStr = licenceEncode(plateNum);
        if (StringUtils.isEmpty(licenceEncodeStr)) {
            result.resultFail("参数异常，请检查！");
            return result;
        }

        Param4IntUtf8Arr param4IntUtf8Arr = new Param4IntUtf8Arr(0x35,licenceEncodeStr,9);
        if (ObjectUtils.isEmpty(param4IntUtf8Arr) || StringUtils.isEmpty(terminalId)){
            return result;
        }
        params.add(param4IntUtf8Arr);
        return viewActionImpl.issuePlateNum(terminalId, params,new SyncBean());
    }

    /**
     * 获取终端参数
     * @param terminalId
     * @return
     * @throws Exception
     */
    public SyncBean<String> readPlateNum(String terminalId) throws Exception {
        int parmas[] = {0x35};
        return viewActionImpl.readPlateNum(terminalId,parmas,new SyncBean());
    }


}
