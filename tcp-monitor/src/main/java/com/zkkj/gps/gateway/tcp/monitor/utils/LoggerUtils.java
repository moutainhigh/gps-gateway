package com.zkkj.gps.gateway.tcp.monitor.utils;

import org.slf4j.Logger;
import org.slf4j.MDC;

/**
 * 日志工具类
 *
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-26 上午 11:03
 */
public final class LoggerUtils {

    public static void info(Logger logger, String roomId, String msg) {
        MDC.put("ROOM_ID", roomId);
        logger.info(msg);
        MDC.remove("ROOM_ID");
    }

    public static void info(Logger logger, String roomId, String msg, Object... arguments) {
        MDC.put("ROOM_ID", roomId);
        logger.info(msg, arguments);
        MDC.remove("ROOM_ID");
    }

    public static void error(Logger logger, String roomId, String msg) {
        MDC.put("ROOM_ID", roomId);
        logger.error(msg);
        MDC.remove("ROOM_ID");
    }

    public static void error(Logger logger, String msg) {
        logger.error(msg);
    }

    public static void error(Logger logger, String roomId, String msg, Object... arguments) {
        MDC.put("ROOM_ID", roomId);
        logger.error(msg, arguments);
        MDC.remove("ROOM_ID");
    }

    public static void error(Logger logger, Exception e) {
        logger.error("", e);
    }

}
