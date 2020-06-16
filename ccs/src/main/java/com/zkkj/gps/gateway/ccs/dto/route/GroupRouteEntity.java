package com.zkkj.gps.gateway.ccs.dto.route;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("线路新增实体类")
public class GroupRouteEntity implements Serializable {
    @ApiModelProperty(value = "线路id", name = "id")
    private String id;
    @ApiModelProperty(value = "线路名称", name = "routeName")
    private String routeName;
    @ApiModelProperty(value = "线路宽度", name = "routeWidth")
    private BigDecimal routeWidth;
    @ApiModelProperty(value = "里程", name = "mileage")
    private BigDecimal mileage;
    @ApiModelProperty(value = "线路颜色", name = "routeColor")
    private String routeColor;
    @ApiModelProperty(value = "线路点序列", name = "pointSequence")
    private String pointSequence;
    @ApiModelProperty(value = "持续时间", name = "duration")
    private String duration;
    @ApiModelProperty(value = "策略：0.速度优先（时间）1.费用优先（不走收费路段的最快道路）2.距离优先3.不走快速路 4.躲避拥堵5.多策略（同时", name = "strategy")
    private int strategy;
    @ApiModelProperty(value = "百度地图比例尺值", name = "scaleBaidu")
    private int scaleBaidu;
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    @ApiModelProperty(value = "创建人", name = "createUser")
    private String createUser;
    @ApiModelProperty(value = "分组唯一识别码（平台传*，平台下子用户传相应的identity）", name = "identity")
    private String identity;
}