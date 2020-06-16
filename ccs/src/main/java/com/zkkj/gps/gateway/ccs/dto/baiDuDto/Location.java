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
@ApiModel("关于百度坐标对象")
public class Location implements Serializable {

    @ApiModelProperty(name = "lat", value = "纬度值")
    private float lat;

    @ApiModelProperty(name = "lng", value = "经度值")
    private float lng;

}
