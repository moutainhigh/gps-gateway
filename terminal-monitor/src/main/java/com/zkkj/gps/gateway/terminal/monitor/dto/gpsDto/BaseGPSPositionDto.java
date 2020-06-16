package com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto;

import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class BaseGPSPositionDto<T> extends BaseFlag {

    /**
     * 终端编号
     */
    @NotBlank(message = "终端编号不能为空")
    private String terminalId;

    /**
     * 系统时间
     */
    private LocalDateTime rcvTime;

    /**
     * 点位数据
     */
    private BasicPositionDto point;

    /**
     * 电子运单数据
     */
    private T eleDispatch;

}
