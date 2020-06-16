package com.zkkj.gps.gateway.tcp.monitor.utils;

import com.zkkj.gps.gateway.tcp.monitor.app.common.CarPlateEnum;
import org.springframework.util.StringUtils;

/**
 * 车牌处理工具类
 * @author suibozhuliu
 */
public class LicencePlateUtils {

    /**
     * 车牌加密处理（新能源车牌验证加密）
     * @param plateNum
     * @return
     */
    public static String licenceEncode(String plateNum) throws Exception {
        if (plateNum.length() < 7){
            return null;
        }
        if (plateNum.length() == 7){//传统车牌
            return plateNum;
        }
        if (plateNum.length() == 8){//新能源车牌
            String plateNumRegion = plateNum.substring(0,1);
            if (!StringUtils.isEmpty(plateNumRegion)){
                String identify = CarPlateEnum.getIdentify(plateNumRegion);
                if (!StringUtils.isEmpty(identify)){
                    plateNum = identify.concat(plateNum.substring(1,plateNum.length()));
                    return plateNum;
                }
            }
        }
        return null;
    }

    /**
     * 车牌解密处理（新能源车牌验证解密）
     * @param plateNum
     * @return
     */
    public static String licenceDecode(String plateNum) throws Exception {
        if (plateNum.length() == 7){//传统车牌
            return plateNum;
        }
        if (plateNum.length() == 9){//新能源车牌
            String plateIdentify = plateNum.substring(0,2);
            if (!StringUtils.isEmpty(plateIdentify)){
                String regiony = CarPlateEnum.getRegiony(plateIdentify);
                if (!StringUtils.isEmpty(regiony)){
                    plateNum = regiony.concat(plateNum.substring(2,plateNum.length()));
                    return plateNum;
                }
            }
        }
        return null;
    }

}
