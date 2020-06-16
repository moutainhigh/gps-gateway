package com.zkkj.gps.gateway.ccs.dto.area;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupAreaUpdateDto {
    @ApiModelProperty(value = "区域id", name = "id")
    private String id;
    @ApiModelProperty(value = "区域名称", name = "areaName")
    private String areaName;
    @ApiModelProperty(value = "区域描述", name = "description")
    private String description;
    @ApiModelProperty(value = "区域类型,1.收货区域，2.发货区域 3 未指定", name = "areaType")
    private Integer areaType;
    @ApiModelProperty(value = "（1：圆，2：矩形，3：多边形）", name = "graphType")
    private Integer graphType;
    @ApiModelProperty(value = "区域中心纬度（地球坐标）", name = "centerLat")
    private BigDecimal centerLat;
    @ApiModelProperty(value = "区域中心经度（地球坐标）", name = "centerLng")
    private BigDecimal centerLng;
    @ApiModelProperty(value = "半径", name = "radius")
    private BigDecimal radius;
    @ApiModelProperty(value = "中心点位置", name = "centerAddress")
    private String centerAddress;
    @ApiModelProperty(value = "区域经纬度点 ：多个点考虑用json二维数组记录(1点顺序2,经度,3维度)", name = "areaPoints")
    private String areaPoints;
    @ApiModelProperty(value = "创建人（从token中获取前端不用传入）", name = "createUser")
    private String createUser;
    @ApiModelProperty(value = "分组唯一识别码（平台传*，平台下子用户传相应identity）", name = "identity")
    public String identity;

}