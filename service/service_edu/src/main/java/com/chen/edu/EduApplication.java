package com.chen.edu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"com.chen"})
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients
public class EduApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(EduApplication.class,args);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("the exception is {}", e.getMessage(), e);
        }
    }
}
