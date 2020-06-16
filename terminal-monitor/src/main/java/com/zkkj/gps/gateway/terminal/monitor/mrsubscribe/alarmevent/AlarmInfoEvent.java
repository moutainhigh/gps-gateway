package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class AlarmInfoEvent implements Serializable {

    /**
     * 报警信息
     */
    private TerminalAlarmInfoDto alarmInfo;

    /**
     * 第三方key
     */
    private String appKey;

    public AlarmInfoEvent(String appKey, TerminalAlarmInfoDto alarmInfo) {
        this.alarmInfo = alarmInfo;
        this.appKey = appKey;
    }

    public AlarmInfoEvent() {
    }
}
