package com.jiac.restaurantsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: SwaggerConfig
 * Author: Jiac
 * Date: 2020/10/9 9:01
 */
@Configuration
@EnableSwagger2
@ComponentScan("com.jiac.restaurantsystem.controller")
public class SwaggerConfig {
    @Bean
    public Docket docker(){
//        List<ResponseMessage> responseMessageList = new ArrayList<>();
//        responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到资源").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(401).message("没有认证").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(403).message("没有没有访问权限").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(200).message("请求成功").responseModel(null).build());
        // 构造函数传入初始化规范，这是swagger2规范
        return new Docket(DocumentationType.SWAGGER_2)
                //apiInfo： 添加api详情信息，参数为ApiInfo类型的参数，这个参数包含了第二部分的所有信息比如标题、描述、版本之类的，开发中一般都会自定义这些信息
                .apiInfo(apiInfo())
//                .groupName("yichun123")
                //配置是否启用Swagger，如果是false，在浏览器将无法访问，默认是true
                .enable(true)
                .select()
                //apis： 添加过滤条件,
                .apis(RequestHandlerSelectors.basePackage("com.jiac.restaurantsystem.controller"))
                //paths： 这里是控制哪些路径的api会被显示出来，比如下方的参数就是除了/user以外的其它路径都会生成api文档
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("餐厅管理系统API接口文档")
                .description("接口定义,统一根接口为/api/dbcourse")
//                .termsOfServiceUrl("http://blog.csdn.net/u014231523网址链接")
//                .contact(new Contact("diaoxingguo", "http://blog.csdn.net/u014231523", "diaoxingguo@163.com"))
                .build();
    }
}