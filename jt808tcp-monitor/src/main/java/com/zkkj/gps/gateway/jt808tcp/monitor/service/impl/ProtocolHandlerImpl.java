package com.zkkj.gps.gateway.jt808tcp.monitor.service.impl;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.jt808tcp.monitor.endpoint.JT808Endpoint;
import com.zkkj.gps.gateway.jt808tcp.monitor.entity.response.LocationBean;
import com.zkkj.gps.gateway.jt808tcp.monitor.entity.response.PositionBean;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.ProtocolId;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.*;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.attribute.TerminalAttributeDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.params.TerminalParamsDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.position.PositionAppendDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.JT_0104;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.JT_0107;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.JT_0200;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch.BusinessBean;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch.ElecDispatchInfo;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup.F_11EE;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractMessage;
import com.zkkj.gps.gateway.jt808tcp.monitor.service.PointTransmitService;
import com.zkkj.gps.gateway.jt808tcp.monitor.service.ProtocolHandlerService;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.BitOperator;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.ParameterId.C0x0083;

/**
 * 协议业务处理类
 * @author suibozhuliu
 */
@Slf4j
@Service
public class ProtocolHandlerImpl implements ProtocolHandlerService{

    @Autowired
    private JT808Endpoint jt808Endpoint;

    @Autowired
    private PointTransmitService pointTransmitService;

    @Override
    public void terminalResponse(ProtocolId protocolId, AbstractMessage msgRequest) {
        try {
            Message message = (Message) msgRequest;
            if (ObjectUtils.isEmpty(message)){
                return;
            }
            switch (protocolId){
                //终端通用应答（终端参数下发、电子运单下发、电子车牌下发）
                case C0x0001:
                    TerminalBaseDto commonResDto = new TerminalBaseDto<>();
                    try {
                        CommonResult commonResult = (CommonResult) msgRequest.getBody();
                        if (!ObjectUtils.isEmpty(commonResult)){
                            commonResDto.setResponseKey(message.getMobileNumber() + commonResult.getFlowId());
                            commonResDto.setResultCode(commonResult.getResultCode());
                        } else {
                            commonResDto.setResultCode(1);
                        }
                    } catch (Exception e){
                        commonResDto.setResultCode(1);
                    }
                    switch (commonResDto.getResultCode()){
                        case 0:
                            commonResDto.setData(true);
                            break;
                        case 1:
                        case 2:
                        case 3:
                            commonResDto.setData(false);
                            break;
                    }
                    commonResDto.setResProtocolId("0001");
                    jt808Endpoint.resultCallback(commonResDto);
                    break;
                //终端心跳
                case C0x0002:
                    //System.out.println("终端心跳：【" + msgRequest.toString() + "】");
                    break;
                //终端注册
                case C0x0100:
                    //System.out.println("终端注册：【" + msgRequest.toString() + "】");
                    break;
                //终端注销
                case C0x0003:
                    //System.out.println("终端注销：【" + msgRequest.toString() + "】");
                    break;
                //终端鉴权
                case C0x0102:
                    //System.out.println("终端注销：【" + msgRequest.toString() + "】");
                    break;
                //终端参数查询
                case C0x0104:
                    JT_0104 jt_0104 = (JT_0104) msgRequest.getBody();
                    if (!ObjectUtils.isEmpty(jt_0104) &&
                            !CollectionUtils.isEmpty(jt_0104.getTerminalParameters()) &&
                            !CollectionUtils.isEmpty(jt_0104.getTerminalParameters())) {
                        List<TerminalParameter> terminalParameters = jt_0104.getTerminalParameters();
                        //读参数
                        if (terminalParameters.size() == 1) {
                            TerminalBaseDto<String> plateNumDto = new TerminalBaseDto<>();
                            //读车牌
                            if (!ObjectUtils.isEmpty(terminalParameters.get(0)) &&
                                    terminalParameters.get(0).getIdType() == C0x0083) {
                                byte[] bytesValue = terminalParameters.get(0).getBytesValue();
                                String plateNumber = HexStringUtils.parseString(bytesValue, "GBK");
                                plateNumDto.setData(plateNumber);
                                plateNumDto.setResultCode(0);
                            } else {
                                plateNumDto.setData(null);
                                plateNumDto.setResultCode(1);
                            }
                            plateNumDto.setResProtocolId("0083");
                            plateNumDto.setResponseKey(message.getMobileNumber() + "81040083");
                            jt808Endpoint.resultCallback(plateNumDto);
                            //读全参
                        } else {
                            TerminalParamsDto paramsDto = new TerminalParamsDto();
                            for (TerminalParameter parames : terminalParameters) {
                                if (!ObjectUtils.isEmpty(parames)) {
                                    byte[] bytesValue = parames.getBytesValue();
                                    switch (parames.getIdType()) {
                                        case C0x0001:
                                            paramsDto.setHeartPeriod(BitOperator.byteToInteger(bytesValue));
                                            break;
                                        case C0x0002:
                                            paramsDto.setTcpResponseTime(BitOperator.byteToInteger(bytesValue));
                                            break;
                                        case C0x0013:
                                            paramsDto.setMainHost(HexStringUtils.parseString(bytesValue, "GBK"));
                                            break;
                                        case C0x0017:
                                            paramsDto.setBackupsHost(HexStringUtils.parseString(bytesValue, "GBK"));
                                            break;
                                        case C0x0018:
                                            paramsDto.setMainPort(BitOperator.byteToInteger(bytesValue));
                                            break;
                                        case C0x0019:
                                            paramsDto.setBackupsPort(BitOperator.byteToInteger(bytesValue));
                                            break;
                                        case C0x0083:
                                            paramsDto.setPlateNumber(HexStringUtils.parseString(bytesValue, "GBK"));
                                            break;
                                    }
                                } else {
                                    continue;
                                }
                            }
                            TerminalBaseDto<TerminalParamsDto> paramsBaseDto = new TerminalBaseDto<>();
                            paramsBaseDto.setData(paramsDto);
                            paramsBaseDto.setResponseKey(message.getMobileNumber() + "8104");
                            paramsBaseDto.setResProtocolId("0104");
                            paramsBaseDto.setResultCode(0);
                            jt808Endpoint.resultCallback(paramsBaseDto);
                        }
                    }
                    break;
                //位置信息上传
                case C0x0200:
                    JT_0200 jt_0200 = (JT_0200) msgRequest.getBody();
                    if (!ObjectUtils.isEmpty(jt_0200)){
                        PositionAppendDto positionAppendDto = new PositionAppendDto();
                        BeanUtils.copyProperties(jt_0200,positionAppendDto);

                        ElecDispatchInfo elecDispatchInfo = null;
                        if (!CollectionUtils.isEmpty(jt_0200.getPositionAttributes())){
                            List<PositionAttribute> attributesList = jt_0200.getPositionAttributes();
                            for (PositionAttribute positionAttribute : attributesList) {
                                byte[] bytesValue = positionAttribute.getBytesValue();
                                if (ObjectUtils.isEmpty(positionAttribute) ||
                                        ObjectUtils.isEmpty(bytesValue) ||
                                        ObjectUtils.isEmpty(positionAttribute.getIdType())) {
                                    continue;
                                } else {
                                    switch (positionAttribute.getIdType()){
                                        case C0x01:
                                            positionAppendDto.setMileage(BitOperator.byteToInteger(bytesValue));
                                            break;
                                        case C0x02:
                                            positionAppendDto.setOilMass(BitOperator.byteToInteger(bytesValue));
                                            break;
                                        case C0xE1:
                                            positionAppendDto.setPower(BitOperator.byteToInteger(bytesValue));
                                            break;
                                        case C0x33:
                                            if (bytesValue != null){
                                                //电子运单上报
                                                if (positionAttribute.getLength() == 207){
                                                    List<Byte> bytes = Lists.newArrayList();
                                                    for (byte b : bytesValue) {
                                                        bytes.add(b);
                                                    }
                                                    elecDispatchInfo = new ElecDispatchInfo(bytes, 8);
                                                    TerminalBaseDto<ElecDispatchInfo> paramsBaseDto = new TerminalBaseDto<>();
                                                    paramsBaseDto.setData(elecDispatchInfo);
                                                    paramsBaseDto.setResponseKey(message.getMobileNumber() + "8183");
                                                    paramsBaseDto.setResProtocolId("8183");
                                                    if (!ObjectUtils.isEmpty(elecDispatchInfo)){
                                                        paramsBaseDto.setResultCode(0);
                                                    } else {
                                                        paramsBaseDto.setResultCode(1);
                                                    }
                                                    jt808Endpoint.resultCallback(paramsBaseDto);
                                                    //电子运单下发、电子车牌下发
                                                } else if (positionAttribute.getLength() == 11 || positionAttribute.getLength() == 10){
                                                    List<Byte> bytes = Lists.newArrayList();
                                                    for (byte b : bytesValue) {
                                                        bytes.add(b);
                                                    }
                                                    byte[] tempByte = new byte[2];
                                                    System.arraycopy(bytesValue,4,tempByte,0,2);
                                                    if (tempByte.length != 0){
                                                        TerminalBaseDto<Boolean> writeBaseDto = new TerminalBaseDto<>();
                                                        Boolean isWriteSuccess = bytes.get(6) == 0x01;
                                                        writeBaseDto.setData(isWriteSuccess);
                                                        if (isWriteSuccess){
                                                            writeBaseDto.setResultCode(0);
                                                        } else {
                                                            writeBaseDto.setResultCode(1);
                                                        }
                                                        String hexStr = HexStringUtils.toHexString(tempByte);
                                                        switch (hexStr){
                                                            //电子运单下发
                                                            case "8182":
                                                                writeBaseDto.setResponseKey(message.getMobileNumber() + "8182");
                                                                writeBaseDto.setResProtocolId("8182");
                                                                break;
                                                            //电子车牌下发
                                                            case "11DD":
                                                                writeBaseDto.setResponseKey(message.getMobileNumber() + "11DD");
                                                                writeBaseDto.setResProtocolId("11DD");
                                                                break;
                                                            default:
                                                                return;
                                                        }
                                                        jt808Endpoint.resultCallback(writeBaseDto);
                                                    }
                                                    //电子车牌读取
                                                } else if (positionAttribute.getLength() == 19){
                                                    List<Byte> bytes = Lists.newArrayList();
                                                    for (byte b : bytesValue) {
                                                        bytes.add(b);
                                                    }
                                                    byte[] tempByte = new byte[2];
                                                    System.arraycopy(bytesValue,4,tempByte,0,2);
                                                    if (tempByte.length != 0){
                                                        String hexStr = HexStringUtils.toHexString(tempByte);
                                                        if (!StringUtils.isEmpty(hexStr) && hexStr.equals("11EE")){
                                                            F_11EE f11EE = new F_11EE(bytes);
                                                            TerminalBaseDto<String> paramsBaseDto = new TerminalBaseDto<>();
                                                            paramsBaseDto.setData(f11EE.getElecPlateNumber());
                                                            paramsBaseDto.setResponseKey(message.getMobileNumber() + "11EE");
                                                            paramsBaseDto.setResProtocolId("11EE");
                                                            if (!ObjectUtils.isEmpty(f11EE) &&
                                                                    !ObjectUtils.isEmpty(paramsBaseDto) &&
                                                                    !StringUtils.isEmpty(f11EE.getElecPlateNumber())){
                                                                paramsBaseDto.setResultCode(0);
                                                            } else {
                                                                paramsBaseDto.setResultCode(1);
                                                            }
                                                            jt808Endpoint.resultCallback(paramsBaseDto);
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                        LocationBean locationBean = transmitPosition(positionAppendDto, message.getMobileNumber(), elecDispatchInfo);
                        pointTransmitService.pointTransmit(locationBean);
                        //LoggerUtils.info(log,locationBean.getTerminalId(),"位置信息回传：【" + locationBean.toString() + "】\n");
                    }
                    break;
                //定位数据批量回传
                case C0x0704:
                    /*System.out.println("定位数据批量上传.............................................................");
                    JT_0704 jt_0704 = (JT_0704) msgRequest.getBody();
                    msgRequest.setBody(null);
                    if (!ObjectUtils.isEmpty(jt_0704)){
                        if (!CollectionUtils.isEmpty(jt_0704.getList())){
                            List<JT_0704.Item> itemList = jt_0704.getList();
                            jt_0704.setList(null);
                            for (JT_0704.Item item : itemList) {
                                if (ObjectUtils.isEmpty(item))continue;
                                JT_0200 jt0200 = item.getPosition();
                                if (ObjectUtils.isEmpty(jt0200) || CollectionUtils.isEmpty(jt0200.getPositionAttributes())) continue;
                                PositionAppendDto positionAppendDto = new PositionAppendDto();
                                BeanUtils.copyProperties(jt0200,positionAppendDto);
                                ElecDispatchInfo elecDispatchInfo = null;
                                List<PositionAttribute> positionAttributes = jt0200.getPositionAttributes();
                                for (PositionAttribute positionAttribute : positionAttributes) {
                                    byte[] bytesValue = positionAttribute.getBytesValue();
                                    if (ObjectUtils.isEmpty(positionAttribute) ||
                                            ObjectUtils.isEmpty(bytesValue) ||
                                            ObjectUtils.isEmpty(positionAttribute.getIdType())) {
                                        continue;
                                    } else {
                                        switch (positionAttribute.getIdType()){
                                            case C0x01:
                                                positionAppendDto.setMileage(BitOperator.byteToInteger(bytesValue));
                                                break;
                                            case C0x02:
                                                positionAppendDto.setOilMass(BitOperator.byteToInteger(bytesValue));
                                                break;
                                            case C0xE1:
                                                positionAppendDto.setPower(BitOperator.byteToInteger(bytesValue));
                                                break;
                                            case C0x33:
                                                if (bytesValue != null){
                                                    if (positionAttribute.getLength() >= 207){
                                                        List<Byte> bytes = Lists.newArrayList();
                                                        for (byte b : bytesValue) {
                                                            bytes.add(b);
                                                        }
                                                        elecDispatchInfo = new ElecDispatchInfo(bytes, 8);
                                                        TerminalBaseDto<ElecDispatchInfo> paramsBaseDto = new TerminalBaseDto<>();
                                                        paramsBaseDto.setData(elecDispatchInfo);
                                                        paramsBaseDto.setResponseKey(message.getMobileNumber() + "8183");
                                                        paramsBaseDto.setResProtocolId("8183");
                                                        if (!ObjectUtils.isEmpty(elecDispatchInfo)){
                                                            paramsBaseDto.setResultCode(0);
                                                        } else {
                                                            paramsBaseDto.setResultCode(1);
                                                        }
                                                        jt808Endpoint.resultCallback(paramsBaseDto);
                                                    } else if (positionAttribute.getLength() == 11){
                                                        List<Byte> bytes = Lists.newArrayList();
                                                        for (byte b : bytesValue) {
                                                            bytes.add(b);
                                                        }
                                                        TerminalBaseDto<Boolean> writeBaseDto = new TerminalBaseDto<>();
                                                        Boolean isWriteSuccess = bytes.get(6) == 0x01;
                                                        if (isWriteSuccess){
                                                            writeBaseDto.setResultCode(0);
                                                        } else {
                                                            writeBaseDto.setResultCode(1);
                                                        }
                                                        writeBaseDto.setResponseKey(message.getMobileNumber() + "8182");
                                                        writeBaseDto.setResProtocolId("8182");
                                                        writeBaseDto.setData(isWriteSuccess);
                                                        jt808Endpoint.resultCallback(writeBaseDto);
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                }
                                LocationBean locationBean = transmitPosition(positionAppendDto, message.getMobileNumber(), elecDispatchInfo);
                                System.out.println("定位数据批量回传的数据：【" + locationBean.toString()+"】");
                                pointTransmitService.pointTransmit(locationBean);
                            }
                        }
                    }*/
                    break;
                //查询终端属性应答
                case C0x0107:
                    JT_0107 jt_0107 = (JT_0107) msgRequest.getBody();
                    TerminalBaseDto<TerminalAttributeDto> attributeDto = new TerminalBaseDto<>();
                    if (!ObjectUtils.isEmpty(jt_0107)){
                        TerminalAttributeDto terminalAttributeDto = new TerminalAttributeDto();
                        BeanUtils.copyProperties(jt_0107,terminalAttributeDto);
                        attributeDto.setData(terminalAttributeDto);
                        attributeDto.setResultCode(0);
                    } else {
                        attributeDto.setResultCode(1);
                        attributeDto.setData(null);
                    }
                    attributeDto.setResponseKey(message.getMobileNumber() + "8107");
                    attributeDto.setResProtocolId("0107");
                    jt808Endpoint.resultCallback(attributeDto);
                    break;
            }
        } catch (Exception e){
            log.error("协议解析出现异常：【" + e + "】");
        }
    }

    /**
     * 定位数据转换
     * @param appendDto
     * @param terminalId
     * @param elecDispatchInfo
     */
    private LocationBean transmitPosition(PositionAppendDto appendDto, String terminalId, ElecDispatchInfo elecDispatchInfo) {
        if (ObjectUtils.isEmpty(appendDto) || StringUtils.isEmpty(terminalId)){
            return null;
        }
        LocationBean<BusinessBean> locationBean = new LocationBean();
        locationBean.setFlag(3);
        locationBean.setEleDispatch(null);
        locationBean.setTerminalId(terminalId);
        PositionBean positionBean = new PositionBean();
        positionBean.setAlarmState(appendDto.getAlarmState() == null ? 0 : appendDto.getAlarmState());
        positionBean.setTerminalState(appendDto.getTerminalState() == null ? 0 : appendDto.getTerminalState());
        positionBean.setLoadSensorIsExist(appendDto.isLoadSensorIsExist());
        positionBean.setLoadSensorValue(appendDto.getLoadSensorValue());
        positionBean.setAcc(appendDto.getAcc() == null ? 0 : appendDto.getAcc());
        positionBean.setOilMass(appendDto.getOilMass() == null ? 0 : appendDto.getOilMass());
        positionBean.setPower("100");
        positionBean.setAntiDismantle("0");
        try {
            positionBean.setLatitude(appendDto.getLatitude() == null ? 0.00 : appendDto.getLatitude() * 0.000001);
        } catch (Exception e) {
            positionBean.setLatitude(0.00);
        }
        try {
            positionBean.setLongitude(appendDto.getLongitude() == null ? 0.00 : appendDto.getLongitude() * 0.000001);
        } catch (Exception e) {
            positionBean.setLongitude(0.00);
        }
        positionBean.setElevation(appendDto.getElevation() == null ? 0 : appendDto.getElevation());
        positionBean.setSpeed(appendDto.getSpeed() == null ? 0 : appendDto.getSpeed());
        positionBean.setDirection(appendDto.getDirection() == null ? 0 : appendDto.getDirection());
        if (!StringUtils.isEmpty(appendDto.getDate())){
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime ldt = LocalDateTime.parse("20" + appendDto.getDate(),dtf);
                positionBean.setDate(ldt);
            } catch (Exception e){
                positionBean.setDate(LocalDateTime.now());
            }
        }
        positionBean.setMileage(appendDto.getMileage() == null ? 0 : appendDto.getMileage());
        locationBean.setPoint(positionBean);
        if (!ObjectUtils.isEmpty(elecDispatchInfo)){
            BusinessBean businessBean = new BusinessBean();
            BeanUtils.copyProperties(elecDispatchInfo,businessBean);
            businessBean.setStatus(String.valueOf(elecDispatchInfo.getStatus()));
            locationBean.setEleDispatch(businessBean);
        }
        return locationBean;
    }
}
