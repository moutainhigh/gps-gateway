package com.zkkj.gps.gateway.ccs.dto.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("运单事件推送对象")
public class DispatchEventDto {

    @ApiModelProperty(name = "id", value = "主键")
    private String id;

    @ApiModelProperty(name = "eventType", value = "事件类型1.进发货区域事件2.发货事件(写矿发量)3.出发货区域事件4.进收货区域5.进厂事件;6.扣吨事件；7.出厂事件8.出收货区域事件；9.运单异常事件")
    private int eventType;

    @ApiModelProperty(name = "identity", value = "事件所属公司ID")
    private String identity;

    @ApiModelProperty(name = "corpName", value = "事件公司名称")
    private String corpName;

    @ApiModelProperty(name = "terminalId", value = "事件终端id")
    private String terminalId;

    @ApiModelProperty(name = "carNum", value = "车牌号")
    private String carNum;

    @ApiModelProperty(name = "longitude", value = "经度")
    private double longitude;

    @ApiModelProperty(name = "latitude", value = "纬度")
    private double latitude;

    @ApiModelProperty(name = "eventInfo", value = "事件内容信息")
    private String eventInfo;

    @ApiModelProperty(name = "alarmType", value = "备注信息")
    private String remark;

    @ApiModelProperty(name = "areaId", value = "区域ID，0表示无区域")
    private String areaId;

    @ApiModelProperty(name = "areaName", value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "eventCreateTime", value = "事件创建时间")
    private String eventCreateTime;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private String createBy;

    @ApiModelProperty(name = "dispatchNo", value = "订单编号")
    private String dispatchNo;

    @ApiModelProperty(name = "appkey", value = "appkey")
    private String appkey;

    @ApiModelProperty(name = "sendTareWeight", value = "发货皮重")
    private Double sendTareWeight;

    @ApiModelProperty(name = "sendGrossWeight", value = "发货量，毛重")
    private Double sendGrossWeight;

    @ApiModelProperty(name = "rcvTareWeight", value = "收货皮重")
    private Double rcvTareWeight;

    @ApiModelProperty(name = "rcvGrossWeight", value = "收货量，毛重")
    private Double rcvGrossWeight;

    @ApiModelProperty(name = "status", value = "运单状态变更字")
    private Integer status;

    @ApiModelProperty(name = "deductWeight", value = "扣吨量")
    private Double deductWeight;

    @ApiModelProperty(name = "deductReason", value = "扣吨原因")
    private String deductReason;

    @ApiModelProperty(name = "mileage", value = "里程")
    private Long mileage;

    @ApiModelProperty(value = "发货单位", name = "consignerName")
    private String consignerName;

    @ApiModelProperty(value = "收货单位", name = "receiverName")
    private String receiverName;

    @ApiModelProperty(value = "运输单位", name = "shipperName")
    private String shipperName;

}
