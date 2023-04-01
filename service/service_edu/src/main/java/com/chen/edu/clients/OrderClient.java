package com.chen.edu.clients;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-order")
public interface OrderClient {
    @GetMapping("/orderservice/order/isBuyCourse/{courseId}/{memeberId}")
    public Boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memeberId);
}
