package com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class TerminalAlarmInfoDto implements Serializable {


    /**
     * 报警分组id
     */
    private String alarmGroupId;

    /**
     * 报警响应类型，开始报警，结束报警
     */
    private AlarmResTypeEnum alarmResType;

    /**
     * 报警所属群组唯一识别码
     */
    private String identity;

    /**
     * 报警所属公司名称
     */
    private String corpName;

    /**
     * 报警终端id
     */
    private String terminalId;

    /**
     * 车牌号
     */
    private String carNum;

    /**
     * 考虑到实时性，检测到开始即产生报警，检测到结束再次产生一条报警信息
     */
    private String alarmTime;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 报警内容信息
     */
    private String alarmInfo;

    /**
     * 报警类型
     */
    private AlarmTypeEnum alarmType;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 区域ID，0表示无区域
     */
    private String areaId;

    /**
     * 报警结束有效，报警值，如：超时为超时时间，掉线时间，最大超速速度，线路偏移最大距离（米）等
     */
    private double alarmValue;

    /**
     * 配置限定值
     */
    private double configValue;

    /**
     * 报警模型id
     */
    private String alarmConfigId;

    /**
     * appKey
     */
    private String appKey;

    /**
     * 运单编号
     */
    private String dispatchNo;

    /**
     * 是否在收发货区域 是1不是0
     */
    private int isDeliveryArea;
}
