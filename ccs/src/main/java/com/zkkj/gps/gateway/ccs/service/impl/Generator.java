package com.zkkj.gps.gateway.ccs.service.impl;

import com.zkkj.gps.gateway.ccs.service.IGenerator;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author : cyc
 * Date : 2019/7/23
 */

@Component
@Lazy
public class Generator implements IGenerator {

    @Autowired
    protected Mapper mapper;

    @Override
    public <T, S> T convert(S s, Class<T> clz) {
        Assert.notNull(s, "Source must not be null");
        return this.mapper.map(s, clz);
    }

    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @param <T>
     * @param <S>
     * @return
     * @Description: list深度复制
     */
    @Override
    public <T, S> List<T> convert(List<S> s, Class<T> clz) {
        Assert.notNull(s, "Source must not be null");
        List<T> list = new ArrayList<T>();
        for (S vs : s) {
            list.add(this.mapper.map(vs, clz));
        }
        return list;
    }

    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @param <T>
     * @param <S>
     * @return
     * @Description: Set深度复制
     */
    @Override
    public <T, S> Set<T> convert(Set<S> s, Class<T> clz) {
        Assert.notNull(s, "Source must not be null");
        Set<T> set = new HashSet<T>();
        for (S vs : s) {
            set.add(this.mapper.map(vs, clz));
        }
        return set;
    }

    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @param <T>
     * @param <S>
     * @return
     * @Description: 数组深度复制
     */
    @Override
    public <T, S> T[] convert(S[] s, Class<T> clz) {
        Assert.notNull(s, "Source must not be null");
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clz, s.length);
        for (int i = 0; i < s.length; i++) {
            arr[i] = this.mapper.map(s[i], clz);
        }
        return arr;
    }
}
