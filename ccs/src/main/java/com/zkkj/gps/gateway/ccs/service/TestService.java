package com.zkkj.gps.gateway.ccs.service;

import org.springframework.web.multipart.MultipartFile;

import com.zkkj.gps.gateway.ccs.entity.test.PointSource;

/**
 * author : cyc
 * Date : 2020/3/16
 */
public interface TestService {

    /**
     * 将路线导出点位集合
     *
     * @param lineStrings
     */
    void exportPositionList(PointSource lineStrings) throws Exception;

    /**
     * 点位导入，模拟真实设备回传定位
     *
     * @param file
     * @param dispatchNo
     */
    void importPositionList(MultipartFile file, String dispatchNo) throws Exception;
}
