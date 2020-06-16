package com.zkkj.gps.gateway.ccs.listener;

import com.zkkj.gps.gateway.ccs.config.AsyncConfig;
import com.zkkj.gps.gateway.ccs.dto.dbDto.SysLogDbDto;
import com.zkkj.gps.gateway.ccs.service.SysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * author : cyc
 * Date : 2020/3/19
 */
@Component
public class RedisReceiverListener {

    private Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    @Autowired
    private SysLogService logService;

    public void receiveListenerMessage(Object o) {
        try {
            logService.insertSysLog((SysLogDbDto) o);
        } catch (Exception e) {
            logger.error("RedisReceiverListener.receiveListenerMessage is error", e);
        }
    }

}
