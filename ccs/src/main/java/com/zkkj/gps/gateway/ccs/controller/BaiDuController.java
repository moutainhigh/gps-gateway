package com.zkkj.gps.gateway.ccs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zkkj.gps.gateway.ccs.annotation.TokenFilter;
import com.zkkj.gps.gateway.ccs.dto.baiDuDto.BaiDuResult;
import com.zkkj.gps.gateway.ccs.dto.baiDuDto.Result;
import com.zkkj.gps.gateway.ccs.dto.baiDuDto.ReverseResult;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.enume.BaiDuStatusEnum;
import com.zkkj.gps.gateway.ccs.service.BaiDuWebApiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * author : cyc
 * Date : 2019/11/14
 */

@RestController
@RequestMapping(value = "/baiDu")
@CrossOrigin
@Api(value = "百度地图相关接口", description = "百度地图相关接口api")
public class BaiDuController {

    private Logger logger = LoggerFactory.getLogger(BaiDuController.class);

    @Autowired
    private BaiDuWebApiService baiDuWebApiService;

    @ApiOperation(value = "百度地理编码api", notes = "百度地理编码api接口api")
    @GetMapping(value = "/geocoding")
    public ResultVo<BaiDuResult<Result>> geocoding(@ApiParam(value = "待解析的地址。最多支持84个字节") @RequestParam String address,
                                           @ApiParam(value = "用户申请注册的key") @RequestParam String ak) {
        ResultVo<BaiDuResult<Result>> resultVo = new ResultVo<>();
        try {
            BaiDuResult<Result> baiDuDtoResultVo = baiDuWebApiService.v3(address, "json", ak);
            if (baiDuDtoResultVo != null) {
                if (baiDuDtoResultVo.getStatus() == (BaiDuStatusEnum.OK.getSeq())) {
                    resultVo.setData(baiDuDtoResultVo);
                    resultVo.setMsg(BaiDuStatusEnum.OK.getDesc());
                    return resultVo;
                }
                setResultMsg(resultVo, baiDuDtoResultVo);
                resultVo.resultFail(resultVo.getMsg());
                return resultVo;
            }
            resultVo.resultFail("请求失败，返回结果为空");
            return resultVo;
        } catch (Exception e) {
            resultVo.resultFail("系统异常:百度地图调用失败");
            logger.error("BaiDuController.geocoding is error", e);
            return resultVo;
        }
    }

    @ApiOperation(value = "百度逆地理编码api", notes = "百度逆地理编码接口api")
    @GetMapping(value = "reverseGeocoding")
    @TokenFilter
    public ResultVo<BaiDuResult<ReverseResult>> reverseGeocoding(@ApiParam(value = "用户申请注册的key") @RequestParam String ak,
                                                    @ApiParam(value = "坐标的类型,bd09ll(百度经纬度坐标)、bd09mc(百度米制坐标)、gcj02ll(国测局经纬度坐标、仅限中国),wgs84ll(GPS经纬度)") @RequestParam String coordtype,
                                                    @ApiParam(value = "经纬度坐标,格式:纬度,经度") @RequestParam String location) {
        ResultVo<BaiDuResult<ReverseResult>> resultVo = new ResultVo<>();
        try {
            BaiDuResult<ReverseResult> baiDuDtoResultVo = baiDuWebApiService.reverseGeocoding(ak, "json", coordtype, location);
            if (baiDuDtoResultVo != null) {
                if (baiDuDtoResultVo.getStatus() == (BaiDuStatusEnum.OK.getSeq())) {
                    resultVo.setMsg(BaiDuStatusEnum.OK.getDesc());
                    resultVo.setData(baiDuDtoResultVo);
                    return resultVo;
                }
                setResultMsg(resultVo, baiDuDtoResultVo);
                resultVo.resultFail(resultVo.getMsg());
                return resultVo;

            }
            resultVo.resultFail("请求失败，返回结果为空");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultVo;
    }


    /**
     * 针对百度返回的状态码设置返回对象提示信息
     *
     * @param resultVo
     * @param baiDuDtoResultVo
     */
    private void setResultMsg(ResultVo resultVo, BaiDuResult baiDuDtoResultVo) {
        int status = baiDuDtoResultVo.getStatus();
        if (status == BaiDuStatusEnum.SERVER_ERROR.getSeq()) {
            resultVo.setMsg(BaiDuStatusEnum.SERVER_ERROR.getDesc());
        }
        if (status == BaiDuStatusEnum.PARAMETER_INVALID.getSeq()) {
            resultVo.setMsg(BaiDuStatusEnum.PARAMETER_INVALID.getDesc());
        }
        if (status == BaiDuStatusEnum.VERIFY_FAILURE.getSeq()) {
            resultVo.setMsg(BaiDuStatusEnum.VERIFY_FAILURE.getDesc());
        }
        if (status == BaiDuStatusEnum.QUOTA_FAILURE.getSeq()) {
            resultVo.setMsg(BaiDuStatusEnum.QUOTA_FAILURE.getDesc());
        }
        if (status == BaiDuStatusEnum.AK_FAILURE.getSeq()) {
            resultVo.setMsg(BaiDuStatusEnum.AK_FAILURE.getDesc());
        }
        if (status == BaiDuStatusEnum.SERVICE_DISABLED.getSeq()) {
            resultVo.setMsg(BaiDuStatusEnum.SERVICE_DISABLED.getDesc());
        }
        if (status == BaiDuStatusEnum.CODE_ERROR.getSeq()) {
            resultVo.setMsg(BaiDuStatusEnum.CODE_ERROR.getDesc());
        }
        if (status >= 200 && status <= 299) {
            resultVo.setMsg(BaiDuStatusEnum.PERMISSION_DENIED.getDesc());
        }
        if (status >= 300 && status <= 399) {
            resultVo.setMsg(BaiDuStatusEnum.QUOTA_ERROR.getDesc());
        }
    }
}
