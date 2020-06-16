package com.zkkj.gps.gateway.ccs.dto.warn;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/12/19
 */

@Data
@ApiModel(value = "量的提醒模型")
public class AmountWarnDto implements Serializable {

    @ApiModelProperty(value = "派单编号", name = "disPatchNo")
    private String disPatchNo;

    @ApiModelProperty(value = "车牌", name = "carNum")
    private String carNum;

    @ApiModelProperty(value = "任务状态字", name = "status")
    private int status;

    @ApiModelProperty(name = "sendTareWeight", value = "发货皮重")
    private Double sendTareWeight;

    @ApiModelProperty(name = "rcvGrossWeight", value = "收货量，毛重")
    private Double rcvGrossWeight;

    @ApiModelProperty(value = "发货单位", name = "consignerName")
    private String consignerName;

    @ApiModelProperty(value = "收货单位", name = "receiverName")
    private String receiverName;
}
