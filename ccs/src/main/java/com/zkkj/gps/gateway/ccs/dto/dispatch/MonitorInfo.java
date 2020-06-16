package com.zkkj.gps.gateway.ccs.dto.dispatch;

import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("前端任务监控显示对象")
public class MonitorInfo {
    @ApiModelProperty(value = "收货区域名称", name = "consignerAreaName")
    private String receiverAreaName;
    @ApiModelProperty(value = "发货区域名称", name = "consignerAreaName")
    private String consignerAreaName;
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
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    public String getCreateTimeStr() {
        return DateTimeUtils.dateToStrLong(this.createTime);
    }

    @ApiModelProperty(value = "创建时间字符串", name = "createTimeStr")
    private String createTimeStr;
    @ApiModelProperty(value = "货物名称", name = "productName")
    private String productName;
    @ApiModelProperty(value = "运单类型1.采购2.销售", name = "dispatchType")
    private int dispatchType;
}
