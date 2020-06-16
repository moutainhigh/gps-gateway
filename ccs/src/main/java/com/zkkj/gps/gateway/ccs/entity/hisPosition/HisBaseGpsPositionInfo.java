package com.zkkj.gps.gateway.ccs.entity.hisPosition;

import com.zkkj.gps.gateway.ccs.entity.basePosition.BaseGpsPositionInfo;
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
@ApiModel("历史定位基本模型")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HisBaseGpsPositionInfo extends BaseGpsPositionInfo {

}
