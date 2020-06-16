package com.zkkj.gps.gateway.ccs.entity.hisPosition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("历史轨迹查询模型")
public class HisGpsPointQuery {
    @ApiModelProperty(value = "设备号",name = "terminalId")
    private String terminalId;
    @ApiModelProperty(value = "开始时间",name = "startTime")
    private String startTime;
    @ApiModelProperty(value = "结束时间",name = "endTime")
    private String endTime;
}
