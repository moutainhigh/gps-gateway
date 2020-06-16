package com.zkkj.gps.gateway.terminal.monitor.algorithm;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;

public class TerminalAlarmAlgorithm {


    public static TerminalAlarmInfoDto terminalAlarm(String terminalId, QueueList<BasicPositionDto> hisListPosition, BasicPositionDto gpsPositionDTO, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
        AlarmTypeEnum alarmType = alarmConfig.getAlarmTypeEnum();
        //违规区域报警
        if (alarmType == AlarmTypeEnum.VIOLATION_AREA) {
            return AreaTerminalAlarmAlgorithm.terminalAlarmInArea(terminalId, hisListPosition, appKeyAlarmConfig);
        }

        //停车超时报警
        if (alarmType == AlarmTypeEnum.STOP_OVER_TIME) {
            return StopTerminalAlarmAlgorithm.terminalAlarmStopOverTime(terminalId, hisListPosition, appKeyAlarmConfig);
        }

        //区域超速报警
        if (alarmType == AlarmTypeEnum.OVER_SPEED) {
            return OverSpeedTerminalAlarmAlgorithm.terminalAlarmOverSpeed(terminalId, hisListPosition, appKeyAlarmConfig);
        }

        //车辆载重报警
        if (alarmType == AlarmTypeEnum.VEHICLE_LOAD) {
            return VehicleLoadTerminalAlarmAlgorithm.terminalAlarmVehicleLoad(terminalId, hisListPosition, appKeyAlarmConfig);
        }

        //线路偏移报警
        if (alarmType == AlarmTypeEnum.LINE_OFFSET) {
            return RouteTerminalAlarmAlgorithm.terminalAlarmRoute(terminalId, hisListPosition, appKeyAlarmConfig);
        }
        //低电量报警
        if (alarmType == AlarmTypeEnum.LOW_POWER) {
            return LowPowerTerminalAlarmAlgorithm.terminalAlarmLowPower(terminalId, gpsPositionDTO, appKeyAlarmConfig);
        }
        //防拆报警
        if (alarmType == AlarmTypeEnum.EQUIP_REMOVE) {
            return TamperTerminalAlarmAlgorithm.terminalAlarmTamper(terminalId, gpsPositionDTO, appKeyAlarmConfig);
        }
        return null;

    }


}
