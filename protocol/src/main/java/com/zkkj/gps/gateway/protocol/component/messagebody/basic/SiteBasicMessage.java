package com.zkkj.gps.gateway.protocol.component.messagebody.basic;

import com.zkkj.gps.gateway.common.constant.BaseConstant;
import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.util.BCD8421Operator;
import com.zkkj.gps.gateway.protocol.util.BitOperator;
import com.zkkj.gps.gateway.protocol.util.DataUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author chailixing
 * 2018/12/26 10:21
 * 位置基本信息 28字节
 */
public final class SiteBasicMessage implements ProtocolSerializable {
	public SiteBasicMessage(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	public SiteBasicMessage() {

	}

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
	private long alarmState;

	/**
	 * 状态定义 4
	 * [0] 0: ACC关;1:ACC开
	 * [1] 0:未定位;1:定位
	 * [2] 0:北纬:1:南纬
	 * [3] 0:东经;1:西经
	 * [4] 0.无载重;1.有传感器
	 * todo 19位表示?
	 * [13-31] 载重AD值(0x0000~0x2710对应重量百分比0%~100%)
	 * 其他位保留
	 */
	private long terminalState;

	/**
	 * 纬度 4
	 */
	private double latitude;

	/**
	 * 经度 4
	 */
	private double longitude;

	/**
	 * 海拔(m) 2
	 */
	private int elevation;

	/**
	 * 速度(1/10 km/h) 2
	 */
	private int speed;

	/**
	 * 方向(0-359°,正北为0,顺时针) 2
	 */
	private int direction;

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

	/**
	 * Acc状态 0: ACC关；1:ACC开
	 */
	private int acc;

	@Override
	public String toString() {
		return "{报警标志:" + alarmState +
				" 状态定义:" + terminalState +
				" ACC:" + acc +
				" 是否存在载重传感器:" + loadSensorIsExist +
				" 压力值:" + loadSensorValue +
				" 纬度:" + latitude +
				" 经度:" + longitude +
				" 海拔:" + elevation +
				" 速度:" + speed +
				" 方向:" + direction +
				" 日期:" + date + "}";
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		alarmState = BitOperator.fourBytesToLong(Arrays.copyOfRange(bytes, 0, 4));
		terminalState = BitOperator.fourBytesToLong(Arrays.copyOfRange(bytes, 4, 8));
		acc = getAcc();
		latitude = (BitOperator.fourBytesToInteger(Arrays.copyOfRange(bytes, 8, 12)) * 1.0F) / 1_000_000;
		longitude = (BitOperator.fourBytesToInteger(Arrays.copyOfRange(bytes, 12, 16)) * 1.0F) / 1_000_000;
		elevation = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 16, 18));
		speed = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 18, 20));
		direction = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 20, 22));
		try {
			String s = BCD8421Operator.bcd2ToString(Arrays.copyOfRange(bytes, 22, 28));
			date = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyMMddHHmmss"));
		} catch (Exception e){
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMddHHmmss");
			LocalDateTime time = LocalDateTime.now();
			String localTime = df.format(time);
			date = LocalDateTime.parse(localTime,df);
		}
		loadSensorIsExist = getSensorExist();
		if (loadSensorIsExist){
			if (getLoad() != null){
				try {
					loadSensorValue = Double.valueOf(DataUtils.getDoublePoint((double)getLoad() / 100));
				} catch (Exception e){
					loadSensorValue = 0.00;
					//e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取ACC状态值
	 * @return
	 */
	public int getAcc(){
		if ((this.terminalState & 1) == 1){
			return 1;
		} else {
			return 0;
		}
	}

	public void setAcc(int acc) {
		this.acc = acc;
	}

	public Long getLoad() {
		if ((this.terminalState & 0x10) != 0) {
			long load = (this.terminalState & 0xFFFF0000) >> 16;
			return load;
		} else {
			//无载重传感器
			return null;
		}
	}

	private boolean getSensorExist(){
		try {
			if ((this.terminalState & 16) == 16) {
				return true;
			}
		} catch (Exception e){
			return false;
		}
		return false;
	}

	public long getAlarmState() {
		return alarmState;
	}

	public long getTerminalState() {
		return terminalState;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getElevation() {
		return elevation;
	}

	public int getSpeed() {
		return speed;
	}

	public int getDirection() {
		return direction;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setAlarmState(long alarmState) {
		this.alarmState = alarmState;
	}

	public void setTerminalState(long terminalState) {
		this.terminalState = terminalState;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	//获取单位为km/h的速度
	public double getSpeedKmH() {
		return speed / 10.0;
	}

    public Double getLoadSensorValue() {
        return loadSensorValue;
    }

	public boolean isLoadSensorIsExist() {
		return loadSensorIsExist;
	}

	public void setLoadSensorValue(Double loadSensorValue) {
		this.loadSensorValue = loadSensorValue;
	}

	public void setLoadSensorIsExist(boolean loadSensorIsExist) {
		this.loadSensorIsExist = loadSensorIsExist;
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

	public SiteBasicMessage(long alarmState, long terminalState, double latitude, double longitude, int elevation, int speed, int direction, LocalDateTime date) {
		this.alarmState = alarmState;
		this.terminalState = terminalState;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
		this.speed = speed;
		this.direction = direction;
		this.date = date;
	}
}