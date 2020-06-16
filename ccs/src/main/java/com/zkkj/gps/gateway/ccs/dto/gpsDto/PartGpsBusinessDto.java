package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * author : cyc
 * Date : 2019-07-04
 * 设置部分电子运单模型
 */
@Data
@ApiModel(value = "设置部分电子运单模型", description = "设置部分电子运单模型")
public class PartGpsBusinessDto {

    @NotBlank(message = "设备终端id不能为空")
    @ApiModelProperty(name = "terminalId", value = "设备终端id")
    private String terminalId;

    @NotBlank(message = "运单状态不能为空")
    @ApiModelProperty(name = "status", value = "任务状态字")
    private String status;

    @NotBlank(message = "扣吨量不能为空")
    @ApiModelProperty(value = "扣吨量", name = "deductWeight")
    private String deductWeight;

    @NotBlank(message = "扣吨原因不能为空")
    @ApiModelProperty(value = "扣吨原因", name = "deductReason")
    private String deductReason;

}
