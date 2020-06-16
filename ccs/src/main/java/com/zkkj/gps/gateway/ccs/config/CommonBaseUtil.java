package com.zkkj.gps.gateway.ccs.config;

import com.zkkj.gps.gateway.ccs.dto.token.TokenUser;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.utils.JSonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Repository
public class CommonBaseUtil {

    @Autowired
    private RedisDao redisDao;

    private Logger logger = LoggerFactory.getLogger(CommonBaseUtil.class);

    public CommonBaseUtil() {
    }

    public TokenUser getUserInfo() {
        TokenUser tokenUser = new TokenUser();
        String token = getToken();
        //将token进行解析
        if (!"".equals(token) && null != token && !"null".equals(token)) {
            String tokenStr = redisDao.getValue(token);
            tokenUser = JSonUtils.readValue(tokenStr, TokenUser.class);
        }
        return tokenUser;
    }

    public static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");
        return !"".equals(authorization) && null != authorization && !"null".equals(authorization) ? authorization : null;

    }

    public String getAppkey() {
        TokenUser tokenUser = this.getUserInfo();
        return tokenUser != null ? tokenUser.getAppkey() : null;
    }

    public String getAppsecret() {
        TokenUser tokenUser = this.getUserInfo();
        return tokenUser != null ? tokenUser.getAppsecret() : null;
    }

    public String getIdentity() {
        TokenUser tokenUser = this.getUserInfo();
        return tokenUser != null ? tokenUser.getIdentity() : null;
    }

    public List<TruckAndTerminal> getTruckAndTerminal() {
        TokenUser tokenUser = this.getUserInfo();
        return tokenUser != null ? tokenUser.getTruckAndTerminalList() : null;
    }

}
