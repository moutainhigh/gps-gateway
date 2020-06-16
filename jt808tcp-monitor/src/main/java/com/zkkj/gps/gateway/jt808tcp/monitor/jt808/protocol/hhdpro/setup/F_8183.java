package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase.ElecProtocolBase;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase.IElecSendAble;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.utils.ElecProtocolUtil;

import java.util.List;

/**
 * 电子运单读取协议组装
 * @author suibozhuliu
 */
public class F_8183 extends ElecProtocolBase implements IElecSendAble {

    byte memoryArea = (byte) 0x41;

    public F_8183(){
        super(new Byte[] { (byte) 0x81, (byte) 0x83 });
    }

    @Override
    public byte[] getSendBytes() {
        btsList = this.produceTheProtocolData();
        btsList.add((byte) 0X01);
        btsList.add(this.memoryArea);
        btsList.addAll(this.appendTailProtocolData());
        Byte[] resultBytes = btsList.toArray(new Byte[btsList.size()]);
        return ElecProtocolUtil.toPrimitives(resultBytes);
    }

    @Override
    public void resloveBackBts(List<Byte> bts) {
    }

}
