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
@ApiModel(value = "路线持久化模型", description = "路线持久化模型")
public class RouteDbDto implements Serializable {

    @ApiModelProperty(name = "id", value = "唯一性")
    private String id;

    @ApiModelProperty(name = "customRouteId", value = "路线主键")
    private String customRouteId;

    @ApiModelProperty(name = "routeName", value = "路线名称")
    private String routeName;

    @ApiModelProperty(name = "radius", value = "路线宽度")
    private double width;

    @ApiModelProperty(name = "graphType", value = "区域点类型，1:圆,2:多边形,3:线")
    private int graphType;

    @ApiModelProperty(name = "pointSequence", value = "路线结合,纬度(lat),经度(lng);34.200753,108.920162;34.201081,108.921837")
    private String pointSequence;

    @ApiModelProperty(name = "alarmConfigId", value = "报警配置id")
    private String alarmConfigId;

    @ApiModelProperty(name = "createTime", value = "'创建时间'")
    private String createTime;

    @ApiModelProperty(name = "createBy", value = "'创建人'")
    private String createBy;

    @ApiModelProperty(name = "appKey", value = "第三方应用key")
    private String appKey;
}
