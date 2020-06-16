package com.zkkj.gps.gateway.ccs;

import com.zkkj.gps.gateway.ccs.websocket.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.zkkj.gps.gateway.ccs.mappper", sqlSessionTemplateRef = "mySqlSessionTemplate")
@ComponentScan(basePackages = {"com.zkkj.gps.gateway.ccs"})
@EnableAsync
public class CCSMain {
    public static void main(String[] args) {
        // SpringApplication.run(CCSMain.class, args);
        SpringApplication springApplication = new SpringApplication(CCSMain.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        //解决WebSocket不能注入的问题
        WebSocketPosition.setApplicationContext(configurableApplicationContext);
        WebSocketAlarmInfo.setApplicationContext(configurableApplicationContext);
        WebSocketMonitorPosition.setApplicationContext(configurableApplicationContext);
        WebSocketAlarmGlobal.setApplicationContext(configurableApplicationContext);
    }

    /**
     * 必须，否则无法访问websocket接口
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
