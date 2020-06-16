package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.position;

import lombok.Data;

/**
 * 位置追加模型
 * @author suibozhuliu
 */
@Data
public class PositionAppendDto extends PositionBaseDto{

    /**
     * 报警标志
     */
    private Integer alarmState;
    /**
     * 终端状态
     */
    private Integer terminalState;
    /**
     * 载重传感器压力值
     */
    private Double loadSensorValue;
    /**
     * 是否存在载重传感器
     */
    private boolean loadSensorIsExist;
    /**
     * 0: ACC 关;1:ACC 开
     */
    private Integer acc;
    /**
     * 油量 1-10L
     */
    private Integer oilMass;
    /**
     * 电压 0.01V
     */
    private Integer power;
    /**
     * 0:未定位;1:定位
     */
    private Integer located;
    /**
     * 防拆开关 1 0:正常 1:被拆触发
     */
    private String antiDismantle;

    /**
     * 获取ACC状态值
     * @return
     */
    public Integer getAcc() {
        if ((this.terminalState & 1) == 1){
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "PositionAppendDto{" +
                "alarmState=" + alarmState +
                ", terminalState=" + terminalState +
                ", loadSensorValue=" + loadSensorValue +
                ", loadSensorIsExist=" + loadSensorIsExist +
                ", acc=" + acc +
                ", oilMass=" + oilMass +
                ", power=" + power +
                ", located=" + located +
                ", antiDismantle='" + antiDismantle + '\'' +
                "} " + super.toString();
    }
}
