package com.zkkj.gps.gateway.ccs.entity.inParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("车牌号查询定位入参模型")
public class InGpsTruck {

    @ApiModelProperty(name = "licensePlates", value = "车牌号集合")
    private List<String> licensePlates;

    @ApiModelProperty(name = "reqZjxlFlag", value = "是否需要从中交兴路获取定位数据：0：不需要；1：需要")
    private int reqZjxlFlag;

}
