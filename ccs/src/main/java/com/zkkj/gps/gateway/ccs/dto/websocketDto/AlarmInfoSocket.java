package com.zkkj.gps.gateway.ccs.dto.websocketDto;


import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("通过socket推送的报警信息")
public class AlarmInfoSocket {

    @ApiModelProperty(name = "appkey", value = "当前用户appkey")
    private String appkey;

    @ApiModelProperty(name = "alarmGroupId", value = "报警分组id")
    private String alarmGroupId;

    @ApiModelProperty(name = "alarmResType", value = "报警响应类型，开始报警，结束报警")
    private AlarmResTypeEnum alarmResType;

    @ApiModelProperty(name = "identity", value = "报警所属群组唯一识别码")
    private String identity;

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

    @ApiModelProperty(name = "alarmType", value = "报警类型:OFF_LINE(1, \"掉线\"), OVER_SPEED(2, \"超速\"), STOP_OVER_TIME(4, \"停车超时\"), VIOLATION_AREA(8, \"违规区域\"), VEHICLE_LOAD(16, \"车辆载重\"),\n" +
            "EQUIP_REMOVE(32, \"设备拆除\"), LINE_OFFSET(64, \"线路偏移\"), LOW_POWER(128, \"低电量报警\"), EQUIP_EX(2048, \"其他设备异常\")")
    private AlarmTypeEnum alarmType;

    @ApiModelProperty(name = "remark", value = "备注信息")
    private String remark;

    @ApiModelProperty(name = "corpName", value = "公司名称")
    private String corpName;

    @ApiModelProperty(name = "alarmCreateTime", value = "报警创建时间")
    private String alarmCreateTime;

    @ApiModelProperty(name = "configValue", value = "报警配置值")
    private String configValue;

    @ApiModelProperty(name = "alarmValue", value = "报警值,如果是OFF_LINE，STOP_OVER_TIME，VIOLATION_AREA是时间，OVER_SPEED为平均速度，EQUIP_REMOVE是0(正常)和1(触发防拆)，LINE_OFFSET是偏移最大距离" +
            "LOW_POWER是电量百分比，VEHICLE_LOAD是车辆载重百分比")
    private String alarmValue;

    @ApiModelProperty(name = "dispatchNo", value = "订单编号")
    private String dispatchNo;

    /*private String getAlarmCreateTimeStr() {
        if (alarmCreateTime != null) {
            return DateTimeUtils.dateToStrLong(this.alarmCreateTime);
        }
        return null;
    }

    @ApiModelProperty(name = "alarmCreateTimeStr", value = "报警创建时间字符串")
    private String alarmCreateTimeStr;*/


//    @ApiModelProperty(name = "区域ID，0表示无区域", value = "areaId")
//    private String areaId;
//    @ApiModelProperty(name = "报警结束有效，报警值，如：超时为超时时间，掉线时间，最大超速速度，线路偏移最大距离（米）等", value = "alarmValue")
//    private double alarmValue;
//    @ApiModelProperty(name = "配置限定值", value = "configValue")
//    private double configValue;
//    @ApiModelProperty(name = "报警模型id", value = "alarmConfigId")
//    private String alarmConfigId;

    public AlarmInfoSocket(String appkey, String alarmGroupId, AlarmResTypeEnum alarmResType, String identity, String terminalId, String carNum, String alarmTime, double longitude, double latitude, String alarmInfo, AlarmTypeEnum alarmType, String remark) {
        this.appkey = appkey;
        this.alarmGroupId = alarmGroupId;
        this.alarmResType = alarmResType;
        this.identity = identity;
        this.terminalId = terminalId;
        this.carNum = carNum;
        this.alarmTime = alarmTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.alarmInfo = alarmInfo;
        this.alarmType = alarmType;
        this.remark = remark;
    }

    public AlarmInfoSocket() {
    }
}
