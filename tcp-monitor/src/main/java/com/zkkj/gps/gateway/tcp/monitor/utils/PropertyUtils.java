package com.zkkj.gps.gateway.tcp.monitor.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

/**
 * 配置文件读写工具
 *
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-29 下午 3:23
 */
@Slf4j
public class PropertyUtils {

    /**
     * 配置文件属性读取
     */
    public static String getProperties(String keyWord) {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "config" +File.separator + "application.properties";
            InputStream in = new BufferedInputStream (new FileInputStream(filePath));
            Properties properties = new Properties();
            properties.load(in);
            return properties.getProperty(keyWord);
        } catch (IOException e) {
            LoggerUtils.error(log,e.getMessage());
        }
        return null;
    }

}
