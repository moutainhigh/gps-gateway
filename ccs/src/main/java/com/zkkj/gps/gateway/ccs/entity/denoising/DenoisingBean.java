package com.zkkj.gps.gateway.ccs.entity.denoising;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DenoisingBean {

    @ApiModelProperty(name = "longitude", value = "地球坐标经度")
    private Double longitude;
    @ApiModelProperty(name = "latitude", value = "地球坐标纬度")
    private Double latitude;
    @ApiModelProperty(name = "gpsTime", value = "gps定位时间")
    private String gpsTimeStr;
    @ApiModelProperty(name = "speedKm", value = "速度（km/h）")
    private Double speedKm;

}
