package com.chen.edu.clients;

import com.chen.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name="service-ucenter",url = "http://localhost:8160/userservice/ucentermember")
@Component
public interface UcenterMemberClient {
    @GetMapping("/getInfo/{id}")
    @ApiOperation("根据ID查询用户信息")
    public R getInfo(@PathVariable String id);
}
