package com.zkkj.gps.gateway.gpsobtain.entity.position;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 点位基础模型
 * @author suibozhuliu
 */
@Data
public class BaseBean {

    @ApiModelProperty(value = "手机号码（11位长度）", name = "phoneNum",required = true)
    private String phoneNum;

    @ApiModelProperty(value = "车牌号", name = "licensePlate",required = true)
    private String licensePlate;

}
