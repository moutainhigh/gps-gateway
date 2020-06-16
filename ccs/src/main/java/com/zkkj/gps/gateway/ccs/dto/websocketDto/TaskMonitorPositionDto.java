package com.zkkj.gps.gateway.ccs.dto.websocketDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * author : cyc
 * Date : 2020/5/10
 */
@Data
@ApiModel(value = "webSocket任务监控定位对象", description = "webSocket任务监控定位对象")
public class TaskMonitorPositionDto extends GPSPositionDto {

    @ApiModelProperty(name = "dispatchNo", value = "运单编号")
    private String dispatchNo;

    @ApiModelProperty(name = "appkey", value = "对应用户appkey")
    private String appkey;

    @ApiModelProperty(value = "发货公司名称", name = "consignerCorpName")
    private String consignerCorpName;

    @ApiModelProperty(value = "承运公司名称", name = "shipperCorpName")
    private String shipperCorpName;

    @ApiModelProperty(value = "收货公司名称", name = "receiverCorpName")
    private String receiverCorpName;

    @ApiModelProperty(value = "公司唯一标识，纳税号，编码等", name = "identity")
    private String identity;

    @ApiModelProperty(value = "创建时间", name = "createTime")
    private String createTime;

}
