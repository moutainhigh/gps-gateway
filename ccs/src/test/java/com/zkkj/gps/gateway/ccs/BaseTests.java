package com.zkkj.gps.gateway.ccs;

import com.alibaba.fastjson.JSON;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CCSMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTests {

    protected static final String defaultToken = "default";
    private static final String baseUrl = "http://hhdv1t.zkkjgs.com:3002";

    private Logger logger = LoggerFactory.getLogger(BaseTests.class);

    @LocalServerPort
    protected Integer port;
    @Autowired
    protected TestRestTemplate restTemplate;
    @Test
    public void testInit()  {
        System.out.println("......................................");
    }

    private String getToken()
    {
        return  "";
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

    protected ResultVo postTest(String method, Object params, String token){
        return base(method, params, token, HttpMethod.POST);
    }




    private ResultVo base(String method, Object params, String token, HttpMethod httpMethod)   {
//        logger.info("测试方法:" + method + "开始");
//        logger.info("----------------------------分割线----------------------------");
//        logger.info("Token:" + token);
        HttpHeaders requestHeaders = new HttpHeaders();
        ResponseEntity<String> response = null;
        requestHeaders.add("charset", "UTF-8");
        requestHeaders.add("Content-Type", "application/json");
        //有token加token
        if (token != null) {
            requestHeaders.add("Authorization",  "0dbdb97c45a4f6496d09a0bc64ac5176");
        }
        String getUrl = baseUrl  + "/" + method;
//        logger.info("getUrl:" + getUrl);
        if (httpMethod == HttpMethod.GET) {
            HashMap<String, String> map = (HashMap<String, String>) params;

            if (map != null && !map.isEmpty()) {
                getUrl += "?";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    getUrl += entry.getKey() + "=" + entry.getValue() + "&";
                }
            }
            HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(null, requestHeaders);
            response = restTemplate.exchange(getUrl, httpMethod, requestEntity, String.class);
        }
        if (httpMethod == HttpMethod.POST) {
            if (params instanceof Map) {
                HashMap<String, String> map = (HashMap<String, String>) params;
                if (map != null && !map.isEmpty()) {
                    getUrl += "?";
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        getUrl += entry.getKey() + "=" + entry.getValue() + "&";
                    }
                }
                HttpEntity<Map> requestEntity = new HttpEntity<>(null, requestHeaders);
                response = restTemplate.exchange(getUrl, httpMethod, requestEntity, String.class);
            } else {
                HttpEntity<Object> requestEntity = new HttpEntity<>(params, requestHeaders);
                response = restTemplate.exchange(baseUrl  + "/" + method, httpMethod, requestEntity, String.class);
            }
        }
        ResultVo re = JSON.parseObject(response.getBody(), ResultVo.class);
//        logger.info("----------------------------分割线----------------------------");
//        logger.info("状态:" + response.getStatusCode());
//        logger.info("结果展示:" + re);
//        logger.info("测试方法:" + method + "结束");
//        Assert.assertTrue("状态异常方法:" + method, response.getStatusCode().compareTo(HttpStatus.OK) == 0);
        return re;

    }

}
