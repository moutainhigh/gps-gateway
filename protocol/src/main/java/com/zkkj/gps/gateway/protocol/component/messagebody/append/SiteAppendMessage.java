package com.zkkj.gps.gateway.protocol.component.messagebody.append;

import com.zkkj.gps.gateway.protocol.ProtocolSerializable;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.apd01.Append_01;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.apd02.Append_02;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.apd33.Append_33;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.apd34.Append_34;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.apd35.Append_35;
import com.zkkj.gps.gateway.protocol.component.messagebody.append.apd36.Append_36;
import com.zkkj.gps.gateway.protocol.util.BitOperator;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author chailixing
 * 2018/12/29 11:40
 * 位置追加信息
 */
public final class SiteAppendMessage implements ProtocolSerializable {
    //里程
    private Append_01 append_01;

    //油量
    private Append_02 append_02;

    //电量、防拆触发等
    private Append_33 append_33;

    //子锁信息
    private Append_34 append_34;

    //基站信息
    private Append_35 append_35;

    //业务信息，电子运单
    private Append_36 append_36;

    public SiteAppendMessage(byte[] bytes) throws Exception {
        this.decoder(bytes);
    }

    public SiteAppendMessage() {
    }

    public SiteAppendMessage(Append_33 append_33) {
        this.append_33 = append_33;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Stream.of(append_01, append_02, append_33, append_34, append_35, append_36)
                .map(Optional::ofNullable)
                .forEach(x -> stringBuilder.append(x.toString()).append("\t"));
        return stringBuilder.toString();
    }

    @Override
    public void decoder(byte[] bytes) throws Exception {
        if (bytes.length > 0) {
            cutData(bytes);
        }
    }

    private void cutData(byte[] bytes) throws Exception {
        append01(bytes);
    }

    private void append01(byte[] bytes) throws Exception {
        if (bytes[0] == 0X01) {
            append_01 = new Append_01(Arrays.copyOfRange(bytes, 0, 6));
            if (bytes.length > 6) {
                cutData(Arrays.copyOfRange(bytes, 6, bytes.length));
            }
            return;
        }
        append02(bytes);
    }

    private void append02(byte[] bytes) throws Exception {
        if (bytes[0] == 0X02) {
            append_02 = new Append_02(Arrays.copyOfRange(bytes, 0, 4));
            if (bytes.length > 4) {
                cutData(Arrays.copyOfRange(bytes, 4, bytes.length));
            }
            return;
        }
        append33(bytes);
    }

    private void append33(byte[] bytes) throws Exception {
        if (bytes[0] == 0X33) {
            int length = BitOperator.oneByteToInteger(bytes[1]);
            append_33 = new Append_33(Arrays.copyOfRange(bytes, 0, length + 2));
            if (bytes.length > length + 2) {
                cutData(Arrays.copyOfRange(bytes, length + 2, bytes.length));
            }
            return;
        }
        append34(bytes);
    }

    private void append34(byte[] bytes) throws Exception {
        if (bytes[0] == 0X34) {
            append_34 = new Append_34(Arrays.copyOfRange(bytes, 0, 4));
            if (bytes.length > 4) {
                cutData(Arrays.copyOfRange(bytes, 4, bytes.length));
            }
            return;
        }
        append35(bytes);
    }

    private void append35(byte[] bytes) throws Exception {
        if (bytes[0] == 0X35) {
            int length = BitOperator.oneByteToInteger(bytes[1]);
            append_35 = new Append_35(Arrays.copyOfRange(bytes, 0, length + 2));
            if (bytes.length > length + 2) {
                cutData(Arrays.copyOfRange(bytes, length + 2, bytes.length));
            }
            return;
        }
        append36(bytes);
    }

    private void append36(byte[] bytes) throws Exception {
        if (bytes[0] == 0X36) {
            int length = BitOperator.oneByteToInteger(bytes[1]);
            append_36 = new Append_36(Arrays.copyOfRange(bytes, 0, length + 2));
            if (bytes.length > length + 2) {
                cutData(Arrays.copyOfRange(bytes, length + 2, bytes.length));
            }
        } else {
            throw new RuntimeException("位置追加解析异常,没有匹配的追加信息id!");
        }
    }

    public Append_01 getAppend_01() {
        return append_01;
    }

    public Append_02 getAppend_02() {
        return append_02;
    }

    public Append_33 getAppend_33() {
        return append_33;
    }

    public Append_34 getAppend_34() {
        return append_34;
    }

    public Append_35 getAppend_35() {
        return append_35;
    }

    public Append_36 getAppend_36() {
        return append_36;
    }

    public void setAppend_01(Append_01 append_01) {
        this.append_01 = append_01;
    }

    public void setAppend_02(Append_02 append_02) {
        this.append_02 = append_02;
    }

    public void setAppend_33(Append_33 append_33) {
        this.append_33 = append_33;
    }

    public void setAppend_34(Append_34 append_34) {
        this.append_34 = append_34;
    }

    public void setAppend_35(Append_35 append_35) {
        this.append_35 = append_35;
    }

    public void setAppend_36(Append_36 append_36) {
        this.append_36 = append_36;
    }


}