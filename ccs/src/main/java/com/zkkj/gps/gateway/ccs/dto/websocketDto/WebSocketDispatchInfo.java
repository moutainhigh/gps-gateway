package com.zkkj.gps.gateway.ccs.dto.websocketDto;

import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2020/5/11
 */

@Data
@ApiModel(value = "针对webSocket推送的运单信息模型", description = "针对webSocket推送的运单信息模型")
public class WebSocketDispatchInfo implements Serializable {

    @ApiModelProperty(name = "运单推送类型", value = "ORDINARY:正常;ADDITIONAL:新增;REMOVED:移除")
    private String webSocketDispatchType;

    @ApiModelProperty(name = "dispatchInfoDto", value = "运单信息")
    private DispatchInfoDto dispatchInfoDto;
}
