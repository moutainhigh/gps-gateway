package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 通过车牌或手机号获取车辆运输轨迹
 * @author suibozhuliu
 */
@Data
public class TrackReqBean {

    @ApiModelProperty(name = "licensePlate", value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(name = "phoneNum", value = "手机号码")
    private String phoneNum;

    @NotBlank(message = "开始时间（注：距结束时间24h以内，格式：yyyy-MM-dd HH:mm:ss）")
    @ApiModelProperty(name = "startTime", value = "开始时间")
    private String startTime;

    @NotBlank(message = "结束时间（注：距开始时间24h以内，格式：yyyy-MM-dd HH:mm:ss）")
    @ApiModelProperty(name = "endTime", value = "结束时间")
    private String endTime;

}
