package com.zkkj.gps.gateway.ccs.dto.baiDuDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/11/13
 */

@Data
@ApiModel("关于百度返回结果")
public class Result implements Serializable {

    @ApiModelProperty(name = "location",value = "经纬度坐标对象")
    private Location location;

    @ApiModelProperty(name = "precise",value = "位置的附加信息，是否精确查找。1为精确查找，即准确打点；0为不精确，即模糊打点")
    private int precise;

    @ApiModelProperty(name = "confidence",value = "描述打点绝对精度（即坐标点的误差范围）")
    private int confidence;

    @ApiModelProperty(name = "comprehension",value = "描述地址理解程度。分值范围0-100，分值越大，服务对地址理解程度越高")
    private int comprehension;

    @ApiModelProperty(name = "level",value = "能精确理解的地址类型，包含：UNKNOWN、国家、省、城市、区县、乡镇等等")
    private String level;
}
