package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity;

import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.ParameterId;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.BitOperator;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.HexStringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.UnsupportedEncodingException;

/**
 * 终端参数项模型
 * @author suibozhuliu
 */
public class TerminalParameter extends AbstractBody {

    private Integer id;
    private ParameterId idType;
    private Integer length;
    private byte[] bytesValue;
    private Object value;

    public TerminalParameter() {
    }

    /**
     * 全参构造器
     * @param id
     * @param idType
     * @param length
     * @param bytesValue
     * @param value
     */
    public TerminalParameter(Integer id, ParameterId idType, Integer length, byte[] bytesValue, Object value) {
        this.id = id;
        this.idType = idType;
        this.length = length;
        this.bytesValue = bytesValue;
        this.value = value;
    }

    public TerminalParameter(Integer id) {
        this.id = id;
    }

    @Property(index = 0, type = DataType.DWORD, desc = "参数ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        this.idType = ParameterId.toEnum(id);
    }

    public ParameterId getIdType() {
        return idType;
    }

    public void setIdType(ParameterId idType) {
        this.idType = idType;
        this.id = idType.value;
    }

    @Property(index = 2, type = DataType.BYTE, desc = "参数长度")
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Property(index = 3, type = DataType.BYTES, lengthName = "length", desc = "参数值")
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