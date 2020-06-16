package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "获取内存中设备信息", description = "获取内存中设备信息")
public class TerminalInfoDto {

    @ApiModelProperty(value = "定位状态字符串", name = "positionStatusStr")
    private String positionStatusStr;

    @ApiModelProperty(value = "定位时间字符串", name = "positionStatusTimeStr")
    private String positionStatusTimeStr;

    @ApiModelProperty(value = "电量", name = "power")
    private String power;
}
