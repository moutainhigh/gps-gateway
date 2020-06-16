###对外通信中心接口
    
    在resources/swagger.properties中加入自定义swagger地址，重新定义swagger地址。
    如：
    /rest/api/doc?group=open-api  //json-doc地址
    /rest/api/doc/index.html      //ui地址
    
    通过注解@OpenApi进行标识对外开放接口，通过config/Swagger2OpenApiConfig进行配置对外开放接口信息

    在pom.xml中配置swagger2markup和asciidoctor相关，执行mvn test 在${asciidoctor.html.output.directory}生成帮助说明html页面
  
~~~
├───docs            //用于自动生成帮助文档
├───main
│   └───resources
│       ├───mapper
│       ├───swagger         //用于自定义swagger页面,修改index.html页面中url参数用于指定默认打开swagger-doc
│       └───templates

~~~
