package com.zkkj.gps.gateway.ccs.dto.route;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("修改线路实体类")
public class GroupRouteUpdateDto {
    @ApiModelProperty(value = "区域id", name = "id")
    private String id;
    @ApiModelProperty(value = "线路名称", name = "routeName")
    private String routeName;
    @ApiModelProperty(value = "线路宽度", name = "routeWidth")
    private BigDecimal routeWidth;
    @ApiModelProperty(value = "里程", name = "mileage")
    private BigDecimal mileage;
    @ApiModelProperty(value = "线路颜色", name = "routeColor")
    private String routeColor;
    @ApiModelProperty(value = "点序列", name = "pointSequence")
    private String pointSequence;
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    @ApiModelProperty(value = "创建人（从token中获取前端不用传入）", name = "createUser")
    private String createUser;
    @ApiModelProperty(value = "分组唯一识别码（平台传*，平台下子用户传相应identity）", name = "identity")
    public String identity;
}