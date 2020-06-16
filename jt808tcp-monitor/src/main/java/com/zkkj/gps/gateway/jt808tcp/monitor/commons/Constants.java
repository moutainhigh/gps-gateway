package com.zkkj.gps.gateway.jt808tcp.monitor.commons;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 应用程序全局变量、常亮类
 * @author suibozhuliu
 */
public class Constants {

    /**
     * 缓存所有的终端编号
     */
    public static CopyOnWriteArrayList<String> terminalCacheList = new CopyOnWriteArrayList<>();

    //线程池
    //public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    //public static List<Object> listItem = Lists.newArrayList();

    //public static Map<String,Object> objCacheMap = new Hashtable<>();

}
