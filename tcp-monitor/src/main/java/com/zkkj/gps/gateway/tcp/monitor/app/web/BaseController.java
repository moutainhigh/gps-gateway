package com.zkkj.gps.gateway.tcp.monitor.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

/**
 * Controller基类
 * @author suibozhuliu
 */
public class BaseController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取终端服务类型：jt1：铜川大区；jt0：洛阳大区
     * @param terminalId
     * @return
     */
    public String getTerminalService (String terminalId) {
        if (StringUtils.isEmpty(terminalId)) {
            return "";
        }
        String typeStr = redisTemplate.opsForValue().get(terminalId);
        return typeStr;
    }

}
