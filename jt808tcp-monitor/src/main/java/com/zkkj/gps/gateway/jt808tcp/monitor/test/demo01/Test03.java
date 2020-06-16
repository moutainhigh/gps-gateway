package com.zkkj.gps.gateway.jt808tcp.monitor.test.demo01;

import java.lang.annotation.*;
import java.lang.reflect.Field;

//反射操作注解
public class Test03 {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        Class c1 = Class.forName("com.zkkj.gps.gateway.jt808tcp.monitor.test.demo01.Students");
        //通过反射获得注解
        Annotation[] annotations = c1.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        //获得注解的value的值
        TableStu tableStu = (TableStu)c1.getAnnotation(TableStu.class);
        System.out.println(tableStu.value());

        //获得类指定的注解
        Field name = c1.getDeclaredField("name");
        FieldAnnotation fieldAnnotation = name.getAnnotation(FieldAnnotation.class);
        System.out.println(fieldAnnotation.columnName());
        System.out.println(fieldAnnotation.type());
        System.out.println(fieldAnnotation.length());
    }

}

@TableStu("tb_student")
class Students{
    @FieldAnnotation(columnName = "db_name",type = "varchar",length = 20)
    private String name;
    @FieldAnnotation(columnName = "db_age",type = "int",length = 10)
    private int age;
    @FieldAnnotation(columnName = "db_id",type = "int",length = 255)
    private int id;

    public Students() {
    }

    public Students(String name, int age, int id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Students{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", id=" + id +
                '}';
    }
}

//类名的注解
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface TableStu{
    String value();
}

//属性的注解
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface FieldAnnotation{
    String columnName();
    String type();
    int length();
}