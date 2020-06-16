package com.zkkj.gps.gateway.gpsobtain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.openapi.sdk.service.DataExchangeService;
import com.openapi.sdk.service.TransCode;
import com.zkkj.gps.gateway.gpsobtain.entity.ResultVo;
import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.TruckBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.ZjxlBaseBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.ZjxlBatchPointBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.ZjxlPointBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.truckbean.TrackBaseBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.truckbean.TrackPointBean;
import com.zkkj.gps.gateway.gpsobtain.service.ZjxlGpsObtainService;
import com.zkkj.gps.gateway.gpsobtain.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.zkkj.gps.gateway.gpsobtain.utils.DateTimeUtils.timeStrToLocalDateTime;
import static com.zkkj.gps.gateway.gpsobtain.utils.DateTimeUtils.timeTrans;

/**
 * 中交兴路平台获取定位数据业务
 *
 * @author suibozhuliu
 */
@Service
@Slf4j
public class ZjxlGpsObtainServiceImpl extends BaseServiceImpl implements ZjxlGpsObtainService {

    @Override
    public ResultVo<PointBaseBean> getGpsByLicensePlate(String licensePlate) {
        ResultVo<PointBaseBean> result = new ResultVo<>();
        result.resultFail("获取定位失败");
        try {
            String token = getZjxlToken();
            if (StringUtils.isEmpty(token)) {
                return result;
            }
            String p = "token=" + token + "&vclN=" + licensePlate + "&timeNearby=24";
            LoggerUtils.info(log,licensePlate,"中交兴路单车定位请求参数：【" + p + "】");
            p = TransCode.encode(p);//DES加密
            String url = zjxlApiUrl + "/vLastLocationV3/" + p + "?client_id=" + zjxlClientId;
            DataExchangeService des = new DataExchangeService(5000, 5000);// 请求访问超时时间,读取数据超时时间
            LoggerUtils.info(log,licensePlate,"中交兴路单车定位请求地址：【" + url + "】");
            String res = des.accessHttps(url, "POST");
            res = TransCode.decode(res);//DES解密
            LoggerUtils.info(log,licensePlate,"中交兴路返回的单车实时定位数据：【" + res + "】");
            ZjxlBaseBean<ZjxlPointBean> baseBean = JSONObject.parseObject(res ,new TypeReference<ZjxlBaseBean<ZjxlPointBean>>(){});
            LoggerUtils.info(log,licensePlate,"中交兴路单车定位结果：【" + baseBean.toString() + "】");
            if (!ObjectUtils.isEmpty(baseBean) && !StringUtils.isEmpty(baseBean.getStatus())) {
                LoggerUtils.info(log,licensePlate,"验证正确：【" +baseBean.toString()+ "】");
                String status = baseBean.getStatus();
                String analysisMsg = analysisStatus(status);
                LoggerUtils.info(log,licensePlate,"获取到的点位信息：【" + baseBean. getResult().toString() + "】");
                if (status.equals("1001")){
                    ZjxlPointBean zjPointBean = baseBean.getResult();
                    if (!ObjectUtils.isEmpty(zjPointBean)) {
                        //响应数据处理
                        PointBaseBean pointBaseBean = locationDataFormart(zjPointBean);
                        pointBaseBean.setLicensePlate(licensePlate);
                        result.resultSuccess(pointBaseBean, "定位成功！");
                        //数据转发
                        transmitPositionCommon(pointBaseBean, licensePlate, 5);
                    }
                    //2020-04-30 添加令牌失效校验
                }else if("1016".equals(status)){
                    getLastToken(token);
                } else {
                    if (!StringUtils.isEmpty(analysisMsg)){
                        result.resultFail(analysisMsg);
                    }
                }
            }
        } catch (Exception ex) {
            LoggerUtils.error(log,licensePlate,"中交兴路单车定位异常：【" + ex.getMessage() + "】");
        }
        return result;
    }

    @Override
    public ResultVo<List<PointBaseBean>> getGpsByLicensePlates(String licensePlates) {
        ResultVo<List<PointBaseBean>> result = new ResultVo<>();
        result.resultFail("获取定位失败");
        try {
            String token = getZjxlToken();
            StringBuilder truckNoListStringBuilder = new StringBuilder();
            String[] truckArrays = licensePlates.split(",");
            for (int i = 0; i < truckArrays.length; i++) {
                truckNoListStringBuilder.append(truckArrays[i] + "_2,");
            }
            licensePlates = truckNoListStringBuilder.substring(0, truckNoListStringBuilder.length() - 1);
            String p = "token=" + token + "&vclNs=" + licensePlates + "&timeNearby=24";
            LoggerUtils.info(log,"中交兴路单车定位请求参数：【" + p + "】");
            p = TransCode.encode(p);//DES加密
            String url = zjxlApiUrl + "/vLastLocationMultiV4/" + p + "?client_id=" + zjxlClientId;
            LoggerUtils.info(log,"中交兴路单车定位请求地址：url：【" + url + "】");
            DataExchangeService des = new DataExchangeService(5000, 5000);// 请求访问超时时间,读取数据超时时间
            String res = des.accessHttps(url, "POST");
            res = TransCode.decode(res);//DES解密
            LoggerUtils.info(log,"中交兴路批量定位结果：【" + res.toString() + "】");
            ZjxlBaseBean<List<ZjxlBatchPointBean>> batchPointBean = JSONObject.parseObject(res ,
                    new TypeReference<ZjxlBaseBean<List<ZjxlBatchPointBean>>>(){});
            if (!ObjectUtils.isEmpty(batchPointBean) && !StringUtils.isEmpty(batchPointBean.getStatus())) {
                String status = batchPointBean.getStatus();
                String analysisMsg = analysisStatus(status);
                if (status.equals("1001")) {
                    List<ZjxlBatchPointBean> zjPointBeanList = batchPointBean.getResult();
                    if (!CollectionUtils.isEmpty(zjPointBeanList)) {
                        List<PointBaseBean> resBeanlist = new ArrayList<>();
                        for (ZjxlBatchPointBean bean : zjPointBeanList) {
                            if (!ObjectUtils.isEmpty(bean) && !StringUtils.isEmpty(bean.getVno())) {
                                //响应数据处理
                                PointBaseBean pointBaseBean = locationDataFormart(bean);
                                pointBaseBean.setLicensePlate(bean.getVno());
                                resBeanlist.add(pointBaseBean);
                                //数据转发
                                transmitPositionCommon(pointBaseBean, bean.getVno(), 5);
                            }
                        }
                        result.resultSuccess(resBeanlist, "定位成功！");
                    }
                    //2020-04-30 添加令牌失效校验
                }else if("1016".equals(status)){
                    getLastToken(token);
                } else {
                    if (!StringUtils.isEmpty(analysisMsg)){
                        result.resultFail(analysisMsg);
                    }
                }
            }
        } catch (Exception ex) {
            LoggerUtils.error(log,"中交兴路多车定位异常：【" + ex.getMessage() + "】");
        }
        return result;
    }

    @Override
    public ResultVo<TrackBaseBean> getTruckByLicensePlate(String licensePlate, String startTime, String endTime) {
        ResultVo<TrackBaseBean> result = new ResultVo<>();
        result.resultFail("获取轨迹失败");
        try {
            String token = getZjxlToken();
            String p = "token=" + token + "&vclN=" + licensePlate + "&qryBtm=" + startTime + "&qryEtm=" + endTime;
            LoggerUtils.info(log,licensePlate,"中交兴路获取单车轨迹请求参 数:" + p);
            p = TransCode.encode(p);//DES加密
            String url = zjxlApiUrl + "/vHisTrack24/" + p + "?client_id=" + zjxlClientId;
            DataExchangeService des = new DataExchangeService(5000, 5000);// 请求访问超时时间,读取数据超时时间
            LoggerUtils.info(log,"中交兴路获取单车轨迹请求地址：url：【" + url + "】");
            String res = des.accessHttps(url, "POST");
            res = TransCode.decode(res);//DES解密
            LoggerUtils.info(log,"中交兴路获取单车轨迹结果：【" + res.toString() + "】");
            ZjxlBaseBean<List<TruckBean>> truckListBean = JSONObject.parseObject(res ,
                    new TypeReference<ZjxlBaseBean<List<TruckBean>>>(){});
            if (!ObjectUtils.isEmpty(truckListBean) && !StringUtils.isEmpty(truckListBean.getStatus())) {
                String status = truckListBean.getStatus();
                String analysisMsg = analysisStatus(status);
                if (status.equals("1001")) {
                    List<TruckBean> zjPointBeanList = truckListBean.getResult();
                    if (!CollectionUtils.isEmpty(zjPointBeanList)) {
                        List<TrackPointBean> pointList = new ArrayList<>();
                        for (TruckBean bean : zjPointBeanList) {
                            if (!ObjectUtils.isEmpty(bean)) {
                                //响应数据处理
                                TrackPointBean trackPointBean = trackDataFormart(bean);
                                pointList.add(trackPointBean);

                                //数据转发
                                PointBaseBean pointBaseBean = new PointBaseBean();
                                if (!ObjectUtils.isEmpty(trackPointBean)){
                                    BeanUtils.copyProperties(trackPointBean, pointBaseBean);
                                    pointBaseBean.setLicensePlate(licensePlate);
                                    if (!ObjectUtils.isEmpty(trackPointBean.getMileage())){
                                        try {
                                            pointBaseBean.setMileage((trackPointBean.getMileage()).longValue());
                                        } catch (Exception e){
                                            pointBaseBean.setMileage(null);
                                        }
                                    }
                                    transmitPositionCommon(pointBaseBean, licensePlate, 5);
                                }
                            }
                        }
                        TrackBaseBean trackBaseBean = new TrackBaseBean();
                        trackBaseBean.setLicensePlate(licensePlate);
                        trackBaseBean.setPointList(pointList);
                        result.resultSuccess(trackBaseBean, "轨迹查询成功！");
                    }
                    //2020-04-30 添加令牌失效校验
                }else if("1016".equals(status)){
                    getLastToken(token);
                } else {
                    if (!StringUtils.isEmpty(analysisMsg)){
                        result.resultFail(analysisMsg);
                    }
                }
            }
        } catch (Exception ex) {
            LoggerUtils.error(log,"中交兴路单车轨迹查询异常：【" + ex + "】");
        }
        return result;
    }

    /**
     * 单车定位模型转换
     *
     * @param zjPointBean
     */
    private PointBaseBean locationDataFormart(ZjxlPointBean zjPointBean) {
        PointBaseBean pointBaseBean = null;
        if (!ObjectUtils.isEmpty(zjPointBean)) {
            pointBaseBean = new PointBaseBean();
            pointBaseBean.setRcvTime(LocalDateTime.now());
            pointBaseBean.setGpsTime(LocalDateTime.now());
            if (!StringUtils.isEmpty(zjPointBean.getUtc())){
                try {
                    pointBaseBean.setGpsTime(timeTrans(zjPointBean.getUtc()));
                } catch (Exception e){}
            }
            pointBaseBean.setDirection(null);
            String drc = zjPointBean.getDrc();
            if (!StringUtils.isEmpty(drc)) {
                drc = drc.replaceAll(" ", "").trim();
                if (!StringUtils.isEmpty(drc)) {
                    try {
                        pointBaseBean.setDirection(Integer.valueOf(drc));
                    } catch (Exception e) {}
                }
            }
            pointBaseBean.setElevation(null);
            pointBaseBean.setLongitude(null);
            String lon = zjPointBean.getLon();
            if (!StringUtils.isEmpty(lon)) {
                lon = lon.replaceAll(" ", "").trim();
                if (!StringUtils.isEmpty(lon)) {
                    try {
                        pointBaseBean.setLongitude(Double.valueOf(lon)/600000.0);
                    } catch (Exception e) {}
                }
            }
            pointBaseBean.setLatitude(null);
            String lat = zjPointBean.getLat();
            if (!StringUtils.isEmpty(lat)) {
                lat = lat.replaceAll(" ", "").trim();
                if (!StringUtils.isEmpty(lat)) {
                    try {
                        pointBaseBean.setLatitude(Double.valueOf(lat)/600000.0);
                    } catch (Exception e) {}
                }
            }
            Double speed = 0D;
            String speedStr = zjPointBean.getSpd();
            if (!StringUtils.isEmpty(speedStr)){
                try {
                    speed = Double.valueOf(speedStr);
                } catch (Exception e){
                    speed = 0D;
                }
            }
            pointBaseBean.setSpeed(speed);
        }
        return pointBaseBean;
    }

    /**
     * 车辆轨迹模型转换
     * @param truckBean
     * @return
     */
    private TrackPointBean trackDataFormart(TruckBean truckBean){
        TrackPointBean trackPointBean = null;
        if (!ObjectUtils.isEmpty(truckBean)){
            trackPointBean = new TrackPointBean();
            trackPointBean.setRcvTime(LocalDateTime.now());
            trackPointBean.setGpsTime(LocalDateTime.now());
            if (!StringUtils.isEmpty(truckBean.getGtm())){
                LocalDateTime gpsTime = timeStrToLocalDateTime(truckBean.getGtm());
                if (!ObjectUtils.isEmpty(gpsTime)){
                    trackPointBean.setGpsTime(gpsTime);
                }
            }
            trackPointBean.setLongitude(null);
            String lon = truckBean.getLon();
            if (!StringUtils.isEmpty(lon)){
                try {
                    lon = lon.replaceAll(" ","");
                    if (!StringUtils.isEmpty(lon)){
                        trackPointBean.setLongitude(Double.valueOf(lon)/600000.0);
                    }
                } catch (Exception e){}
            }
            trackPointBean.setLatitude(null);
            String lat = truckBean.getLat();
            if (!StringUtils.isEmpty(lat)){
                try {
                    lat = lat.replaceAll(" ","");
                    if (!StringUtils.isEmpty(lat)){
                        trackPointBean.setLatitude(Double.valueOf(lat)/600000.0);
                    }
                } catch (Exception e){}
            }
            trackPointBean.setDirection(null);
            String drc = truckBean.getAgl();
            if (!StringUtils.isEmpty(drc)) {
                drc = drc.replaceAll(" ", "").trim();
                if (!StringUtils.isEmpty(drc)) {
                    try {
                        trackPointBean.setDirection(Integer.valueOf(drc));
                    } catch (Exception e) {}
                }
            }
            trackPointBean.setElevation(null);
            String elevation = truckBean.getHgt();
            if (!StringUtils.isEmpty(elevation)){
                elevation = elevation.replaceAll(" ","");
                if (!StringUtils.isEmpty(elevation)){
                    trackPointBean.setElevation(Integer.valueOf(elevation));
                }
            }
            Double speed = 0D;
            String speedStr = truckBean.getSpd();
            if (!StringUtils.isEmpty(speed)){
                try {
                    speed = Double.valueOf(speedStr);
                } catch (Exception e){
                    speed = 0D;
                }
            }
            trackPointBean.setSpeed(speed);

            Double mileage = 0D;
            String mileageStr = truckBean.getMlg();
            if (!StringUtils.isEmpty(mileageStr)){
                try {
                    mileage = Double.valueOf(truckBean.getMlg());
                    if (mileage > 0){
                        mileage = mileage * 0.1;
                    }
                } catch (Exception e){
                    mileage = 0D;
                }
            }
            trackPointBean.setMileage(mileage);
        }
        return trackPointBean;
    }

}
