package com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PositionInfo {
    @ApiModelProperty(name = "country", value = "县名 例如：府谷县")
    private String country;
    @ApiModelProperty(name = "vco", value = "车牌颜色1.蓝色2.黄色")
    private String vco;
    @ApiModelProperty(name = "utc", value = "车辆定位时间")
    private String utc;
    @ApiModelProperty(name = "city", value = "城市 例如：榆林市")
    private String city;
    @ApiModelProperty(name = "spd", value = "速度 字符串类型单位 km/h ")
    private String spd;
    @ApiModelProperty(name = "lon", value = "车辆定位经度  字符串类型单位：1/600000.0 ")
    private String lon;
    @ApiModelProperty(name = "adr", value = "车辆地理位置名称")
    private String adr;
    @ApiModelProperty(name = "mil", value = "里程")
    private String mil;
    @ApiModelProperty(name = "province", value = "省名 例如：陕西省")
    private String province;
    @ApiModelProperty(name = "drc", value = "方向 字符串类型（0 或 360：正北,大于 0 且小于 90：东北，等于 90：正东，大于 90 且小\n" +
            "于 180：东南，等于 180：正南，大于 180 且小于 270：14 西南，等于 270：正西，大于 270 且小于等于 359：西北，其他：未知） ")
    private String drc;
    @ApiModelProperty(name = "vno", value = "车牌号")
    private String vno;
    @ApiModelProperty(name = "state", value = "状态")
    private int state;
    @ApiModelProperty(name = "lat", value = "车辆定位纬度 字符串类型单位：1/600000.0 ")
    private String lat;
}
