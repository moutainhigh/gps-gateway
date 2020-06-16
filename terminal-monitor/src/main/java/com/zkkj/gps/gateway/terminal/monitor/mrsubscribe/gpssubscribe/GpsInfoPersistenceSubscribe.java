package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpssubscribe;

import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsOriginalInfoEvent;
import org.greenrobot.eventbus.Subscribe;

public class GpsInfoPersistenceSubscribe {
    @Subscribe
    public void  subscribe(GpsOriginalInfoEvent gpsInfoEvent)
    {
        //TODO 持久化经纬度信息
    }
}
