package com.zkkj.gps.gateway.ccs.dto.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/9/11
 */

@Data
@ApiModel(value = "根据运单编号更新运单配置模型", description = "根据运单编号更新运单配置模型")
public class UpdateDispatchInfoDto extends BaseUpdateDispatchInfoDto {

    @NotBlank(message = "需要修改的设备编号不能为空")
    @ApiModelProperty(value = "修改前的设备编号", name = "oldTerminalNo")
    private String oldTerminalNo;

}
