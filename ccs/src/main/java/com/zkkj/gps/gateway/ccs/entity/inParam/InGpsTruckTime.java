package com.zkkj.gps.gateway.ccs.entity.inParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("车牌号查询历史轨迹入参模型")
public class InGpsTruckTime {

    @ApiModelProperty(name = "startTime", value = "开始时间（注：格式：yyyy-MM-dd HH:mm:ss）")
    private String startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间（注：格式：yyyy-MM-dd HH:mm:ss）")
    private String endTime;

    @ApiModelProperty(name = "licensePlates", value = "车牌号集合")
    private List<String> licensePlates;

    @ApiModelProperty(name = "reqZjxlFlag", value = "是否需要从中交兴路获取轨迹数据：0：不需要；1：需要")
    private int reqZjxlFlag;

}
