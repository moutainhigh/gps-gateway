package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * author : cyc
 * Date : 2019-06-14
 */

@Data
@ApiModel(value = "终端区域模型", description = "终端区域模型")
public class TerminalAreaDto {

    @ApiModelProperty(name = "terminalId", value = "终端id,如果传多个设备用,拼接起来例如：35001211，9846454,8764646")
    @NotBlank(message = "终端编号不能为空")
//    @Length(max = 16, message = "终端编号不能超过16位")
    private String terminalId;

    @ApiModelProperty(name = "area", value = "区域模型")
    @NotNull(message = "传入区域对象不能为空")
    @Valid
    private InAreaDto area;
}
