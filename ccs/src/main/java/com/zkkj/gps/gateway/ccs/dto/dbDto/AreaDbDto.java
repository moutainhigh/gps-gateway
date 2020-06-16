package com.zkkj.gps.gateway.ccs.dto.dbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019-05-13
 */
@Data
@ApiModel(value = "区域持久化模型", description = "区域持久化模型")
public class AreaDbDto implements Serializable {

    @ApiModelProperty(name = "id", value = "唯一性")
    private String id;

    @ApiModelProperty(name = "customAreaId", value = "外部传来的区域id")
    private String customAreaId;

    @ApiModelProperty(name = "appKey", value = "第三方应用key")
    private String appKey;

    @ApiModelProperty(name = "areaName", value = "'区域名'")
    private String areaName;

    @ApiModelProperty(name = "address", value = "区域详细地址")
    private String address;

    @ApiModelProperty(name = "centerLng", value = "中心点，经度")
    private double centerLng;

    @ApiModelProperty(name = "centerLat", value = "中心点，纬度")
    private double centerLat;

    @ApiModelProperty(name = "radius", value = "半径")
    private double radius;

    @ApiModelProperty(name = "areaPoints", value = "区域点集合，纬度,经度;23.45,130.12;23.45,130.12")
    private String areaPoints;

    @ApiModelProperty(name = "graphType", value = "区域点类型，1:圆,2:多边形,3:线")
    private int graphType;

    @ApiModelProperty(name = "alarmConfigId", value = "报警配置id")
    private String alarmConfigId;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private String createTime;

    @ApiModelProperty(name = "createBy", value = "'创建人'")
    private String createBy;

}
