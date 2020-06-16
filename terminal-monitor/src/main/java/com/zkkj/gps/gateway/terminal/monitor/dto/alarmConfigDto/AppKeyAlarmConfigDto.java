package com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class AppKeyAlarmConfigDto implements Serializable {


    public AppKeyAlarmConfigDto(String appKey, AlarmConfigDto alarmConfig) {
        this.appKey = appKey;
        this.alarmConfig = alarmConfig;
    }

    public AppKeyAlarmConfigDto() {
    }

    private AlarmConfigDto alarmConfig;

    private String appKey;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppKeyAlarmConfigDto that = (AppKeyAlarmConfigDto) o;
        return alarmConfig.getDispatchNo() != null ? Objects.equals(alarmConfig.getCustomAlarmConfigId(), that.alarmConfig.getCustomAlarmConfigId()) &&
                Objects.equals(appKey, that.appKey) && Objects.equals(alarmConfig.getDispatchNo(), that.alarmConfig.getDispatchNo()) : Objects.equals(alarmConfig.getCustomAlarmConfigId(), that.alarmConfig.getCustomAlarmConfigId()) &&
                Objects.equals(appKey, that.appKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(alarmConfig, appKey);
    }
}
