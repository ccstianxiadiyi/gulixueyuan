package com.chen.order.client;

import com.chen.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface UserClinet {
    @GetMapping("/userservice/ucentermember/getInfo/{id}")
    @ApiOperation("根据ID查询用户信息")
    public R getInfo(@PathVariable String id);
}
