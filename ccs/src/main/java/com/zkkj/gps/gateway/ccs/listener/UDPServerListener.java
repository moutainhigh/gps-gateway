package com.zkkj.gps.gateway.ccs.listener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import com.zkkj.gps.gateway.common.utils.DataCommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.zkkj.gps.gateway.ccs.dto.amqp.RcvMonitorDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchEventDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.monitorconfig.MonitorConfigCache;
import com.zkkj.gps.gateway.ccs.mq.producer.ProducerSubscribe;
import com.zkkj.gps.gateway.ccs.service.GpsInternalService;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseFlag;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.HhdBusinessDto;

/**
 * author : cyc
 * Date : 2019-06-07
 * 服务端，udp接收数据
 */
@Component
public class UDPServerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ProducerSubscribe producerSubscribe;

    /**
     * 创建一个单线程的线程池，以无界队列方式来运行该线程。当多个任务提交到单线程线程池中，线程池将逐个去进行执行，未执行的任务将放入无界队列进行等待。
     */
    public static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    private Logger logger = LoggerFactory.getLogger(UDPServerListener.class);

    /**
     * 因为构造函数优于配置文件中的属性复制给成员变量，所以在构造函数中获取到的成员变量值是默认值
     *
     * @PostConstruct注解，在对象加载完依赖注入后执行的
     */
    @PostConstruct
    public void initConstruct() {
        try {
            socket = new DatagramSocket(UDP_PORT);
            packet = new DatagramPacket(new byte[MAX_UDP_DATA_SIZE], MAX_UDP_DATA_SIZE);
        } catch (SocketException e) {
            logger.error("UDPServerListener.initConstruct is error", e);
        }
    }

    //udp协议的端口号
    @Value("${udp.port}")
    private Integer UDP_PORT;

    //udp协议
    @Value("${max.udp.data.size}")
    private int MAX_UDP_DATA_SIZE;

    @Autowired
    private GpsInternalService gpsInternalService;

    // 数据报套接字
    private DatagramSocket socket;

    //用以接收数据报
    private DatagramPacket packet;

    /**
     * 使用springboot操作，此处没有出现二次调用问题；
     * 在传统的spring项目中配置application.xml和project-servlet.xml配置中会出现二次调用问题。
     * 主要原因是初始化root容器后，会初始化project-sevlet.xml对应的容器。
     * 所以在使用spring过程中可以添加如下代码，避免二次调用
     * if(contextRefreshedEvent.getApplicationContext().getParent() == null){ //root application context}
     *
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        singleThreadPool.execute(() -> {
            logger.info("点位信息线程名称......." + Thread.currentThread().getName());
            while (true) {
                //创建数据包对象，传递字节数组
                try {
                    //receive()来等待接收UDP数据报
                    socket.receive(packet);
                    //解析数据
                    String receiveStr = new String(packet.getData(), 0, packet.getLength(),"utf-8");
                    //将json数据转成对象并进行业务处理
                    converseDate(receiveStr);
                    //将json数据转成对象并进行业务处理
                } catch (Exception e) {
                    logger.error("UDPServerListener.onApplicationEvent", e);
                }
            }
        });
    }

    private void converseDate(String receiveStr) throws Exception {
        BaseFlag baseFlag = FastJsonUtils.toBean(receiveStr, BaseFlag.class);
        BaseGPSPositionDto baseGPSPositionDto = null;
        switch (baseFlag.getFlag()) {
            case 0:
                baseGPSPositionDto = FastJsonUtils.toBean(receiveStr, BaseGPSPositionDto.class);
                break;
            case 1:
                baseGPSPositionDto = FastJsonUtils.parseObject(receiveStr, new TypeReference<BaseGPSPositionDto<HhdBusinessDto>>() {
                });
                break;
            case 3:
                baseGPSPositionDto = FastJsonUtils.parseObject(receiveStr, new TypeReference<BaseGPSPositionDto<HhdBusinessDto>>() {
                });
                break;
            case 4:
                baseGPSPositionDto = FastJsonUtils.toBean(receiveStr, BaseGPSPositionDto.class);
                break;
            case 5:
                baseGPSPositionDto = FastJsonUtils.toBean(receiveStr, BaseGPSPositionDto.class);
                break;
        }
        BeanValidate.checkParam(baseGPSPositionDto);
        transformPosition(baseGPSPositionDto);
        baseGPSPositionDto.setRcvTime(baseGPSPositionDto.getRcvTime() == null ? DateTimeUtils.getCurrentLocalDateTime() : baseGPSPositionDto.getRcvTime());
        DispatchInfoDto dispatchInfoDto = MonitorConfigCache.getInstance().getDispatchInfoDtoMap().get(baseGPSPositionDto.getTerminalId());
        String dispatchNo = dispatchInfoDto == null ? "" : dispatchInfoDto.getDispatchNo();
        gpsInternalService.positionChange(baseGPSPositionDto.getTerminalId(), baseGPSPositionDto, dispatchNo);
    }

    /**
     * 转化点位对象
     *
     * @param baseGPSPositionDto
     */
    private void transformPosition(BaseGPSPositionDto baseGPSPositionDto) {
        try {
            String terminalId = baseGPSPositionDto.getTerminalId();
            //获取缓存中的运单信息
            DispatchInfoDto dispatchInfoDto = MonitorConfigCache.getInstance().getDispatchInfoDtoMap().get(terminalId);
            int flag = baseGPSPositionDto.getFlag();
            //针对有运单的处理,(航鸿达,甲天行)设备协议数据
            if (flag == 1 || flag == 3) {
                //处理航鸿达,甲天行定位信息推送事件
                handleHhdGPSPositionDto(baseGPSPositionDto, dispatchInfoDto);
            }
        } catch (Exception e) {
            logger.error("UDPServerListener.transformPosition is error，udpgpsPosition", FastJsonUtils.toJSONString(baseGPSPositionDto));
            logger.error("UDPServerListener.transformPosition is error", e);
        }

    }

    /**
     * 处理航鸿达定位信息推送事件
     *
     * @param baseGPSPositionDto
     * @param dispatchInfoDto
     */
    private void handleHhdGPSPositionDto(BaseGPSPositionDto baseGPSPositionDto, DispatchInfoDto dispatchInfoDto) {
        HhdBusinessDto hhdBusinessDto = (HhdBusinessDto) baseGPSPositionDto.getEleDispatch();
        if (hhdBusinessDto != null && StringUtils.isNotBlank(hhdBusinessDto.getStatus()) && dispatchInfoDto != null) {
            int state = Integer.valueOf(hhdBusinessDto.getStatus());
            DispatchEventDto dispatchEventDto = new DispatchEventDto();
            dispatchEventDto.setTerminalId(baseGPSPositionDto.getTerminalId());
            dispatchEventDto.setMileage(baseGPSPositionDto.getPoint().getMileage());
            /**
             * 电子运单状态 消息发布数据异常
             * 1：待装货
             * 2：装货完成
             * 3：运输中
             * 4：待卸货
             * 5：正在卸货
             * 6：出厂
             * 7：完成
             */
            if (dispatchInfoDto.getConWeight() == 0 && state <= 5) {
                dispatchEventDto.setEventType(2);
                //发货皮重
                Double sendTareWeight = StringUtils.isNotBlank(hhdBusinessDto.getSendTareWeight()) ? Double.valueOf(hhdBusinessDto.getSendTareWeight()) : 0.0;
                //发货毛重
                Double sendGrossWeight = StringUtils.isNotBlank(hhdBusinessDto.getSendGrossWeight()) ? Double.valueOf(hhdBusinessDto.getSendGrossWeight()) : 0.0;
                if (sendGrossWeight > 0) {
                    //矿发量 = 发货毛重 - 发货皮重
                    Double oreYield;
                    try {
                        oreYield = DataCommonUtils.getTwoDecimals(Math.abs(sendGrossWeight - sendTareWeight));
                    } catch (Exception e) {
                        logger.error("净重数据处理异常", e);
                        oreYield = Math.abs(sendGrossWeight - sendTareWeight);
                    }
                    dispatchEventDto.setEventInfo("运单写入矿发量【" + oreYield + "】");
                    dispatchEventToMQ(dispatchEventDto, baseGPSPositionDto, dispatchInfoDto);
                    dispatchInfoDto.setConWeight(oreYield);
                }
            }
            if (state == 5) {
                if (StringUtils.isNotBlank(hhdBusinessDto.getDeductWeight()) &&
                        StringUtils.isNotBlank(hhdBusinessDto.getDeductReason()) &&
                        !hhdBusinessDto.getDeductReason().equals("000")) {
                    dispatchEventDto.setEventType(6);
                    dispatchEventDto.setEventInfo("车辆扣吨【" + Double.valueOf(hhdBusinessDto.getDeductWeight()) + "】");
                } else {
                    dispatchEventDto.setEventType(5);
                    dispatchEventDto.setEventInfo("车辆进厂");
                }
                dispatchEventToMQ(dispatchEventDto, baseGPSPositionDto, dispatchInfoDto);
            }
            if (state == 6) {
                dispatchEventDto.setEventType(7);
                dispatchEventDto.setEventInfo("车辆出厂");
                dispatchEventToMQ(dispatchEventDto, baseGPSPositionDto, dispatchInfoDto);
            }
        }
    }

    /**
     * 运单事件推送
     *
     * @param dispatchEventDto
     * @param baseGPSPositionDto
     * @param dispatchInfoDto
     */
    private void dispatchEventToMQ(DispatchEventDto dispatchEventDto, BaseGPSPositionDto baseGPSPositionDto, DispatchInfoDto dispatchInfoDto) {
        getDispatchEvent(dispatchEventDto, baseGPSPositionDto, dispatchInfoDto);
        //向消息队列推送事件消息
        RcvMonitorDto<DispatchEventDto> result = new RcvMonitorDto<>();
        result.setData(dispatchEventDto);
        result.setFlag(1);
        result.setAppKey(dispatchEventDto.getAppkey());
        //logger.info("dispatchEventDto.getAppkey:【" + dispatchEventDto.getAppkey() + "】;result:【" + result + "】 \n");
        boolean pushMessage = producerSubscribe.produceMessage(dispatchEventDto.getAppkey(), result);
        logger.info("事件消息发布结果：【" + pushMessage + "】；appKey：【" + dispatchEventDto.getAppkey() + "】；result：【" + result.toString() + "】 \n");
    }

    /**
     * 获取运单事件模型
     *
     * @param dispatchEventDto
     * @param baseGPSPositionDto
     * @param dispatchInfoDto
     */
    private void getDispatchEvent(DispatchEventDto dispatchEventDto, BaseGPSPositionDto baseGPSPositionDto, DispatchInfoDto dispatchInfoDto) {
        BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
        dispatchEventDto.setAppkey(dispatchInfoDto.getAppkey());
        dispatchEventDto.setIdentity(dispatchInfoDto.getIdentity());
        dispatchEventDto.setLatitude(basicPositionDto.getLatitude());
        dispatchEventDto.setLongitude(basicPositionDto.getLongitude());
        dispatchEventDto.setTerminalId(baseGPSPositionDto.getTerminalId());
        if ((baseGPSPositionDto.getFlag() == 1 || baseGPSPositionDto.getFlag() == 3) && baseGPSPositionDto.getEleDispatch() != null) {
            HhdBusinessDto hhdBusinessDto = (HhdBusinessDto) baseGPSPositionDto.getEleDispatch();
            dispatchEventDto.setDispatchNo(hhdBusinessDto.getDisPatchNo());
            dispatchEventDto.setEventCreateTime(DateTimeUtils.getCurrentDateTimeStr());
            dispatchEventDto.setStatus(Integer.valueOf(hhdBusinessDto.getStatus()));
            dispatchEventDto.setId(UUID.randomUUID().toString());
            dispatchEventDto.setCarNum(hhdBusinessDto.getPlateNumber());
            dispatchEventDto.setConsignerName(hhdBusinessDto.getConsignerName());
            dispatchEventDto.setReceiverName(hhdBusinessDto.getReceiverName());
            dispatchEventDto.setShipperName(hhdBusinessDto.getShipperName());
            dispatchEventDto.setSendGrossWeight(StringUtils.isNotBlank(hhdBusinessDto.getSendGrossWeight()) ? Double.valueOf(hhdBusinessDto.getSendGrossWeight()) : 0.0);
            dispatchEventDto.setRcvGrossWeight(StringUtils.isNotBlank(hhdBusinessDto.getRcvGrossWeight()) ? Double.valueOf(hhdBusinessDto.getRcvGrossWeight()) : 0.0);
            dispatchEventDto.setDeductReason(hhdBusinessDto.getDeductReason());
            dispatchEventDto.setDeductWeight(StringUtils.isNotBlank(hhdBusinessDto.getDeductWeight()) ? Double.valueOf(hhdBusinessDto.getDeductWeight()) : 0.0);
            dispatchEventDto.setRcvTareWeight(StringUtils.isNotBlank(hhdBusinessDto.getRcvTareWeight()) ? Double.valueOf(hhdBusinessDto.getRcvTareWeight()) : 0.0);
            dispatchEventDto.setSendTareWeight(StringUtils.isNotBlank(hhdBusinessDto.getSendTareWeight()) ? Double.valueOf(hhdBusinessDto.getSendTareWeight()) : 0.0);
            dispatchEventDto.setCreateBy(hhdBusinessDto.getLastChangeBy());
        }
    }
}