package com.zkkj.gps.gateway.ccs.entity.dispatch.dispatchverity;

import com.zkkj.gps.gateway.ccs.dto.gpsDto.InAreaDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车辆派车验证模型
 * @author suibozhuliu
 */

@Data
@ApiModel("车辆派车验证模型")
public class VehicleDispatchBean {

    @ApiModelProperty(name = "licensePlate",value = "车牌号")
    private String licensePlate;
    @ApiModelProperty("是否安装平台GPS设备：false：否，true：是")
    private boolean isPlatformGps;
    @ApiModelProperty(name = "isOnlineGps",value ="平台GPS设备是否在线：false：否，true：是")
    private boolean isOnlineGps;
    @ApiModelProperty(name = "isGetOrderLocationRequire",value ="派单/接单位置要求：false：无，true：有")
    private boolean isGetOrderLocationRequire;
    @ApiModelProperty(name = "areaInfo",value ="区域信息模型")
    private InAreaDto areaInfo;

}
