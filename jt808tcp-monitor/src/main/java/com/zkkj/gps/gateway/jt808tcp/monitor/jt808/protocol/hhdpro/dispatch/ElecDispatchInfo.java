package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.utils.ElecProtocolUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 定位数据中存储的电子运单类
 * @author suibozhuliu
 */
@Data
public class ElecDispatchInfo {

    public ElecDispatchInfo(List<Byte> bts, int offset) {
        this.resloveElecDispFromBytes(bts, offset);
    }

    public ElecDispatchInfo() {
    }

    /**
     * 运单编号
     */
    private String disPatchNo;

    /**
     * 计划卡号（任务编号）
     */
    private String taskNumber;
    /**
     * 承运商
     */
    private String shipperName;
    /**
     * 收货方
     */
    private String receiverName;
    /**
     * 发货方
     */
    private String consignerName;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 司机名称
     */
    private String driverName;
    /**
     * 货物名称
     */
    private String goodsName;
    /**
     * 货物类型
     */
    private String goodCategory;
    /**
     * 发货总重量，毛重
     */
    private double sendGrossWeight;
    /**
     * 发货皮重
     */
    private double sendTareWeight;
    /**
     * 收货总重量,毛重
     */
    private double rcvGrossWeight;
    /**
     * 收货皮重
     */
    private double rcvTareWeight;
    /**
     * 扣吨重量
     */
    private double deductWeight;
    /**
     * 扣吨原因
     */
    private String deductReason;
    /**
     * 运单状态
     */
    private byte status;
    /**
     * 发热量
     */
    private String kcal;
    /**
     * 最近修改人
     */
    private String lastChangeBy;

    /**
     * 将电子运单转换为byte数组原生
     *
     * @return
     */
    public List<Byte> generateDispToBytes() {
        List<Byte> listResult = new ArrayList<>();
        //运单编号
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.disPatchNo) ? "" : this.disPatchNo, 20));
        //计划卡号
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.taskNumber) ? "" : this.taskNumber, 10));
        //发货单位名称
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.consignerName) ? "" : this.consignerName, 30));
        //收货单位名称
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.receiverName) ? "" : this.receiverName, 30));
        //承运单位名称
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.shipperName) ? "" : this.shipperName, 30));
        //车牌
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.plateNumber) ? "" : this.plateNumber, 9));
        //司机名称
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.driverName) ? "" : this.driverName, 10));
        //货物名称
        this.goodsName = StringUtils.isEmpty(this.goodsName) ? "" : this.goodsName;
        if (StringUtils.isEmpty(this.kcal)) {
            listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(this.goodsName, 10));
        } else {
            List<Byte> goodsNameBytes = ElecProtocolUtil.gbk2BytesWithFixLength(this.goodsName, 6);
            listResult.addAll(goodsNameBytes);
            List<Byte> kcalBytes = ElecProtocolUtil.gbk2BytesWithFixLength(this.kcal, 4);
            listResult.addAll(kcalBytes);
        }
        //货物类型
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.goodCategory) ? "" : this.goodCategory, 10));
        //发货皮重
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(Double.toString(this.sendTareWeight), 5));
        //发货毛重
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(Double.toString(this.sendGrossWeight), 5));
        //收货皮重
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(Double.toString(this.rcvTareWeight), 5));
        //收货毛重
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(Double.toString(this.rcvGrossWeight), 5));
        //运单状态
        listResult.add(this.status);//洛阳电厂
		/*listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(Integer.toString(this.status), 1));//十六进制   倒数第20位为状态字段
		System.out.println("原生扣吨电子运单状态：【" + this.status + "】；转换成字节数据：【" + ElecProtocolUtil.gbk2BytesWithFixLength(
				Integer.toString(this.status), 1) + "】；转换成数字：【" + Integer.toString(this.status) + "】");*/
        //扣吨重量
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(Double.toString(this.deductWeight), 5));
        //扣吨原因
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.deductReason) ? "" : this.deductReason, 3));
        listResult.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(StringUtils.isEmpty(this.lastChangeBy) ? "" : this.lastChangeBy, 8));
        return listResult;
    }

    /**
     * 将获取到的byte数组转换为对应的电子运单模型
     *
     * @param bts
     * @param offset
     * @return
     */
    private void resloveElecDispFromBytes(List<Byte> bts, int offset) {
        int dataCursor = 0;
        //运单编号
        this.disPatchNo = ElecProtocolUtil.btsList2GBKString(bts, offset, 20);
        dataCursor += 20;
        //计划卡号
        this.taskNumber = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 10);
        dataCursor += 10;
        // 发货单位名称
        this.consignerName = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 30);
        dataCursor += 30;
        //收货单位名称
        this.receiverName = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 30);
        dataCursor += 30;
        //承运单位名称
        this.shipperName = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 30);
        dataCursor += 30;
        //车牌号
        this.plateNumber = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 9);
        dataCursor += 9;
        //司机名称
        this.driverName = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 10);
        dataCursor += 10;
        //发热量
        String kal = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor + 6, 4);
        if (StringUtils.isEmpty(kal)) {//判断发热量为空时：10位全部为货物名称
            this.goodsName = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor,  10);
        } else {//不为空时前6位为货物名称，后4位位发热量
            this.goodsName = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 6);
            this.kcal = kal;
        }
        dataCursor += 10;
        //货物类型名称
        this.goodCategory = ElecProtocolUtil.btsList2GBKString(bts, offset+ dataCursor, 10);
        dataCursor += 10;
        //发货皮重
        String sendTareWeightStr = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 5);
        this.sendTareWeight = ElecProtocolUtil.isEmptyString(sendTareWeightStr) ? 0 : ElecProtocolUtil.string2Double(sendTareWeightStr);
        dataCursor += 5;
        //发货毛重
        String sendGrossWeightStr = ElecProtocolUtil.btsList2GBKString(bts,offset + dataCursor, 5);
        this.sendGrossWeight = ElecProtocolUtil.isEmptyString(sendGrossWeightStr) ? 0 : ElecProtocolUtil.string2Double(sendGrossWeightStr);
        dataCursor += 5;
        //收货皮重
        String rcvTareWeightStr = ElecProtocolUtil.btsList2GBKString(bts,offset + dataCursor, 5);
        this.rcvTareWeight = ElecProtocolUtil.isEmptyString(rcvTareWeightStr) ? 0 : ElecProtocolUtil.string2Double(rcvTareWeightStr);
        dataCursor += 5;
        //收货毛重
        String rcvGrossWeightStr = ElecProtocolUtil.btsList2GBKString(bts,offset + dataCursor, 5);
        this.rcvGrossWeight = ElecProtocolUtil.isEmptyString(rcvGrossWeightStr) ? 0 : ElecProtocolUtil.string2Double(rcvGrossWeightStr);
        dataCursor += 5;
        //运单状态
        Byte statusbyte = bts.get(offset + dataCursor);
        // /判断任务状态字是否在0到9之间
		if (statusbyte >= 48 && statusbyte <= 57) {
			char c = (char) Integer.parseInt(statusbyte.toString());
			this.status = (byte) Integer.parseInt(String.valueOf(c));
		}else {
			this.status = statusbyte;//洛阳电厂
		}
        dataCursor += 1;
        //扣吨重量
        String deductWeightStr = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 5);
        this.deductWeight = ElecProtocolUtil.isEmptyString(deductWeightStr) ? 0 : ElecProtocolUtil.string2Double(deductWeightStr);
        dataCursor += 5;
        // 扣吨原因
        this.deductReason = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 3);
        dataCursor += 3;
        this.lastChangeBy = ElecProtocolUtil.btsList2GBKString(bts, offset + dataCursor, 8);
    }

    @Override
    public String toString() {
        return "ElecDispatchInfo [disPatchNo=" + disPatchNo
                + ", taskNumber=" + taskNumber + ", shipperName="
                + shipperName + ", receiverName=" + receiverName
                + ", consignerName=" + consignerName + ", plateNumber=" + plateNumber
                + ", driverName=" + driverName + ", goodsName=" + goodsName
                + ", goodCategory=" + goodCategory + ", sendGrossWeight=" + sendGrossWeight
                + ", sendTareWeight=" + sendTareWeight + ", rcvGrossWeight="
                + rcvGrossWeight + ", rcvTareWeight=" + rcvTareWeight
                + ", deductWeight=" + deductWeight + ", deductReason="
                + deductReason + ", status=" + status + ", kcal=" + kcal
                + ", lastChangeBy=" + lastChangeBy + "]";
    }

}
