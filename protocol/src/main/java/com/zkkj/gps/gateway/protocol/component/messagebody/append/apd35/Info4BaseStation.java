package com.zkkj.gps.gateway.protocol.component.messagebody.append.apd35;

import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/13 10:15
 * 基站信息
 */
public final class Info4BaseStation implements ProtocolSerializable {

	/**
	 * mcc初始化
	 */
	Info4BaseStation(byte[] mcc) {
		this.mcc = BitOperator.twoBytesToInteger(mcc);
	}

	/**
	 * 移动国家码 2
	 */
	private  int mcc;

	/**
	 * 接受场强 1
	 */
	private int rxl;

	/**
	 * 移动网络码 2
	 */
	private int mnc;

	/**
	 * 位置区号码 2
	 */
	private int cellId;

	/**
	 * 小区号 2
	 */
	private int lac;

	@Override
	public String toString() {
		return "移动国家码:" + mcc +
				" 接受场强:" + rxl +
				" 移动网络码:" + mnc +
				" 位置区号码:" + cellId +
				" 小区号:" + lac;
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		// mcc在初始化时进行填充,只进行一次
		rxl = BitOperator.byteToInteger(Arrays.copyOfRange(bytes, 2, 3));
		mnc = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 3, 5));
		cellId = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 5, 7));
		lac = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 7, 9));
	}

	public int getMcc() {
		return mcc;
	}

	public int getRxl() {
		return rxl;
	}

	public int getMnc() {
		return mnc;
	}

	public int getCellId() {
		return cellId;
	}

	public int getLac() {
		return lac;
	}

	public Info4BaseStation() {
	}

	public void setRxl(int rxl) {
		this.rxl = rxl;
	}

	public void setMnc(int mnc) {
		this.mnc = mnc;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}
}