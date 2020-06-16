package com.zkkj.gps.gateway.ccs.dto.warn;

import com.zkkj.gps.gateway.ccs.dto.gpsDto.GpsBusinessDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zkkjgs
 * @date 2020-05-09
 */

@Data
@ToString(callSuper = true)
public class ElecBusinessWarnDto extends GpsBusinessDto implements Serializable {

    @ApiModelProperty(value = "设备编号", name = "terminalNo")
    private String terminalNo;

    @ApiModelProperty(value = "设备类型", name = "flag")
    private Integer flag;

}
