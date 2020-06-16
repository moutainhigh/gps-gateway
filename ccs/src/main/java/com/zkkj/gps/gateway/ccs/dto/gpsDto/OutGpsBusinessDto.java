package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * author : cyc
 * Date : 2019-06-19
 */
@Data
@ApiModel(value = "向外暴露下发电子运单模型", description = "向外暴露下发电子运单模型")
public class OutGpsBusinessDto {

    @NotBlank(message = "设备终端id不能为空")
    @ApiModelProperty(name = "terminalId", value = "设备终端id")
    private String terminalId;

    @NotNull(message = "电子运单不能为空")
    @Valid
    @ApiModelProperty(name = "gpsBusinessDto", value = "电子运单模型")
    private GpsBusinessDto gpsBusinessDto;
}
