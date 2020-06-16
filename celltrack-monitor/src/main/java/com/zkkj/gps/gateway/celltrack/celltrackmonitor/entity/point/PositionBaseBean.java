package com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.point;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 点位信息基类模型
 * @author suibozhuliu
 */
@Data
public class PositionBaseBean {

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度", name = "latitude")
    private double latitude;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度", name = "longitude")
    private double longitude;
    /**
     * 海拔(m)
     */
    @ApiModelProperty(value = "海拔（m）", name = "elevation")
    private int elevation;
    /**
     * 速度
     */
    @ApiModelProperty(value = "速度", name = "speed")
    private int speed;
    /**
     * 方向(0-359°,正北为0,顺时针)
     */
    @ApiModelProperty(value = "方向", name = "direction")
    private int direction;
    /**
     * 定位时间 GMT+8时间,本标准之后涉及的时间均采用此时区
     */
    @ApiModelProperty(value = "定位时间", name = "date")
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    /**
     * 里程数 1-10km
     */
    @ApiModelProperty(value = "里程数", name = "mileage")
    private long mileage;

}

