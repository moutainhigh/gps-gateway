package com.zkkj.gps.gateway.ccs.dto.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/9/26
 */
@Data
@ApiModel(value = "修改运单模型基类", description = "修改运单模型基类")
public class BaseUpdateDispatchInfoDto implements Serializable {

    @NotBlank(message = "当前最新设备编号不能为空")
    @ApiModelProperty(value = "当前最新设备编号", name = "terminalNo")
    private String terminalNo;

    @NotBlank(message = "运单编号不能为空")
    @ApiModelProperty(value = "运单编号", name = "dispatchNo")
    private String dispatchNo;

    @ApiModelProperty(value = "appKey", name = "appKey")
    private String appKey;

}
