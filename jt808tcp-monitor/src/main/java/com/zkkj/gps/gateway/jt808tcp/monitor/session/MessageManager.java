package com.zkkj.gps.gateway.jt808tcp.monitor.session;

import com.zkkj.gps.gateway.jt808tcp.monitor.message.SyncFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息管理类
 * @author suibozhuliu
 */
public enum MessageManager {

    INSTANCE;

    private Map<String, SyncFuture> map = new ConcurrentHashMap<>();


    public SyncFuture receive(String key) {
        SyncFuture future = new SyncFuture();
        map.put(key, future);
        return future;
    }

    public void remove(String key) {
        map.remove(key);
    }

    public void put(String key, Object value) {
        SyncFuture syncFuture = map.get(key);
        if (syncFuture == null)
            return;
        syncFuture.setResponse(value);
    }

}
