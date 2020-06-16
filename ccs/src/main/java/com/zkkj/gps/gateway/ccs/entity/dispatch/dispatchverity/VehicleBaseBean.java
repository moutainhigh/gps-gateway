package com.zkkj.gps.gateway.ccs.entity.dispatch.dispatchverity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆信息验证基类
 * @author suibozhuliu
 */
@Data
public class VehicleBaseBean implements Serializable{

    @ApiModelProperty(name = "licensePlate",value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(name = "reqZjxlFlag", value = "是否需要从中交兴路获取定位数据：0：不需要；1：需要")
    private int reqZjxlFlag;

}
