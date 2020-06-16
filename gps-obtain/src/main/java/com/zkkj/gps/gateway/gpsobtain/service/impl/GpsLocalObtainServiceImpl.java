package com.zkkj.gps.gateway.gpsobtain.service.impl;

import com.zkkj.gps.gateway.gpsobtain.entity.ResultVo;
import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsquery.AuthlbsQueryBean;
import com.zkkj.gps.gateway.gpsobtain.entity.gpsobtain.GpsObtainBean;
import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;
import com.zkkj.gps.gateway.gpsobtain.service.AuthlbsLocalService;
import com.zkkj.gps.gateway.gpsobtain.service.GpsLocalObtainService;
import com.zkkj.gps.gateway.gpsobtain.service.ZjxlGpsObtainService;
import com.zkkj.gps.gateway.gpsobtain.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.zkkj.gps.gateway.gpsobtain.utils.DateTimeUtils.strToLocalDateTime;

/**
 * 本地远程调用获取定位信息接口业务实现
 * @author suibozhuliu
 */
@Service
@Slf4j
public class GpsLocalObtainServiceImpl implements GpsLocalObtainService {

    //基站定位接口
    @Autowired
    private AuthlbsLocalService authlbsLocalService;

    //中交兴路定位接口
    @Autowired
    private ZjxlGpsObtainService zjxlGpsObtainService;

    @Override
    public ResultVo<PointBaseBean> getGpsInfo(GpsObtainBean gpsObtainBean) {
        ResultVo<PointBaseBean> resultVo = new ResultVo<>();
        resultVo.resultFail("获取定位失败！");
        if (ObjectUtils.isEmpty(gpsObtainBean) ||
                StringUtils.isEmpty(gpsObtainBean.getPhoneNum()) ||
                StringUtils.isEmpty(gpsObtainBean.getLicensePlate())){
            resultVo.resultFail("参数异常，请检查！");
            return resultVo;
        }
        if (gpsObtainBean.getPhoneNum().trim().replaceAll(" ","").length() != 11){
            resultVo.resultFail("不合法的参数，电话号码不正确！");
            return resultVo;
        }
        String phoneNum = gpsObtainBean.getPhoneNum();
        String licensePlate = gpsObtainBean.getLicensePlate();
        //1.中交兴路

        try {
            ResultVo<PointBaseBean> zjxlGpsObtainRes = zjxlGpsObtainService.getGpsByLicensePlate(licensePlate);
            if (!ObjectUtils.isEmpty(zjxlGpsObtainRes) && !ObjectUtils.isEmpty(zjxlGpsObtainRes.getData()) && zjxlGpsObtainRes.isSuccess()){
                PointBaseBean pointBaseBean = zjxlGpsObtainRes.getData();
                System.out.println(pointBaseBean.toString());
            }
        } catch (Exception e){
            LoggerUtils.error(log,phoneNum,"中交兴路定位异常：【" + e + "】");
            resultVo.resultFail("定位异常，请重试！");
        }

        //2.基站定位
        /*try {
            ResultVo<AuthlbsQueryBean> authlbsQuery = authlbsLocalService.authlbsquery(phoneNum);
            if (!ObjectUtils.isEmpty(authlbsQuery)){
                if (authlbsQuery.isSuccess()){
                    if (!ObjectUtils.isEmpty(authlbsQuery.getData())){//基站定位数据正常返回，定位成功
                        PointBaseBean baseBean = transformPosition(authlbsQuery.getData(), 4);
                        baseBean.setPhoneNum(gpsObtainBean.getPhoneNum());
                        baseBean.setLicensePlate(gpsObtainBean.getLicensePlate());
                        resultVo.resultSuccess(baseBean,"定位成功！");
                    } else {
                        if (!StringUtils.isEmpty(authlbsQuery.getMsg())){
                            resultVo.resultSuccess(null,authlbsQuery.getMsg());
                        }
                    }
                } else {
                    if (!StringUtils.isEmpty(authlbsQuery.getMsg())){
                        resultVo.resultFail(authlbsQuery.getMsg());
                    }
                }
            }
        } catch (Exception e) {
            LoggerUtils.error(log,phoneNum,"手机基站定位异常：【" + e + "】");
            resultVo.resultFail("定位异常，请重试！");
        }*/
        return resultVo;
    }

    /**
     * 点位转换
     * @param originalData
     */
    private PointBaseBean transformPosition(Object originalData,int flag) {
        PointBaseBean pointBaseBean = null;
        try {
            switch (flag){
                case 4://基站定位
                    AuthlbsQueryBean queryBean = (AuthlbsQueryBean) originalData;
                    if (!ObjectUtils.isEmpty(queryBean)){
                        pointBaseBean = new PointBaseBean();
                        pointBaseBean.setRcvTime(LocalDateTime.now());
                        pointBaseBean.setGpsTime(strToLocalDateTime(queryBean.getRtime()));
                        pointBaseBean.setDirection(null);
                        pointBaseBean.setElevation(null);
                        pointBaseBean.setLongitude(queryBean.getLng());
                        pointBaseBean.setLatitude(queryBean.getLat());
                    }
                    break;
                case 5://中交兴路
                    break;
            }
        } catch (Exception e){
            log.error("点位转换异常：【" + e + "】");
        }
        return pointBaseBean;
    }

}
