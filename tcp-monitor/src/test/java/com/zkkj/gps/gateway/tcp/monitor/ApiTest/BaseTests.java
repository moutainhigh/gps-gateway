package com.zkkj.gps.gateway.tcp.monitor.ApiTest;

import com.alibaba.fastjson.JSON;
import com.zkkj.gps.gateway.protocol.dto.base.ResultVo;
import com.zkkj.gps.gateway.tcp.monitor.TcpMain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TcpMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTests {

    protected static final String defaultToken = "default";
    private static final String baseUrl = "http://localhost:";

    private Logger logger = LoggerFactory.getLogger(BaseTests.class);

    @LocalServerPort
    protected Integer port;
    @Autowired
    protected TestRestTemplate restTemplate;
    @Test
    public void testInit()  {
        System.out.println("......................................");
    }

    private String getToken(){
        return "";
    }

    protected ResultVo getTest(String method, Map<String, String> params)  {
        return base(method, params, getToken(), HttpMethod.GET);
    }

    protected ResultVo getTest(String method, Map<String, String> params, String token)  {
        return base(method, params, token, HttpMethod.GET);
    }

    protected ResultVo postTest(String method, Object params)  {
        return base(method, params, getToken(), HttpMethod.POST);
    }

    protected ResultVo postTestBody(String method, Object address, Object params)  {
        return baseBody(method, address, params, getToken(), HttpMethod.POST);
    }

    protected ResultVo postTest(String method, Object params, String token){
        return base(method, params, token, HttpMethod.POST);
    }

    private ResultVo base(String method, Object params, String token, HttpMethod httpMethod)   {
        logger.info("测试方法:" + method + "开始");
        logger.info("----------------------------分割线----------------------------");
        logger.info("Token:" + token);
        HttpHeaders requestHeaders = new HttpHeaders();
        ResponseEntity<String> response = null;
        requestHeaders.add("charset", "UTF-8");
        requestHeaders.add("Content-Type", "application/json");
        //有token加token
        if (token != null) {
            requestHeaders.add("Authorization",  token);
        }
        String getUrl = baseUrl + port + "/" + method;
        logger.info("getUrl:" + getUrl);
        //GET请求
        if (httpMethod == HttpMethod.GET) {
            logger.info("GET请求");
            HashMap<String, Object> map = (HashMap<String, Object>) params;
            if (map != null && !map.isEmpty()) {
                getUrl += "?";
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    getUrl += entry.getKey() + "=" + entry.getValue() + "&";
                }
                logger.info("GetUrl拼接：" + getUrl);
            }
            HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(null, requestHeaders);
            response = restTemplate.exchange(getUrl, httpMethod, requestEntity, String.class);
        }
        //POST请求
        if (httpMethod == HttpMethod.POST) {
            logger.info("POST请求");
            if (params instanceof Map) {
                HashMap<String, Object> map = (HashMap<String, Object>) params;
                if (map != null && !map.isEmpty()) {
                    getUrl += "?";
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        getUrl += entry.getKey() + "=" + entry.getValue() + "&";
                    }
                    logger.info("PostUrl拼接：" + getUrl);
                }
                //REST模板
                HttpEntity<Map> requestEntity = new HttpEntity<>(null, requestHeaders);
                response = restTemplate.exchange(getUrl, httpMethod, requestEntity, String.class);
            } else {
                HttpEntity<Object> requestEntity = new HttpEntity<>(params, requestHeaders);
                response = restTemplate.exchange(baseUrl + port + "/" + method, httpMethod, requestEntity, String.class);
            }
        }
        ResultVo re = JSON.parseObject(response.getBody(), ResultVo.class);
        logger.info("----------------------------分割线----------------------------");
        logger.info("状态:" + response.getStatusCode());
        logger.info("结果展示:" + re);
        logger.info("测试方法:" + method + "结束");
        Assert.assertTrue("状态异常方法:" + method, response.getStatusCode().compareTo(HttpStatus.OK) == 0);
        return re;

    }

    private ResultVo baseBody(String method,Object address, Object params, String token, HttpMethod httpMethod)   {
        ResultVo resultVo = new ResultVo();
        resultVo.resultFail("参数异常");
        logger.info("测试方法:" + method + "开始");
        logger.info("----------------------------分割线----------------------------");
        logger.info("Token:" + token);
        HttpHeaders requestHeaders = new HttpHeaders();
        ResponseEntity<String> response = null;
        requestHeaders.add("charset", "UTF-8");
        requestHeaders.add("Content-Type", "application/json");
        //有token加token
        if (token != null) {
            requestHeaders.add("Authorization",  token);
        }
        String getUrl = baseUrl + port + "/" + method;
        logger.info("getUrl:" + getUrl);
        //POST请求
        if (httpMethod == HttpMethod.POST) {
            logger.info("POST请求");
            if (address instanceof Map) {
                HashMap<String, Object> map = (HashMap<String, Object>) address;
                if (map != null && !map.isEmpty()) {
                    getUrl += "?";
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        getUrl += entry.getKey() + "=" + entry.getValue() + "&";
                    }
                    logger.info("PostUrl地址拼接：" + getUrl);
                }
                if (ObjectUtils.isEmpty(params)){
                    return resultVo;
                }
                //REST模板
                HttpEntity<Object> entity = new HttpEntity<>(params,requestHeaders);
                response = restTemplate.exchange(getUrl, httpMethod, entity, String.class);
            } else {
                return resultVo;
            }
        }
        resultVo = JSON.parseObject(response.getBody(), ResultVo.class);
        logger.info("----------------------------分割线----------------------------");
        logger.info("状态:" + response.getStatusCode());
        logger.info("结果展示:" + resultVo);
        logger.info("测试方法:" + method + "结束");
        Assert.assertTrue("状态异常方法:" + method, response.getStatusCode().compareTo(HttpStatus.OK) == 0);
        return resultVo;

    }

}
