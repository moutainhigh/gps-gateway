package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * author : cyc
 * Date : 2019-07-04
 */
@Data
@ApiModel(value = "入参区域详细点模型", description = "入参区域详细点模型")
public class InAreaPointsDto {

    @ApiModelProperty(name = "sequence", value = "点位顺序")
    private int sequence;

    @ApiModelProperty(name = "lat", value = "纬度")
    private double lat;

    @ApiModelProperty(name = "lng", value = "经度")
    private double lng;
}
