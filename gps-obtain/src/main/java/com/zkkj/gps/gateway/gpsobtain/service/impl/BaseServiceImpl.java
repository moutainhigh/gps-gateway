package com.zkkj.gps.gateway.gpsobtain.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.openapi.sdk.service.DataExchangeService;
import com.openapi.sdk.service.TransCode;
import com.zkkj.gps.gateway.datatransfer.udp.UdpClient;
import com.zkkj.gps.gateway.gpsobtain.config.RedisConfig;
import com.zkkj.gps.gateway.gpsobtain.entity.basepoint.LocationBean;
import com.zkkj.gps.gateway.gpsobtain.entity.basepoint.PositionBean;
import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.ZjxlBaseBean;
import com.zkkj.gps.gateway.gpsobtain.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 通用接口实现类基类
 *
 * @author suibozhuliu
 */
@Slf4j
@Component
public class BaseServiceImpl {

    //中交兴路API账号
    @Value("${zjxl_api_user}")
    public String zjxlApiUser;

    //中交兴路API密码
    @Value("${zjxl_api_password}")
    public String zjxlApiPassword;

    //中交兴路客户端id
    @Value("${zjxl_client_id}")
    public String zjxlClientId;

    //中交兴路接口根目录
    @Value("${zjxl_api_url}")
    public String zjxlApiUrl;

    //神州伟智基站定位开通后注册的key
    @Value("${authlbs_key}")
    public String authlbsKey;

    //神州伟智基站定位平台开通的秘钥值
    @Value("${authlbs_secret}")
    public String authlbsSecret;

    //Udp客户端
    public UdpClient udpClient;

    //Udp服务端地址
    @Value("${udp_host}")
    private String udpAddress;
    //Udp服务端端口
    @Value("${udp_port}")
    private int udpPort;

    @Autowired
    private RedisConfig redisConfig;

    //创建固定大小线程池
    public final ExecutorService threadPool = Executors.newFixedThreadPool(20);

    public String getZjxlToken() {
        String zjxlToken = null;
        try {
            zjxlToken = redisConfig.getValue("zjxlToken");
            if (StringUtils.isEmpty(zjxlToken)) {
                zjxlToken = getLastToken(zjxlToken);
            }
        } catch (Exception e) {
            log.error("获取中交兴路Token异常：【" + e + "】");
        }
        return zjxlToken;
    }

    /**
     * 2020-04-30 使用redis对令牌存储
     * @param zjxlToken
     * @return
     * @throws Exception
     */
    public String getLastToken(String zjxlToken) throws Exception {
        log.info("API用户登陆");
        String p = "user=" + zjxlApiUser + "&pwd=" + zjxlApiPassword;
        log.info("中交兴路Api登录参数：【" + p + "】");
        p = TransCode.encode(p);//DES加密
        String url = zjxlApiUrl + "/login/" + p + "?client_id=" + zjxlClientId;
        DataExchangeService des = new DataExchangeService(5000, 5000);// 请求访问超时时间,读取数据超时时间
        log.info("中交兴路Api登录地址：【" + url + "】");
        String res = des.accessHttps(url, "POST");
        res = TransCode.decode(res);//DES解密
        log.info("中交兴路Api登录响应：【" + res + "】");
        ZjxlBaseBean<String> baseBean = JSON.parseObject(res, ZjxlBaseBean.class);
        log.info("中交兴路Api登录响应原数据解析：【" + baseBean + "】");
        if ("1001".equals(baseBean.getStatus())) {
            //redisConfig.setKeyTime("zjxlToken", baseBean.getResult().toString(), 60 * 60 * 12);
            redisConfig.setKey("zjxlToken", baseBean.getResult());
            zjxlToken = baseBean.getResult();
        }
        return zjxlToken;
    }

    /**
     * 中交兴路解析接口返回状态及详情
     */
    public String analysisStatus(String status) {
        String responseMsg;
        switch (status) {
            case "1001":
                log.info("√	1001	接口执行成功");
                responseMsg = "接口执行成功";
                break;
            case "1002":
                log.info("×	1002	参数不正确（参数为空、查询时间范围不正确、参数数量不正确）");
                responseMsg = "参数不正确（参数为空、查询时间范围不正确、参数数量不正确）";
                break;
            case "1003":
                log.info("×	1003	车辆调用数量已达上限");
                responseMsg = "车辆调用数量已达上限";
                break;
            case "1004":
                log.info("×	1004	接口调用次数已达上限");
                responseMsg = "接口调用次数已达上限";
                break;
            case "1005":
                log.info("×	1005	该API账号未授权指定所属行政区划数据范围");
                responseMsg = "该API账号未授权指定所属行政区划数据范围";
                break;
            case "1006":
                log.info("√	1006	无结果");
                responseMsg = "无结果";
                break;
            case "1010":
                log.info("×	1010	用户名或密码不正确");
                responseMsg = "用户名或密码不正确";
                break;
            case "1011":
                log.info("×	1011	IP不在白名单列表");
                responseMsg = "IP不在白名单列表";
                break;
            case "1012":
                log.info("×	1012	账号已禁用");
                responseMsg = "账号已禁用";
                break;
            case "1013":
                log.info("×	1013	账号已过有效期");
                responseMsg = "账号已过有效期";
                break;
            case "1014":
                log.info("×	1014	无接口权限");
                responseMsg = "无接口权限";
                break;
            case "1015":
                log.info("×	1015	用户认证系统已升级，请使用令牌访问");
                responseMsg = "用户认证系统已升级，请使用令牌访问";
                break;
            case "1016":
                log.info("√	1016	令牌失效");
                responseMsg = "令牌失效";
                break;
            case "1017":
                log.info("×	1017	账号欠费");
                responseMsg = "账号欠费";
                break;
            case "1018":
                log.info("×	1018	授权的接口已禁用");
                responseMsg = "授权的接口已禁用";
                break;
            case "1019":
                log.info("×	1019	授权的接口已过期");
                responseMsg = "授权的接口已过期";
                break;
            case "1020":
                log.info("×	1020	该车调用次数已达上限");
                responseMsg = "该车调用次数已达上限";
                break;
            case "9001":
                log.info("×	9001	系统异常");
                responseMsg = "系统异常";
                break;
            default:
                responseMsg = "";
                break;
        }
        return responseMsg;
    }

    /**
     * 定位模型转换
     *
     * @param pointBaseBean：定位公共模型
     * @param terminalId：终端号
     * @param flag：数据来源标记：4：基站定位；5：中交兴路
     * @throws Exception
     */
    public void transmitPositionCommon(PointBaseBean pointBaseBean, String terminalId, int flag) throws Exception {
        threadPool.execute(() -> {
            LocationBean locationBean = new LocationBean();
            locationBean.setFlag(flag);
            locationBean.setEleDispatch(null);
            locationBean.setTerminalId(terminalId);
            PositionBean positionBean = new PositionBean();
            positionBean.setAlarmState(0);
            positionBean.setTerminalState(0);
            positionBean.setLoadSensorIsExist(false);
            positionBean.setLoadSensorValue(null);
            positionBean.setAcc(0);
            positionBean.setOilMass(0);
            positionBean.setPower("100");
            positionBean.setAntiDismantle("0");
            try {
                positionBean.setLatitude(pointBaseBean.getLatitude());
            } catch (Exception e) {
                positionBean.setLatitude(0.00);
            }
            try {
                positionBean.setLongitude(pointBaseBean.getLongitude());
            } catch (Exception e) {
                positionBean.setLongitude(0.00);
            }
            positionBean.setElevation(pointBaseBean.getElevation() == null ? 0 : pointBaseBean.getElevation());
            //速度
            int speed = 0;
            if (flag == 5 && !ObjectUtils.isEmpty(pointBaseBean.getSpeed())) {
                try {
                    speed = pointBaseBean.getSpeed().intValue();
                } catch (Exception e) {
                    speed = 0;
                }
            }
            positionBean.setSpeed(speed);
            positionBean.setDirection(pointBaseBean.getDirection() == null ? 0 : pointBaseBean.getDirection());
            positionBean.setDate(pointBaseBean.getGpsTime());
            long mileage = 0;
            if (flag == 5 && !ObjectUtils.isEmpty(pointBaseBean.getMileage())) {
                try {
                    mileage = pointBaseBean.getMileage();
                } catch (Exception e) {
                    mileage = 0;
                }
            }
            positionBean.setMileage(mileage);
            locationBean.setPoint(positionBean);
            locationDataTransfer(locationBean);
        });
    }

    /**
     * 定位数据转发
     *
     * @param locationBean
     */
    public void locationDataTransfer(LocationBean locationBean) {
        try {
            if (ObjectUtils.isEmpty(udpClient)) {
                udpClient = UdpClient.getInstance(udpAddress, udpPort);
            }
            if (ObjectUtils.isEmpty(udpClient)) {
                LoggerUtils.error(log, "定位数据转发创建客户端失败");
                return;
            }
            if (!ObjectUtils.isEmpty(locationBean) && !StringUtils.isEmpty(locationBean.getTerminalId())) {
                String locationJson = JSON.toJSONString(locationBean, SerializerFeature.WriteMapNullValue);
                if (!StringUtils.isEmpty(locationJson)) {
                    LoggerUtils.info(log, locationBean.getTerminalId(), "定位数据转发JSON数据：【" + locationJson + "】");
                    udpClient.sendMessage(locationJson);
                }
            }
        } catch (Exception e) {
            LoggerUtils.error(log, "定位数据转发异常：【" + e + "】");
        }

    }

}
