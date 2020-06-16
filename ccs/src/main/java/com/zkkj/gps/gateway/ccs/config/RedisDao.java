package com.zkkj.gps.gateway.ccs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Component
public class RedisDao {

    @Autowired
    private StringRedisTemplate template;

    public RedisDao() {
    }

    public void setKey(String key, String value) {
        //设置过期时间两个小时
        this.template.opsForValue().set(key, value, 60*60*2L, TimeUnit.SECONDS);
    }

    public void setKeyForApp(String key, String value) {
        this.template.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        Long expire = this.template.getExpire(key);
        String result = (String)this.template.opsForValue().get(key);
        if (result != null && expire != -1L) {
            this.template.opsForValue().set(key, result, 60*60*2L, TimeUnit.SECONDS);
            return result;
        } else {
            return result;
        }
    }

    public void deleteValue(String key) {
        this.template.delete(key);
    }

    public void setKeyTime(String key, String value, int secondNumber) {
        this.template.opsForValue().set(key, value, (long)secondNumber, TimeUnit.SECONDS);
    }
}
