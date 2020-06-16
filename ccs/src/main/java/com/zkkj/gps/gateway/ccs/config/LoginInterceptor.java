package com.zkkj.gps.gateway.ccs.config;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zkkj.gps.gateway.ccs.annotation.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.utils.HttpExternal;

/*
 * @Author lx
 * @Description  调用接口前添加对应的权限认证信息
 * @Date 9:04 2019/6/19
 * @Param
 * @return
 **/
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisDao redisDao;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //方法上含有TokenFilter注解放行，不需要token校验
        if (dealFilterToken(handler))
            return true;
        String token = HttpExternal.getToken();
        if (token != null && !token.equals("")) {
            String tokenUser = redisDao.getValue(token);
            if (tokenUser != null && tokenUser.length() > 0) {
                return true;
            }
        }
        response.setStatus(401);
        response.setHeader("errorInfo", "You are not logged in map, please log in again");
        ResultVo result = new ResultVo();
        result.resultFail("You are not logged in map, please log in again");
        PrintWriter out = response.getWriter();
        out.println(JSON.toJSON(result));
        return false;
    }

    /**
     * 对含有TokenFilter注解放行
     * @param handler
     * @return
     */
    private boolean dealFilterToken(Object handler) {
        /**
         * isAssignableFrom 该方法意思就是判断当前对象是否可以被目标对象强转
         * 1.A是B的超类或者接口
         * 2.A和B是同一接口或者对象
         */
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethodAnnotation(TokenFilter.class) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}