package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase.ElecProtocolBase;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase.IElecSendAble;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.utils.ElecProtocolUtil;

import java.util.List;

/**
 * 电子车牌读取
 * @author suibozhuliu
 */
public class F_11EE extends ElecProtocolBase implements IElecSendAble {

    //电子车牌
    private String elecPlateNumber;
    //功能字
    protected  Byte[] functionWord=new Byte[]{(byte)0x11, (byte)0xEE};

    public String getElecPlateNumber() {
        return elecPlateNumber;
    }

    public F_11EE(){
        super(new Byte[]{(byte)0x11,(byte)0xEE});
    }
    /**
     * 获取协议对应,返回数据的构造函数
     */
    public F_11EE(List<Byte> btsList) {
        super(btsList);
    }

    @Override
    public byte[] getSendBytes() {
        btsList=this.produceTheProtocolData();
        btsList.add((byte) 00);
        btsList.addAll(this.appendTailProtocolData());
        Byte[] resultBytes = btsList.toArray(new Byte[btsList.size()]);
        return ElecProtocolUtil.toPrimitives(resultBytes);
    }

    @Override
    public void resloveBackBts(List<Byte> bts) {
        elecPlateNumber= ElecProtocolUtil.btsList2GBKString(bts,8,bts.get(7));
        elecPlateNumber.toString();
    }

}
