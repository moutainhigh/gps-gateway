package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity;

import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.AttributeId;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 位置附加信息项
 * @author suibozhuliu
 */
public class PositionAttribute extends AbstractBody {

    private Integer id;
    private Integer length;
    private AttributeId idType;
    private byte[] bytesValue;
    private Object value;

    @Property(index = 0, type = DataType.BYTE, desc = "参数ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        this.idType = AttributeId.toEnum(id);
    }

    public AttributeId getIdType() {
        return idType;
    }

    public void setIdType(AttributeId idType) {
        this.idType = idType;
        this.id = idType.value;
    }

    @Property(index = 1, type = DataType.BYTE, desc = "参数长度")
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Property(index = 2, type = DataType.BYTES, lengthName = "length", desc = "参数值")
    public byte[] getBytesValue() {
        return bytesValue;
    }

    public void setBytesValue(byte[] bytesValue) {
        this.bytesValue = bytesValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}