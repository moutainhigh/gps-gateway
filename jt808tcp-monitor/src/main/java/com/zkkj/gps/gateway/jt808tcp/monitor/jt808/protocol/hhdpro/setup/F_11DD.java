package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase.ElecProtocolBase;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase.IElecSendAble;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.utils.ElecProtocolUtil;

import java.util.List;

/**
 * 电子车牌下发
 * @author suibozhuliu
 */
public class F_11DD extends ElecProtocolBase implements IElecSendAble {

    private String elecPlateNumber;
    protected  Byte[] functionWord=new Byte[]{(byte)0x11, (byte)0xDD};
    public String getElecPlateNumber() {
        return elecPlateNumber;
    }

    public F_11DD(String elecNumber){
        super(new Byte[]{(byte)0x11,(byte)0xDD});
        elecPlateNumber=elecNumber;
    }

    /**
     * 获取协议对应,返回数据的构造函数
     *
     */
    public F_11DD(List<Byte> btsList) {
        super(btsList);
    }

    @Override
    public void resloveBackBts(List<Byte> bts) {

    }

    @Override
    public byte[] getSendBytes() {
        btsList = this.produceTheProtocolData();
        btsList.add((byte) 0x08);
        int plateByteLength = ElecProtocolUtil.gbk2Bytes(elecPlateNumber).length;
        btsList.addAll(ElecProtocolUtil.gbk2BytesWithFixLength(elecPlateNumber, plateByteLength < 15 ? plateByteLength : 15));
        btsList.addAll(this.appendTailProtocolData());
        Byte[] resultBytes = btsList.toArray(new Byte[btsList.size()]);
        return ElecProtocolUtil.toPrimitives(resultBytes);
    }

}
