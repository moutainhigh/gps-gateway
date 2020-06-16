package com.zkkj.gps.gateway.gpsZjxl.scheduled;

import com.alibaba.fastjson.TypeReference;
import com.zkkj.gps.gateway.gpsZjxl.common.ResultVo;
import com.zkkj.gps.gateway.gpsZjxl.dao.RedisDao;
import com.zkkj.gps.gateway.gpsZjxl.entity.position.RealBaseGpsPositionInfo;
import com.zkkj.gps.gateway.gpsZjxl.entity.terminal.TruckAndTerminal;
import com.zkkj.gps.gateway.gpsZjxl.service.LocationWebApiService;
import com.zkkj.gps.gateway.gpsZjxl.utils.ConstantUtils;
import com.zkkj.gps.gateway.gpsZjxl.utils.DateTimeUtils;
import com.zkkj.gps.gateway.gpsZjxl.utils.FastJsonUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019/11/30
 * 针对府谷煤管站从中交信路获取定位信息定时任务
 */
@Configuration
@Component
@EnableScheduling // 该注解必须要加
public class FgmgzPositionScheduleTask {

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    @Autowired
    private RedisDao redisDao;

    @Value("${fgmgz.appkey}")
    private String fgmgzAppKey;

    @Value("${zjxl.limit.count}")
    private int zjxlLimitCount;

    @Autowired
    private LocationWebApiService locationWebApiService;

    private Logger logger = LoggerFactory.getLogger(FgmgzPositionScheduleTask.class);

    /**
     * 1.先从redis中获取府谷appkey下对应的所有车辆
     * 2.从中交兴路获取定位信息
     */
    public void fgmgzPositionScheduleRun() {
        try {
            Map<String, TruckAndTerminal> truckAndTerminalMap = FastJsonUtils.parseObject(redisDao.getValue(fgmgzAppKey), new TypeReference<Map<String, TruckAndTerminal>>() {
            });
            logger.info(DateTimeUtils.getCurrentDateTimeStr() + "当前线程:" + Thread.currentThread().getName() + ";truckAndTerminalMap:" + (truckAndTerminalMap == null ? "null" : truckAndTerminalMap.size()));
            if (MapUtils.isNotEmpty(truckAndTerminalMap)) {
                //当前设备号就是中交兴路的车牌号
                List<TruckAndTerminal> truckAndTerminals = truckAndTerminalMap.entrySet().stream().map(p -> new TruckAndTerminal(p.getKey(), p.getValue().getTruckNo())).collect(Collectors.toList());
                List<List<TruckAndTerminal>> partition = ListUtils.partition(truckAndTerminals, zjxlLimitCount);
                for (List<TruckAndTerminal> truckAndTerminalsList : partition) {
                    List<String> terminalNoList = truckAndTerminalsList.stream().map(p -> p.getTerminalNo()).filter(p -> p.matches(ConstantUtils.PLATE_NUMBER)).collect(Collectors.toList());
                    String truckAndTerminalString = StringUtils.join(terminalNoList, ",");
                    //使用Future实现对任务的取消，获取结果等操作
                    executorService.execute(() -> {
                        try {
                            long time3 = System.currentTimeMillis();
                            ResultVo<List<RealBaseGpsPositionInfo>> resultVo = locationWebApiService.getGpsByLicensePlates(truckAndTerminalString);
                            long time4 = System.currentTimeMillis();
                            logger.info(DateTimeUtils.getCurrentDateTimeStr() + "线程:" + Thread.currentThread().getName() + ";线程hash:" + Thread.currentThread().hashCode() + "耗时:" + (time4 - time3) + ";");
                            if (!resultVo.isSuccess()) {
                                logger.info(DateTimeUtils.getCurrentDateTimeStr() + "线程:" + Thread.currentThread().getName() + ";hash:" + Thread.currentThread().hashCode() + ";结果" + resultVo);
                            }
                        } catch (Exception e) {
                            logger.error("FgmgzPositionScheduleTask fixedThreadPool execute is error", e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("FgmgzPositionScheduleTask fgmgzPositionScheduleRun is error", e);
        }

    }
}
