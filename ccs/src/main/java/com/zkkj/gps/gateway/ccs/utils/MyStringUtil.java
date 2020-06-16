package com.zkkj.gps.gateway.ccs.utils;

import com.zkkj.gps.gateway.common.constant.BaseConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author : cyc
 * Date : 2019/8/19
 */
public class MyStringUtil {

    private static Logger logger = LoggerFactory.getLogger(MyStringUtil.class);

    public static String subStr(String source, int len) {
        if (StringUtils.isBlank(source)) {
            return "";
        }
        byte[] bytes = null;
        try {
            bytes = source.getBytes(BaseConstant.CHAR_SET);
            int length = bytes.length;
            //如果截取的字符串长度小于要截取的长度或者要截取的长度小于1，则返回原来字符串
            if (len >= length || len < 1) {
                return source;
            }
            int count = 0;
            for (int i = 0; i < len; i++) {
                //将每个字节数组转化为整型数，根据值的正负来判断是都为汉子
                int value = bytes[i];
                if (value < 0) {
                    //如果是汉子(负数),则统计截取字符串中汉子所占的字节数
                    count++;
                }
            }
            if (count % 2 != 0) {
                //如果在截取的长度为1时，则将该汉子取出
                //其他情况则按字符长度截取(截取字节长度数-截取汉子字节数/2- 截取到的半个汉子字节数)
                len = (len == 1) ? len : len - count / 2 - 1;
            } else {
                len = len - (count / 2);
            }
            return source.substring(0, len);
        } catch (Exception e) {
            logger.error("MyStringUtil.subStr is error", e);
            return source;
        }
    }
}
