package com.chen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 */

@EnableDiscoveryClient
@MapperScan("com.chen.cms.mapper")
@SpringBootApplication

public class CMSApp {
    public static void main(String[] args) {
        SpringApplication.run(CMSApp.class,args);
    }
}
