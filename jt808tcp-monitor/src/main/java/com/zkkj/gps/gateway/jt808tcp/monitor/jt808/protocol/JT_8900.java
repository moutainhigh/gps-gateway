package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol;

import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Type;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.MessageId;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;

/**
 * 数据下行透传
 * @author suibozhuliu
 */
@Type(MessageId.数据下行透传)
public class JT_8900 extends AbstractBody {

    // 透传消息类型
    private Integer type;

    //透传消息内容
    private byte[] content;

    @Property(index = 0, type = DataType.BYTE, desc = "透传消息类型")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Property(index = 1, type = DataType.BYTES, desc = "透传消息内容")
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
