package com.zkkj.gps.gateway.ccs.dto.baiDuDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2020/4/15
 */

@Data
@ApiModel(value = "逆地理结果模型", description = "逆地理结果模型")
public class ReverseResult implements Serializable {

    @ApiModelProperty(name = "location", value = "百度坐标对象")
    private Location location;

    @ApiModelProperty(name = "addressComponent", value = "地区行政规划对象")
    private AddressComponent addressComponent;

    @ApiModelProperty(name = "formattedAddress", value = "结构化地址信息")
    private String formattedAddress;

    @ApiModelProperty(name = "business", value = "坐标所在商圈信息，如 人民大学,中关村,苏州街。最多返回3个。")
    private String business;

}
