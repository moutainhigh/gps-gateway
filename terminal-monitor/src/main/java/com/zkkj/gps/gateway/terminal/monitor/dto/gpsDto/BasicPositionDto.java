package com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto;

import com.zkkj.gps.gateway.common.constant.BaseConstant;
import com.zkkj.gps.gateway.terminal.monitor.utils.DataUtils;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class BasicPositionDto implements Serializable {

    /**
     * 报警标志 4
     * [1] 1：超速报警
     * [2] 1：疲劳驾驶
     * [4] 1：GNSS模块发生故障
     * [5] 1：GNSS天线未接或被断路
     * [6] 1：GNSS天线短路
     * [7] 1：终端主电源欠压
     * [8] 1：终端主电源掉电
     * [19] 1：超时停车
     * 其他位保留
     */
    private Long alarmState;

    /**
     * 状态定义 4
     */
    private Long terminalState;

    /**
     * 纬度 4
     */
    private Double latitude;

    /**
     * 经度 4
     */
    private Double longitude;

    /**
     * 海拔(m) 2
     */
    private Integer elevation;
    /**
     * 速度(1/10 km/h) 2
     */
    private Integer speed;

    /**
     * 方向(0-359°,正北为0,顺时针) 2
     */
    private Integer direction;

    /**
     * 日期 6 GMT+8时间,本标准之后涉及的时间均采用此时区
     */
    private LocalDateTime date;

    /**
     * 载重传感器压力值
     */
    private Double loadSensorValue;

    /**
     * 是否存在载重传感器
     */
    private boolean loadSensorIsExist;

    public boolean getLoadSensorIsExist() {

        return loadSensorIsExist;
    }


    /**
     * Acc状态 0: ACC关；1:ACC开
     */
    private Integer acc;

    /**
     * 获取ACC状态值
     *
     * @return
     */
    public int getAcc() {
        if(this.terminalState == null){
            return 0;
        }
        if ((this.terminalState & 1) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 里程数 4  1-10km
     */
    private Long mileage;

    /**
     * 油量 2 1-10L
     */
    private Integer oilMass;

    /**
     * 电量值 3 400 (毫伏,相当于4.00V)
     */
    private String power;

    /**
     * 防拆开关 1 0:正常 1:被拆触发
     */
    private String antiDismantle;


    public Double getLoadSensorValue() {
        loadSensorIsExist = getSensorExist();
        if (loadSensorIsExist) {
            if (getLoad() != null) {
                try {
                    loadSensorValue = Double.valueOf(DataUtils.getDoublePoint((double) getLoad() / 100));
                } catch (Exception e) {
                    loadSensorValue = 0.00;
                }
            }
        }
        return loadSensorValue;
    }

    public Long getLoad() {
        if(this.terminalState == null){
            return null;
        }
        if ((this.terminalState & 0x10) != 0) {
            long load = (this.terminalState & 0xFFFF0000) >> 16;
            return load;
        } else {
            //无载重传感器
            return null;
        }
    }

    private boolean getSensorExist() {
        try {
            if ((this.terminalState & 16) == 16) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    //获取单位为km/h的速度
    public double getSpeedKmH() {
        if(speed == null){
            return 0.0;
        }
        return speed / 10.0;
    }


    /**
     * 当前经纬度信息验证
     */
    public boolean validate() {
        //TODO 验证当前经纬度是否有效
        /**
         * 验证经纬度有效规则， 经度范围-180°~180°，纬度-90°~90°
         */
        String lat = String.valueOf(latitude);
        String lng = String.valueOf(longitude);
        return Pattern.matches(BaseConstant.LONGITUDE_REGEX, lng) ? (Pattern.matches(BaseConstant.LATITUDE_REGEX, lat) ? true : false) : false;
    }

    @Override
    public String toString() {
        return "BasicPositionDto{" +
                "alarmState=" + alarmState +
                ", terminalState=" + terminalState +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", elevation=" + elevation +
                ", speed=" + speed +
                ", direction=" + direction +
                ", date=" + date +
                ", loadSensorValue=" + loadSensorValue +
                ", loadSensorIsExist=" + loadSensorIsExist +
                ", acc=" + acc +
                ", mileage=" + mileage +
                ", oilMass=" + oilMass +
                ", power='" + power + '\'' +
                ", antiDismantle='" + antiDismantle + '\'' +
                '}';
    }
}
