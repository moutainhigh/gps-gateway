package com.zkkj.gps.gateway.jt808tcp.monitor.test.Classdemo;

import com.zkkj.gps.gateway.jt808tcp.monitor.test.demo01.User;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

//通过反射获取泛型
public class Test02 {

    public void test01(Map<String,User> map, List<User> list){
        System.out.println("test01");
    }

    public Map<String,User> test02(){
        System.out.println("test02");
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        /*Method test01 = Test02.class.getMethod("test01", Map.class, List.class);
        Type[] types = test01.getGenericParameterTypes();
        for (Type type : types) {
            System.out.println("#" + type);
            if (type instanceof ParameterizedType){
                Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                for (Type argument : arguments) {
                    System.out.println("#" + argument);
                }
            }
        }*/

        Method test02 = Test02.class.getMethod("test02",null);
        //方法返回值类型
        Type type = test02.getGenericReturnType();
        if (type instanceof ParameterizedType){
            Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
            for (Type argument : arguments) {
                System.out.println("#" + argument);
            }
        }
    }

}
