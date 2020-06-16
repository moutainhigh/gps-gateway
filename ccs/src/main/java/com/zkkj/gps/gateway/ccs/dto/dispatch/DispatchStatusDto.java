package com.zkkj.gps.gateway.ccs.dto.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("运单状态信息")
public class DispatchStatusDto {
    @ApiModelProperty(value = "电子运单状态", name = "status")
    public int status;
    @ApiModelProperty(value = "通讯中心运单状态", name = "dispatchStatus")
    public int dispatchStatus;
    @ApiModelProperty(value = "事件状态", name = "eventType")
    public int eventType;
    @ApiModelProperty(value = "事件信息", name = "eventInfo")
    public String eventInfo;
    @ApiModelProperty(value = "区域名称", name = "areaName")
    public String areaName;
    @ApiModelProperty(value = "实收毛量", name = "rcvGrossWeight")
    public double rcvGrossWeight;
    @ApiModelProperty(value = "实收皮量", name = "rcvTareWeight")
    public double rcvTareWeight;
    @ApiModelProperty(value = "扣顿量", name = "deductWeight")
    public double deductWeight;

    public DispatchStatusDto(int status, int dispatchStatus, int eventType, String eventInfo, String areaName) {
        this.status = status;
        this.dispatchStatus = dispatchStatus;
        this.eventType = eventType;
        this.eventInfo = eventInfo;
        this.areaName = areaName;
    }

    public DispatchStatusDto() {

    }
}
