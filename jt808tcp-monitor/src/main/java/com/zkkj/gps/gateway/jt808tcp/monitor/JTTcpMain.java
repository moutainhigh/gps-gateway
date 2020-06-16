package com.zkkj.gps.gateway.jt808tcp.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.zkkj.gps.gateway.jt808tcp.monitor"})
@SpringBootApplication
@Slf4j
public class JTTcpMain {

	public static void main(String[] args) {
		SpringApplication.run(JTTcpMain.class, args);
	}

}
