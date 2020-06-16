package com.zkkj.gps.gateway.ccs.entity.dispatch.dispatchverity;

import com.zkkj.gps.gateway.ccs.dto.gpsDto.InAreaDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆区域验证模型
 * @author suibozhuliu
 */
@Data
@ApiModel("车辆区域验证模型")
public class VehicleAreaBean extends VehicleBaseBean implements Serializable {

    @ApiModelProperty(name = "areaInfo",value ="区域信息模型")
    private InAreaDto areaInfo;

}
