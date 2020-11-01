package com.jiac.restaurantsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import javax.annotation.PostConstruct;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.TimeZone;

@MapperScan("com.jiac.restaurantsystem.mapper")
@SpringBootApplication
@ServletComponentScan
public class RestaurantsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantsystemApplication.class, args);
    }

}
