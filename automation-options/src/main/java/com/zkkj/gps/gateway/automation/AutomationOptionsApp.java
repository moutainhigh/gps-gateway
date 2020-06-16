package com.zkkj.gps.gateway.automation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "com.zkkj.gps.gateway.automation.mapper")
@ComponentScan(basePackages = "com.zkkj.gps.gateway.automation")
public class AutomationOptionsApp {

	public static void main(String[] args) {
		SpringApplication.run(AutomationOptionsApp.class, args);
	}

}
