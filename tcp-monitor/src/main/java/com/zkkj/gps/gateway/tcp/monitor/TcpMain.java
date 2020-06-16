package com.zkkj.gps.gateway.tcp.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zkkj.gps.gateway.tcp.monitor"})
public class TcpMain {

	public static void main(String[] args) {
		SpringApplication.run(TcpMain.class, args);
	}

}
