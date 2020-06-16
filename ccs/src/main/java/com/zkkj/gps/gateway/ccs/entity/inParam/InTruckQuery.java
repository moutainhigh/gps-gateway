package com.zkkj.gps.gateway.ccs.entity.inParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/11/20
 */
@Data
@ApiModel("查询车辆相关信息的入参模型")
public class InTruckQuery implements Serializable {

    @ApiModelProperty(name = "licensePlate", value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(name = "phoneNum", value = "手机号码")
    private String phoneNum;

    @NotBlank(message = "开始时间（格式：yyyy-MM-dd HH:mm:ss）")
    @ApiModelProperty(name = "startTime", value = "开始时间")
    private String startTime;

    @NotBlank(message = "结束时间（格式：yyyy-MM-dd HH:mm:ss）")
    @ApiModelProperty(name = "endTime", value = "结束时间")
    private String endTime;

}
