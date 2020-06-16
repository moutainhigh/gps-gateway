package com.zkkj.gps.gateway.gpsobtain.entity.gpsobtain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Gps信息获取入参对象
 * @author suibozhuliu
 */
@Data
public class GpsObtainBean {

    @ApiModelProperty(value = "手机号码（11位长度）", name = "phoneNum",required = true)
    private String phoneNum;

    @ApiModelProperty(value = "车牌号", name = "licensePlate",required = true)
    private String licensePlate;

}
