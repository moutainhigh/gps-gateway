package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity;


import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Type;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.MessageId;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;

import java.util.List;

/**
 * 终端参数模型
 * @author suibozhuliu
 */
@Type(MessageId.查询指定终端参数)
public class ParameterSetting extends AbstractBody {

    private Integer total;
    private List<TerminalParameter> parameters;

    @Property(index = 0, type = DataType.BYTE, desc = "参数总数")
    public Integer getTotal() {
        if (parameters == null || parameters.isEmpty())
            return 0;
        return parameters.size();
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Property(index = 1, type = DataType.LIST, desc = "参数项列表")
    public List<TerminalParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<TerminalParameter> parameters) {
        this.parameters = parameters;
    }
}