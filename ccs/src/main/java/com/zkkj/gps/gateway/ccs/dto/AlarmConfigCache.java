package com.zkkj.gps.gateway.ccs.dto;

import java.io.Serializable;
import java.util.List;

import com.zkkj.gps.gateway.ccs.dto.dbDto.AreaDbDto;
import com.zkkj.gps.gateway.ccs.dto.dbDto.RouteDbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * author : cyc
 * Date : 2019-05-14
 */

@Data
@ApiModel(value = "报警配置模型", description = "报警配置模型")
public class AlarmConfigCache implements Serializable {

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

    @ApiModelProperty(name = "carNum", value = "车牌号")
    private String carNum;

    @ApiModelProperty(name = "dispatchNo", value = "订单编号")
    private String dispatchNo;

    @ApiModelProperty(name = "areaDbDto", value = "区域点对象")
    private AreaDbDto areaDbDto;

    @ApiModelProperty(name = "routeList", value = "多条路线对象")
    private List<RouteDbDto> routeList;

    @ApiModelProperty(name = "isDeliveryArea", value = "是否在收发货区域是1不是0")
    private int isDeliveryArea;
}
