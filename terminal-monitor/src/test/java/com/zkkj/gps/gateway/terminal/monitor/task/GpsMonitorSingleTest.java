package com.zkkj.gps.gateway.terminal.monitor.task; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 

/** 
* GpsMonitorSingle Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 19, 2019</pre> 
* @version 1.0 
*/ 
public class GpsMonitorSingleTest {


    /**
     *
     * Method: positionChange(String terminalId, GPSPositionDTO gpsPosition)
     *
     */
    @Test
    public void testPositionChange() throws Exception {
        //TODO: Test 位置变更测试，需要进行大量多线程测试
    }



    /**
     *
     * Method: validatePosition(String terminalId, SiteBasicMessage siteBasicMessage)
     *
     */
    @Test
    public void testValidatePosition() throws Exception {
        //TODO: Test 验证经纬度是否正确
        /*
        try {
           Method method = GpsMonitorSingle.getClass().getMethod("validatePosition", String.class, SiteBasicMessage.class);
           method.setAccessible(true);
           method.invoke(<Object>, <Parameters>);
        } catch(NoSuchMethodException e) {
        } catch(IllegalAccessException e) {
        } catch(InvocationTargetException e) {
        }
        */
    }

    /**
     *
     * Method: updateTerminalHisPosition(String terminalId, GPSPositionDTO siteBasicMessage)
     *
     */
    @Test
    public void testUpdateTerminalHisPosition() throws Exception {
        //TODO: Test 更新历史经纬度信息
        /*
        try {
           Method method = GpsMonitorSingle.getClass().getMethod("updateTerminalHisPosition", String.class, GPSPositionDTO.class);
           method.setAccessible(true);
           method.invoke(<Object>, <Parameters>);
        } catch(NoSuchMethodException e) {
        } catch(IllegalAccessException e) {
        } catch(InvocationTargetException e) {
        }
        */
    }

} 
