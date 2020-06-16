package com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 多个车辆设备的多个报警配置，此对象中所有设备车辆都将对所有的报警配置进行监控
 */
public class CarAlarmConfigDto implements Serializable {

    /**
     * 多个车辆设备信息
     */
    @NotEmpty(message = "车辆终端信息不能为空")
    @Valid
    private List<CarNumTerminalIdDto> cars;

    /**
     * 报警配置信息
     */
//    @NotEmpty(message = "报警配置信息不能为空")
//    @Valid
    private List<AlarmConfigDto> alarmConfigs;

    public List<CarNumTerminalIdDto> getCars() {
        return cars;
    }

    public void setCars(List<CarNumTerminalIdDto> cars) {
        this.cars = cars;
    }

    public List<AlarmConfigDto> getAlarmConfig() {
        return alarmConfigs;
    }

    public void setAlarmConfig(List<AlarmConfigDto> alarmConfig) {
        this.alarmConfigs = alarmConfig;
    }

    public CarAlarmConfigDto() {
    }

    public CarAlarmConfigDto(List<CarNumTerminalIdDto> cars, List<AlarmConfigDto> alarmConfigs) {

        this.cars = cars;
        this.alarmConfigs = alarmConfigs;
    }
}
