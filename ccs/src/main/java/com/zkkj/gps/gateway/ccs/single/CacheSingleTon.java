package com.zkkj.gps.gateway.ccs.single;

import com.google.common.collect.Maps;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;

import java.util.List;
import java.util.Map;

/**
 * author : cyc
 * Date : 2019/12/2
 * 针对缓存的单例类
 */
public class CacheSingleTon {

    /**
     * 中交兴路车辆缓存
     */
    private Map<String, List<TruckAndTerminal>> zjxlCacheMap;

    private CacheSingleTon() {
        zjxlCacheMap = Maps.newHashMap();
    }

    private volatile static CacheSingleTon cacheSingleTon = null;

    public static CacheSingleTon getInstance() {
        if (cacheSingleTon == null) {
            synchronized (CacheSingleTon.class) {
                if (cacheSingleTon == null) {
                    cacheSingleTon = new CacheSingleTon();
                }
            }
        }
        return cacheSingleTon;
    }

    public Map<String, List<TruckAndTerminal>> getZjxlCacheMap() {
        return zjxlCacheMap;
    }
}
