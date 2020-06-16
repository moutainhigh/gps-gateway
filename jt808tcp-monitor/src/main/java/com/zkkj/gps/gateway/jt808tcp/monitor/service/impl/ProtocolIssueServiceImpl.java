package com.zkkj.gps.gateway.jt808tcp.monitor.service.impl;

import com.zkkj.gps.gateway.jt808tcp.monitor.endpoint.JT808Endpoint;
import com.zkkj.gps.gateway.jt808tcp.monitor.entity.ResultVo;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.MessageId;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.ParameterId;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.Message;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.ParameterSetting;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.TerminalBaseDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.TerminalParameter;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.attribute.TerminalAttributeDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.params.TerminalParamsDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.JT_8900;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch.ElecDispatchInfo;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup.F_11DD;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup.F_11EE;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup.F_8182;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup.F_8183;
import com.zkkj.gps.gateway.jt808tcp.monitor.service.ProtocolIssueService;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.BitOperator;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.HexStringUtils;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 协议下行业务处理类
 * @author suibozhuliu
 */
@Slf4j
@Service
public class ProtocolIssueServiceImpl implements ProtocolIssueService {

    @Autowired
    private JT808Endpoint endpoint;

    @Override
    public ResultVo<TerminalParamsDto> getParameters(String terminalId) {
        ResultVo<TerminalParamsDto> resultVo = new ResultVo<>();
        resultVo.resultFail("终端参数查询失败，请重试！");
        if (StringUtils.isEmpty(terminalId)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        //终端参数id表
        int[] idList = {0x0001,0x0002,0x0013,0x0017,0x0018,0x0019,0x0083};
        List<TerminalParameter> list = new ArrayList(idList.length);
        for (int id : idList) {
            list.add(new TerminalParameter(id));
        }
        ParameterSetting body = new ParameterSetting();
        body.setParameters(list);
        Message message = new Message(MessageId.查询指定终端参数, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, false, "8104");
        if (!ObjectUtils.isEmpty(resBaseDto)) {
            TerminalParamsDto paramsDto = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof TerminalParamsDto){
                paramsDto = (TerminalParamsDto) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode())
                    && resBaseDto.getResultCode() == 0 &&
                    !ObjectUtils.isEmpty(paramsDto)){
                resultVo.resultSuccess(paramsDto,resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"终端参数查询结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

    @Override
    public ResultVo<Boolean> setParameters(String terminalId, TerminalParamsDto terminalParamsDto) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        resultVo.resultFail("终端参数设置失败，请重试！");
        if (StringUtils.isEmpty(terminalId) || ObjectUtils.isEmpty(terminalParamsDto)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        int heartPeriod = terminalParamsDto.getHeartPeriod();//心跳周期
        int tcpResponseTime = terminalParamsDto.getTcpResponseTime();//响应时间
        String mainHost = terminalParamsDto.getMainHost();//主服务IP
        int mainPort = terminalParamsDto.getMainPort();//主服务端口
        String backupsHost = terminalParamsDto.getBackupsHost();//备用服务IP
        int backupsPort = terminalParamsDto.getBackupsPort();//备用服务端口
        String plateNum = terminalParamsDto.getPlateNumber();//车牌号
        List<TerminalParameter> parameters = new ArrayList<>();
        TerminalParameter parameter;
        //心跳周期
        parameter = new TerminalParameter(1, ParameterId.C0x0001,4, BitOperator.integerTo4Bytes(heartPeriod),heartPeriod);
        parameters.add(parameter);
        //响应时间
        parameter = new TerminalParameter(2,ParameterId.C0x0002,4,BitOperator.integerTo4Bytes(tcpResponseTime),tcpResponseTime);
        parameters.add(parameter);
        //主服务IP或域名
        parameter = new TerminalParameter(19,ParameterId.C0x0013,15, HexStringUtils.stringToBytes(mainHost,15,"GBK"),mainHost);
        parameters.add(parameter);
        //主服务端口
        parameter = new TerminalParameter(24,ParameterId.C0x0018,5, BitOperator.integerTo4Bytes(mainPort),mainPort);
        parameters.add(parameter);
        //备用服务IP或域名
        parameter = new TerminalParameter(23,ParameterId.C0x0017,15, HexStringUtils.stringToBytes(backupsHost,15,"GBK"),backupsHost);
        parameters.add(parameter);
        //备用服务端口
        parameter = new TerminalParameter(25,ParameterId.C0x0019,5, BitOperator.integerTo4Bytes(backupsPort),backupsPort);
        parameters.add(parameter);
        //机动车牌号
        parameter = new TerminalParameter(131,ParameterId.C0x0083,9, HexStringUtils.stringToBytes(plateNum,9,"GBK"),plateNum);
        parameters.add(parameter);
        ParameterSetting body = new ParameterSetting();
        body.setParameters(parameters);
        Message message = new Message(MessageId.设置终端参数, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, true, "8103");
        if (!ObjectUtils.isEmpty(resBaseDto)){
            Boolean isSuccess = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof Boolean){
                isSuccess = (Boolean) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode())
                    && resBaseDto.getResultCode() == 0 &&
                    !ObjectUtils.isEmpty(isSuccess) && isSuccess){
                resultVo.resultSuccess(isSuccess,resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"终端参数设置结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

    @Override
    public ResultVo<String> getDeviceCarNum(String terminalId) {
        ResultVo<String> resultVo = new ResultVo<>();
        resultVo.resultFail("设备车牌读取失败，请重试！");
        if (StringUtils.isEmpty(terminalId)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        int[] idList = {0x0083};//终端参数id表
        List<TerminalParameter> list = new ArrayList(idList.length);
        for (int id : idList) {
            list.add(new TerminalParameter(id));
        }
        ParameterSetting body = new ParameterSetting();
        body.setParameters(list);
        Message message = new Message(MessageId.查询指定终端参数, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, false, "81040083");
        if (!ObjectUtils.isEmpty(resBaseDto)){
            String plateNumber = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof String){
                plateNumber = (String) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode())
                    && resBaseDto.getResultCode() == 0 &&
                    !StringUtils.isEmpty(plateNumber)) {
                resultVo.resultSuccess(plateNumber, resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"设备车牌读取结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

    @Override
    public ResultVo<String> readPlateNum(String terminalId) {
        ResultVo<String> resultVo = new ResultVo<>();
        resultVo.resultFail("电子车牌读取失败，请重试！");
        if (StringUtils.isEmpty(terminalId)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }

        F_11EE f11EE = new F_11EE();
        byte[] f11EESendBytes = f11EE.getSendBytes();
        //System.out.println("11EE转换十六进制：【" + HexStringUtils.toHexString(f11EESendBytes) + "】");
        JT_8900 body = new JT_8900();
        body.setType(0XF1);
        body.setContent(f11EESendBytes);

        /*byte[] bytes = JSON.toJSONString(body).getBytes();
        System.out.println("解析开始：【");
        for (byte aByte : bytes) {
            System.out.print(aByte + ",");
        }
        System.out.println("】解析结束：");
        System.out.println("十六进制：" + HexStringUtils.toHexString(bytes));*/

        Message message = new Message(MessageId.数据下行透传, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, false, "11EE");
        if (!ObjectUtils.isEmpty(resBaseDto)){
            String plateNumber = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof String){
                plateNumber = (String) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode())
                    && resBaseDto.getResultCode() == 0 &&
                    !StringUtils.isEmpty(plateNumber)) {
                resultVo.resultSuccess(plateNumber, resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"电子车牌读取结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

    @Override
    public ResultVo<Boolean> setDeviceCarNum(String terminalId, String plateNumber) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        resultVo.resultFail("设备车牌下发失败，请重试！");
        if (StringUtils.isEmpty(plateNumber)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        List<TerminalParameter> parameters = new ArrayList<>();
        TerminalParameter parameter = new TerminalParameter(131,ParameterId.C0x0083,9,
                HexStringUtils.stringToBytes(plateNumber,9,"GBK"),plateNumber);
        parameters.add(parameter);
        ParameterSetting body = new ParameterSetting();
        body.setParameters(parameters);
        Message message = new Message(MessageId.设置终端参数, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, true, "81030083");
        if (!ObjectUtils.isEmpty(resBaseDto)){
            Boolean isSuccess = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof Boolean){
                isSuccess = (Boolean) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode())
                    && resBaseDto.getResultCode() == 0 &&
                    !ObjectUtils.isEmpty(isSuccess) && isSuccess){
                resultVo.resultSuccess(isSuccess,resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"设备车牌下发结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

    @Override
    public ResultVo<String> issuePlateNum(String terminalId, String plateNumber) {
        ResultVo<String> resultVo = new ResultVo<>();
        resultVo.resultFail("电子车牌下发失败，请重试！");
        if (StringUtils.isEmpty(plateNumber)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        F_11DD f11DD = new F_11DD(plateNumber);
        byte[] f11EESendBytes = f11DD.getSendBytes();
        JT_8900 body = new JT_8900();
        body.setType(0XF1);
        body.setContent(f11EESendBytes);
        Message message = new Message(MessageId.数据下行透传, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, false, "11DD");
        if (!ObjectUtils.isEmpty(resBaseDto)){
            Boolean isSuccess = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof Boolean){
                isSuccess = (Boolean) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode())
                    && resBaseDto.getResultCode() == 0 &&
                    !ObjectUtils.isEmpty(isSuccess) && isSuccess){
                resultVo.resultSuccess(resBaseDto.getResMsg(),resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"电子车牌下发结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

    @Override
    public ResultVo<TerminalAttributeDto> getAttribute(String terminalId) {
        ResultVo<TerminalAttributeDto> resultVo = new ResultVo<>();
        resultVo.resultFail("终端属性查询失败，请重试！");
        if (StringUtils.isEmpty(terminalId)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        ParameterSetting body = new ParameterSetting();
        Message message = new Message(MessageId.查询终端属性, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, false, "8107");
        if (!ObjectUtils.isEmpty(resBaseDto)){
            TerminalAttributeDto attributeDto = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof TerminalAttributeDto){
                attributeDto = (TerminalAttributeDto) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode())
                    && resBaseDto.getResultCode() == 0
                    && !ObjectUtils.isEmpty(attributeDto)){
                resultVo.resultSuccess(attributeDto,resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"终端属性查询结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

    @Override
    public ResultVo<ElecDispatchInfo> readBussiness(String terminalId) {
        ResultVo<ElecDispatchInfo> resultVo = new ResultVo<>();
        resultVo.resultFail("电子运单查询失败，请重试！");
        if (StringUtils.isEmpty(terminalId)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        F_8183 f8183 = new F_8183();
        byte[] f8183SendBytes = f8183.getSendBytes();
        JT_8900 body = new JT_8900();
        body.setType(0XF1);
        body.setContent(f8183SendBytes);

        /*System.out.println("8183转换十六进制：【" + HexStringUtils.toHexString(f8183SendBytes) + "】");
        byte[] bytes = JSON.toJSONString(body).getBytes();
        System.out.println("解析开始：【");
        for (byte aByte : bytes) {
            System.out.print(aByte + ",");
        }
        System.out.println("】解析结束：");
        System.out.println("十六进制：" + HexStringUtils.toHexString(bytes));*/

        Message message = new Message(MessageId.数据下行透传, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, false, "8183");
        if (!ObjectUtils.isEmpty(resBaseDto)){
            ElecDispatchInfo elecDispatchInfo = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof ElecDispatchInfo){
                elecDispatchInfo = (ElecDispatchInfo) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode()) &&
                    resBaseDto.getResultCode() == 0 &&
                    !ObjectUtils.isEmpty(elecDispatchInfo)){
                resultVo.resultSuccess(elecDispatchInfo,resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"电子运单查询结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

    @Override
    public ResultVo<String> setBusiness(String terminalId, ElecDispatchInfo elecDispatchInfo) {
        ResultVo<String> resultVo = new ResultVo<>();
        resultVo.resultFail("电子运单下发失败，请重试！");
        if (StringUtils.isEmpty(terminalId) || ObjectUtils.isEmpty(elecDispatchInfo)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        F_8182 f_8182 = new F_8182(elecDispatchInfo);
        byte[] f_8182SendBytes = f_8182.getSendBytes();
        JT_8900 body = new JT_8900();
        body.setType(0XF1);
        body.setContent(f_8182SendBytes);
        Message message = new Message(MessageId.数据下行透传, terminalId, body);
        TerminalBaseDto resBaseDto = endpoint.sendMessage(message, false, "8182");
        if (!ObjectUtils.isEmpty(resBaseDto)){
            Boolean isSuccess = null;
            if (!ObjectUtils.isEmpty(resBaseDto.getData()) && resBaseDto.getData() instanceof Boolean){
                isSuccess = (Boolean) resBaseDto.getData();
            }
            if (!ObjectUtils.isEmpty(resBaseDto.getResultCode())
                    && resBaseDto.getResultCode() == 0 &&
                    !ObjectUtils.isEmpty(isSuccess) && isSuccess){
                resultVo.resultSuccess(resBaseDto.getResMsg(),resBaseDto.getResMsg());
            } else {
                if (!StringUtils.isEmpty(resBaseDto.getResMsg())){
                    resultVo.resultFail(resBaseDto.getResMsg());
                }
            }
        }
        LoggerUtils.info(log,terminalId,"电子运单下发结果：【" + resultVo.toString() + "】");
        return resultVo;
    }

}
