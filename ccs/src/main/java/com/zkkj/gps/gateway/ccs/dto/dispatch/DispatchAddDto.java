package com.zkkj.gps.gateway.ccs.dto.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("保存运单接口对象")
public class DispatchAddDto {
    @ApiModelProperty(value = "id", name = "id")
    private String id;
    @ApiModelProperty(value = "对应用户appkey", name = "appkey")
    private String appkey;
    @ApiModelProperty(value = "公司唯一标识，纳税号，编码等（主公司为默认identity）", name = "identity")
    private String identity;
    @ApiModelProperty(value = "车牌号", name = "carNumber")
    private String carNumber;
    @ApiModelProperty(value = "设备编号", name = "terminalNo")
    private String terminalNo;
    @ApiModelProperty(value = "收货公司名称", name = "receiverCorpName")
    private String receiverCorpName;
    @ApiModelProperty(value = "发货公司名称", name = "consignerCorpName")
    private String consignerCorpName;
    @ApiModelProperty(value = "承运公司名称", name = "shipperCorpName")
    private String shipperCorpName;
    @ApiModelProperty(value = "运单编号", name = "dispatchNo")
    private String dispatchNo;
    @ApiModelProperty(value = "司机电话", name = "driverName")
    private String driverName;
    @ApiModelProperty(value = "司机电话", name = "driverMobile")
    private String driverMobile;
    @ApiModelProperty(value = "创建人", name = "createByName")
    private String createByName;
    @ApiModelProperty(value = "客户传过来创建人", name = "createByUserName")
    private String createByUserName;
    @ApiModelProperty(value = "运单状态信息", name = "status")
    private int status;
    @ApiModelProperty(value = "发货区域名称", name = "consignerAreaName")
    private String consignerAreaName;
    @ApiModelProperty(value = "收货区域名称", name = "receiverAreaName")
    private String receiverAreaName;
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;
    @ApiModelProperty(value = "商品名称", name = "productName")
    private String productName;
    @ApiModelProperty(value = "发货区域id", name = "consignerAreaId")
    private String consignerAreaId;
    @ApiModelProperty(value = "收货区域id", name = "receiverAreaId")
    private String receiverAreaId;
    @ApiModelProperty(value = "运单类型1.采购2.销售", name = "dispatchType")
    private int dispatchType;
}
