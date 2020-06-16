package com.zkkj.gps.gateway.ccs.dto.dispatch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("添加运单信息接口")
public class DispatchInfoDto {

    @ApiModelProperty(value = "收货区域信息", name = "receiverArea")
    private AreaDto receiverArea;

    @ApiModelProperty(value = "发货区域信息", name = "consignerArea")
    private AreaDto consignerArea;

    @ApiModelProperty(value = "报警区域配置信息", name = "dispatchAreaMonitorList")
    private List<DispatchAreaMonitor> dispatchAreaMonitorList;

    @ApiModelProperty(value = "其他报警配置信息", name = "extraAlarmConfigList")
    private List<DispatchAreaMonitor> extraAlarmConfigList;

    @ApiModelProperty(value = "报警线路配置信息", name = "dispatchRouteMonitorList")
    private List<DispatchRouteMonitor> dispatchRouteMonitorList;

    @NotBlank(message = "传入的appkey不能为空")
    @ApiModelProperty(value = "对应用户appkey", name = "appkey")
    private String appkey;

    @NotBlank(message = "传入的公司唯一标识不能为空")
    @ApiModelProperty(value = "公司唯一标识，纳税号，编码等", name = "identity")
    private String identity;

    @NotBlank(message = "车牌号不能为空")
    @ApiModelProperty(value = "车牌号", name = "carNumber")
    private String carNumber;

    @NotBlank(message = "设备编号不能为空")
    @ApiModelProperty(value = "设备编号", name = "terminalNo")
    private String terminalNo;

    @NotBlank(message = "收货公司名称不能为空")
    @ApiModelProperty(value = "收货公司名称", name = "receiverCorpName")
    private String receiverCorpName;

    @NotBlank(message = "发货公司名称不能为空")
    @ApiModelProperty(value = "发货公司名称", name = "consignerCorpName")
    private String consignerCorpName;

    @NotBlank(message = "承运公司名称不能为空")
    @ApiModelProperty(value = "承运公司名称", name = "shipperCorpName")
    private String shipperCorpName;

    @NotBlank(message = "运单编号不能为空")
    @ApiModelProperty(value = "运单编号", name = "dispatchNo")
    private String dispatchNo;

    @ApiModelProperty(value = "司机名称", name = "driverName")
    private String driverName;

    @ApiModelProperty(value = "司机电话", name = "driverMobile")
    private String driverMobile;

    @ApiModelProperty(value = "创建时间", name = "createTime")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "创建人名称", name = "createUserName")
    private String createUserName;

    @ApiModelProperty(value = "货物名称", name = "productName")
    private String productName;

    @ApiModelProperty(value = "货物品类", name = "goodCategory")
    private String goodCategory;

    @ApiModelProperty(value = "最后修改人", name = "lastChangeBy")
    private String lastChangeBy;

    //@NotNull(message = "发热量不能为null")
    @ApiModelProperty(value = "发热量(不存在发热量时传空字符串，不可传null)", name = "kcal")
    private String kcal;

    //@NotNull(message = "扣吨量不能为null")
    @ApiModelProperty(value = "扣吨量", name = "deductWeight")
    private String deductWeight;

    //@NotNull(message = "扣吨原因不能为null")
    @ApiModelProperty(value = "扣吨原因", name = "deductReason")
    private String deductReason;

    //@NotNull(message = "运单状态不能为null")
    @ApiModelProperty(value = "运单状态", name = "status")
    private String status;

    //@NotBlank(message = "计划卡号不能为空")
    @ApiModelProperty(value = "计划卡号", name = "taskNumber")
    private String taskNumber;

    private double conWeight;
    @ApiModelProperty(value = "运单类型1.采购2.销售", name = "dispatchType")
    private int dispatchType;

}
