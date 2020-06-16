package com.zkkj.gps.gateway.ccs.entity.realPosition;

import com.zkkj.gps.gateway.ccs.entity.basePosition.BaseGpsPositionInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel("车辆当前位置模型")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RealTruckPosition extends BaseGpsPositionInfo {

    @ApiModelProperty(name = "licensePlate", value = "车牌号")
    private String licensePlate;

}
