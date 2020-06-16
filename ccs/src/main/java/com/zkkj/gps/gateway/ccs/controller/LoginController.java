package com.zkkj.gps.gateway.ccs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zkkj.gps.gateway.ccs.annotation.NoAccessResponseLogger;
import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.config.RedisDao;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.token.PlatformLogin;
import com.zkkj.gps.gateway.ccs.dto.token.PlatformLoginTruckNum;
import com.zkkj.gps.gateway.ccs.dto.token.TerminalGroup;
import com.zkkj.gps.gateway.ccs.dto.token.TerminalGroupDto;
import com.zkkj.gps.gateway.ccs.dto.token.TerminalGroupInfo;
import com.zkkj.gps.gateway.ccs.dto.token.TerminalTruckOnly;
import com.zkkj.gps.gateway.ccs.dto.token.TokenUser;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.service.CommonService;
import com.zkkj.gps.gateway.ccs.utils.JSonUtils;
import com.zkkj.gps.gateway.ccs.utils.Md5Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/login")
@CrossOrigin
@Api(value = "用户登录接口", description = "用户登录接口api")
public class LoginController extends CommonBaseUtil {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private RedisDao redisdao;


    @PostMapping(value = "/login")
    @ApiOperation("用户登录")
    public ResultVo<String> login(@RequestBody PlatformLogin platformLogin) {

        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (platformLogin.getAppkey() == null || platformLogin.getAppkey().length() <= 0 ||
                    platformLogin.getAppsecret() == null || platformLogin.getAppsecret().length() <= 0 ||
                    platformLogin.getTerminals() == null) {
                resultVo.resultFail("传入的appkey、appsecret、terminals不能为空");
            } else {
                ResultVo<List<TruckAndTerminal>> listResultVo = commonService.Login(platformLogin);
                if (!ObjectUtils.isEmpty(listResultVo) && listResultVo.isSuccess() == true) {
                    StringBuilder sb = new StringBuilder();
                    String timespan = System.currentTimeMillis() + "";
                    sb.append(platformLogin.getAppkey() + "-" + platformLogin.getAppsecret() + "-" + timespan);
                    //MD5加密后的字符串
                    String token = Md5Util.MD5Encode(sb.toString());
                    TokenUser tokenUser = new TokenUser();
                    tokenUser.setAppkey(platformLogin.getAppkey());
                    tokenUser.setAppsecret(platformLogin.getAppsecret());
                    tokenUser.setTimespan(timespan);
                    tokenUser.setTruckAndTerminalList(listResultVo.getData());
                    tokenUser.setIdentity(platformLogin.getIdentity());
                    redisdao.setKey(token, JSonUtils.toJSon(tokenUser));
                    resultVo.resultSuccess(token);
                } else {
                    resultVo.resultFail(listResultVo.getMsg());
                }
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:用户登录失败");
            logger.error("LoginController login is error" , ex);
        }
        return resultVo;
    }


    @PostMapping(value = "/loginByTruckNum")
    @ApiOperation("用户登录通过车牌号")
    public ResultVo<String> loginByTruckNum(@RequestBody PlatformLoginTruckNum platformLogin) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (platformLogin.getAppkey() == null || platformLogin.getAppkey().length() <= 0 ||
                    platformLogin.getAppsecret() == null || platformLogin.getAppsecret().length() <= 0 ||
                    platformLogin.getTruckNums() == null) {
                resultVo.resultFail("传入的appkey、appsecret、truckNums不能为空");
            } else {
                ResultVo<List<TruckAndTerminal>> listResultVo = commonService.loginByTruckNum(platformLogin);
                if (!ObjectUtils.isEmpty(listResultVo) && listResultVo.isSuccess() == true) {
                    StringBuilder sb = new StringBuilder();
                    String timespan = System.currentTimeMillis() + "";
                    sb.append(platformLogin.getAppkey() + "-" + platformLogin.getAppsecret() + "-" + timespan);
                    //MD5加密后的字符串
                    String token = Md5Util.MD5Encode(sb.toString());
                    TokenUser tokenUser = new TokenUser();
                    tokenUser.setAppkey(platformLogin.getAppkey());
                    tokenUser.setAppsecret(platformLogin.getAppsecret());
                    tokenUser.setTimespan(timespan);
                    tokenUser.setTruckAndTerminalList(listResultVo.getData());
                    tokenUser.setIdentity(platformLogin.getIdentity());
                    redisdao.setKey(token, JSonUtils.toJSon(tokenUser));
                    resultVo.resultSuccess(token);
                } else {
                    resultVo.resultFail(listResultVo.getMsg());
                }
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:用户登录失败");
            logger.error("LoginController loginByTruckNum is error", ex);
        }
        return resultVo;
    }

    @PostMapping(value = "/loginByTruckNumAndKey")
    @ApiOperation("用户登录通过车牌号（平台3.0地图通用接口）")
    public ResultVo<String> loginByTruckNumAndKey(@RequestBody PlatformLoginTruckNum platformLogin) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (platformLogin.getAppkey() == null || platformLogin.getAppkey().length() <= 0 ||
                    platformLogin.getAppsecret() == null || platformLogin.getAppsecret().length() <= 0 ||
                    platformLogin.getTruckNums() == null) {
                resultVo.resultFail("传入的appkey、appsecret、truckNums不能为空");
            } else {
                ResultVo<List<TruckAndTerminal>> listResultVo = commonService.loginByTruckNumAndKey(platformLogin);
                if (!ObjectUtils.isEmpty(listResultVo) && listResultVo.isSuccess() == true) {
                    StringBuilder sb = new StringBuilder();
                    String timespan = System.currentTimeMillis() + "";
                    sb.append(platformLogin.getAppkey() + "-" + platformLogin.getAppsecret() + "-" + timespan);
                    //MD5加密后的字符串
                    String token = Md5Util.MD5Encode(sb.toString());
                    TokenUser tokenUser = new TokenUser();
                    tokenUser.setAppkey(platformLogin.getAppkey());
                    tokenUser.setAppsecret(platformLogin.getAppsecret());
                    tokenUser.setTimespan(timespan);
                    tokenUser.setTruckAndTerminalList(listResultVo.getData());
                    tokenUser.setIdentity(platformLogin.getIdentity());
                    redisdao.setKey(token, JSonUtils.toJSon(tokenUser));
                    resultVo.resultSuccess(token);
                } else {
                    resultVo.resultFail(listResultVo.getMsg());
                }
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:用户登录失败");
            logger.error("LoginController loginByTruckNumAndKey is error", ex);
        }
        return resultVo;
    }


    @GetMapping(value = "/getTruckAndTerminalInfo")
    @ApiOperation("通过token获取对应的用户信息")
    @NoAccessResponseLogger
    public ResultVo<List<TruckAndTerminal>> getTruckAndTerminalInfo() {
        ResultVo<List<TruckAndTerminal>> resultVo = new ResultVo<>();
        try {
            List<TruckAndTerminal> list = getTruckAndTerminal();
            if (list != null) {
                resultVo.resultSuccess(list);
            } else {
                resultVo.resultFail("未获取信息失败");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取用户信息失败");
            logger.error("LoginController getTokenUserByToken  is error", ex);
        }
        return resultVo;
    }


    @PostMapping(value = "addTerminalGroupInfo")
    @ApiOperation(value = "新增设备群组信息", notes = "新增设备群组信息api")
    public ResultVo<String> addTerminalGroupInfo(@RequestBody TerminalGroupInfo terminalGroupList) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            TokenUser tokenUser = getUserInfo();
            List<TruckAndTerminal> truckAndTerminals = tokenUser.getTruckAndTerminalList();
            if (!ObjectUtils.isEmpty(terminalGroupList) && !CollectionUtils.isEmpty(truckAndTerminals)) {
                for (TerminalGroup terminalGroup : terminalGroupList.getTerminalGroupList()) {
                    for (String terminalNo : terminalGroup.getTerminalNo()) {
                        for (TruckAndTerminal truckAndTerminal : truckAndTerminals) {
                            if (truckAndTerminal.getTerminalNo().equals(terminalNo)) {
                                if (truckAndTerminal.getBelongGroup() == null || truckAndTerminal.getBelongGroup().size() <= 0) {
                                    truckAndTerminal.setBelongGroup(new ArrayList<>());
                                }
                                truckAndTerminal.getBelongGroup().add(terminalGroup.getGroupName());
                            }
                        }
                    }
                }
                tokenUser.setTruckAndTerminalList(truckAndTerminals);
                redisdao.setKey(getToken(), JSonUtils.toJSon(tokenUser));
                resultVo.resultSuccess("新增成功");
            } else {
                resultVo.resultFail("传入信息有误");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:新增设备群组信息失败");
            logger.error("LoginController addTerminalGroupInfo is error", ex);
        }
        return resultVo;
    }

    @GetMapping(value = "getTerminalGroupInfo")
    @ApiOperation(value = "获取设备群组信息", notes = "获取设备群组信息api")
    @NoAccessResponseLogger
    public ResultVo<TerminalGroupDto> getTerminalGroupInfo() {
        ResultVo<TerminalGroupDto> resultVo = new ResultVo<>();
        try {
            TerminalGroupDto terminalGroupDto = new TerminalGroupDto();
            TokenUser tokenUser = getUserInfo();
            if (tokenUser != null) {
                terminalGroupDto.setCorpName(tokenUser.getCorpName());
                Map<String, List<TerminalTruckOnly>> groupMap = new HashMap<>();
                List<TerminalTruckOnly> noGroupTerminal = new ArrayList<>();
                TerminalTruckOnly terminalOnly = new TerminalTruckOnly();
                if (tokenUser.getTruckAndTerminalList() != null && tokenUser.getTruckAndTerminalList().size() > 0) {
                    for (TruckAndTerminal truckAndTerminal : tokenUser.getTruckAndTerminalList()) {
                        if (truckAndTerminal != null && truckAndTerminal.getBelongGroup() != null && truckAndTerminal.getBelongGroup().size() > 0
                                && !ObjectUtils.isEmpty(truckAndTerminal.getTruckNo()) && !ObjectUtils.isEmpty(truckAndTerminal.getTerminalNo())) {
                            for (String groupName : truckAndTerminal.getBelongGroup()) {
                                if (groupMap.containsKey(groupName)) {
                                    List<TerminalTruckOnly> list = groupMap.get(groupName);
                                    list.add(new TerminalTruckOnly(truckAndTerminal.getTruckNo(), truckAndTerminal.getTerminalNo()));
                                    groupMap.put(groupName, list);
                                } else {
                                    List<TerminalTruckOnly> list = new ArrayList<>();
                                    list.add(new TerminalTruckOnly(truckAndTerminal.getTruckNo(), truckAndTerminal.getTerminalNo()));
                                    groupMap.put(groupName, list);
                                }
                            }
                        } else {
                            terminalOnly = new TerminalTruckOnly(truckAndTerminal.getTruckNo(), truckAndTerminal.getTerminalNo());
                            noGroupTerminal.add(terminalOnly);
                        }
                    }
                }
                terminalGroupDto.setTerminalInfo(noGroupTerminal);
                terminalGroupDto.setGroupTerminalList(groupMap);
            }
            resultVo.resultSuccess(terminalGroupDto);
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取设备群组信息失败");
            logger.error("LoginController getTerminalGroupInfo is error", ex);
        }
        return resultVo;
    }


}
