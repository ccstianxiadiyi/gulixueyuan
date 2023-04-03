package com.chen.acl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.chen.acl.mapper")
@ComponentScan("com.chen")
@EnableDiscoveryClient
public class AclApp {
    public static void main(String[] args) {
        SpringApplication.run(AclApp.class,args);
    }
}
