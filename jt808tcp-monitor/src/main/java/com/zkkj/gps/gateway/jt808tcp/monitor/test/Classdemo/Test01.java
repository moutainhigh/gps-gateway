package com.zkkj.gps.gateway.jt808tcp.monitor.test.Classdemo;

import com.zkkj.gps.gateway.jt808tcp.monitor.test.demo01.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

///动态的创建对象
public class Test01 {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        //获得class对象
        Class c1 = Class.forName("com.zkkj.gps.gateway.jt808tcp.monitor.test.demo01.User");
        //构造对象,本质上调用了类的无参构造器
        //User user = (User) c1.newInstance();
        //System.out.println(user);

        //通过构造器创建对象
        /*Constructor constructor = c1.getDeclaredConstructor(String.class, int.class, int.class);
        User user = (User) constructor.newInstance("张三", 12, 34);
        System.out.println(user);*/

        //通过反射调用方法
        User user1 = (User) c1.newInstance();
        //通过反射获取一个方法（getDeclaredMethod()获取本类的制定形参类型的构造器）
        Method setName = c1.getDeclaredMethod("setName", String.class);
        //invoke：激活的意思，第一个参数是对象，第二个参数是方法的值
        setName.invoke(user1,"李四");
        System.out.println(user1.getName());

        //通过反射操作属性
        User user2 = (User) c1.newInstance();
        Field id = c1.getDeclaredField("id");
        //不能直接操作私有属性，需要关闭程序的安全监测，通过属性或方法的setAccessible(true)
        id.setAccessible(true);
        id.set(user2,12);
        System.out.println(user2.getId());

    }
}
