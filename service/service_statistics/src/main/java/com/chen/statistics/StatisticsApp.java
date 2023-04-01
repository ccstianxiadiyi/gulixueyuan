package com.chen.statistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan("com.chen.statistics.mapper")
@ComponentScan("com.chen")
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class StatisticsApp {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsApp.class,args);
    }
}
