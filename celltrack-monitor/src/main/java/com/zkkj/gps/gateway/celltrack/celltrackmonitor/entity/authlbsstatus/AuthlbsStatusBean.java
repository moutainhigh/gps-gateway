package com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.authlbsstatus;

import com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.basebean.AuthlbsBaseBean;
import lombok.Data;
import lombok.ToString;

/**
 * 状态查询响应模型
 * @author suibozhuliu
 */
@Data
@ToString(callSuper = true)
public class AuthlbsStatusBean extends AuthlbsBaseBean {

    /**
     * 被查询的手机号码
     */
    private String mobile;

}
