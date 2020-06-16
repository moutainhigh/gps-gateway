package com.zkkj.gps.gateway.ccs.dto.dbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019-05-13
 */
@ApiModel(value = "报警信息持久化模型", description = "报警信息持久化模型")
@Data
public class AlarmInfoDbDto implements Serializable {

    @ApiModelProperty(name = "id", value = "主键")
    private String id;

    @ApiModelProperty(name = "alarmGroupId", value = "报警分组id")
    private String alarmGroupId;

    @ApiModelProperty(name = "resType", value = "报警响应类型，开始报警，结束报警,0:开始,1:结束")
    private int resType;

    @ApiModelProperty(name = "identity", value = "报警所属公司ID")
    private String identity;

    @ApiModelProperty(name = "corpName", value = "报警公司名称")
    private String corpName;

    @ApiModelProperty(name = "terminalId", value = "报警终端id")
    private String terminalId;

    @ApiModelProperty(name = "carNum", value = "车牌号")
    private String carNum;

    @ApiModelProperty(name = "alarmTime", value = "考虑到实时性，检测到开始即产生报警，检测到结束再次产生一条报警信息")
    private String alarmTime;

    @ApiModelProperty(name = "longitude", value = "经度")
    private double longitude;

    @ApiModelProperty(name = "latitude", value = "纬度")
    private double latitude;

    @ApiModelProperty(name = "alarmInfo", value = "报警内容信息")
    private String alarmInfo;

    @ApiModelProperty(name = "alarmType", value = "掉线:1;超速:2,停车超时:4,进入区域:8,离开区域:16,设备拆除:32,线路偏移::64,低电量报警:128,其他设备异常:2048")
    private int alarmType;

    @ApiModelProperty(name = "alarmType", value = "备注信息")
    private String remark;

    @ApiModelProperty(name = "areaId", value = "区域ID，0表示无区域")
    private String areaId;

    @ApiModelProperty(name = "alarmValue", value = "报警结束有效，报警值，如：超时为超时时间，掉线时间，最大超速速度，线路偏移最大距离（米）等")
    private double alarmValue;

    @ApiModelProperty(name = "alarmConfigId", value = "报警配置id")
    private String alarmConfigId;

    @ApiModelProperty(name = "configValue", value = "报警配置限定值")
    private double configValue;

    @ApiModelProperty(name = "alarmCreateTime", value = "报警创建时间")
    private String alarmCreateTime;

    @ApiModelProperty(name = "appKey", value = "第三方,数据来源")
    private String appKey;

    @ApiModelProperty(name = "alarmType", value = "创建时间")
    private String createTime;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private String createBy;

    @ApiModelProperty(name = "dispatchNo", value = "订单编号")
    private String dispatchNo;

    @ApiModelProperty(name = "isDeliveryArea", value = "是否在收发货区域是1不是0")
    private int isDeliveryArea;

}
