package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author suibozhuliu
 * @Description:
 * @Date: 2019-05-15 下午 4:41
 */
@Data
@ApiModel(value = "电子运单模型", description = "电子运单模型")
public class BusinessBean {

    /**
     * 派单编号 20
     */
    private String disPatchNo;

    /**
     * 任务编号 10
     */
    private String taskNumber;

    /**
     * 发货单位 30
     */
    private String consignerName;

    /**
     * 收货单位 30
     */
    private String receiverName;

    /**
     * 运输单位 30
     */
    private String shipperName;

    /**
     * 车牌 9
     */
    private String plateNumber;

    /**
     * 司机 10
     */
    private String driverName;

    /**
     * 货物名称 10
     */
    private String goodsName;

    /**
     * 发热量（与货物名称放在一起共占10字节）， 4
     */
    private String kcal;

    /**
     * 品类 10
     */
    private String goodCategory;

    /**
     * 发货皮重 5
     */
    private String sendTareWeight;

    /**
     * 发货毛重 5
     */
    private String sendGrossWeight;

    /**
     * 收货皮重 5
     */
    private String rcvTareWeight;

    /**
     * 收货毛重 5
     */
    private String rcvGrossWeight;

    /**
     * 任务状态字 1
     */
    private String status;

    /**
     * 扣吨量 5
     */
    private String deductWeight;
    /**
     * 扣吨原因 3
     */
    private String deductReason;

    /**
     * 最后修改人  8
     */
    private String lastChangeBy;

}
