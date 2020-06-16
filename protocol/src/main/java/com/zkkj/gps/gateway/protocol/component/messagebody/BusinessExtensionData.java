package com.zkkj.gps.gateway.protocol.component.messagebody;

import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.util.BitOperator;
import com.zkkj.gps.gateway.protocol.util.DataUtils;
import com.zkkj.gps.gateway.protocol.util.HexStringUtils;

import java.util.Arrays;

/**
 * @author chailixing
 * 2019/1/4 15:03
 * 业务扩展数据（电子运单） 196
 */
public final class BusinessExtensionData implements ProtocolSerializable {


	public BusinessExtensionData() {
	}

	public BusinessExtensionData(byte[] bytes) throws Exception {
		this.decoder(bytes);
	}



	/**
	 * 状态原始编码
	 */
	private byte statusByte;
	/**
	 * 派单编号 20
	 */
	private String disPatchNo;

	/**
	 * 任务编号 10
	 */
	private String taskNumber;

	/**
	 * 发货单位 30
	 */
	private String consignerName;

	/**
	 * 收货单位 30
	 */
	private String receiverName;

	/**
	 * 运输单位 30
	 */
	private String shipperName;

	/**
	 * 车牌 9
	 */
	private String plateNumber;

	/**
	 * 司机 10
	 */
	private String driverName;

	/**
	 * 货物名称 10
	 */
	private String goodsName;

	/**
	 * 发热量（与货物名称放在一起共占10字节）， 4
	 */
	private String kcal;

	/**
	 * 品类 10
	 */
	private String goodCategory;

	/**
	 * 发货皮重 5
	 */
	private String sendTareWeight;

	/**
	 * 发货毛重 5
	 */
	private String sendGrossWeight;

	/**
	 * 收货皮重 5
	 */
	private String rcvTareWeight;

	/**
	 * 收货毛重 5
	 */
	private String rcvGrossWeight;

	/**
	 * 任务状态字 1
	 */
	private String status;

	/**
	 * 扣吨量 5
	 */
	private String deductWeight;

	/**
	 * 扣吨原因 3
	 */
	private String deductReason;

	/**
	 * 最后修改人  8
	 */
	private String lastChangeBy;

	@Override
	public String toString() {
		return "{派单编号:" + disPatchNo +
				" 任务编号:" + taskNumber +
				" 发货单位:" + consignerName +
				" 收货单位:" + receiverName +
				" 运输单位:" + shipperName +
				" 车牌:" + plateNumber +
				" 司机:" + driverName +
				" 运输货物:" + goodsName +
				" 发热量:" + kcal +
				" 品类:" + goodCategory +
				" 发货皮重:" + sendTareWeight +
				" 发货毛重:" + sendGrossWeight +
				" 收货皮重:" + rcvTareWeight +
				" 收货毛重:" + rcvGrossWeight +
				" 任务状态字:" + status +
				" 扣吨量：" + deductWeight +
				" 扣吨原因：" + deductReason +
				" 最后修改人:" + lastChangeBy + "}";
	}

	@Override
	public byte[] encoder() throws Exception {
		String charset = "GBK";
		byte[] totalBytes = new byte[10];//存储发热量和货物名称
		byte[] goodsBytes;//货物名称字节数据
		byte[] kcalBytes;//发热量字节数组
		if (DataUtils.isEmptyString(kcal)){
			goodsBytes = HexStringUtils.parseByte(goodsName.getBytes(charset), 10);
			kcalBytes = new byte[0];
		} else {
			goodsBytes = HexStringUtils.parseByte(goodsName.getBytes(charset), 6);
			kcalBytes = HexStringUtils.parseByte(kcal.getBytes(charset), 4);
		}
		/*System.arraycopy(kcalBytes,0,totalBytes,0,kcalBytes.length);
		System.arraycopy(goodsBytes,0,totalBytes,kcalBytes.length,goodsBytes.length);*/
		//System.out.println("货物名称的字节长度：【" + goodsBytes.length + "】");
		System.arraycopy(goodsBytes,0,totalBytes,0,goodsBytes.length);
		if (kcalBytes != null && kcalBytes.length > 0){
			//System.out.println("发热量的字节长度：【" + kcalBytes.length + "】");
			System.arraycopy(kcalBytes,0,totalBytes,goodsBytes.length,kcalBytes.length);
		}
		//System.out.println("货物名称及发热量的字节总长度：【" + totalBytes.length + "】");
		return BitOperator.concatAll(
				HexStringUtils.parseByte(disPatchNo.getBytes(charset), 20),
				HexStringUtils.parseByte(taskNumber.getBytes(charset), 10),
				HexStringUtils.parseByte(consignerName.getBytes(charset), 30),
				HexStringUtils.parseByte(receiverName.getBytes(charset), 30),
				HexStringUtils.parseByte(shipperName.getBytes(charset), 30),
				HexStringUtils.parseByte(plateNumber.getBytes(charset), 9),
				HexStringUtils.parseByte(driverName.getBytes(charset), 10),
				totalBytes,
				HexStringUtils.parseByte(goodCategory.getBytes(charset), 10),
				HexStringUtils.parseByte(sendTareWeight.getBytes(charset), 5),
				HexStringUtils.parseByte(sendGrossWeight.getBytes(charset), 5),
				/*HexStringUtils.parseByte(sendGrossWeight.getBytes(charset), 5),
				HexStringUtils.parseByte(sendTareWeight.getBytes(charset), 5),*/
				HexStringUtils.parseByte(rcvTareWeight.getBytes(charset), 5),
				HexStringUtils.parseByte(rcvGrossWeight.getBytes(charset), 5),
				HexStringUtils.parseByte(status.getBytes(charset), 1),
				HexStringUtils.parseByte(deductWeight.getBytes(charset), 5),
				HexStringUtils.parseByte(deductReason.getBytes(charset), 3),
				HexStringUtils.parseByte(lastChangeBy.getBytes(charset), 8)
		);
	}
	String charset = "GBK";
	@Override
	public void decoder(byte[] bytes) throws Exception {

		disPatchNo = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 0, 20), charset);
		taskNumber = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 20, 30), charset);
		consignerName = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 30, 60), charset);
		receiverName = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 60, 90), charset);
		shipperName = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 90, 120), charset);
		plateNumber = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 120, 129), charset);
		driverName = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 129, 139), charset);
		kcal = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 145, 149), charset);
		if (DataUtils.isEmptyString(kcal)){
			goodsName = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 139, 149), charset);
			kcal = "";
		} else {
			goodsName = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 139, 145), charset);
		}
		goodCategory = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 149, 159), charset);
		sendTareWeight = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 159, 164), charset);
		sendGrossWeight = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 164, 169), charset);
		/*sendGrossWeight = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 159, 164), charset);
		sendTareWeight = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 164, 169), charset);*/
		rcvTareWeight = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 169, 174), charset);
		rcvGrossWeight = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 174, 179), charset);
		byte[] buf= Arrays.copyOfRange(bytes, 179, 180);

		if(buf!=null&&buf.length>0) {
            statusByte = buf[0];
			//当进行编码后状态区间为48~57
			if (statusByte >= 48 && statusByte <= 57) {
				status = HexStringUtils.parseString(buf, charset);
			} else {
				status = String.valueOf(statusByte);
			}
		}

		deductWeight = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 180, 185), charset);
		deductReason = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 185, 188), charset);
		lastChangeBy = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 188, 196), charset);
	}
	public byte getStatusByte() {
		return statusByte;
	}
	public String getDisPatchNo() {
		return disPatchNo;
	}

	public void setDisPatchNo(String disPatchNo) {
		this.disPatchNo = disPatchNo;
	}

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getConsignerName() {
		return consignerName;
	}

	public void setConsignerName(String consignerName) {
		this.consignerName = consignerName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getShipperName() {
		return shipperName;
	}

	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getKcal() {
		return kcal;
	}

	public void setKcal(String kcal) {
		this.kcal = kcal;
	}

	public String getGoodCategory() {
		return goodCategory;
	}

	public void setGoodCategory(String goodCategory) {
		this.goodCategory = goodCategory;
	}

	public String getSendTareWeight() {
		return sendTareWeight;
	}

	public void setSendTareWeight(String sendTareWeight) {
		this.sendTareWeight = sendTareWeight;
	}

	public String getSendGrossWeight() {
		return sendGrossWeight;
	}

	public void setSendGrossWeight(String sendGrossWeight) {
		this.sendGrossWeight = sendGrossWeight;
	}

	public String getRcvTareWeight() {
		return rcvTareWeight;
	}

	public void setRcvTareWeight(String rcvTareWeight) {
		this.rcvTareWeight = rcvTareWeight;
	}

	public String getRcvGrossWeight() {
		return rcvGrossWeight;
	}

	public void setRcvGrossWeight(String rcvGrossWeight) {
		this.rcvGrossWeight = rcvGrossWeight;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeductWeight() {
		return deductWeight;
	}

	public void setDeductWeight(String deductWeight) {
		this.deductWeight = deductWeight;
	}

	public String getDeductReason() {
		return deductReason;
	}

	public void setDeductReason(String deductReason) {
		this.deductReason = deductReason;
	}

	public String getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(String lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}
}