package com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.authlbsopen;

import com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.basebean.AuthlbsBaseBean;
import lombok.Data;
import lombok.ToString;

/**
 * 开通授权定位响应模型
 * @author suibozhuliu
 */
@Data
@ToString(callSuper = true)
public class AuthlbsOpenBean extends AuthlbsBaseBean{

    /**
     * 申请白名单的手机号码
     */
    private String mobile;

}
