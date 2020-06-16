package com.zkkj.gps.gateway.gpsobtain.service.impl;

import com.zkkj.gps.gateway.gpsobtain.entity.ResultVo;
import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsclose.AuthlbsCloseBean;
import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsopen.AuthlbsOpenBean;
import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsquery.AuthlbsQueryBean;
import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsstatus.AuthlbsStatusBean;
import com.zkkj.gps.gateway.gpsobtain.entity.basepoint.LocationBean;
import com.zkkj.gps.gateway.gpsobtain.entity.basepoint.PositionBean;
import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;
import com.zkkj.gps.gateway.gpsobtain.service.AuthlbsLocalService;
import com.zkkj.gps.gateway.gpsobtain.service.AuthlbsRemoteService;
import com.zkkj.gps.gateway.gpsobtain.utils.DateTimeUtils;
import com.zkkj.gps.gateway.gpsobtain.utils.LngLatRegularUtils;
import com.zkkj.gps.gateway.gpsobtain.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 本地远程调用Api业务实现类
 * @author suibozhuliu
 */
@Slf4j
@Service
public class AuthlbsLocalServiceImpl extends BaseServiceImpl implements AuthlbsLocalService {

    @Autowired
    private AuthlbsRemoteService authlbsRemoteService;

    @Override
    public ResultVo<String> authlbsopen(String mobile){
        if (!StringUtils.isEmpty(mobile)){
            LoggerUtils.info(log,mobile,"开通授权定位请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        } else {
            log.info("开通授权定位请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        }
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(authlbsKey) || StringUtils.isEmpty(authlbsSecret) || StringUtils.isEmpty(mobile)){
                resultVo.resultFail("参数异常，请检查！");
                return resultVo;
            }
            AuthlbsOpenBean authlbsOpen = authlbsRemoteService.authlbsopen(authlbsKey, authlbsSecret, mobile);
            if (!ObjectUtils.isEmpty(authlbsOpen)){
                LoggerUtils.info(log,mobile,"神州基站定位返回开通授权定位结果：【" + authlbsOpen.toString() + "】");
                if (authlbsOpen.getResid() >= 0){
                    resultVo.resultSuccess(authlbsOpen.getMobile(),authlbsOpen.getResmsg());
                } else {
                    resultVo.setData(authlbsOpen.getMobile());
                    resultVo.resultFail(authlbsOpen.getResmsg());
                }
            } else {
                resultVo.setData(authlbsOpen.getMobile());
                resultVo.resultFail("开通授权定位失败，请重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVo.resultFail("开通授权定位异常，请重试！【" + e + "】");
        }
        return resultVo;
    }

    @Override
    public ResultVo<String> authlbsstatus(String mobile) {
        if (!StringUtils.isEmpty(mobile)){
            LoggerUtils.info(log,mobile,"状态查询请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        } else {
            log.info("状态查询请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        }
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(authlbsKey) || StringUtils.isEmpty(authlbsSecret) || StringUtils.isEmpty(mobile)){
                resultVo.resultFail("参数异常，请检查！");
                return resultVo;
            }
            AuthlbsStatusBean authlbsStatus = authlbsRemoteService.authlbsstatus(authlbsKey, authlbsSecret, mobile);
            if (!ObjectUtils.isEmpty(authlbsStatus)){
                LoggerUtils.info(log,mobile,"神州基站定位返回状态查询结果：【" + authlbsStatus.toString() + "】");
                resultVo.resultSuccess(authlbsStatus.getMobile(),authlbsStatus.getResmsg());
                switch (authlbsStatus.getResid()){
                    case 0:
                        //白名单用户,已开通定位
                        break;
                    case 1:
                        //用户尚未开通定位
                        break;
                    case -1:
                        //用户不存在
                        break;
                    case -99:
                        //黑名单用户
                        break;
                }
            } else {
                resultVo.setData(authlbsStatus.getMobile());
                resultVo.resultFail("状态查询失败，请重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVo.resultFail("状态查询异常，请重试！【" + e + "】");
        }
        return resultVo;
    }

    @Override
    public ResultVo<AuthlbsQueryBean> authlbsquery(String mobile) {
        if (!StringUtils.isEmpty(mobile)){
            LoggerUtils.info(log,mobile,"实时定位请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        } else {
            log.info("实时定位请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        }
        ResultVo<AuthlbsQueryBean> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(authlbsKey) || StringUtils.isEmpty(authlbsSecret) || StringUtils.isEmpty(mobile)){
                resultVo.resultFail("参数异常，请检查！");
                return resultVo;
            }
            resultVo.resultFail("实时定位失败，请重试！");
            /*
            执行流程：
            1.验证授权状态：
            ①.已在白名单------>执行定位获取操作
            ②.用户尚未开通定位----->开通定位授权，发送短信，并执行2
            ③.黑名单用户、不存在的用户及其他情况则直接返回失败并说明原因
            2.验证开通授权状态：
            ①.白名单开通成功，等待用户回复短信，需要到开通授权回调中执行获取定位操作
            ②.已在白名单中----->执行定位获取操作
            3.执行定位获取操作：
            ①.执行成功，返回定位信息，并通知CCS做点位存储
            ②.执行失败，直接返回失败并说明原因
             */
            AuthlbsStatusBean authlbsStatus = authlbsRemoteService.authlbsstatus(authlbsKey, authlbsSecret, mobile);
            if (!ObjectUtils.isEmpty(authlbsStatus)) {
                switch (authlbsStatus.getResid()) {
                    case -1://用户不存在----->开通定位授权，发短信
                    case 0://用户尚未开通定位---->开通定位授权，发短信
                        AuthlbsOpenBean authlbsOpen = authlbsRemoteService.authlbsopen(authlbsKey, authlbsSecret, mobile);
                        if (!ObjectUtils.isEmpty(authlbsOpen)) {
                            LoggerUtils.info(log, mobile, "神州基站定位返回开通授权定位结果：【" + authlbsOpen.toString() + "】");
                            if (authlbsOpen.getResid() == 0) {//白名单开通成功，请通知用户回复短信小写的y，需要到开通授权回调中执行获取定位操作
                                resultVo.resultSuccess(null, authlbsOpen.getResmsg());
                            } else if (authlbsOpen.getResid() == 1) {//当前手机号已在白名单内
                                AuthlbsQueryBean authlbsQuery = authlbsRemoteService.authlbsquery(authlbsKey, authlbsSecret, mobile);
                                if (!ObjectUtils.isEmpty(authlbsQuery)) {
                                    LoggerUtils.info(log, mobile, "神州基站定位返回实时定位结果：【" + authlbsQuery.toString() + "】");
                                    if (authlbsQuery.getResid() == 0) {//定位数据获取成功，数据正常返回
                                        if (LngLatRegularUtils.getLngLatVerify(authlbsQuery.getLng(),authlbsQuery.getLat())){
                                            resultVo.resultSuccess(authlbsQuery, authlbsQuery.getResmsg());
                                            transmitPosition(authlbsQuery, authlbsQuery.getMobile());
                                        } else {
                                            resultVo.resultFail("定位响应成功，但不是合法经纬度，请重新定位！");
                                        }
                                    } else {//定位数据获取失败
                                        resultVo.resultFail(authlbsQuery.getResmsg());
                                    }
                                }
                            } else {
                                resultVo.resultFail(authlbsOpen.getResmsg());
                            }
                        }
                        break;
                    case 1:
                        //白名单用户,已开通定位---->执行定位操作
                        AuthlbsQueryBean authlbsQuery = authlbsRemoteService.authlbsquery(authlbsKey, authlbsSecret, mobile);
                        if (!ObjectUtils.isEmpty(authlbsQuery)) {
                            LoggerUtils.info(log, mobile, "神州基站定位返回实时定位结果：【" + authlbsQuery.toString() + "】");
                            if (authlbsQuery.getResid() == 0) {//定位数据正常返回
                                if (LngLatRegularUtils.getLngLatVerify(authlbsQuery.getLng(),authlbsQuery.getLat())){
                                    resultVo.resultSuccess(authlbsQuery, authlbsQuery.getResmsg());
                                    transmitPosition(authlbsQuery, authlbsQuery.getMobile());
                                } else {
                                    resultVo.resultFail("定位响应成功，但不是合法经纬度，请重新定位！");
                                }
                            } else {
                                resultVo.resultFail(authlbsQuery.getResmsg());
                            }
                        }
                        break;
                    case -99:
                        //黑名单用户
                        if (!StringUtils.isEmpty(authlbsStatus.getResmsg())) {
                            resultVo.resultFail(authlbsStatus.getResmsg());
                        } else {
                            resultVo.resultFail("黑名单用户！");
                        }
                        break;
                    default:
                        if (!StringUtils.isEmpty(authlbsStatus.getResmsg())) {
                            resultVo.resultFail(authlbsStatus.getResmsg());
                        }
                        break;
                }
            }
        } catch (Exception e) {
            resultVo.resultFail("实时定位异常，请重试！【" + e + "】");
        }
        return resultVo;
    }

    @Override
    public ResultVo<PointBaseBean> getGpsByphoneNum(String mobile) {
        if (!StringUtils.isEmpty(mobile)){
            LoggerUtils.info(log,mobile,"实时定位请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        } else {
            log.info("实时定位请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        }
        ResultVo<PointBaseBean> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(authlbsKey) || StringUtils.isEmpty(authlbsSecret) || StringUtils.isEmpty(mobile)){
                resultVo.resultFail("参数异常，请检查！");
                return resultVo;
            }
            resultVo.resultFail("实时定位失败，请重试！");
            /*
            执行流程：
            1.验证授权状态：
            ①.已在白名单------>执行定位获取操作
            ②.用户尚未开通定位----->开通定位授权，发送短信，并执行2
            ③.黑名单用户、不存在的用户及其他情况则直接返回失败并说明原因
            2.验证开通授权状态：
            ①.白名单开通成功，等待用户回复短信，需要到开通授权回调中执行获取定位操作
            ②.已在白名单中----->执行定位获取操作
            3.执行定位获取操作：
            ①.执行成功，返回定位信息，并通知CCS做点位存储
            ②.执行失败，直接返回失败并说明原因
             */
            AuthlbsStatusBean authlbsStatus = authlbsRemoteService.authlbsstatus(authlbsKey, authlbsSecret, mobile);
            if (!ObjectUtils.isEmpty(authlbsStatus)) {
                switch (authlbsStatus.getResid()) {
                    case -1://用户不存在----->开通定位授权，发短信
                    case 0://用户尚未开通定位---->开通定位授权，发短信
                        resultVo.resultFail(authlbsStatus.getResmsg());
                        /*AuthlbsOpenBean authlbsOpen = authlbsRemoteService.authlbsopen(authlbsKey, authlbsSecret, mobile);
                        if (!ObjectUtils.isEmpty(authlbsOpen)) {
                            LoggerUtils.info(log, mobile, "神州基站定位返回开通授权定位结果：【" + authlbsOpen.toString() + "】");
                            if (authlbsOpen.getResid() == 0) {//白名单开通成功，请通知用户回复短信小写的y，需要到开通授权回调中执行获取定位操作
                                resultVo.resultFail(authlbsOpen.getResmsg());
                            } else if (authlbsOpen.getResid() == 1) {//当前手机号已在白名单内
                                AuthlbsQueryBean authlbsQuery = authlbsRemoteService.authlbsquery(authlbsKey, authlbsSecret, mobile);
                                if (!ObjectUtils.isEmpty(authlbsQuery)) {
                                    LoggerUtils.info(log, mobile, "神州基站定位返回实时定位结果：【" + authlbsQuery.toString() + "】");
                                    if (authlbsQuery.getResid() == 0) {//定位数据获取成功，数据正常返回
                                        if (LngLatRegularUtils.getLngLatVerify(authlbsQuery.getLng(),authlbsQuery.getLat())){
                                            PointBaseBean pointBaseBean = locationDataFormart(authlbsQuery);
                                            if (!ObjectUtils.isEmpty(pointBaseBean)){
                                                resultVo.resultSuccess(pointBaseBean, "定位成功！");
                                                transmitPosition(authlbsQuery, authlbsQuery.getMobile());
                                            }
                                        } else {
                                            resultVo.resultFail("定位响应成功，但不是合法经纬度，请重新定位！");
                                        }
                                    } else {//定位数据获取失败
                                        resultVo.resultFail(authlbsQuery.getResmsg());
                                    }
                                }
                            } else {
                                resultVo.resultFail(authlbsOpen.getResmsg());
                            }
                        }*/
                        break;
                    case 1:
                        //白名单用户,已开通定位---->执行定位操作
                        AuthlbsQueryBean authlbsQuery = authlbsRemoteService.authlbsquery(authlbsKey, authlbsSecret, mobile);
                        if (!ObjectUtils.isEmpty(authlbsQuery)) {
                            LoggerUtils.info(log, mobile, "神州基站定位返回实时定位结果：【" + authlbsQuery.toString() + "】");
                            if (authlbsQuery.getResid() == 0) {//定位数据正常返回
                                if (LngLatRegularUtils.getLngLatVerify(authlbsQuery.getLng(),authlbsQuery.getLat())){
                                    PointBaseBean pointBaseBean = locationDataFormart(authlbsQuery);
                                    if (!ObjectUtils.isEmpty(pointBaseBean)){
                                        pointBaseBean.setPhoneNum(mobile);
                                        resultVo.resultSuccess(pointBaseBean, "定位成功！");
                                        transmitPositionCommon(pointBaseBean, mobile,4);
                                    }
                                } else {
                                    resultVo.resultFail("定位响应成功，但不是合法经纬度，请重新定位！");
                                }
                            } else {
                                resultVo.resultFail(authlbsQuery.getResmsg());
                            }
                        }
                        break;
                    case -99:
                        //黑名单用户
                        if (!StringUtils.isEmpty(authlbsStatus.getResmsg())) {
                            resultVo.resultFail(authlbsStatus.getResmsg());
                        } else {
                            resultVo.resultFail("黑名单用户！");
                        }
                        break;
                    default:
                        if (!StringUtils.isEmpty(authlbsStatus.getResmsg())) {
                            resultVo.resultFail(authlbsStatus.getResmsg());
                        }
                        break;
                }
            }
        } catch (Exception e) {
            resultVo.resultFail("实时定位异常，请重试！【" + e + "】");
        }
        return resultVo;
    }

    @Override
    public ResultVo<Boolean> sendMsgToAuthlbs(String mobile) {
        if (!StringUtils.isEmpty(mobile)){
            LoggerUtils.info(log,mobile,"通过手机号发送短信请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        } else {
            log.info("通过手机号发送短信请求参数，手机号为空：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        }
        ResultVo<Boolean> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(authlbsKey) || StringUtils.isEmpty(authlbsSecret) || StringUtils.isEmpty(mobile)) {
                resultVo.resultFail("参数异常，请检查！");
                return resultVo;
            }
            resultVo.resultFail("实时定位失败，请重试！");
            /*
            执行流程：
            1.验证授权状态：
            ①.已在白名单------>执行定位获取操作
            ②.用户尚未开通定位----->开通定位授权，发送短信，并执行2
            ③.黑名单用户、不存在的用户及其他情况则直接返回失败并说明原因
            2.验证开通授权状态：
            ①.白名单开通成功，等待用户回复短信，需要到开通授权回调中执行获取定位操作
            ②.已在白名单中----->执行定位获取操作
            3.执行定位获取操作：
            ①.执行成功，返回定位信息，并通知CCS做点位存储
            ②.执行失败，直接返回失败并说明原因
             */
            AuthlbsStatusBean authlbsStatus = authlbsRemoteService.authlbsstatus(authlbsKey, authlbsSecret, mobile);
            if (!ObjectUtils.isEmpty(authlbsStatus)) {
                switch (authlbsStatus.getResid()) {
                    case -1://用户不存在----->开通定位授权，发短信
                    case 0://用户尚未开通定位---->开通定位授权，发短信
                        AuthlbsOpenBean authlbsOpen = authlbsRemoteService.authlbsopen(authlbsKey, authlbsSecret, mobile);
                        if (!ObjectUtils.isEmpty(authlbsOpen)) {
                            LoggerUtils.info(log, mobile, "神州基站定位返回开通授权定位结果：【" + authlbsOpen.toString() + "】");
                            if (authlbsOpen.getResid() == 0) {//白名单开通成功，请通知用户回复短信小写的y，需要到开通授权回调中执行获取定位操作
                                resultVo.resultSuccess(true,authlbsOpen.getResmsg());
                            } else if (authlbsOpen.getResid() == 1) {//当前手机号已在白名单内
                                resultVo.resultFail("当前手机号已在白名单内，请查询定位！");
                            } else {
                                resultVo.resultFail(authlbsOpen.getResmsg());
                            }
                        }
                        break;
                    case 1:
                        //白名单用户,已开通定位---->执行定位操作
                        resultVo.resultFail("当前手机号已在白名单内，请查询定位！");
                        break;
                    case -99:
                        //黑名单用户
                        if (!StringUtils.isEmpty(authlbsStatus.getResmsg())) {
                            resultVo.resultFail(authlbsStatus.getResmsg());
                        } else {
                            resultVo.resultFail("黑名单用户！");
                        }
                        break;
                    default:
                        if (!StringUtils.isEmpty(authlbsStatus.getResmsg())) {
                            resultVo.resultFail(authlbsStatus.getResmsg());
                        }
                        break;
                }
            }
        } catch (Exception e){
            resultVo.resultFail("实时定位异常，请重试！【" + e + "】");
        }
        return resultVo;
    }

    @Override
    public ResultVo<String> authlbsclose(String mobile) {
        if (!StringUtils.isEmpty(mobile)){
            LoggerUtils.info(log,mobile,"定位关闭请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        } else {
            log.info("定位关闭请求参数：【key：" + authlbsKey + "】；【secret：" + authlbsSecret + "】；mobile：【" + mobile + "】");
        }
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(authlbsKey) || StringUtils.isEmpty(authlbsSecret) || StringUtils.isEmpty(mobile)){
                resultVo.resultFail("参数异常，请检查！");
                return resultVo;
            }
            AuthlbsCloseBean authlbsClose = authlbsRemoteService.authlbsclose(authlbsKey, authlbsSecret, mobile);
            if (!ObjectUtils.isEmpty(authlbsClose)){
                LoggerUtils.info(log,mobile,"神州基站定位返回定位关闭结果：【" + authlbsClose.toString() + "】");
                switch (authlbsClose.getResid()){
                    case 0:
                        resultVo.resultSuccess(null,authlbsClose.getResmsg());
                        break;
                    case -1:
                        resultVo.resultFail(authlbsClose.getResmsg());
                        break;
                    default:
                        resultVo.resultFail(authlbsClose.getResmsg());
                        break;
                }
            } else {
                resultVo.resultFail("定位关闭失败，请重试！");
            }
        } catch (Exception e) {
            resultVo.resultFail("定位关闭异常，请重试！【" + e + "】");
        }
        return resultVo;
    }

    @Override
    public void callBack(String mobileno, String action, String sign, String msg) {
        LoggerUtils.info(log,mobileno,"开通回调结果：mobileno：【" + mobileno + "】；action：【" + action + "】；sign：【" + sign + "】；msg：【" + msg + "】");
        if (!StringUtils.isEmpty(mobileno) && !StringUtils.isEmpty(action)){
            switch (action){
                case "open":
                    //开通定位,指收到用户的回复同意定位的短信通知----->执行定位数据获取操作
                    try {
                        AuthlbsQueryBean authlbsQuery = authlbsRemoteService.authlbsquery(authlbsKey, authlbsSecret, mobileno);
                        if (!ObjectUtils.isEmpty(authlbsQuery)) {
                            LoggerUtils.info(log, mobileno, "神州基站定位回调返回实时定位结果：【" + authlbsQuery.toString() + "】");
                            if (authlbsQuery.getResid() == 0) {//定位数据正常返回
                                //resultVo.resultSuccess(authlbsQuery, authlbsQuery.getResmsg());
                            } else {
                                //resultVo.resultFail(authlbsQuery.getResmsg());
                            }
                        }
                    } catch (Exception e) {
                        LoggerUtils.error(log,mobileno,"实时定位异常，请重试！【" + e + "】");
                    }
                    break;
                case "reject":
                    //移动用户开通定位时,如果用户回复了非y的短信内容，会推送此状态
                    break;
                case "close":
                    //关闭定位
                    break;
                case "black":
                    //当前手机号被加到企业的黑名单
                    break;
                case "forbid":
                    //当前用户被加到平台的黑名单，禁止一切操作
                    break;
                case "noresp24h":
                    //电信手机号,如果用户超过24小时未回复,会回调推送此信息
                    break;
                case "unbind24h":
                    //电信手机号,如果解绑超过24小时用户未确认，会回调推送此信息
                    break;
            }
        }
    }

    /**
     * 定位数据格式化
     * @param authlbsQuery
     * @return
     */
    private PointBaseBean locationDataFormart(AuthlbsQueryBean authlbsQuery) {
        PointBaseBean positionBean = null;
        if (!ObjectUtils.isEmpty(authlbsQuery)) {
            positionBean = new PointBaseBean();
            positionBean.setRcvTime(LocalDateTime.now());
            positionBean.setGpsTime(LocalDateTime.now());
            if (!StringUtils.isEmpty(authlbsQuery.getRtime())){
                try {
                    LocalDateTime localDateTime = DateTimeUtils.strToLocalDateTime(authlbsQuery.getRtime());
                    if (!ObjectUtils.isEmpty(localDateTime)){
                        positionBean.setGpsTime(localDateTime);
                    }
                } catch (Exception e){}
            }
            positionBean.setDirection(null);
            positionBean.setElevation(null);
            positionBean.setLongitude(authlbsQuery.getLng());
            positionBean.setLatitude(authlbsQuery.getLat());
        }
        return positionBean;
    }

    /**
     * 定位模型转换
     * @param authlbsQuery
     * @param terminalId
     * @return
     */
    private void transmitPosition(AuthlbsQueryBean authlbsQuery, String terminalId) throws Exception {
        LocationBean locationBean = new LocationBean();
        locationBean.setFlag(4);
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
            positionBean.setLatitude(authlbsQuery.getLat());
        } catch (Exception e) {
            positionBean.setLatitude(0.00);
        }
        try {
            positionBean.setLongitude(authlbsQuery.getLng());
        } catch (Exception e) {
            positionBean.setLongitude(0.00);
        }
        positionBean.setElevation(0);
        positionBean.setSpeed(0);
        positionBean.setDirection(0);
        positionBean.setDate(LocalDateTime.now());
        if (!StringUtils.isEmpty(authlbsQuery.getRtime())){
            try {
                LocalDateTime localDateTime = DateTimeUtils.strToLocalDateTime(authlbsQuery.getRtime());
                if (!ObjectUtils.isEmpty(localDateTime)){
                    positionBean.setDate(localDateTime);
                }
            } catch (Exception e){}
        }
        positionBean.setMileage(0);
        locationBean.setPoint(positionBean);
        locationDataTransfer(locationBean);
    }

}
