package com.zkkj.gps.gateway.protocol.component.messagebody;

import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.util.BCD8421Operator;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/3/5 10:31
 * 多项参数规则
 */
public final class ParamRules implements ProtocolSerializable {
	public ParamRules(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}

	public ParamRules() {
	}

	/**
	 * 存储地址序号(0X0000至0X03E7) 2
	 */
	private int addressId;

	/**
	 * IC卡号 4  ASCII码为：12345678 转后变为：0X12345678
	 */
	private String IcCard = "";

	/**
	 * 经度 4
	 */
	private double longitude;

	/**
	 * 纬度 4
	 */
	private double latitude;

	/**
	 * 半径(m) 2
	 */
	private int radius;

	/**
	 * 子锁控制 2
	 */
	private int subLockControl;

	@Override
	public String toString() {
		return "\n{存储地址序号:" + addressId +
				" IC卡号:" + IcCard +
				" 经度:" + longitude +
				" 纬度:" + latitude +
				" 半径:" + radius +
				" 子锁控制:" + subLockControl + "}";
	}

	@Override
	public byte[] encoder() {
		return BitOperator.concatAll(
				BitOperator.integerTo2Bytes(addressId),
				BCD8421Operator.str2Bcd(IcCard),
				BitOperator.integerTo4Bytes((int) (longitude * 1000000)),
				BitOperator.integerTo4Bytes((int) (latitude * 1000000)),
				BitOperator.integerTo2Bytes(radius),
				BitOperator.integerTo2Bytes(subLockControl)
		);
	}

	@Override
	public void decoder(byte[] bytes) throws Exception {
		addressId = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 0, 2));
		IcCard = BCD8421Operator.bcd2ToString((Arrays.copyOfRange(bytes, 2, 6)));
		longitude = (BitOperator.fourBytesToInteger(Arrays.copyOfRange(bytes, 6, 10)) * 1.0F) / 1000000;
		latitude = (BitOperator.fourBytesToInteger(Arrays.copyOfRange(bytes, 10, 14)) * 1.0F) / 1000000;
		radius = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 14, 16));
		subLockControl = BitOperator.twoBytesToInteger(Arrays.copyOfRange(bytes, 16, 18));
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getIcCard() {
		return IcCard;
	}

	public void setIcCard(String icCard) {
		IcCard = icCard;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getSubLockControl() {
		return subLockControl;
	}

	public void setSubLockControl(int subLockControl) {
		this.subLockControl = subLockControl;
	}
}