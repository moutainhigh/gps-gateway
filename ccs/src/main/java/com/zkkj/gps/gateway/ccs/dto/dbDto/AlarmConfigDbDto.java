package com.zkkj.gps.gateway.ccs.dto.dbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019-05-13
 */
@ApiModel(value = "报警配置持久化模型", description = "报警配置持久化模型")
@Data
public class AlarmConfigDbDto implements Serializable{

    @ApiModelProperty(name = "id", value = "唯一索引")
    private String id;

    @ApiModelProperty(name = "appKey", value = "第三方应用key")
    private String appKey;

    @ApiModelProperty(name = "customConfigId", value = "外部传来的报警配置id")
    private String customConfigId;

    @ApiModelProperty(name = "alarmType", value = "报警类型，掉线:1;超速:2,停车超时:4,进入区域:8,离开区域:16,设备拆除:32,线路偏移::64,低电量报警:128,其他设备异常:2048")
    private int alarmType;

    @ApiModelProperty(name = "configValue", value = "报警配置值")
    private double configValue;

    @ApiModelProperty(name = "startTime", value = "开始时间")
    private String startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间")
    private String endTime;

    @ApiModelProperty(name = "identity", value = "公司唯一标识")
    private String identity;

    @ApiModelProperty(name = "corpName", value = "公司名称")
    private String corpName;

    @ApiModelProperty(name = "terminalId", value = "终端编号")
    private String terminalId;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private String createTime;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private String createBy;

    @ApiModelProperty(name = "carNum", value = "车牌号")
    private String carNum;

    @ApiModelProperty(name = "taskOrder", value = "任务订单")
    private String taskOrder;

    @ApiModelProperty(name = "dispatchNo", value = "订单编号")
    private String dispatchNo;

    @ApiModelProperty(name = "isDeliveryArea", value = "是否在收发货区域是1不是0")
    private int isDeliveryArea;


}
