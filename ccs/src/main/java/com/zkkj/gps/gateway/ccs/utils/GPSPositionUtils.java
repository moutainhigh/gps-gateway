package com.zkkj.gps.gateway.ccs.utils;

import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;

/**
 * author : cyc
 * Date : 2020/5/7
 */
public class GPSPositionUtils {


    /**
     * 将BaseGPSPositionDto转化成GPSPositionDto对象
     * @param baseGPSPositionDto
     * @return
     */
    public static GPSPositionDto getGPSPositionDto(BaseGPSPositionDto baseGPSPositionDto) {
        GPSPositionDto gpsPositionDto = null;
        if (baseGPSPositionDto == null || baseGPSPositionDto.getPoint() == null) {
            return gpsPositionDto;
        }
        BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
        GPSPositionDto position = new GPSPositionDto();
        position.setAcc(basicPositionDto.getAcc());
        position.setAlarmState(basicPositionDto.getAlarmState() == null ? 0 : basicPositionDto.getAlarmState());
        position.setCourse(basicPositionDto.getDirection() == null ? 0 : basicPositionDto.getDirection());
        position.setElevation(basicPositionDto.getElevation() == null ? 0 : basicPositionDto.getElevation());
        position.setLatitude(basicPositionDto.getLatitude() == null ? 0.0 : basicPositionDto.getLatitude());
        position.setLongitude(basicPositionDto.getLongitude() == null ? 0.0 : basicPositionDto.getLongitude());
        try {
            position.setLoad(basicPositionDto.getLoad() == null ? 0.0 : Double.valueOf(basicPositionDto.getLoad()));
        } catch (Exception e) {
            position.setLoad(0.0);
        }
        position.setOilMass(basicPositionDto.getOilMass() == null ? 0 : basicPositionDto.getOilMass());
        position.setTerminalState(basicPositionDto.getTerminalState() == null ? 0 : basicPositionDto.getTerminalState());
        position.setPower(basicPositionDto.getPower());
        position.setGpsTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
        position.setRecTime(DateTimeUtils.formatLocalDateTime(baseGPSPositionDto.getRcvTime()));
        position.setMilesKM(basicPositionDto.getMileage() == null ? 0.0 : basicPositionDto.getMileage());
        position.setSpeed(basicPositionDto.getSpeed() == null ? 0.0 : basicPositionDto.getSpeed());
        position.setSimId(baseGPSPositionDto.getTerminalId());
        return position;
    }
}
