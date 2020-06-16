package com.zkkj.gps.gateway.ccs.dto.gpsDto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * author : cyc
 * Date : 2019/11/14
 */

@Data
@ApiModel("gps信息统一返回模型,包含手机，终端等设备公共参数")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseGpsInfoDto extends GpsObtainDto {

    @ApiModelProperty(name = "rcvTime", value = "系统时间接收到点位时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime rcvTime;

    @ApiModelProperty(name = "gpsTime", value = "点位时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gpsTime;

    @ApiModelProperty(name = "rcvTimeStr", value = "系统时间接收到定位时间字符串格式")
    private String rcvTimeStr;

    @ApiModelProperty(name = "gpsTimeStr", value = "定位时间字符串格式")
    private String gpsTimeStr;

    @ApiModelProperty(name = "direction", value = "方向(0-359°,正北为0,顺时针) ")
    private Integer direction;

    @ApiModelProperty(name = "elevation", value = "海拔")
    private Integer elevation;

    @ApiModelProperty(name = "latitude", value = "纬度")
    private Double latitude;

    @ApiModelProperty(name = "longitude", value = "经度")
    private Double longitude;

    public String getRcvTimeStr() {
        return DateTimeUtils.formatLocalDateTime(this.rcvTime);
    }

    public String getGpsTimeStr() {
        return DateTimeUtils.formatLocalDateTime(this.gpsTime);
    }
}
