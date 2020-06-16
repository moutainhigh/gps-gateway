package com.zkkj.gps.gateway.tcp.monitor.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会话同步锁、响应对象
 *
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-13 下午 7:28
 */
@Data
@ApiModel(value = "统一返回类型", description = "统一返回类型")
public class SyncBean<T> {
    /**
     * 返回统一值
     */
    @ApiModelProperty(value = "请求成功失败标识 true 成功 false 失败", name = "success")
    private boolean success = true;
    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息 当success错误时 返回错误信息", name = "msg")
    private String msg = "success";

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;

    public SyncBean() {
    }

    /**
     * 失败响应数据设置
     *
     * @param msg
     */
    public void resultFail(String msg) {
        this.success = false;
        this.msg = msg;
    }

    /**
     * 成功响应数据设置
     *
     * @param obj
     */
    public void resultSuccess(T obj, String msg) {
        this.success = true;
        this.data = obj;
        this.msg = msg;
    }
}
