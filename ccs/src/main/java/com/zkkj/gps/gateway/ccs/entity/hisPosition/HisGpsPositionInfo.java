package com.zkkj.gps.gateway.ccs.entity.hisPosition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * author : cyc
 * Date : 2019/11/20
 */
@Data
@ApiModel("历史轨迹定位模型")
public class HisGpsPositionInfo implements Serializable {

    @ApiModelProperty(name = "licensePlate", value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(name = "pointList", value = "点位集合")
    private List<? extends HisBaseGpsPositionInfo> pointList;

}
