package com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.authlbsquery;

import com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.basebean.AuthlbsBaseBean;
import lombok.Data;
import lombok.ToString;

/**
 * 实时定位接口(返回经纬度)响应模型
 * @author suibozhuliu
 */
@Data
@ToString(callSuper = true)
public class AuthlbsQueryBean extends AuthlbsBaseBean{

    /**
     * 纬度
     */
    private double lat;

    /**
     * 经度
     */
    private double lng;

    /**
     * 请求返回的地理位置所在的城市
     */
    private String city;

    /**
     * 请求返回的地理位置所在的街道
     */
    private String street;

    /**
     * 请求返回的地理位置所在的区域
     */
    private String district;

    /**
     * 请求返回的地理位置信息
     */
    private String location;

    /**
     * 请求返回的地理位置所在的省
     */
    private String province;

    /**
     * 手机号码归属地区，不加前面的0（例如：天津 22）
     */
    private String areacode;

    /**
     * 手机号码所在的漫游地区，不加前面的0（例如：天津 22）
     */
    private String roamcity;

    /**
     * 被定位的手机号码
     */
    private String mobile;

    /**
     * 当前请求的创建时间
     */
    private String ctime;

    /**
     * 当前请求的返回时间
     */
    private String rtime;

    /**
     * 当前请求的创建时间 与 返回时间 相差的毫秒值
     */
    private long msec;

    /**
     * 运营商返回的结果
     */
    private String exinfo;

}
