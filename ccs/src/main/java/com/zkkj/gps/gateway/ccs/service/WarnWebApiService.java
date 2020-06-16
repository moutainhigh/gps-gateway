package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.warn.AmountWarnDto;
import com.zkkj.gps.gateway.ccs.dto.warn.ElecBusinessWarnDto;
import feign.Headers;
import feign.RequestLine;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author : cyc
 * Date : 2019/12/19
 */
public interface WarnWebApiService {

    /**
     * 矿发量实收量提醒
     *
     * @param amountWarnDto
     * @return
     */
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST /amount/amountAlarm")
    @Async
    void amountAlarm(@RequestBody AmountWarnDto amountWarnDto);

    /**
     * 电子运单下发失败提醒
     * @param elecBusinessWarnDto
     */
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST /senddispatch/sendDispatchAlarm")
    @Async
    void sendDispatchAlarm(@RequestBody ElecBusinessWarnDto elecBusinessWarnDto);

}
