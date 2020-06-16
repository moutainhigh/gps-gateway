package com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsstatus;

import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.basebean.AuthlbsBaseBean;
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
