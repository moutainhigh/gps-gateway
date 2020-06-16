package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.setup;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch.ElecDispatchInfo;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase.ElecProtocolBase;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase.IElecSendAble;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.utils.ElecProtocolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 下发电子运单
 * @author suibozhuliu
 */
public class F_8182 extends ElecProtocolBase implements IElecSendAble {

    private ElecDispatchInfo elecDispInfo;
    /**
     * 初始化
     * @param elecDispInfo 写入电子运单模型对象
     */
    public F_8182(ElecDispatchInfo elecDispInfo) {
        super(new Byte[] { (byte) 0x81, (byte) 0x82 });
        this.elecDispInfo = elecDispInfo;
    }

    @Override
    public byte[] getSendBytes() {
        btsList = this.produceTheProtocolData();
        List<Byte> dispBytes = new ArrayList<>();
        //写内存区域
        dispBytes.add((byte) 0x41);
        //写电子运单数据
        dispBytes.addAll(this.elecDispInfo.generateDispToBytes());
        //数据长度
        btsList.add((byte) dispBytes.size());
        //业务数据，及内存区域
        btsList.addAll(dispBytes);
        btsList.addAll(this.appendTailProtocolData());
        Byte[] resultBytes = btsList.toArray(new Byte[btsList.size()]);
        return ElecProtocolUtil.toPrimitives(resultBytes);
    }

    @Override
    public void resloveBackBts(List<Byte> bts) {
    }

}
