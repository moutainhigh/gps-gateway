package com.zkkj.gps.gateway.protocol.dto;

import com.zkkj.gps.gateway.protocol.component.messagebody.append.SiteAppendMessage;
import com.zkkj.gps.gateway.protocol.component.messagebody.basic.SiteBasicMessage;

import java.time.LocalDateTime;

/**
 * GPS位置信息
 */
public class GPSPositionDTO {
    //经纬度位置信息
    private SiteBasicMessage siteBasicMessage;

    /**
     * 位置追加信息
     */
    private SiteAppendMessage siteAppendMessage;


    //接收到经纬度的服务器时间
    private LocalDateTime rcvTime;

    public SiteBasicMessage getSiteBasicMessage() {
        return siteBasicMessage;
    }

    public void setSiteBasicMessage(SiteBasicMessage siteBasicMessage) {
        this.siteBasicMessage = siteBasicMessage;
    }

    public LocalDateTime getRcvTime() {
        return rcvTime;
    }

    public void setRcvTime(LocalDateTime rcvTime) {
        this.rcvTime = rcvTime;
    }

    public SiteAppendMessage getSiteAppendMessage() {
        return siteAppendMessage;
    }

    public void setSiteAppendMessage(SiteAppendMessage siteAppendMessage) {
        this.siteAppendMessage = siteAppendMessage;
    }

    public GPSPositionDTO(SiteBasicMessage siteBasicMessage, SiteAppendMessage siteAppendMessage, LocalDateTime rcvTime) {
        this.siteBasicMessage = siteBasicMessage;
        this.siteAppendMessage = siteAppendMessage;
        this.rcvTime = rcvTime;
    }

    public GPSPositionDTO() {
    }
    public GPSPositionDTO(SiteBasicMessage siteBasicMessage, LocalDateTime rcvTime) {
        this.siteBasicMessage = siteBasicMessage;

        this.rcvTime = rcvTime;
    }

    @Override
    public String toString() {
        return "GPSPositionDTO{" +
                "siteBasicMessage=" + siteBasicMessage +
                ", siteAppendMessage=" + siteAppendMessage +
                ", rcvTime=" + rcvTime +
                '}';
    }
}
