package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.GraphTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * author : cyc
 * Date : 2019-07-04
 */
@Data
@ApiModel(value = "入参区域模型", description = "入参区域模型")
public class InAreaDto {

    @NotNull(message = "区域类型不能为空，CIRCLE代表圆，POLYGON代表多边形")
    @ApiModelProperty(name = "graphTypeEnum", value = "字符串枚举类型，CIRCLE(1, \"圆\"), POLYGON(2, \"多边形\")")
    private GraphTypeEnum graphTypeEnum;

    @ApiModelProperty(name = "areaName", value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "address", value = "区域详细地址")
    private String address;

    @NotNull(message = "中心点,经度不能为空")
    @ApiModelProperty(name = "centerLng", value = "中心点，经度，如果区域类型是圆的话必填")
    private Double centerLng;

    @NotNull(message = "中心点，纬度不能为空")
    @ApiModelProperty(name = "centerLat", value = "中心点，纬度，如果区域类型是圆的话必填")
    private Double centerLat;

    @ApiModelProperty(name = "radius", value = "半径，如果区域类型是圆的话必填")
    private Double radius;

    @ApiModelProperty(name = "areaPoints", value = "多边形区域点，如果区域类型为多边形的话必填")
    private InAreaPointsDto[] areaPoints;
}
