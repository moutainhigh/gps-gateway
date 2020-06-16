package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol;


import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Type;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.MessageId;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;

/**
 * 分包补传请求指令
 * @author suibozhuliu
 */

@Type(MessageId.补传分包请求)
public class JT_8003 extends AbstractBody {

    private Integer serialNumber;
    private Integer packageTotal;
    private byte[] idList;

    public JT_8003() {
    }

    @Property(index = 0, type = DataType.WORD, desc = "原始消息流水号")
    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Property(index = 4, type = DataType.BYTE, desc = "重传包总数")
    public Integer getPackageTotal() {
        return packageTotal;
    }

    public void setPackageTotal(Integer packageTotal) {
        this.packageTotal = packageTotal;
    }

    @Property(index = 5, type = DataType.BYTES, desc = "重传包ID列表")
    public byte[] getIdList() {
        return idList;
    }

    public void setIdList(byte[] idList) {
        this.idList = idList;
    }
}