package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol;


import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Type;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.MessageId;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;

/**
 * 终端鉴权
 * @author suibozhuliu
 */
@Type(MessageId.终端鉴权)
public class JT_0102 extends AbstractBody {

    /** 终端重连后上报鉴权码 */
    private String token;

    @Property(index = 0, type = DataType.STRING, desc = "鉴权码")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}