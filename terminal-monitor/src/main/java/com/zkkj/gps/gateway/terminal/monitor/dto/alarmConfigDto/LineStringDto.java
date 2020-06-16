package com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto;

import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class LineStringDto implements Serializable {

    /**
     * id，由外部数据源传入，非主键
     */
    private String customLineId;

    /**
     * 线路名称
     */
    private String name;

    /**
     * 线路宽度，单位米
     */
    private double width;

    /**
     * 点序列，纬度(lat),经度(lng);，34.200753,108.920162;34.201081,108.921837;.......
     */
    private String pointSequence;

    /**
     * 是否在当前线路上的标识
     */
    private boolean onRouteLine;
}
