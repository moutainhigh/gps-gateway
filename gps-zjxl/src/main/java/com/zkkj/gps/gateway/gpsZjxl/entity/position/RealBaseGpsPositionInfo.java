package com.zkkj.gps.gateway.gpsZjxl.entity.position;

import com.zkkj.gps.gateway.gpsZjxl.entity.position.BaseGpsPositionInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * author : cyc
 * Date : 2019/11/20
 */
@Data
@ApiModel("最新定位信息基本模型")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RealBaseGpsPositionInfo extends BaseGpsPositionInfo {

    @ApiModelProperty(name = "licensePlate", value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(name = "phoneNum", value = "手机号码(11位长度)")
    private String phoneNum;
}
