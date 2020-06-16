package com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean;

import lombok.Data;

/**
 * 中交兴路定位基础模型
 * @param <T>
 *     @author suibozhuliu
 */
@Data
public class ZjxlBaseBean<T> {

    private T result;//数据内容

    private String status;//状态码
}
