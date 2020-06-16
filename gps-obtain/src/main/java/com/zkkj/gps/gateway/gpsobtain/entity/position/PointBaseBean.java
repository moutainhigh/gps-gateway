package com.zkkj.gps.gateway.gpsobtain.entity.position;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 点位基础模型
 * @author suibozhuliu
 */
@Data
@ToString(callSuper = true)
public class PointBaseBean extends BaseBean{

    @ApiModelProperty(name = "rcvTime", value = "系统时间接收到点位时间")
    private LocalDateTime rcvTime;

    @ApiModelProperty(name = "gpsTime", value = "点位时间")
    private LocalDateTime gpsTime;

    @ApiModelProperty(name = "direction", value = "方向(0-359°,正北为0,顺时针) ")
    private Integer direction;

    @ApiModelProperty(name = "elevation", value = "海拔")
    private Integer elevation;

    @ApiModelProperty(name = "latitude", value = "纬度")
    private Double latitude;

    @ApiModelProperty(name = "longitude", value = "经度")
    private Double longitude;

    @ApiModelProperty(name = "speed", value = "速度（百米/h）")
    private Double speed;

    @ApiModelProperty(name = "mileage", value = "里程（Km）")
    private Long mileage;

}
