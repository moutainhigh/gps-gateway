package com.zkkj.gps.gateway.tcp.monitor.app.proforward.build;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-08-20 下午 8:82
 */

import com.zkkj.gps.gateway.protocol.component.messagebody.BusinessExtensionData;
import com.zkkj.gps.gateway.protocol.source.P_0410;

/**
 * 下发扩展业务数据协议构建器
 */
public class P_0410Build extends ProBuildBase {

    private BusinessExtensionData businessData;

    public P_0410Build(String terminalId,BusinessExtensionData business) {
        super(terminalId);
        this.businessData = business;
    }

    /**
     * 组装P_0410对象
     *
     * @return
     */
    public P_0410 getP_0410() throws Exception{
        int storageRegion = 0;
        //String terminalId, int serialNumber, int storageRegion, BusinessExtensionData businessExtensionData
        P_0410 p_0410 = new P_0410(getTerminalId(), getReqSerialnumber(), storageRegion, businessData);
        return p_0410;
    }

}
