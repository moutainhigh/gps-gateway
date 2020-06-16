package com.zkkj.gps.gateway.ccs.dto.dbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019-05-16
 */

@Data
@ApiModel(value = "历史轨迹信息模型", description = "历史轨迹信息模型")
public class VehicleLocationHisDto implements Serializable {

    @ApiModelProperty(name = "terminalId", value = "终端id")
    private String terminalId;

    @ApiModelProperty(name = "alarmState", value = "报警状态")
    private Long alarmState;

    @ApiModelProperty(name = "terminalState", value = "终端状态")
    private Long terminalState;

    @ApiModelProperty(name = "longitude", value = "经度")
    private Double longitude;

    @ApiModelProperty(name = "latitude", value = "纬度")
    private Double latitude;

    @ApiModelProperty(name = "elevation", value = "海拔")
    private Integer elevation;

    @ApiModelProperty(name = "speed", value = "速度（1/10 km/h）即百米每小时")
    private Integer speed;

    @ApiModelProperty(name = "direction", value = "方向")
    private Integer direction;

    @ApiModelProperty(name = "gpsTime", value = "gps时间")
    private String gpsTime;

    @ApiModelProperty(name = "rcvTime", value = "'接收时间'")
    private String rcvTime;

    @ApiModelProperty(name = "gsmSignal", value = "gsm信号")
    private Integer gsmSignal;

    @ApiModelProperty(name = "gpsSignal", value = "gps信号")
    private Integer gpsSignal;

    @ApiModelProperty(name = "oilMass", value = "油量")
    private Integer oilMass;

    @ApiModelProperty(name = "mileage", value = "里程(1/10km)")
    private Long mileage;

    @ApiModelProperty(name = "carState", value = "车辆状态")
    private Long carState;

    @ApiModelProperty(name = "power", value = "电量")
    private Integer power;

    @ApiModelProperty(name = "pressure", value = "压力值")
    private Double pressure;

    @ApiModelProperty(name = "loadSensorValue", value = "载重百分比")
    private Double loadSensorValue;

    @ApiModelProperty(name = "antiDismantle", value = "防拆开关 1 0:正常 1:被拆触发")
    private Integer antiDismantle;

    @ApiModelProperty(name = "flag", value = "电子运单数据来源标识(0:手机端(电子运单不存在);1:航宏达;2:甲天行;3:自定义...)")
    private Integer flag;

    @ApiModelProperty(name = "eleDispatch", value = "额外业务(电子运单)的信息")
    private String eleDispatch;


    public VehicleLocationHisDto(String terminalId, Long alarmState, Long terminalState, Double longitude, Double latitude, Integer elevation, Integer speed, Integer direction, String gpsTime, String rcvTime, Integer gsmSignal, Integer gpsSignal, Integer oilMass, Long mileage, Long carState, Integer power, Double pressure, Double loadSensorValue, Integer antiDismantle) {
        this.terminalId = terminalId;
        this.alarmState = alarmState;
        this.terminalState = terminalState;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.speed = speed;
        this.direction = direction;
        this.gpsTime = gpsTime;
        this.rcvTime = rcvTime;
        this.gsmSignal = gsmSignal;
        this.gpsSignal = gpsSignal;
        this.oilMass = oilMass;
        this.mileage = mileage;
        this.carState = carState;
        this.power = power;
        this.pressure = pressure;
        this.loadSensorValue = loadSensorValue;
        this.antiDismantle = antiDismantle;
    }

    public VehicleLocationHisDto() {
    }
}
