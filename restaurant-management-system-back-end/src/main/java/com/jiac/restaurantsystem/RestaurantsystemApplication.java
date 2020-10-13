package com.jiac.restaurantsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.jiac.restaurantsystem.mapper")
@SpringBootApplication
public class RestaurantsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantsystemApplication.class, args);
    }

}
