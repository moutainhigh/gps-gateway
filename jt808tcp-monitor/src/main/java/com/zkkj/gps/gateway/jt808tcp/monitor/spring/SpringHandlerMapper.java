package com.zkkj.gps.gateway.jt808tcp.monitor.spring;

import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Endpoint;
import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Mapping;
import com.zkkj.gps.gateway.jt808tcp.monitor.commons.ClassUtils;
import com.zkkj.gps.gateway.jt808tcp.monitor.mapping.Handler;
import com.zkkj.gps.gateway.jt808tcp.monitor.mapping.HandlerMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringHandlerMapper implements HandlerMapper, ApplicationContextAware {

    protected String[] packageNames;
    private Map<Integer, Handler> handlerMap = new HashMap(55);

    public SpringHandlerMapper(String... packageNames) {
        this.packageNames = packageNames;
    }

    @Override
    public Handler getHandler(Integer key) {
        return handlerMap.get(key);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        for (String packageName : packageNames) {
            List<Class<?>> handlerClassList = ClassUtils.getClassList(packageName, Endpoint.class);
            for (Class<?> handlerClass : handlerClassList) {
                Method[] methods = handlerClass.getDeclaredMethods();
                if (methods != null) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Mapping.class)) {
                            Mapping annotation = method.getAnnotation(Mapping.class);
                            String desc = annotation.desc();
                            int[] types = annotation.types();
                            Handler value = new Handler(applicationContext.getBean(handlerClass), method, desc);
                            for (int type : types) {
                                handlerMap.put(type, value);
                            }
                        }
                    }
                }
            }
        }
    }
}