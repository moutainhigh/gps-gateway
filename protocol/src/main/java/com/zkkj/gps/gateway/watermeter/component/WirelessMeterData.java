package com.zkkj.gps.gateway.watermeter.component;


import com.zkkj.gps.gateway.watermeter.util.BitOperator;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author chailixing
 * 2019/4/29 11:36
 * 无线水表数据
 */
public final class WirelessMeterData implements ProtocolSerializable {
	public WirelessMeterData(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	public WirelessMeterData() {
	}

	/**
	 * 实时时钟 4
	 */
	private String realTimeClock;

	/**
	 * 流量及瞬时流量单位 1
	 */
	private String fluxUnit;

	/**
	 * 累积流量 3
	 */
	private int accFlux;

	/**
	 * 结算日流量 3
	 */
	private int settlementDateFlux;

	/**
	 * 上个月流量 3
	 */
	private int lastMonthFlux;

	/**
	 * 瞬时流量 2
	 */
	private int instantFlux;

	/**
	 * 电池电压 1
	 */
	private int cellVoltage;

	/**
	 * 状态 1字节 (6种状态)
	 */
	private int[] status;

	@Override
	public String toString() {
		return "\n实时时钟:" + realTimeClock +
				"\n流量及瞬时流量单位:" + fluxUnit +
				"\n累积流量:" + accFlux +
				"\n结算日流量:" + settlementDateFlux +
				"\n上个月流量:" + lastMonthFlux +
				"\n瞬时流量:" + instantFlux +
				"\n电池电压:" + cellVoltage +
				"\n状态:{" +
				" 阀门状态:" + status[0] +
				" 传感器状态:" + status[1] +
				" 磁干扰(当前):" + status[2] +
				" 磁干扰(记录):" + status[3] +
				" 余额不足告警:" + status[4] +
				" 欠压:" + status[5] + "}";
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		realTimeClock = castDate(BitOperator.fourBytesToInteger(Arrays.copyOfRange(bytes, 0, 4)));
		fluxUnit = castFlux(BitOperator.oneByteToInteger(bytes[4]));
		accFlux = BitOperator.threeBytesToInteger(Arrays.copyOfRange(bytes, 5, 8));
		settlementDateFlux = BitOperator.threeBytesToInteger(Arrays.copyOfRange(bytes, 8, 11));
		lastMonthFlux = BitOperator.threeBytesToInteger(Arrays.copyOfRange(bytes, 11, 14));
		instantFlux = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 14, 16));
		cellVoltage = BitOperator.oneByteToInteger(bytes[16]);
		status = castStatus(BitOperator.oneByteToInteger(bytes[17]));
	}

	/**
	 * 按bit解析状态
	 */
	private int[] castStatus(int status) throws Exception {
		int[] result = new int[6];
		// 阀门状态
		result[0] = status & 0x3;
		// 传感器状态
		result[1] = (status >>> 2) & 0X3;
		// 磁干扰(当前)
		result[2] = (status >>> 4) & 0X1;
		// 磁干扰(记录)
		result[3] = (status >>> 5) & 0X1;
		// 余额不足告警
		result[4] = (status >>> 6) & 0X1;
		// 欠压
		result[5] = (status >>> 7) & 0X1;
		return result;
	}

	/**
	 * 按bit解析日期
	 */
	private String castDate(int dateInt) throws Exception {
		// 年 6bit
		int year = (dateInt >>> 26) & 0X3F;
		// 月 4bit
		int month = (dateInt >>> 22) & 0XF;
		// 日 5bit
		int day = (dateInt >>> 17) & 0X1F;
		// 时 5bit
		int hour = (dateInt >>> 12) & 0X1F;
		// 分 6bit
		int minute = (dateInt >>> 6) & 0X3F;
		// 秒 6bit
		int second = dateInt & 0X3F;
		return year + ":" + month + ":" + day + ":" + hour + ":" + minute + ":" + second;
	}

	/**
	 * 按bit解析流量相关
	 * 0-3位流量单位:公式(0.1^n)(m3), 4-7位表示瞬时流量:公式(0.1^n)(m3/h)
	 */
	private String castFlux(int flux) throws Exception {
		// 获取流量单位计算
		int n1 = flux & 0XF;
		// 获取瞬时流量单位计算
		int n = (flux >>> 4) & 0XF;
		double a = Math.pow(0.1, n1);
		double b = Math.pow(0.1, n);
		DecimalFormat df = new DecimalFormat("0.0000000000"); // 多留几位,防止变动
		// 搞成字符串算了
		return df.format(a) + "," + df.format(b);
	}

	public String getRealTimeClock() {
		return realTimeClock;
	}

	public String getFluxUnit() {
		return fluxUnit;
	}

	public int getAccFlux() {
		return accFlux;
	}

	public int getSettlementDateFlux() {
		return settlementDateFlux;
	}

	public int getLastMonthFlux() {
		return lastMonthFlux;
	}

	public int getInstantFlux() {
		return instantFlux;
	}

	public int getCellVoltage() {
		return cellVoltage;
	}

	public int[] getStatus() {
		return status;
	}
}