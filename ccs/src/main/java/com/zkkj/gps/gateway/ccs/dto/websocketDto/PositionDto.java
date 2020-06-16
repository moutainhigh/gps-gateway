package com.zkkj.gps.gateway.ccs.dto.websocketDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/8/20
 */

@Data
@ApiModel(value = "坐标点模型", description = "坐标点模型")
public class PositionDto implements Serializable {

    @ApiModelProperty(name = "longitude", value = "经度")
    private double longitude;

    @ApiModelProperty(name = "latitude", value = "纬度")
    private double latitude;

    public PositionDto(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public PositionDto() {
    }
}
