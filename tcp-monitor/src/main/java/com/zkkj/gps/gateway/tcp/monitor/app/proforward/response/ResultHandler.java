package com.zkkj.gps.gateway.tcp.monitor.app.proforward.response;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.component.messagebody.BatchSiteMessage;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.SiteAppendMessage;
import com.zkkj.gps.gateway.protocol.component.messagebody.basic.SiteBasicMessage;
import com.zkkj.gps.gateway.protocol.destination.P_0200;
import com.zkkj.gps.gateway.protocol.destination.P_0210;
import com.zkkj.gps.gateway.protocol.dto.GPSPositionDTO;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.LocationBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.PositionDto;
import com.zkkj.gps.gateway.tcp.monitor.app.service.impl.PositionServiceImpl;
import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * 终端上传后数据处理
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-18 下午 3:46
 */
@Component
@Slf4j
public class ResultHandler implements ResponseInterface {

    @Autowired
    private PositionServiceImpl positionService;

    @Override
    public void positionChange(DestinationBaseCompose compose, String terminalId, String msgIdStr) {
        try {
            GPSPositionDTO gpsPositionDTO ;
            SiteBasicMessage siteBasicMessage = null;
            SiteAppendMessage siteAppendMessage = null;
            //LocalDateTime localDateTime = null;
            switch (msgIdStr){
                case "0200":
                    P_0200 p_0200 = (P_0200) compose;
                    if (p_0200 == null) return;
                    if (p_0200.getSiteBasicMessage() != null){
                        siteBasicMessage = p_0200.getSiteBasicMessage();
                        // TODO: 2019-05-21
                        /*if (p_0200.getSiteBasicMessage().getDate() != null){
                            localDateTime = p_0200.getSiteBasicMessage().getDate();
                        }*/
                    }
                    if (p_0200.getSiteAppendMessage() != null){
                        siteAppendMessage = p_0200.getSiteAppendMessage();
                    }

                    gpsPositionDTO = new GPSPositionDTO(siteBasicMessage,siteAppendMessage,LocalDateTime.now());
                    LocationBean locationBeanBatch = transformPoint(terminalId, gpsPositionDTO);
                    positionService.positionChange(locationBeanBatch);
                    break;
                case "0210":
                    P_0210 p_0210 = (P_0210) compose;
                    if (p_0210 == null) return;
                    if (p_0210.getSiteMessageList() != null && p_0210.getSiteMessageList().size() > 0){
                        for (BatchSiteMessage batchSiteMessage : p_0210.getSiteMessageList()) {
                            if (batchSiteMessage != null){
                                if (batchSiteMessage.getSiteBasicMessage() != null){
                                    siteBasicMessage = batchSiteMessage.getSiteBasicMessage();
                                    siteAppendMessage = batchSiteMessage.getSiteAppendMessage();
                                    //localDateTime = batchSiteMessage.getSiteBasicMessage().getDate();

                                    gpsPositionDTO = new GPSPositionDTO(siteBasicMessage,siteAppendMessage,LocalDateTime.now());
                                    LocationBean locationBean = transformPoint(terminalId, gpsPositionDTO);
                                    positionService.positionChange(locationBean);
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e){
            LoggerUtils.error(log,e.toString());
        }

    }

    /**
     * 点位信息转换（重构后）
     * @param terminalId
     * @param positionDTO
     * @return
     */
    private LocationBean transformPoint(String terminalId, GPSPositionDTO positionDTO){
        LocationBean location = new LocationBean();
        location.setFlag(1);
        location.setEleDispatch(null);
        location.setTerminalId(terminalId);
        PositionDto positionDto = new PositionDto();
        if (!ObjectUtils.isEmpty(positionDTO.getSiteBasicMessage())) {
            SiteBasicMessage siteBasicMessage = positionDTO.getSiteBasicMessage();
            //报警标志
            positionDto.setAlarmState(siteBasicMessage.getAlarmState());
            //终端状态
            positionDto.setTerminalState(siteBasicMessage.getTerminalState());
            //纬度
            positionDto.setLatitude(siteBasicMessage.getLatitude());
            //经度
            positionDto.setLongitude(siteBasicMessage.getLongitude());
            //海拔
            positionDto.setElevation(siteBasicMessage.getElevation());
            //速度
            positionDto.setSpeed(siteBasicMessage.getSpeed());
            //方向
            positionDto.setDirection(siteBasicMessage.getDirection());
            //定位时间
            positionDto.setDate(siteBasicMessage.getDate());
            //是否有载重传感器
            positionDto.setLoadSensorIsExist(siteBasicMessage.isLoadSensorIsExist());
            //载重量
            positionDto.setLoadSensorValue(siteBasicMessage.getLoadSensorValue());
            //ACC
            positionDto.setAcc(siteBasicMessage.getAcc());
        }
        if (!ObjectUtils.isEmpty(positionDTO.getSiteAppendMessage())) {
            SiteAppendMessage siteAppendMessage = positionDTO.getSiteAppendMessage();
            //里程数
            if (!ObjectUtils.isEmpty(siteAppendMessage.getAppend_01())) {
                positionDto.setMileage(siteAppendMessage.getAppend_01().getMileage());
            }
            //油量
            if (!ObjectUtils.isEmpty(siteAppendMessage.getAppend_02())) {
                positionDto.setOilMass(siteAppendMessage.getAppend_02().getOilMass());
            }
            //电量
            if (!ObjectUtils.isEmpty(siteAppendMessage.getAppend_33())) {
                positionDto.setPower(siteAppendMessage.getAppend_33().getPower());
                positionDto.setAntiDismantle(siteAppendMessage.getAppend_33().getAntiDismantle());
            }
            //电子运单
            if (!ObjectUtils.isEmpty(siteAppendMessage.getAppend_36()) && !ObjectUtils.isEmpty(siteAppendMessage.getAppend_36().getBusinessExtensionData())) {
                location.setEleDispatch(siteAppendMessage.getAppend_36().getBusinessExtensionData());
            }
        }
        location.setPoint(positionDto);
        return location;
    }

}
