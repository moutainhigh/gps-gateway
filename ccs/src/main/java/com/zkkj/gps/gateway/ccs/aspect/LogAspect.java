package com.zkkj.gps.gateway.ccs.aspect;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import com.zkkj.gps.gateway.ccs.dto.token.PlatformLogin;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zkkj.gps.gateway.ccs.annotation.NoAccessResponseLogger;
import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dbDto.SysLogDbDto;
import com.zkkj.gps.gateway.ccs.enume.RedisChannelEnum;
import com.zkkj.gps.gateway.ccs.utils.WebUtils;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;

/**
 * author : cyc
 * Date : 2019/7/23
 * 日志切面类
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommonBaseUtil commonBaseUtil;

    //切点，对包含NoLogger注解的方法不会记录到文件和日志表中
    @Pointcut("execution(public * com.zkkj.gps.gateway.ccs.controller.*Controller.*(..))&& !@annotation(com.zkkj.gps.gateway.ccs.annotation.NoLogger)")
    public void logger() {
    }

    //环绕通知
    @Around("logger()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        return getSysLogDbDto(joinPoint);
    }

    private Object getSysLogDbDto(ProceedingJoinPoint joinPoint) {
        Object proceed = null;
        SysLogDbDto sysLogDbDto = new SysLogDbDto();
        LocalDateTime startTime = DateTimeUtils.getCurrentLocalDateTime();
        try {
            HttpServletRequest request = WebUtils.getRequest();
            sysLogDbDto.setIp(WebUtils.getClientIp(request));
            sysLogDbDto.setUrl(request.getRequestURL().toString());
            sysLogDbDto.setMethodType(request.getMethod());
            sysLogDbDto.setAccessParameter(FastJsonUtils.toJSONString(joinPoint.getArgs()));
            String appKey;
            if (StringUtils.isBlank(commonBaseUtil.getAppkey())) {
                try {
                    appKey = ((PlatformLogin) joinPoint.getArgs()[0]).getAppkey();
                } catch (Exception e) {
                    appKey = "";
                }
                sysLogDbDto.setRequestSource(appKey);
            } else {
                sysLogDbDto.setRequestSource(commonBaseUtil.getAppkey());
            }
            sysLogDbDto.setController(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            sysLogDbDto.setRequestTime(DateTimeUtils.formatLocalDateTime(startTime));
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            NoAccessResponseLogger noFileLogger = methodSignature.getMethod().getAnnotation(NoAccessResponseLogger.class);

            proceed = joinPoint.proceed();
            ResultVo resultVo = FastJsonUtils.toBean(FastJsonUtils.toJSONString(proceed), ResultVo.class);
            sysLogDbDto.setResponseTime(DateTimeUtils.formatLocalDateTime(DateTimeUtils.getCurrentLocalDateTime()));
            sysLogDbDto.setExecutionDuration(DateTimeUtils.durationMillis(startTime, DateTimeUtils.getCurrentLocalDateTime()));
            sysLogDbDto.setAccessResponse(noFileLogger == null ? FastJsonUtils.toJSONString(proceed) : FastJsonUtils.toJSONString(ResultVo.builder().success(resultVo.isSuccess()).msg(resultVo.getMsg()).build()));
        } catch (Throwable throwable) {
            sysLogDbDto.setResponseTime(DateTimeUtils.formatLocalDateTime(DateTimeUtils.getCurrentLocalDateTime()));
            sysLogDbDto.setExecutionDuration(DateTimeUtils.durationMillis(startTime, DateTimeUtils.getCurrentLocalDateTime()));
            sysLogDbDto.setAccessResponse(FastJsonUtils.toJSONString(ResultVo.builder().success(false).msg(throwable.getMessage())));
        }
        redisTemplate.convertAndSend(RedisChannelEnum.SYSLOG.name(), sysLogDbDto);
        return proceed;
    }

}
