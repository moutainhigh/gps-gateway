package com.zkkj.gps.gateway.celltrack.celltrackmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 手机基站定位
 * @author suibozhuliu
 */
@ComponentScan(basePackages = {"com.zkkj.gps.gateway.celltrack.celltrackmonitor"})
@SpringBootApplication
public class CellTrackApp {

	public static void main(String[] args) {
		SpringApplication.run(CellTrackApp.class, args);
	}

}
