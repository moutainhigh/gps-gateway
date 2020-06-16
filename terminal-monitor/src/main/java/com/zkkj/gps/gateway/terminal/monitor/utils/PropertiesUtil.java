package com.zkkj.gps.gateway.terminal.monitor.utils;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * author : cyc
 * Date : 2019/8/12
 * 读取property文件工具类
 */
public class PropertiesUtil {

    /**
     * 根据key读取value
     *
     * @param filePath
     * @param key
     * @return
     */
    public static String readValue(String filePath, String key) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new BufferedInputStream(PropertiesUtil.class.getResourceAsStream(filePath));
        properties.load(inputStream);
        return properties.getProperty(key);
    }

    /**
     * 获取配置文件中的所有的信息，写成格式 key=value;key1=value1;
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readAllValue(String filePath) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        Properties properties = new Properties();
        InputStream inputStream = new BufferedInputStream(PropertiesUtil.class.getResourceAsStream(filePath));
        properties.load(inputStream);
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String vaule = properties.getProperty(key);
            stringBuffer.append(key).append("=").append(vaule).append(";");
        }
        inputStream.close();
        return stringBuffer.toString();
    }

    /*public static void writeProperties(String  filePath, String parameterName, String parameterValue) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new BufferedInputStream(PropertiesUtil.class.getResourceAsStream("/alarm.properties"));
        properties.load(inputStream);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        properties.setProperty(parameterName, parameterValue);
        properties.store(outputStream, "Update '" + parameterName + "' value");

    }*/

}
