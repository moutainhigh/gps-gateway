package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * author : cyc
 * Date : 2019/11/14
 */

@Data
@ApiModel("通过手机号和车牌号查询历史定位模型")
public class GpsObtainDto implements Serializable {

    @ApiModelProperty(name = "licensePlate", value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(name = "phoneNum", value = "手机号码(11位长度)")
    private String phoneNum;


}
