package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol;


import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Type;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.MessageId;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.PositionAttribute;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;

import java.util.List;

/**
 * 位置信息汇报应答
 * @author suibozhuliu
 */
@Type(MessageId.位置信息汇报)
public class JT_0200 extends AbstractBody {

    private Integer alarmState;
    private Integer terminalState;
    private Integer latitude;
    private Integer longitude;
    private Integer elevation;
    private Integer speed;
    private Integer direction;
    private String date;

    private List<PositionAttribute> positionAttributes;

    @Property(index = 0, type = DataType.DWORD, desc = "报警标志")
    public Integer getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(Integer alarmState) {
        this.alarmState = alarmState;
    }

    @Property(index = 4, type = DataType.DWORD, desc = "状态")
    public Integer getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(Integer terminalState) {
        this.terminalState = terminalState;
    }

    @Property(index = 8, type = DataType.DWORD, desc = "纬度")
    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    @Property(index = 12, type = DataType.DWORD, desc = "经度")
    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    @Property(index = 16, type = DataType.WORD, desc = "海拔")
    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    @Property(index = 18, type = DataType.WORD, desc = "速度")
    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    @Property(index = 20, type = DataType.WORD, desc = "方向")
    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    @Property(index = 22, type = DataType.BCD8421, length = 6, desc = "时间")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Property(index = 28, type = DataType.LIST, desc = "位置附加信息")
    public List<PositionAttribute> getPositionAttributes() {
        return positionAttributes;
    }

    public void setPositionAttributes(List<PositionAttribute> positionAttributes) {
        this.positionAttributes = positionAttributes;
    }
}