package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import com.zkkj.gps.gateway.ccs.annotation.ByteSize;
import com.zkkj.gps.gateway.ccs.utils.MyStringUtil;
import com.zkkj.gps.gateway.common.constant.BaseConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Pattern;

/**
 * author : cyc
 * Date : 2019-06-13
 */
@Data
@ApiModel(value = "航鸿达设备电子运单模型", description = "航鸿达设备电子运单模型")
@Builder
@NoArgsConstructor
@AllArgsConstructor//使用无参构造
public class GpsBusinessDto {


    /**
     * 派单编号 20
     */
    @ByteSize(max = 20, message = "派单编号长度最多不能超过20个字节")
    @ApiModelProperty(value = "派单编号", name = "disPatchNo")
    @Getter
    @Setter
    private String disPatchNo;

    /**
     * 任务编号 10
     */
    @ByteSize(max = 10, message = "任务编号长度最多不能超过10个字节")
    @ApiModelProperty(value = "任务编号", name = "taskNumber")
    private String taskNumber;

    /**
     * 发货单位 30
     */
    //@ByteSize(max = 30, message = "发货单位长度最多不能超过30个字节")
    @ApiModelProperty(value = "发货单位", name = "consignerName")
    private String consignerName;

    /**
     * 收货单位 30
     */
    //@ByteSize(max = 30, message = "收货单位长度最多不能超过30个字节")
    @ApiModelProperty(value = "收货单位", name = "receiverName")
    private String receiverName;

    /**
     * 运输单位 30
     */
    //@ByteSize(max = 30, message = "运输单位长度最多不能超过30个字节")
    @ApiModelProperty(value = "运输单位", name = "shipperName")
    private String shipperName;

    /**
     * 车牌 9
     */
    @Pattern(regexp = BaseConstant.PLATE_NUMBER, message = "车牌号输入有误")
    @ApiModelProperty(value = "车牌", name = "plateNumber")
    private String plateNumber;

    /**
     * 司机 10
     */
    //@ByteSize(max = 10, message = "司机名称长度最多不能超过10个字节")
    @ApiModelProperty(value = "司机", name = "driverName")
    private String driverName;

    /**
     * 运输货物 10
     */
    @ByteSize(max = 10, message = "运输货物与发热量一起长度最多不能超过10个字节")
    @ApiModelProperty(value = "运输货物", name = "goodsName")
    private String goodsName;

    /**
     * 发热量（与货物名称放在一起共占10字节）， 4
     */
    @ByteSize(max = 4, message = "发热量长度最多不能超过4个字节,取值范围0~9999")
    @ApiModelProperty(value = "发热量", name = "kcal")
    private String kcal;

    /**
     * 品类 10
     */
    @ByteSize(max = 10, message = "货物品类长度最多不能超过10个字节")
    @ApiModelProperty(value = "货物品类", name = "goodCategory")
    private String goodCategory;

    /**
     * 发货皮重 5
     */
    @ByteSize(max = 5, message = "发货皮重长度最多不能超过5个字节")
    @ApiModelProperty(value = "发货皮重", name = "sendTareWeight")
    private String sendTareWeight;

    /**
     * 发货毛重 5
     */
    @ByteSize(max = 5, message = "发货毛重长度最多不能超过5个字节")
    @ApiModelProperty(value = "发货毛重", name = "sendGrossWeight")
    private String sendGrossWeight;

    /**
     * 收货皮重 5
     */
    @ByteSize(max = 5, message = "收货皮重长度最多不能超过5个字节")
    @ApiModelProperty(value = "收货皮重", name = "rcvTareWeight")
    private String rcvTareWeight;

    /**
     * 收货毛重 5
     */
    @ByteSize(max = 5, message = "收货毛重长度最多不能超过5个字节")
    @ApiModelProperty(value = "收货毛重", name = "rcvGrossWeight")
    private String rcvGrossWeight;

    /**
     * 任务状态字 1
     */
    @ByteSize(max = 1, message = "任务状态字长度最多不能超过1个字节")
    @ApiModelProperty(value = "任务状态字", name = "status")
    private String status;

    /**
     * 扣吨量 5
     */
    @ByteSize(max = 5, message = "扣吨量长度最多不能超过5个字节")
    @ApiModelProperty(value = "扣吨量", name = "deductWeight")
    private String deductWeight;

    /**
     * 扣吨原因 3
     */
    @ByteSize(max = 3, message = "扣吨原因长度最多不能超过3个字节,取值范围0~999")
    @ApiModelProperty(value = "扣吨原因", name = "deductReason")
    private String deductReason;

    /**
     * 最后修改人  8
     */
    //@ByteSize(max = 8, message = "最后修改人长度最多不能超过8个字节")
    @ApiModelProperty(value = "最后修改人", name = "lastChangeBy")
    private String lastChangeBy;

    public String getConsignerName() {
        return consignerName;
    }

    public void setConsignerName(String consignerName) {
        //根据gbk判断
        this.consignerName = MyStringUtil.subStr(consignerName, 30);
    }


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = MyStringUtil.subStr(receiverName, 30);
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = MyStringUtil.subStr(shipperName, 30);
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = MyStringUtil.subStr(driverName, 10);
    }

    public String getLastChangeBy() {
        return lastChangeBy;
    }

    public void setLastChangeBy(String lastChangeBy) {
        this.lastChangeBy = MyStringUtil.subStr(lastChangeBy, 8);
    }

}
