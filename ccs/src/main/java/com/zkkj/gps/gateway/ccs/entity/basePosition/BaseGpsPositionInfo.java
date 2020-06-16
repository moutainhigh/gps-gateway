package com.zkkj.gps.gateway.ccs.entity.basePosition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.PositionDto;
import com.zkkj.gps.gateway.ccs.utils.GpsConversionUtil;
import com.zkkj.gps.gateway.common.utils.DataCommonUtils;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author : cyc
 * Date : 2019/11/20
 */
@Data
@ApiModel("gps定位信息基本模型,包含手机，终端等设备公共参数")
public class BaseGpsPositionInfo implements Serializable {

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

    @ApiModelProperty(name = "latitude", value = "wgs84纬度")
    private Double latitude;

    @ApiModelProperty(name = "longitude", value = "wgs84经度")
    private Double longitude;

    @ApiModelProperty(name = "bdLatitude", value = "BD09纬度")
    private Double bdLatitude;

    @ApiModelProperty(name = "bdLongitude", value = "BD09经度")
    private Double bdLongitude;

    @ApiModelProperty(name = "gcjLatitude", value = "GCJ02纬度")
    private Double gcjLatitude;

    @ApiModelProperty(name = "gcjLongitude", value = "GCJ02经度")
    private Double gcjLongitude;

    @ApiModelProperty(name = "speed", value = "速度（百米/h）")
    private Double speed;

    @ApiModelProperty(value = "里程数（单位：Km）,", name = "mileage")
    private Double mileage;

    /**
     * 获取速度（km/h）
     * @return
     */
    public Double getSpeedKm() {
        Double speedD = 0.0;
        try {
            if (!ObjectUtils.isEmpty(this.speed)){
                speedD = DataCommonUtils.getTwoDecimals(this.speed/10);
            }
        } catch (Exception e){
            speedD = 0.0;
        }
        return speedD;
    }

    public String getRcvTimeStr() {
        if (ObjectUtils.isEmpty(this.rcvTime)){
            return null;
        }
        return DateTimeUtils.formatLocalDateTime(this.rcvTime);
    }

    public String getGpsTimeStr() {
        if (ObjectUtils.isEmpty(this.gpsTime)){
            return null;
        }
        return DateTimeUtils.formatLocalDateTime(this.gpsTime);
    }

    public Double getBdLatitude() {
        if (ObjectUtils.isEmpty(this.latitude)){
            return null;
        }
        return GpsConversionUtil.wgs84tobd09(this.longitude, this.latitude)[1];
    }

    public Double getBdLongitude() {
        if (ObjectUtils.isEmpty(this.longitude)){
            return null;
        }
        return GpsConversionUtil.wgs84tobd09(this.longitude, this.latitude)[0];
    }

    public Double getGcjLatitude() {
        if (ObjectUtils.isEmpty(this.latitude)){
            return null;
        }
        return GpsConversionUtil.wgs84togcj02(this.longitude, this.latitude)[1];
    }

    public Double getGcjLongitude() {
        if (ObjectUtils.isEmpty(this.longitude)){
            return null;
        }
        return GpsConversionUtil.wgs84togcj02(this.longitude, this.latitude)[0];
    }

}
