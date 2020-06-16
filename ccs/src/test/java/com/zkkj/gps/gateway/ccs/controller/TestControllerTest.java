package com.zkkj.gps.gateway.ccs.controller;

import com.zkkj.gps.gateway.ccs.BaseTests;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestControllerTest extends BaseTests {

    @Test
    public void getHelloWorld() {
        String arg = "你好！";
        Map<String, String> params = new HashMap<>();
        params.put("arg", arg);
        ResultVo<String> resultVo = postTest("test/helloworld", params);
//        Assert.assertTrue(resultVo.isSuccess());
//        Assert.assertEquals(resultVo.getData(),"Hello World!"+arg);
    }
}