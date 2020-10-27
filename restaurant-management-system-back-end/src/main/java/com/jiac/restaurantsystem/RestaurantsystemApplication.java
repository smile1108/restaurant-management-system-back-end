package com.jiac.restaurantsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.TimeZone;

@MapperScan("com.jiac.restaurantsystem.mapper")
@SpringBootApplication
public class RestaurantsystemApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SpringApplication.run(RestaurantsystemApplication.class, args);
    }

}
