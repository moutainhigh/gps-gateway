package com.zkkj.gps.gateway.ccs.enume;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.IBaseEnum;
import io.swagger.annotations.ApiModel;

/**
 * author : cyc
 * Date : 2019/11/14
 */
@ApiModel("百度返回状态信息")
public enum BaiDuStatusEnum implements IBaseEnum {

    OK(0, "正常"),
    SERVER_ERROR(1, "服务器内部错误"),
    PARAMETER_INVALID(2, "请求参数非法"),
    VERIFY_FAILURE(3, "权限校验失败"),
    QUOTA_FAILURE(4, "配额校验失败"),
    AK_FAILURE(5, "ak不存在或者非法"),
    SERVICE_DISABLED(101, "服务禁用"),
    CODE_ERROR(102, "不通过白名单或者安全码不对"),
    PERMISSION_DENIED(200, "无权限"),
    QUOTA_ERROR(300, "配额错误");

    BaiDuStatusEnum(int seq, String desc) {
        this.seq = seq;
        this.desc = desc;
    }

    /**
     * 状态码
     */
    private final int seq;

    /**
     * 状态对应的描述
     */
    private final String desc;

    @Override
    public int getSeq() {
        return this.seq;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
