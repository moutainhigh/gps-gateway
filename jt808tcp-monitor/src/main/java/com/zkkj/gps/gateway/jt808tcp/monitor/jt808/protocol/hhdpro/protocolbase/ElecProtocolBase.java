package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.utils.ElecProtocolUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 协议对象基类
 * @author suibozhuliu
 */
public abstract class ElecProtocolBase implements IElecRcvAble{

    /**
     * 获取协议对应返回数据的构造函数， 协议返回数据，必须使用此构造函数
     * @param bts
     */
    public ElecProtocolBase(List<Byte> bts) {
        this.btsList = bts;
        this.resloveBackBts(btsList);
    }

    public ElecProtocolBase(Byte[] functionWord) {
        this.setFunctionWord(functionWord);
    }

    // 帧头
    protected Byte[] head = new Byte[] { (byte) 0xF1, (byte) 0x1F };
    // 通道（设备地址）
    protected Byte[] tunnel = { (byte) 0xFF, (byte) 0xFF };
    // 功能字
    protected Byte[] functionWord;
    // 帧尾
    protected Byte[] tail = new Byte[] { (byte) 0xF2, (byte) 0x2F };

    /**
     * 协议byte数组
     */
    protected List<Byte> btsList;

    /**
     * 生成发送数据
     *
     * @return
     */
    public List<Byte> produceTheProtocolData() {
        getTunnel();
        List<Byte> backBts = new ArrayList<>();
        backBts.addAll(Arrays.asList(head));
        backBts.addAll(Arrays.asList(tunnel));
        backBts.addAll(Arrays.asList(functionWord));
        return backBts;
    }

    private void getTunnel() {
        try {
            tunnel =new Byte[] { (byte) 0xFF, (byte) 0xFF };
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 追加协议校验码和尾部
     *
     * @return
     */
    public List<Byte> appendTailProtocolData() {
        List<Byte> backBts = new ArrayList<>();
        backBts.add(ElecProtocolBase.getByteCheckSum(btsList.toArray(new Byte[btsList. size()]), 2,btsList.size()));
        backBts.addAll(Arrays.asList(tail));
        return backBts;
    }

    /**
     * 设置功能字
     * @param functionWord
     */
    public void setFunctionWord(Byte[] functionWord) {
        this.functionWord = functionWord;
    }

    @Override
    public String toString() {
        if (btsList == null)
            return "";
        return ElecProtocolUtil.bytesToHexString(btsList, 0, btsList.size());
    }

    /**
     * 获取校验码
     *
     * @param bts
     * @param startIndex
     * @return
     */
    public static byte getByteCheckSum(Byte[] bts, int startIndex, int lenght) {
        int sum = 0;
        for (int i = startIndex; i < lenght; i++) {
            sum += Math.abs(bts[i] & 0xFF);
        }
        sum = (~sum) + 1;
        sum = sum & 0xFF;
        if (sum > 240) {
            sum -= 16;
        }
        byte hexByte = (byte) (sum & 0xff);
        return hexByte;
    }

}
