package com.zkkj.gps.gateway.ccs.entity.test;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * author : cyc
 * Date : 2020/3/16
 */
@Data
@ApiModel(value = "点位来源模型(针对测试)", description = "点位来源模型(针对测试)")
public class PointSource implements Serializable {

    @NotBlank(message = "设备编号不能为空")
    @ApiModelProperty(name = "terminalId", value = "设备编号")
    private String terminalId;

    @NotBlank(message = "模拟线路不能为空")
    @ApiModelProperty(name = "lineStrings", value = "路线，格式:纬度,经度;...")
    private String lineStrings;

    @ApiModelProperty(name = "type", value = "1:甲天行3:航鸿达")
    private int type;
}
