package com.zkkj.gps.gateway.ccs.dto.dbDto;

import lombok.Data;

/**
 * author : cyc
 * Date : 2019/9/11
 */

@Data
public class UpdateAlarmConfigDto {

    /**
     * 运单编号
     */
    private String dispatchNo;

    /**
     * 终端编号
     */
    private String terminalNo;

    /**
     * 结束时间
     */
    private String endTime;
}
