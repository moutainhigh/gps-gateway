package com.zkkj.gps.gateway.ccs.dto.websocketDto;

import com.zkkj.gps.gateway.ccs.utils.GpsConversionUtil;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("当前GPS信息")
public class GPSPositionDto {

    @ApiModelProperty(name = "course", value = "方向")
    private int course;

    @ApiModelProperty(name = "gpsTime", value = "gps定位时间")
    private String gpsTime;

    @ApiModelProperty(name = "recTime", value = "服务器接收时间")
    private String recTime;

    @ApiModelProperty(name = "latitude", value = "地球坐标纬度")
    private double latitude;

    @ApiModelProperty(name = "longitude", value = "地球坐标经度")
    private double longitude;

    @ApiModelProperty(name = "milesKM", value = "里程")
    private double milesKM;

    @ApiModelProperty(name = "simId", value = "设备id")
    private String simId;

    @ApiModelProperty(name = "speed", value = "速度")
    private double speed;

    @ApiModelProperty(name = "power", value = "电量")
    private String power;

    @ApiModelProperty(name = "alarmState", value = "报警状态")
    private long alarmState;

    @ApiModelProperty(name = "carState", value = "车辆状态")
    private int carState;

    @ApiModelProperty(name = "terminalState", value = "终端状态")
    private long terminalState;

    @ApiModelProperty(name = "truckNo", value = "车牌号")
    private String truckNo;

    @ApiModelProperty(name = "speedKm", value = "速度km/h")
    private double speedKm;

    @ApiModelProperty(name = "load", value = "载重传感器")
    private Double load;

    @ApiModelProperty(name = "baiDuPosition", value = "百度坐标")
    private PositionDto baiDuPosition;

    @ApiModelProperty(name = "googlePosition", value = "谷歌坐标")
    private PositionDto googlePosition;

    @ApiModelProperty(name = "elevation", value = "海拔")
    private int elevation;

    @ApiModelProperty(name = "oilMass", value = "油量")
    private int oilMass;

    public PositionDto getBaiDuPosition() {
        double[] doubles = GpsConversionUtil.wgs84tobd09(this.longitude, this.latitude);
        return new PositionDto(doubles[0], doubles[1]);
    }

    public PositionDto getGooglePosition() {
        double[] doubles = GpsConversionUtil.wgs84togcj02(this.longitude, this.latitude);
        return new PositionDto(doubles[0], doubles[1]);
    }

    public int getAcc() {
        if (((int) this.terminalState & 1) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @ApiModelProperty(name = "acc", value = "Acc状态 0: ACC关；1:ACC开")
    private int acc;

    public Double getLoad() {
        if (load == null) {
            return 0.0;
        }
        return load;
    }


    @ApiModelProperty(name = "isHaveLoad", value = "是否包含载重传感器")
    private boolean isHaveLoad;

    public boolean isHaveLoad() {
        if (getLoad() == null) {
            return false;
        }
        return true;
    }

    public boolean isOffline() {
        //判断gpsTime和当前时间是否超过15分钟，超过15分钟默认离线为true
        if(this.gpsTime == null){
            return false;
        }
        Date recTime = DateTimeUtils.getDateByString(this.gpsTime);
        double minutes = DateTimeUtils.durationMinutes(new Date(), recTime);
        if (minutes >= 15) {
            return true;
        } else {
            return false;
        }
    }

    @ApiModelProperty(name = "isOffline", value = "是否在线")
    private boolean isOffline;

    /**
     * 判断终端是否定位
     * @return
     */
    public boolean isLocated() {
        if (((int) this.terminalState & 2) == 2 || ((int) this.terminalState & 2) == 0) {
            return true;
        } else {
            return false;
        }
    }

    @ApiModelProperty(name = "isLocated", value = "是否定位 true是定位 false 未定位")
    private boolean isLocated;

    public double getSpeedKm() {
        return speed / 10;
    }

    public GPSPositionDto(int course, String gpsTime, String recTime, double latitude, double longitude, double milesKM, String simId, double speed, String power, long alarmState, int carState, long terminalState, Double load) {
        this.course = course;
        this.gpsTime = gpsTime;
        this.recTime = recTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.milesKM = milesKM;
        this.simId = simId;
        this.speed = speed;
        this.power = power;
        this.alarmState = alarmState;
        this.carState = carState;
        this.terminalState = terminalState;
        this.load = load;
    }

    public GPSPositionDto() {

    }

    public String getPositionStatusStr() {
        //通过状态码获取状态字符串
        StringBuffer stringBuffer = new StringBuffer();
        if (((int) this.terminalState & 1) == 1) {
            stringBuffer.append("ACC开;");
        } else {
            stringBuffer.append("ACC关;");
        }
        if (((int) this.terminalState & 2) == 2) {
            stringBuffer.append("定位有效;");
        } else {
            stringBuffer.append("未定位;");
        }
        if (((int) this.terminalState & 4) == 4) {
            stringBuffer.append("南纬;");
        } else {
            stringBuffer.append("北纬;");
        }
        if (((int) this.terminalState & 8) == 8) {
            stringBuffer.append("西经;");
        } else {
            stringBuffer.append("东经;");
        }
        if (((int) this.terminalState & 16) == 16) {
            stringBuffer.append("有载重传感器;");
        } else {
            stringBuffer.append("无载重传感器;");
        }
        return stringBuffer.toString();
    }

    @ApiModelProperty(value = "定位状态字符串", name = "positionStatusStr")
    private String positionStatusStr;

}
