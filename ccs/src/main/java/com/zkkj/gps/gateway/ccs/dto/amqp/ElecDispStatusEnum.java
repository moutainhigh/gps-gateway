package com.zkkj.gps.gateway.ccs.dto.amqp;

import java.util.HashMap;
import java.util.Map;

/**
 * 电子运单状态枚举类
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-07-03 下午 3:27
 */
public enum ElecDispStatusEnum {

    SET_UP(0,"新增"),
    AWAIT_LOAD(1,"待装货"),
    LOAD_FINISH(2,"装货完成"),
    TRANSPORT_ING(3,"运输中"),
    AWAIT_UNLOAD(4,"待卸货"),
    UNLOAD_ING(5,"正在卸货"),
    LEAVE_SPACE(6,"出厂"),
    DISPATCH_FINISH(7,"运单完成");

    /**
     * 电子运单状态
     */
    private final Integer status;
    /**
     * 电子运单状态说明
     */
    private final String statusDesc;

    ElecDispStatusEnum(int status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public Integer getKey() {
        return status;
    }

    public String getValue() {
        return statusDesc;
    }

    /**
     * 根据key获取value
     * @param key : 键值key
     * @return String
     */
    public static String getValueByKey(Integer key) {
        ElecDispStatusEnum[] enums = ElecDispStatusEnum.values();
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].getKey().equals(key)) {
                return enums[i].getValue();
            }
        }
        return "";
    }

    /**
     * 转换为MAP集合
     *
     * @returnMap<Integer, String>
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<>();
        ElecDispStatusEnum[] enums = ElecDispStatusEnum.values();
        for (int i = 0; i < enums.length; i++) {
            map.put(enums[i].getKey(), enums[i].getValue());
        }
        return map;
    }

}
