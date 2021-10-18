package com.liangzhicheng.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SeckillServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillServerApplication.class, args);
    }

}
