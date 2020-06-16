package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * author : cyc
 * Date : 2019-06-19
 */

@Data
@ApiModel(value = "向外暴露终端参数信息模型", description = "向外暴露终端参数信息模型")
public class OutTerminalParamsInfoDto {

    @NotBlank(message = "设备终端id不能为空")
    @ApiModelProperty(name = "terminalId", value = "设备终端id")
    private String terminalId;

    @NotNull(message = "终端参数不能为空")
    @ApiModelProperty(name = "terminalParamsInfoDto", value = "终端参数信息模型")
    private TerminalParamsInfoDto terminalParamsInfoDto;
}
