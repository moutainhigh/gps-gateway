package com.zkkj.gps.gateway.ccs.dto.websocketDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ApiModel("历史报警信息输入类")
public class HistoryAlarmInfoDto {

    @ApiModelProperty(name = "设备id", value = "terminalId")
    private String terminalId;

    @Min(value = 1,message = "页码最小从1开始")
    @ApiModelProperty(name = "页码", value = "pageIndex")
    private int pageIndex;

    @Min(value = 1, message = "每页个数最小从1开始")
    @ApiModelProperty(name = "个数", value = "pageSize")
    private int pageSize;

    @ApiModelProperty(name = "开始时间", value = "startTime")
    private String startTime;

    @ApiModelProperty(name = "结束时间", value = "endTime")
    private String endTime;

}
