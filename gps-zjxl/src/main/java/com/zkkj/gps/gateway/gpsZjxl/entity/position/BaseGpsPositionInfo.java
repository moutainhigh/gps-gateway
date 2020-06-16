package com.zkkj.gps.gateway.gpsZjxl.entity.position;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author : cyc
 * Date : 2019/11/20
 */
@Data
@ApiModel("gps定位信息基本模型,包含手机，终端等设备公共参数")
public class BaseGpsPositionInfo implements Serializable {

    @ApiModelProperty(name = "rcvTime", value = "系统时间接收到点位时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime rcvTime;

    @ApiModelProperty(name = "gpsTime", value = "点位时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gpsTime;

    @ApiModelProperty(name = "direction", value = "方向(0-359°,正北为0,顺时针) ")
    private Integer direction;

    @ApiModelProperty(name = "elevation", value = "海拔")
    private Integer elevation;

    @ApiModelProperty(name = "latitude", value = "wgs84纬度")
    private Double latitude;

    @ApiModelProperty(name = "longitude", value = "wgs84经度")
    private Double longitude;

    @ApiModelProperty(name = "speed", value = "速度（百米/h）")
    private Double speed;

    @ApiModelProperty(value = "里程数（单位：Km）,", name = "mileage")
    private Double mileage;


}
