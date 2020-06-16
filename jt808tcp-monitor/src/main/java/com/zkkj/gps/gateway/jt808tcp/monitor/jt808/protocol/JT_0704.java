package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol;


import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Type;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.MessageId;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;

import java.util.List;

/**
 * 定位数据批量上传
 * @author suibozhuliu
 */
@Type(MessageId.定位数据批量上传)
public class JT_0704 extends AbstractBody {

    private Integer total;
    private Integer type;
    private List<Item> list;

    @Property(index = 0, type = DataType.WORD, desc = "数据项个数")
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Property(index = 2, type = DataType.BYTE, desc = "位置数据类型 0：正常位置批量汇报，1：盲区补报")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Property(index = 3, type = DataType.LIST, desc = "位置汇报数据项")
    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }

    public static class Item {

        private Integer length;
        private JT_0200 position;

        @Property(index = 0, type = DataType.WORD, desc = "位置汇报数据体长度")
        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        @Property(index = 2, type = DataType.OBJ, lengthName = "length", desc = "位置汇报数据项")
        public JT_0200 getPosition() {
            return position;
        }

        public void setPosition(JT_0200 position) {
            this.position = position;
        }

    }
}