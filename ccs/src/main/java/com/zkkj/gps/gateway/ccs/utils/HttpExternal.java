package com.zkkj.gps.gateway.ccs.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.token.TokenUser;
import com.zkkj.gps.gateway.ccs.service.IGenerator;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.common.utils.HttpUtil;

@Configuration
public class HttpExternal {


    private static Logger logger = LoggerFactory.getLogger(HttpExternal.class);

    @Value("${urlForCommon}")
    private String urlForMapProperties;

    private static String urlForMap;

    @PostConstruct
    public void initConstruct() {
        urlForMap = urlForMapProperties;
    }

    @Autowired
    private IGenerator iGenerator;


//    /*
//     * @Author lx
//     * @Description 访问外部方法验证
//     * @Date 17:39 2019/5/23
//     * @Param
//     * @return
//     **/
//    public static TokenUser getTokenUserByToken(String token) {
//        TokenUser tokenUser = null;
//        try {
//            Map<String, String> mapHeader = new HashMap<>();
//            StringBuilder stringBuilder = HttpUtil.get(urlForMap + "platformLogin/getTerminalInfoByToken?token=" + token, mapHeader);
//            ResultVo<TokenUser> tokenUserResultVo = JSON.parseObject(stringBuilder.toString(), new TypeReference<ResultVo<TokenUser>>() {
//            });
//            if (tokenUserResultVo != null && tokenUserResultVo.isSuccess()) {
//                TokenUser curUser = tokenUserResultVo.getData();
//                if (curUser != null && curUser.getAppkey() != null
//                        && curUser.getAppkey().length() > 0) {
////                    tokenUser = new TokenUser();
////                    BeanUtils.copyProperties(curUser, tokenUser);
//                    tokenUser = FastJsonUtils.toBean(FastJsonUtils.toJSONString(curUser), TokenUser.class);
//                }
//            }
//
//        } catch (Exception ex) {
//            logger.error("", ex);
//            ex.printStackTrace();
//        }
//        return tokenUser;
//    }

    public static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");
        return !"".equals(authorization) && null != authorization && !"null".equals(authorization) ? authorization : null;

    }

}
