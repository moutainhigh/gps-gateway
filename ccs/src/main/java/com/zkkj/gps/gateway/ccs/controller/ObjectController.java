package com.zkkj.gps.gateway.ccs.controller;

import java.lang.reflect.Field;

import com.zkkj.gps.gateway.ccs.annotation.NoLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/object")
@CrossOrigin
@Api(value = "从内存中获取对象", description = "从内存中获取对象api")
public class ObjectController {
    private Logger logger = LoggerFactory.getLogger(ObjectController.class);

    @ApiOperation(value = "从内存中获取对象！", notes = "从内存中获取对象")
    @PostMapping(value = "/getObjectByClassPath")
    @NoLogger
    public ResultVo<Object> getObjectByClassPath(@RequestParam @ApiParam("对象路径") String classPath, @RequestParam @ApiParam("对象属性") String targetFieldName) {
        ResultVo<Object> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isBlank(classPath) || StringUtils.isBlank(targetFieldName)) {
                resultVo.resultFail("传入参数有误");
                return resultVo;
            }
            Class objectClass = Class.forName(classPath);
            if (objectClass == null) {
                resultVo.resultFail("获取对象失败");
                return resultVo;
            }
            Field[] field02 = objectClass.getDeclaredFields();
            for (Field field : field02) {
                field.setAccessible(true);
                if (field.getName().equals(targetFieldName)) {
                    resultVo.resultSuccess(field.get(targetFieldName));
                    return resultVo;
                }
            }
            resultVo.resultFail("未获取到对象属性信息");
            return resultVo;
        } catch (Exception ex) {
            logger.error("ObjectController.getObjectByClassPath is error", ex);
            resultVo.resultFail("系统异常:从内存中获取对象失败");
        }
        return resultVo;
    }
}
