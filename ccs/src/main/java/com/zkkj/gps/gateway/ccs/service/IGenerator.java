package com.zkkj.gps.gateway.ccs.service;

import java.util.List;
import java.util.Set;

/**
 * author : cyc
 * Date : 2019/7/23
 */
public interface IGenerator {

    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @return
     * @Description: 单个对象的深度复制及类型转换
     */
    <T, S> T convert(S s, Class<T> clz);


    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @return
     * @Description: list深度复制
     */
    <T, S> List<T> convert(List<S> s, Class<T> clz);

    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @return
     * @Description: set深度复制
     */
    <T, S> Set<T> convert(Set<S> s, Class<T> clz);

    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @return
     * @Description: 数组深度复制
     */
    <T, S> T[] convert(S[] s, Class<T> clz);
}
