package com.zkkj.gps.gateway.ccs.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.base.Predicate;
import com.zkkj.gps.gateway.ccs.annotation.OpenApi;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 用于配置对外暴漏接口的swagger-json说明文档
 * 启用配置文件swagger.properties，设置配置地址springfox.documentation.swagger.v2.path=/rest/api/doc
 * 配置资源地址映射registry.addResourceHandler("/rest/api/doc/**").addResourceLocations("classpath:/swagger/dist/");
 * 静态页面放入resources/swagger/dist中
 * 配置resources/swagger/dist/index.html页面中 url: "/rest/api/doc?group=open-api",默认打开api文档内容
 * 通过new Docket(DocumentationType.SWAGGER_2).groupName("open-api")配置不同对外暴漏接口信息
 * 最终/rest/api/doc/index.html#/
 */
@Configuration
@PropertySource("classpath:swagger.properties")
public class Swagger2OpenApiConfig extends WebMvcConfigurerAdapter {
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.zkkj.gps.gateway.ccs.controller";
    public static final String VERSION = "0.0.1";

    @Bean
    public Docket createOpenRestApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name
                ("Authorization").description("token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("open-api")
                .apiInfo(apiInfo())
                .select()
                .apis(openApiPredicate)
                .paths(PathSelectors.any())
                .build()
//                .pathMapping("/zkgis")
                .forCodeGeneration(false)
                .useDefaultResponseMessages(false)
                .globalOperationParameters(pars)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("通信中心API文档")
                .description("通信中心对外接口")
                .version(VERSION)
                .build();
    }

    Predicate<RequestHandler> openApiPredicate = new Predicate<RequestHandler>() {
        @Override
        public boolean apply(RequestHandler input) {

//                Class<?> declaringClass = input.declaringClass();
//                if (declaringClass == BasicErrorController.class)// 排除
//                    return false;
//                if(declaringClass.isAnnotationPresent(ApiOperation.class)) // 被注解的类
//                    return true;
//                if(input.isAnnotatedWith(ResponseBody.class)) // 被注解的方法
//                    return true;
            if (input.isAnnotatedWith(OpenApi.class))//只有添加了OpenApi注解的method才在API中显示
            {
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/rest/api/doc/**").addResourceLocations("classpath:/swagger/dist/");
    }


}
